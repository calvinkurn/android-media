package com.tokopedia.instantloan.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.InstantLoanComponentInstance
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.LOAN_AMOUNT_QUERY_PARAM
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.WEB_LINK_NO_COLLATERAL
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment.Companion.LOGIN_REQUEST_CODE
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.content_tanpa_agunan.*
import javax.inject.Inject


class TanpaAgunanFragment : BaseDaggerFragment(), InstantLoanContractor.View {

    @Inject
    lateinit var presenter: InstantLoanPresenter
    @Inject
    lateinit var instantLoanAnalytics: InstantLoanAnalytics

    @Inject
    lateinit var userSession: UserSession

    private var mCurrentTab: Int = 0
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        mCurrentTab = arguments?.getInt(TAB_POSITION) ?: 0
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onAttachActivity(context: Context) {
        this.mContext = context
        super.onAttachActivity(context)
    }

    override fun initInjector() {
        val daggerInstantLoanComponent = InstantLoanComponentInstance.get(activity!!.application)
        daggerInstantLoanComponent!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_tanpa_agunan, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupLoanAmountSpinner()
    }

    private fun setupLoanAmountSpinner() {
        val adapter = ArrayAdapter.createFromResource(getContext()!!,
                R.array.values_amount_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_value_nominal!!.adapter = adapter
    }

    private fun initView(view: View) {
        text_value_amount.text = resources.getStringArray(R.array.values_amount)[mCurrentTab]
        text_value_duration.text = resources.getStringArray(R.array.values_duration)[mCurrentTab]
        text_value_processing_time.text = resources.getStringArray(R.array.values_processing_time)[mCurrentTab]
        text_value_interest_rate.text = resources.getStringArray(R.array.values_interest_rate)[mCurrentTab]
        text_form_description.text = resources.getStringArray(R.array.values_description)[mCurrentTab]

        view.findViewById<View>(R.id.button_search_pinjaman).setOnClickListener { view1 ->

            if (spinner_value_nominal!!.selectedItem.toString()
                            .equals(getString(R.string.label_select_nominal), ignoreCase = true)) {
                val errorText = spinner_value_nominal!!.selectedView as TextView
                errorText.setTextColor(Color.RED)
            } else {
                sendCariPinjamanClickEvent()
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        spinner_value_nominal!!.selectedItem.toString()
                                .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace(".", ""))
            }
        }
    }

    private fun sendCariPinjamanClickEvent() {
        val eventLabel = screenName + " - " + spinner_value_nominal!!.selectedItem.toString()
        instantLoanAnalytics.eventCariPinjamanClick(eventLabel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn) {
                showToastMessage(resources.getString(R.string.login_to_proceed), Toast.LENGTH_SHORT)
            } else {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        spinner_value_nominal!!.selectedItem.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun getScreenName(): String {
        return InstantLoanEventConstants.Screen.TANPA_AGUNAN_SCREEN_NAME
    }

    override fun getAppContext(): Context? {
        return getContext()?.applicationContext
    }

    override fun getActivityContext(): Context? {
        return getContext()
    }

    override fun onSuccessLoanProfileStatus(status: UserProfileLoanEntity) {

    }

    override fun setUserOnGoingLoanStatus(status: Boolean, loanId: Int) {

    }

    override fun onErrorLoanProfileStatus(onErrorLoanProfileStatus: String) {

    }

    override fun onSuccessPhoneDataUploaded(data: PhoneDataEntity) {

    }

    override fun onErrorPhoneDataUploaded(errorMessage: String) {

    }

    override fun navigateToLoginPage() {
        if (activity != null && activity!!.application is InstantLoanRouter) {
            startActivityForResult((activity!!.application as InstantLoanRouter).getLoginIntent(getContext()), LOGIN_REQUEST_CODE)
        }
    }

    override fun startIntroSlider() {

    }

    override fun showToastMessage(message: String, duration: Int) {
        Toast.makeText(getContext(), message, duration).show()
    }

    override fun openWebView(url: String) {
        RouteManager.route(context!!, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun searchLoanOnline() {

    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }

    override fun showLoaderIntroDialog() {

    }

    override fun hideLoaderIntroDialog() {

    }

    override fun hideIntroDialog() {

    }

    companion object {

        private val TAB_POSITION = "tab_position"

        fun createInstance(position: Int): TanpaAgunanFragment {
            val bundle = Bundle()
            bundle.putInt(TAB_POSITION, position)
            val tanpaAgunanFragment = TanpaAgunanFragment()
            tanpaAgunanFragment.arguments = bundle
            return tanpaAgunanFragment
        }
    }
}
