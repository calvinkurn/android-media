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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateTutorialPagerAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.liveDataExtension.toFreshLiveData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateWebViewBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_login_fragment_layout.*
import javax.inject.Inject

class AffiliateLoginFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    private lateinit var affiliateLoginSharedViewModel: AffiliateRegistrationSharedViewModel

    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelProvider) }

    companion object {
        const val TAG = "AffiliateLoginFrament"
        fun getFragmentInstance(): Fragment {
            return AffiliateLoginFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateLoginSharedViewModel = viewModelFragmentProvider.get(AffiliateRegistrationSharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_login_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent().injectLoginFragment(this)
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
        initObserver()
        checkLoggedIn()
        sendTracking()
    }

    private fun sendTracking() {
        val loginText = if(userSessionInterface.isLoggedIn)"login" else "non login"
        AffiliateAnalytics.sendOpenScreenEvent(
                AffiliateAnalytics.EventKeys.OPEN_SCREEN,
                "${AffiliateAnalytics.ScreenKeys.AFFILIATE_LOGIN_SCREEN_NAME}$loginText",
                userSessionInterface.isLoggedIn,
                userSessionInterface.userId
        )
    }

    private fun setUpNavBar() {
        val customView = layoutInflater.inflate(R.layout.affiliate_navbar_custom_content,null,false)
        view?.findViewById<com.tokopedia.header.HeaderUnify>(R.id.affiliate_login_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            actionTextView?.setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_PELJARI,if(userSessionInterface.isLoggedIn)AffiliateAnalytics.LabelKeys.LOGIN else AffiliateAnalytics.LabelKeys.NON_LOGIN)
                AffiliateWebViewBottomSheet.newInstance("", AFFILIATE_MICRO_SITE_LINK).show(childFragmentManager,"")
            }
        }
    }

    private fun checkLoggedIn() {
        affiliate_login_ticker.hide()
        if (!affiliateLoginSharedViewModel.isUserLoggedIn()) {
            affiliate_login_text.isVisible = true
            affiliate_login_text.text = getString(com.tokopedia.affiliate_toko.R.string.affiliate_daftar_sekarang_dengan_akun_tokopedia_kamu)
            affiliate_daftar_text.text = getString(R.string.affiliate_belum_punya_akun_tokopedia)
            affiliate_keluar_btn.text = getString(R.string.affiliate_daftar)
            affiliate_sign_up_btn.text = getString(R.string.affiliate_masuk)
            affiliate_sign_up_btn.isVisible = true
            affiliate_sign_up_btn.setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_MASUK)
                startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    AFFILIATE_LOGIN_REQUEST_CODE)
            }

            affiliate_keluar_btn.setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_DAFTAR)
                startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.REGISTER),
                        AFFILIATE_REGISTER_REQUEST_CODE)
            }

            affiliate_login_card.hide()

        } else {
            affiliateLoginSharedViewModel.getAffiliateValidateUser()

            affiliate_login_text.isVisible = true
            affiliate_login_text.text = getString(R.string.affiliate_daftarkan_akun_ini)
            affiliate_daftar_text.text = getString(R.string.affiliate_daftar_affiliate_dengan_akun_lain)
            affiliate_keluar_btn.text = getString(R.string.affiliate_keluar)
            affiliate_sign_up_btn.text = getString(R.string.affiliate_daftar_sekarang)
            affiliate_sign_up_btn.isVisible = true
            affiliate_sign_up_btn.setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_DAFTAR_SEKARANG)
                affiliateLoginSharedViewModel.navigateToPortFolio()
            }

            affiliate_keluar_btn.setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_KELUAR)
                showDialogLogout()
            }

            affiliate_login_card.show()
            affiliate_user_name.text = affiliateLoginSharedViewModel.getUserName()
            affiliate_user_email.text = affiliateLoginSharedViewModel.getUserEmail()
            ImageHandler.loadImageCircle2(context, affiliate_user_image, affiliateLoginSharedViewModel.getUserProfilePicture())
        }
    }

    private fun initObserver() {
        affiliateLoginSharedViewModel.getUserAction().toFreshLiveData().observe(viewLifecycleOwner,
             {
            when(it){
                AffiliateRegistrationSharedViewModel.UserAction.RegisteredAction -> {
                    onUserRegistered()
                }
                AffiliateRegistrationSharedViewModel.UserAction.FraudAction -> {
                    showFraudTicker()
                }
                else -> {}
            }
        })
        affiliateLoginSharedViewModel.getProgressBar().observe(viewLifecycleOwner,{
            it?.let {
                login_progress.isVisible = it
            }
        })
    }

    private fun onUserRegistered() {
        RouteManager.route(context,"tokopedia://affiliate")
        activity?.finish()
    }

    private fun sendButtonClick(eventAction: String,label: String= "") {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_REGISTRATION_PAGE,
            label,
            userSessionInterface.userId
        )
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

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()


    fun showFraudTicker() {
        affiliate_sign_up_btn.isVisible = false
        affiliate_login_text.isVisible = false
        affiliate_login_ticker.run {
            show()
            affiliate_login_ticker_cv.show()
            setHtmlDescription(getString(R.string.affiliate_login_ticker_text))
            tickerType = Ticker.TYPE_ERROR
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    AffiliateWebViewBottomSheet.newInstance("", AFFILIATE_FRAUD_URL).show(childFragmentManager,"")
                }
                override fun onDismiss() {}
            })
        }
    }
}