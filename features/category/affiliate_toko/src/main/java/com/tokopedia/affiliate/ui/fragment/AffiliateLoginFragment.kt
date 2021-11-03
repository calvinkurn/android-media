package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.AFFILIATE_LOGIN_REQUEST_CODE
import com.tokopedia.affiliate.AFFILIATE_REGISTER_REQUEST_CODE
import com.tokopedia.affiliate.adapter.AffiliateTutorialPagerAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
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
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_login_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        afterViewCreated()
    }

    private fun setupViewPager () {
        val viewPager = view?.findViewById<TouchViewPager>(R.id.affiliate_login_view_pager)
        val tutorialArray = arrayListOf<AffiliateTutorialPagerAdapter.LoginTutorialData>().apply {
            context?.apply {
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_1),
                        getString(R.string.tutorial_subtitle_1),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_never_bought_product.png"))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_2),
                        getString(R.string.tutorial_subtitle_2),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_product_not_found.png"))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_3),
                        getString(R.string.tutorial_subtitle_3),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_never_bought_product.png"))
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
            inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
            setIndicator(tutorialArray.size)
        }
    }

    private fun afterViewCreated() {
        checkLoggedIn()
        setListeners()
    }

    private fun setListeners() {

    }

    private fun setObservers() {

    }

    private fun checkLoggedIn() {
        if (!affiliateLoginViewModel.isUserLoggedIn()) {
            affiliate_login_text.text = "Daftar sekarang dengan akun Tokopedia kamu"
            affiliate_daftar_text.text = "Belum punya akun Tokopedia? "
            affiliate_keluar_btn.text = "Daftar"
            affiliate_sign_up_btn.text = "Masuk"
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
            affiliate_login_text.text = "Daftarkan akun ini:"
            affiliate_daftar_text.text = "Daftar Affiliate dengan akun lain? "
            affiliate_keluar_btn.text = "Keluar"
            affiliate_sign_up_btn.text = "Daftar Sekarang"
            affiliate_sign_up_btn.setOnClickListener {
                affiliateNavigationInterface.navigateToTermsFragment()
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
            dialog.setTitle("Yakin ingin keluar?")
            dialog.setDescription("Dengan kamu keluar akun disini, akun kamu di Tokopedia juga akan keluar")
            dialog.setPrimaryCTAText("Keluar")
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                doLogout()
            }
            dialog.setSecondaryCTAText("Batal")
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun doLogout() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.LOGOUT))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AFFILIATE_LOGIN_REQUEST_CODE, AFFILIATE_REGISTER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    checkLoggedIn()
                }
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
}