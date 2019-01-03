package com.tokopedia.instantloan.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator
import com.tokopedia.instantloan.InstantLoanComponentInstance
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.WEB_LINK_OTP
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.activity.InstantLoanActivity
import com.tokopedia.instantloan.view.adapter.InstantLoanIntroViewPagerAdapter
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.content_instant_loan_home_page.*
import javax.inject.Inject


class DanaInstantFragment : BaseDaggerFragment(), InstantLoanContractor.View {

    private var mDialogIntro: Dialog? = null

    private var activityInteractor: ActivityInteractor? = null
    private var mContext: Context? = null

    @Inject
    lateinit var presenter: InstantLoanPresenter
    @Inject
    lateinit var instantLoanAnalytics: InstantLoanAnalytics

    @Inject
    lateinit var userSession: UserSession

    private var mCurrentTab: Int = 0
    private var mCurrentPagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        mCurrentTab = arguments?.getInt(TAB_POSITION) ?: 0
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun initInjector() {
        val daggerInstantLoanComponent = InstantLoanComponentInstance.get(activity!!.application)
        daggerInstantLoanComponent!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_instant_loan_home_page, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {

        text_value_amount.text = resources.getStringArray(R.array.values_amount)[mCurrentTab]
        text_value_duration.text = resources.getStringArray(R.array.values_duration)[mCurrentTab]
        text_value_processing_time.text = resources.getStringArray(R.array.values_processing_time)[mCurrentTab]
        text_value_interest_rate.text = resources.getStringArray(R.array.values_interest_rate)[mCurrentTab]
        text_form_description.text = resources.getStringArray(R.array.values_description)[mCurrentTab]

        button_search_pinjaman.setOnClickListener { view1 -> searchLoanOnline() }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onAttachActivity(context: Context) {
        this.mContext = context
        try {
            this.activityInteractor = context as ActivityInteractor
        } catch (e: Exception) {

        }

        super.onAttachActivity(context)
    }

    override fun getAppContext(): Context? {
        return getContext()?.applicationContext
    }

    override fun getActivityContext(): Context? {
        return getContext()
    }

    override fun onSuccessLoanProfileStatus(data: UserProfileLoanEntity) {

        if (!data.whitelist) {
            if (!TextUtils.isEmpty(data.whiteListUrl)) {
                openWebView(data.whiteListUrl!!)
            } else {
                NetworkErrorHelper.showSnackbar(activity,
                        resources.getString(R.string.instant_loan_coming_soon))
            }
        } else if (!data.dataCollection || data.dataCollection && data.dataCollected) {
            if (!TextUtils.isEmpty(data.redirectUrl)) {
                openWebView(data.redirectUrl!!)
            } else {
                NetworkErrorHelper.showSnackbar(activity,
                        resources.getString(R.string.default_request_error_unknown))
            }

        } else {
            startIntroSlider()
        }

    }

    override fun setUserOnGoingLoanStatus(status: Boolean, loanId: Int) {
        if (activityInteractor != null) {
            activityInteractor!!.setUserOnGoingLoanStatus(status, loanId)
        }
    }

    override fun onErrorLoanProfileStatus(onErrorLoanProfileStatus: String) {
        hideLoader()
        NetworkErrorHelper.showSnackbar(activity, onErrorLoanProfileStatus)
    }

    override fun onSuccessPhoneDataUploaded(data: PhoneDataEntity) {
        hideLoaderIntroDialog()

        if (data.mobileDeviceId > 0) {
            openWebView(WEB_LINK_OTP)
            if (mDialogIntro != null && !activity!!.isFinishing) {
                mDialogIntro!!.dismiss()
            }
        }
    }

    override fun onErrorPhoneDataUploaded(errorMessage: String) {
        hideLoaderIntroDialog()
        hideIntroDialog()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun navigateToLoginPage() {
        if (activity != null && activity!!.application is InstantLoanRouter) {
            startActivityForResult((activity!!.application as InstantLoanRouter).getLoginIntent(getContext()), LOGIN_REQUEST_CODE)
        }
    }


    override fun startIntroSlider() {

        val view = layoutInflater.inflate(R.layout.dialog_intro_instnat_loan, null)
        val pager = view.findViewById<ViewPager>(R.id.view_pager_il_intro)
        val pageIndicator = view.findViewById<CirclePageIndicator>(R.id.page_indicator_il_intro)
        val btnNext = view.findViewById<FloatingActionButton>(R.id.button_next)

        val layouts = intArrayOf(R.layout.intro_instant_loan_slide_1, R.layout.intro_instant_loan_slide_2, R.layout.intro_instant_loan_slide_3)

        pager.adapter = InstantLoanIntroViewPagerAdapter(activity as InstantLoanActivity,
                layouts, presenter)
        pageIndicator.fillColor = ContextCompat.getColor(getContext()!!, R.color.tkpd_main_green)
        pageIndicator.pageColor = ContextCompat.getColor(getContext()!!, R.color.black_38)
        pageIndicator.setViewPager(pager, 0)
        btnNext.show()
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == layouts.size - 1) {
                    pageIndicator.visibility = View.INVISIBLE
                    btnNext.hide()
                } else {
                    pageIndicator.visibility = View.VISIBLE
                    btnNext.show()
                }

                val right = mCurrentPagePosition < position

                if (mCurrentPagePosition == 0 && right) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_FIRST_NEXT)
                } else if (mCurrentPagePosition == 1 && !right) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_SECOND_PREVIOUS)
                } else if (mCurrentPagePosition == 1) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_SECOND_NEXT)
                } else if (mCurrentPagePosition == 2 && !right) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_THIRD_PREVIOUS)
                }

                mCurrentPagePosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        btnNext.setOnClickListener { v ->
            if (pager.currentItem != layouts.size) {

                val position = pager.currentItem

                if (position == 0) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_FIRST_NEXT)
                } else if (position == 1) {
                    sendIntroSliderScrollEvent(InstantLoanEventConstants.EventLabel.PL_INTRO_SLIDER_SECOND_NEXT)
                }

                pager.setCurrentItem(pager.currentItem + 1, true)
            }
        }

        mDialogIntro = Dialog(getContext()!!)


        mDialogIntro!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialogIntro!!.setContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(mDialogIntro!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        mDialogIntro!!.setCanceledOnTouchOutside(false)
        mDialogIntro!!.show()
        mDialogIntro!!.window!!.attributes = lp
    }

    private fun sendIntroSliderScrollEvent(label: String) {
        instantLoanAnalytics.eventIntroSliderScrollEvent(label)
    }

    override fun showToastMessage(message: String, duration: Int) {
        Toast.makeText(getContext(), message, duration).show()
    }

    override fun openWebView(url: String) {
        RouteManager.route(context!!, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun searchLoanOnline() {
        if (presenter.isUserLoggedIn()) {
            sendCariPinjamanClickEvent()
            presenter.getLoanProfileStatus()
        } else {
            navigateToLoginPage()
        }
    }

    override fun showLoader() {
        progress_bar_status!!.visibility = View.VISIBLE
        activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun hideLoader() {
        progress_bar_status!!.visibility = View.GONE
        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun showLoaderIntroDialog() {
        if (mDialogIntro == null || activity!!.isFinishing) {
            return
        }

        mDialogIntro!!.window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro) != null &&
                mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2) != null &&
                mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2)
                        .findViewById<View>(R.id.progress_bar_status) != null) {

            mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2)
                    .findViewById<View>(R.id.progress_bar_status).visibility = View.VISIBLE

        }
    }

    override fun hideLoaderIntroDialog() {
        if (mDialogIntro == null || activity!!.isFinishing) {
            return
        }

        mDialogIntro!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro) != null &&
                mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2) != null &&
                mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2)
                        .findViewById<View>(R.id.progress_bar_status) != null) {

            mDialogIntro!!.findViewById<View>(R.id.view_pager_il_intro).findViewWithTag<View>(2)
                    .findViewById<View>(R.id.progress_bar_status).visibility = View.INVISIBLE

        }
    }

    override fun hideIntroDialog() {
        if (mDialogIntro == null || activity!!.isFinishing) {
            return
        }
        mDialogIntro!!.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn) {
                presenter.getLoanProfileStatus()
            } else {
                NetworkErrorHelper.showSnackbar(activity,
                        resources.getString(R.string.login_to_proceed))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun getScreenName(): String {
        return InstantLoanEventConstants.Screen.DANA_INSTAN_SCREEN_NAME
    }

    private fun sendCariPinjamanClickEvent() {
        val eventLabel = screenName
        instantLoanAnalytics.eventCariPinjamanClick(eventLabel)
    }

    interface ActivityInteractor {
        fun setUserOnGoingLoanStatus(status: Boolean, id: Int)
    }

    companion object {

        val LOGIN_REQUEST_CODE = 1005
        private val TAB_POSITION = "tab_position"

        fun createInstance(position: Int): DanaInstantFragment {
            val args = Bundle()
            args.putInt(TAB_POSITION, position)
            val danaInstantFragment = DanaInstantFragment()
            danaInstantFragment.arguments = args
            return danaInstantFragment
        }
    }
}
