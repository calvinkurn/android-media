package com.tokopedia.instantloan.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
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
import com.tokopedia.instantloan.data.model.response.*
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.activity.SelectLoanParamActivity
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment.Companion.LOGIN_REQUEST_CODE
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter
import com.tokopedia.instantloan.view.ui.WidgetAddRemove
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.content_tanpa_agunan.*
import javax.inject.Inject


class TanpaAgunanFragment : BaseDaggerFragment(), OnlineLoanContractor.View, WidgetAddRemove.OnButtonClickListener {


    @Inject
    lateinit var presenter: OnlineLoanPresenter
    @Inject
    lateinit var instantLoanAnalytics: InstantLoanAnalytics

    @Inject
    lateinit var userSession: UserSession

    private lateinit var loanPeriodLabelTV: TextView
    private lateinit var widgetAddRemove: WidgetAddRemove
    private lateinit var loanAmountWarning: TextView
    private lateinit var loanPeriodValueTV: TextView
    private lateinit var selectedLoanPeriodType: LoanPeriodType
    private lateinit var selectedLoanPeriodMonth: LoanPeriodType
    private lateinit var selectedLoanPeriodYear: LoanPeriodType
    private lateinit var selectedLoanAmountResponse: GqlLoanAmountResponse

    private var currentLoanPeriodType: Int = 0

