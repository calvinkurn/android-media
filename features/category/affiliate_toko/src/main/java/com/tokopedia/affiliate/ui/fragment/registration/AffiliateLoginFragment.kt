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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AFFILIATE_LOGIN_REQUEST_CODE
import com.tokopedia.affiliate.AFFILIATE_MICRO_SITE_LINK
import com.tokopedia.affiliate.AFFILIATE_REGISTER_REQUEST_CODE
import com.tokopedia.affiliate.AFFILIATE_REQUEST_CODE_LOGOUT
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ON_BOARDING_TUTORIAL_IMAGE_1
import com.tokopedia.affiliate.ON_BOARDING_TUTORIAL_IMAGE_2
import com.tokopedia.affiliate.ON_BOARDING_TUTORIAL_IMAGE_3
import com.tokopedia.affiliate.adapter.AffiliateTutorialPagerAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateWebViewBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateLoginFragment : BaseDaggerFragment() {

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    private var affiliateLoginSharedViewModel: AffiliateRegistrationSharedViewModel? = null

    private val viewModelFragmentProvider by lazy {
        viewModelProvider?.let { viewModelProvider ->
            activity?.let { activity ->
                ViewModelProvider(
                    activity,
                    viewModelProvider
                )
            }
        }
    }

    companion object {
        const val TAG = "AffiliateLoginFrament"
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        fun getFragmentInstance(): Fragment {
            return AffiliateLoginFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateLoginSharedViewModel =
            viewModelFragmentProvider?.get(AffiliateRegistrationSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    private fun setupViewPager() {
        val viewPager = view?.findViewById<TouchViewPager>(R.id.affiliate_login_view_pager)
        val pageControl = view?.findViewById<PageControl>(R.id.affiliate_login_page_control)
        val tutorialArray = arrayListOf<AffiliateTutorialPagerAdapter.LoginTutorialData>().apply {
            context?.apply {
                add(
                    AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_1),
                        getString(R.string.affiliate_tutorial_subtitle_1),
                        ON_BOARDING_TUTORIAL_IMAGE_1
                    )
                )
                add(
                    AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_2),
                        getString(R.string.affiliate_tutorial_subtitle_2),
                        ON_BOARDING_TUTORIAL_IMAGE_2
                    )
                )
                add(
                    AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.affiliate_tutorial_title_3),
                        getString(R.string.affiliate_tutorial_subtitle_3),
                        ON_BOARDING_TUTORIAL_IMAGE_3
                    )
                )
            }
        }
        val tutorialAdapter = AffiliateTutorialPagerAdapter(tutorialArray)
        viewPager?.adapter = tutorialAdapter
        viewPager?.setCurrentItem(0, true)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) = Unit

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) = Unit

            override fun onPageSelected(position: Int) {
                pageControl?.setCurrentIndicator(position)
            }
        })

        pageControl?.apply {
            inactiveColor =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
            setIndicator(tutorialArray.size)
        }
    }

    private fun afterViewCreated() {
        hideAllView()
        setupViewPager()
        setUpNavBar()
        initObserver()
        checkLoggedIn()
        sendTracking()
    }

    private fun sendTracking() {
        val loginText = if (userSessionInterface?.isLoggedIn == true) {
            "login"
        } else {
            "non login"
        }
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            "${AffiliateAnalytics.ScreenKeys.AFFILIATE_LOGIN_SCREEN_NAME}$loginText",
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun setUpNavBar() {
        val customView =
            layoutInflater.inflate(R.layout.affiliate_navbar_custom_content, null, false)
        view?.findViewById<com.tokopedia.header.HeaderUnify>(R.id.affiliate_login_toolbar)?.apply {
            customView(customView)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            actionTextView?.setOnClickListener {
                sendButtonClick(
                    AffiliateAnalytics.ActionKeys.CLICK_PELJARI,
                    if (userSessionInterface?.isLoggedIn == true) {
                        AffiliateAnalytics.LabelKeys.LOGIN
                    } else {
                        AffiliateAnalytics.LabelKeys.NON_LOGIN
                    }
                )
                AffiliateWebViewBottomSheet.newInstance("", AFFILIATE_MICRO_SITE_LINK)
                    .show(childFragmentManager, "")
            }
        }
    }

    private fun checkLoggedIn() {
        val loginText = view?.findViewById<Typography>(R.id.affiliate_login_text)
        val daftarText = view?.findViewById<Typography>(R.id.affiliate_daftar_text)
        val keluarButton = view?.findViewById<Typography>(R.id.affiliate_keluar_btn)
        val signUpButton = view?.findViewById<UnifyButton>(R.id.affiliate_sign_up_btn)
        view?.findViewById<Ticker>(R.id.affiliate_login_ticker)?.hide()
        if (affiliateLoginSharedViewModel?.isUserLoggedIn() == false) {
            showAllView()
            loginText?.apply {
                isVisible = true
                text =
                    getString(R.string.affiliate_daftar_sekarang_dengan_akun_tokopedia_kamu)
            }
            daftarText?.text = getString(R.string.affiliate_belum_punya_akun_tokopedia)
            keluarButton?.apply {
                text = getString(R.string.affiliate_daftar)
                setOnClickListener {
                    sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_DAFTAR)
                    startActivityForResult(
                        RouteManager.getIntent(activity, ApplinkConst.REGISTER),
                        AFFILIATE_REGISTER_REQUEST_CODE
                    )
                }
            }
            signUpButton?.apply {
                text = getString(R.string.affiliate_masuk)
                isVisible = true
                setOnClickListener {
                    sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_MASUK)
                    startActivityForResult(
                        RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                        AFFILIATE_LOGIN_REQUEST_CODE
                    )
                }
            }
            view?.findViewById<CardUnify>(R.id.affiliate_login_card)?.hide()
        } else {
            affiliateLoginSharedViewModel?.getAffiliateValidateUser()
        }
    }

    private fun setUserInformation() {
        view?.findViewById<CardUnify>(R.id.affiliate_login_card)?.show()
        view?.findViewById<Typography>(R.id.affiliate_user_name)?.text =
            affiliateLoginSharedViewModel?.getUserName()
        view?.findViewById<Typography>(R.id.affiliate_user_email)?.text =
            affiliateLoginSharedViewModel?.getUserEmail()
        view?.findViewById<ImageUnify>(R.id.affiliate_user_image)
            ?.loadImageCircle(affiliateLoginSharedViewModel?.getUserProfilePicture())
    }

    private fun initObserver() {
        affiliateLoginSharedViewModel?.getLoginScreenAction()?.observe(viewLifecycleOwner) {
            when (it) {
                AffiliateRegistrationSharedViewModel.UserAction.RegisteredAction -> {
                    onUserRegistered()
                }

                AffiliateRegistrationSharedViewModel.UserAction.FraudAction -> {
                    showFraudTicker()
                }

                AffiliateRegistrationSharedViewModel.UserAction.SignUpAction -> {
                    setSignupData()
                }

                AffiliateRegistrationSharedViewModel.UserAction.SystemDown -> {
                    hideAllView()
                    view?.findViewById<TouchViewPager>(R.id.affiliate_login_view_pager)?.hide()
                    view?.findViewById<PageControl>(R.id.affiliate_login_page_control)?.hide()
                    view?.findViewById<GlobalError>(R.id.login_error)?.run {
                        show()
                        errorAction.show()
                        errorAction.text = "Ke Home"
                    }
                }

                else -> {}
            }
        }
        affiliateLoginSharedViewModel?.getProgressBar()?.observe(viewLifecycleOwner) {
            it?.let {
                view?.findViewById<LoaderUnify>(R.id.login_progress)?.isVisible = it
            }
        }
        affiliateLoginSharedViewModel?.getAffiliateAnnouncement()
            ?.observe(viewLifecycleOwner) { announcementData ->
                if (announcementData.getAffiliateAnnouncementV2?.announcementData?.subType != TICKER_BOTTOM_SHEET) {
                    view?.findViewById<Ticker>(R.id.affiliate_login_ticker)
                        ?.setAnnouncementData(announcementData, activity)
                }
            }
    }

    private fun setSignupData() {
        showAllView()
        view?.findViewById<Typography>(R.id.affiliate_login_text)?.apply {
            isVisible = true
            text = getString(R.string.affiliate_daftarkan_akun_ini)
        }
        view?.findViewById<Typography>(R.id.affiliate_daftar_text)?.text =
            getString(R.string.affiliate_daftar_affiliate_dengan_akun_lain)
        view?.findViewById<Typography>(R.id.affiliate_keluar_btn)?.apply {
            text = getString(R.string.affiliate_keluar)
            setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_KELUAR)
                showDialogLogout()
            }
        }
        view?.findViewById<UnifyButton>(R.id.affiliate_sign_up_btn)?.apply {
            text = getString(R.string.affiliate_daftar_sekarang)
            setOnClickListener {
                sendButtonClick(AffiliateAnalytics.ActionKeys.CLICK_DAFTAR_SEKARANG)
                affiliateLoginSharedViewModel?.navigateToPortFolio()
            }
        }
        setUserInformation()
    }

    private fun onUserRegistered() {
        RouteManager.route(context, "tokopedia://affiliate")
        activity?.finish()
    }

    private fun sendButtonClick(eventAction: String, label: String = "") {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_REGISTRATION_PAGE,
            label,
            userSessionInterface?.userId.orEmpty()
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
            val intent = RouteManager.getIntent(it, ApplinkConstInternalUserPlatform.LOGOUT)
            intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, false)
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

    private fun showFraudTicker() {
        hideAllView()
        affiliateLoginSharedViewModel?.getAnnouncementInformation()
    }

    private fun hideAllView() {
        view?.apply {
            findViewById<CardUnify>(R.id.affiliate_login_card)?.hide()
            findViewById<Typography>(R.id.affiliate_daftar_text)?.hide()
            findViewById<Typography>(R.id.affiliate_keluar_btn)?.hide()
            findViewById<UnifyButton>(R.id.affiliate_sign_up_btn)?.hide()
            findViewById<Typography>(R.id.affiliate_login_text)?.hide()
        }
    }

    private fun showAllView() {
        view?.apply {
            findViewById<CardUnify>(R.id.affiliate_login_card)?.show()
            findViewById<Typography>(R.id.affiliate_daftar_text)?.show()
            findViewById<Typography>(R.id.affiliate_keluar_btn)?.show()
            findViewById<UnifyButton>(R.id.affiliate_sign_up_btn)?.show()
            findViewById<Typography>(R.id.affiliate_login_text)?.show()
        }
    }
}
