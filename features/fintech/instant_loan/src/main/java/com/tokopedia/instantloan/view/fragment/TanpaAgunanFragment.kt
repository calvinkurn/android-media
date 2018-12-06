package com.tokopedia.instantloan.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
import com.tokopedia.instantloan.di.component.InstantLoanComponent
import com.tokopedia.instantloan.network.InstantLoanUrl.Companion.LOAN_AMOUNT_QUERY_PARAM
import com.tokopedia.instantloan.network.InstantLoanUrl.Companion.WEB_LINK_NO_COLLATERAL
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter
import com.tokopedia.user.session.UserSession

import javax.inject.Inject

import com.tokopedia.instantloan.view.fragment.DanaInstantFragment.Companion.LOGIN_REQUEST_CODE


class TanpaAgunanFragment : BaseDaggerFragment(), InstantLoanContractor.View {
    private var mSpinnerLoanAmount: Spinner? = null

    @Inject
    var presenter: InstantLoanPresenter? = null
    @Inject
    var instantLoanAnalytics: InstantLoanAnalytics? = null

    @Inject
    var userSession: UserSession? = null

    private var mCurrentTab: Int = 0
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter!!.attachView(this)
        mCurrentTab = arguments!!.getInt(TAB_POSITION)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachView(this)
    }

    override fun onAttachActivity(context: Context) {
        this.mContext = context
        super.onAttachActivity(context)
    }

    override fun initInjector() {
        val daggerInstantLoanComponent = InstantLoanComponentInstance
                .Companion.get(activity!!.application)
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
        mSpinnerLoanAmount!!.adapter = adapter
    }

    private fun initView(view: View) {

        mSpinnerLoanAmount = view.findViewById(R.id.spinner_value_nominal)
        val mBtnInstantFund = view.findViewById<Button>(R.id.button_instant_fund)
        val mTextAmount = view.findViewById<TextView>(R.id.text_value_amount)
        val mTextDuration = view.findViewById<TextView>(R.id.text_value_duration)
        val mTextProcessingTime = view.findViewById<TextView>(R.id.text_value_processing_time)
        val mTextInterestRate = view.findViewById<TextView>(R.id.text_value_interest_rate)
        val mTextFormDescription = view.findViewById<TextView>(R.id.text_form_description)

        mTextAmount.text = resources.getStringArray(R.array.values_amount)[mCurrentTab]
        mTextDuration.text = resources.getStringArray(R.array.values_duration)[mCurrentTab]
        mTextProcessingTime.text = resources.getStringArray(R.array.values_processing_time)[mCurrentTab]
        mTextInterestRate.text = resources.getStringArray(R.array.values_interest_rate)[mCurrentTab]
        mTextFormDescription.text = resources.getStringArray(R.array.values_description)[mCurrentTab]

        view.findViewById<View>(R.id.button_search_pinjaman).setOnClickListener { view1 ->

            if (mSpinnerLoanAmount!!.selectedItem.toString()
                            .equals(getString(R.string.label_select_nominal), ignoreCase = true)) {
                val errorText = mSpinnerLoanAmount!!.selectedView as TextView
                errorText.setTextColor(Color.RED)
//                return@view.findViewById(R.id.button_search_pinjaman).setOnClickListener
            }

            sendCariPinjamanClickEvent()
            openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                    mSpinnerLoanAmount!!.selectedItem.toString()
                            .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace(".", ""))
        }
    }

    private fun sendCariPinjamanClickEvent() {
        val eventLabel = screenName + " - " + mSpinnerLoanAmount!!.selectedItem.toString()
        instantLoanAnalytics!!.eventCariPinjamanClick(eventLabel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession!!.isLoggedIn) {
                showToastMessage(resources.getString(R.string.login_to_proceed), Toast.LENGTH_SHORT)
            } else {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        mSpinnerLoanAmount!!.selectedItem.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter!!.detachView()
    }

    override fun getScreenName(): String {
        return InstantLoanEventConstants.Screen.TANPA_AGUNAN_SCREEN_NAME
    }

    override fun getAppContext(): Context {
        return getContext()!!.applicationContext
    }

    override fun getActivityContext(): Context? {
        return getContext()
    }

    override fun onSuccessLoanProfileStatus(status: UserProfileLoanEntity) {

    }

    override fun setUserOnGoingLoanStatus(status: Boolean, id: Int) {

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