    private var loanPeriodMonthList: ArrayList<LoanPeriodType> = ArrayList()
    private var loanPeriodYearList: ArrayList<LoanPeriodType> = ArrayList()
    private var loanPeriodTypeList: ArrayList<LoanPeriodType> = ArrayList()
    private var loanAmountList: ArrayList<GqlLoanAmountResponse> = ArrayList()
    private var mCurrentTab: Int = 0
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        mCurrentTab = arguments?.getInt(TAB_POSITION) ?: 0
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
//        spinner_value_nominal!!.adapter = adapter
    }

    private fun initView(view: View) {

        prepareLoanPeriodTypeList()

        loanPeriodLabelTV = spinner_label_nominal.findViewById(R.id.tv_label_text)
        loanPeriodValueTV = spinner_value_nominal.findViewById(R.id.tv_label_text)
        widgetAddRemove = view.findViewById(R.id.widget_add_remove)

        widgetAddRemove.setButtonClickListener(this)


        loanAmountWarning = widgetAddRemove.findViewById(R.id.tv_warning)

        loanPeriodLabelTV.text = "Pilih"
        spinner_label_nominal.setOnClickListener {

            val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodTypeList, null)
            startActivityForResult(intent, LOAN_PERIOD_TYPE)
        }

        loanPeriodValueTV.text = "Pilih Jumlah"

        spinner_value_nominal.setOnClickListener {

            if (!::selectedLoanPeriodType.isInitialized) {
                loanPeriodLabelTV.error = ""

            } else if (selectedLoanPeriodType.value.equals("Year", true)) {

                val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodYearList, null)
                startActivityForResult(intent, LOAN_PERIOD_YEAR)

            } else if (selectedLoanPeriodType.value.equals("Month", true)) {

                val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodMonthList, null)
                startActivityForResult(intent, LOAN_PERIOD_MONTH)

            }

        }
        /*text_value_amount.text = resources.getStringArray(R.array.values_amount)[mCurrentTab]
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
        }*/
    }


    /*private fun sendCariPinjamanClickEvent() {
        val eventLabel = screenName + " - " + spinner_value_nominal!!.selectedItem.toString()
        instantLoanAnalytics.eventCariPinjamanClick(eventLabel)
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn) {
                showToastMessage(resources.getString(R.string.login_to_proceed), Toast.LENGTH_SHORT)
            } else {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        spinner_value_nominal!!.selectedItem.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            }
        }
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn) {
                showToastMessage(resources.getString(R.string.login_to_proceed), Toast.LENGTH_SHORT)
            } else {
                /*openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        spinner_value_nominal!!.selectedItem.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])*/
            }
        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_TYPE) {

            selectedLoanPeriodType = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            if (!loanPeriodLabelTV.text.equals(selectedLoanPeriodType.label)) {

                for (loanPeriodType in loanPeriodTypeList) {
                    loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodType.id
                }
                loanPeriodLabelTV.text = selectedLoanPeriodType.label

                resetLoanValueData()


            }


        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_MONTH) {

            selectedLoanPeriodMonth = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            for (loanPeriodType in loanPeriodMonthList) {
                loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodMonth.id
            }
            loanPeriodValueTV.text = selectedLoanPeriodMonth.label

        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_YEAR) {

            selectedLoanPeriodYear = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            for (loanPeriodType in loanPeriodYearList) {
                loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodYear.id
            }
            loanPeriodValueTV.text = selectedLoanPeriodYear.label

        }
    }

    private fun resetLoanValueData() {

        for (loanData in loanPeriodYearList) {
            loanData.isSelected = false
            selectedLoanPeriodYear = LoanPeriodType("", "", -1, false)
        }

        for (loanData in loanPeriodMonthList) {
            loanData.isSelected = false
            selectedLoanPeriodMonth = LoanPeriodType("", "", -1, false)
        }

        loanPeriodValueTV.text = "Pilih Jumlah"
    }

    override fun setFilterDataForOnlineLoan(gqlFilterData: GqlFilterData) {

        prepareLoanPeriodListMonth(gqlFilterData.gqlLoanPeriodResponse.loanMonth.min, gqlFilterData.gqlLoanPeriodResponse.loanMonth.max)
        prepareLoanPeriodListYear(gqlFilterData.gqlLoanPeriodResponse.loanYear.min, gqlFilterData.gqlLoanPeriodResponse.loanYear.max)

        gqlFilterData.gqlLoanAmountResponse.sortBy { it.value }

        loanAmountList = gqlFilterData.gqlLoanAmountResponse


        widgetAddRemove.setMinQuantity(0)
        widgetAddRemove.setMaxQuantity(loanAmountList.size)

        loan_amount_limit.text = "(${loanAmountList[0].label} - ${loanAmountList[loanAmountList.lastIndex].label})"
        loan_amount_limit.visibility = View.VISIBLE

        widgetAddRemove.setText(loanAmountList[0].label)

    }

    private fun prepareLoanPeriodTypeList() {

        loanPeriodTypeList = ArrayList()
        var loanPeriodType = LoanPeriodType("Month", "Bulan", 1)
        loanPeriodTypeList.add(loanPeriodType)

        loanPeriodType = LoanPeriodType("Year", "Tahun", 2)
        loanPeriodTypeList.add(loanPeriodType)

    }

    private fun prepareLoanPeriodListYear(min: Int, max: Int) {

        var loanPeriodYear: LoanPeriodType
        var i = 0
        for (value in min..max) {
            loanPeriodYear = LoanPeriodType(value.toString(), "$value Tahun", i++)
            loanPeriodYearList.add(loanPeriodYear)
        }

    }

    private fun prepareLoanPeriodListMonth(min: Int, max: Int) {

        var loamPeriodMonth: LoanPeriodType
        var i = 0
        for (value in min..max) {
            loamPeriodMonth = LoanPeriodType(value.toString(), "$value Bulan", i++)
            loanPeriodMonthList.add(loamPeriodMonth)
        }
    }

    override fun onDecreaseButtonClicked(currentQuantity: Int) {
        if (currentQuantity in 0 until loanAmountList.size) {
            loanAmountWarning.visibility = View.INVISIBLE
            widgetAddRemove.setText(loanAmountList[currentQuantity].label)
        } else {
            loanAmountWarning.visibility = View.VISIBLE
            loanAmountWarning.text = "Minimal Pinjaman ${loanAmountList[0].label}"
        }
    }

    override fun onIncreaseButtonClicked(currentQuantity: Int) {


        if (currentQuantity in 0 until loanAmountList.size) {
            loanAmountWarning.visibility = View.INVISIBLE
            widgetAddRemove.setText(loanAmountList[currentQuantity].label)
        } else {
            loanAmountWarning.visibility = View.VISIBLE
            loanAmountWarning.text = "Maksimal Pinjaman ${loanAmountList.last().label} "
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

        private val LOAN_PERIOD_TYPE = 12
        private val LOAN_PERIOD_MONTH = 13
        private val LOAN_PERIOD_YEAR = 14

        fun createInstance(position: Int): TanpaAgunanFragment {
            val bundle = Bundle()
            bundle.putInt(TAB_POSITION, position)
            val tanpaAgunanFragment = TanpaAgunanFragment()
            tanpaAgunanFragment.arguments = bundle
            return tanpaAgunanFragment
        }
    }
}
