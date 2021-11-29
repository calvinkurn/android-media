package com.tokopedia.affiliate.ui.fragment.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateTutorialPagerAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.viewmodel.AffiliateLoginViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_login_fragment_layout.*
import javax.inject.Inject

class AffiliateLoginFragment : BaseViewModelFragment<AffiliateLoginViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private lateinit var affiliateLoginViewModel: AffiliateLoginViewModel

    private lateinit var affiliateNavigationInterface: AffiliateActivityInterface

    companion object {
        fun getFragmentInstance(affiliateActivityInterface: AffiliateActivityInterface): Fragment {
            return AffiliateLoginFragment().apply {
                affiliateNavigationInterface = affiliateActivityInterface
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_login_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun setupViewPager () {
        val viewPager = view?.findViewById<TouchViewPager>(R.id.affiliate_login_view_pager)
        val tutorialArray = arrayListOf<AffiliateTutorialPagerAdapter.LoginTutorialData>().apply {
            context?.apply {
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_1),
                        getString(R.string.affiliate_tutorial_subtitle_1),
                        ON_BOARDING_TUTORIAL_IMAGE_1))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_2),
                        getString(R.string.affiliate_tutorial_subtitle_2),
                        ON_BOARDING_TUTORIAL_IMAGE_2))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_3),
                        getString(R.string.affiliate_tutorial_subtitle_3),
                        ON_BOARDING_TUTORIAL_IMAGE_3))
            }
        }
        val tutorialAdapter = AffiliateTutorialPagerAdapter(tutorialArray)
        viewPager?.adapter = tutorialAdapter
        viewPager?.setCurrentItem(0, true)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                affiliate_login_page_control.setCurrentIndicator(position)
            }
        })

        affiliate_login_page_control.apply {
            inactiveColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
            setIndicator(tutorialArray.size)
        }
    }

    private fun afterViewCreated() {
        setupViewPager()
        setUpNavBar()
        checkLoggedIn()
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        affiliate_login_toolbar.apply {
            customView(customView)
            setNavigationOnClickListener {
                affiliateNavigationInterface.handleBackButton()
            }
            actionTextView?.setOnClickListener {
                (activity as? AffiliateActivity)?.showAffiliatePortal()
            }
        }
    }

    private fun checkLoggedIn() {
        affiliate_login_ticker.hide()
        if (!affiliateLoginViewModel.isUserLoggedIn()) {
            affiliate_login_text.text = getString(com.tokopedia.affiliate_toko.R.string.affiliate_daftar_sekarang_dengan_akun_tokopedia_kamu)
            affiliate_daftar_text.text = getString(R.string.affiliate_belum_punya_akun_tokopedia)
            affiliate_keluar_btn.text = getString(R.string.affiliate_daftar)
            affiliate_sign_up_btn.text = getString(R.string.affiliate_masuk)
            affiliate_sign_up_btn.setOnClickListener {
                startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    AFFILIATE_LOGIN_REQUEST_CODE)
            }

            affiliate_keluar_btn.setOnClickListener {
                startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.REGISTER),
                        AFFILIATE_REGISTER_REQUEST_CODE)
            }

            affiliate_login_card.hide()

        } else {

            affiliateNavigationInterface.validateUserStatus()

            affiliate_login_text.text = getString(R.string.affiliate_daftarkan_akun_ini)
            affiliate_daftar_text.text = getString(R.string.affiliate_daftar_affiliate_dengan_akun_lain)
            affiliate_keluar_btn.text = getString(R.string.affiliate_keluar)
            affiliate_sign_up_btn.text = getString(R.string.affiliate_daftar_sekarang)
            affiliate_sign_up_btn.setOnClickListener {
                affiliateNavigationInterface.navigateToPortfolioFragment()
            }

            affiliate_keluar_btn.setOnClickListener {
                showDialogLogout()
            }

            affiliate_login_card.show()
            affiliate_user_name.text = affiliateLoginViewModel.getUserName()
            affiliate_user_email.text = affiliateLoginViewModel.getUserEmail()
            ImageHandler.loadImageCircle2(context, affiliate_user_image, affiliateLoginViewModel.getUserProfilePicture())
        }
    }

    private fun showDialogLogout() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.affiliate_yakin_ingin_keluar))
            dialog.setDescription(getString(R.string.affiliate_logout_description))
            dialog.setPrimaryCTAText(getString(R.string.affiliate_keluar))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                doLogout()
            }
            dialog.setSecondaryCTAText(getString(R.string.affiliate_batal))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun doLogout() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.LOGOUT)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, false)
            startActivityForResult(intent, AFFILIATE_REQUEST_CODE_LOGOUT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AFFILIATE_LOGIN_REQUEST_CODE, AFFILIATE_REGISTER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    checkLoggedIn()
                }
            }
            AFFILIATE_REQUEST_CODE_LOGOUT -> {
                checkLoggedIn()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectLoginFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliateLoginViewModel> {
        return AffiliateLoginViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateLoginViewModel = viewModel as AffiliateLoginViewModel
    }

    fun showFraudTicker() {
        affiliate_login_ticker.run {
            show()
            affiliate_login_ticker_cv.show()
            setHtmlDescription(getString(R.string.affiliate_login_ticker_text))
            tickerType = Ticker.TYPE_ERROR
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {

                }
                override fun onDismiss() {}
            })
        }
    }
}