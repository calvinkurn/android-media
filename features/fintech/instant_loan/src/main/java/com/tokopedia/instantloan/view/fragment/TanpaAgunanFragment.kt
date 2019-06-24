package com.tokopedia.instantloan.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.InstantLoanComponentInstance
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.data.model.response.GqlFilterData
import com.tokopedia.instantloan.data.model.response.GqlLoanAmountResponse
import com.tokopedia.instantloan.data.model.response.LoanPeriodType
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.LOAN_AMOUNT_QUERY_PARAM
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.WEB_LINK_NO_COLLATERAL
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.activity.SelectLoanParamActivity
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment.Companion.LOGIN_REQUEST_CODE
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter
import com.tokopedia.instantloan.view.ui.WidgetAddRemove
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.content_tanpa_agunan.*
import java.net.URLEncoder
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
    }

    private fun initView(view: View) {

        prepareLoanPeriodTypeList()

        loanPeriodLabelTV = spinner_label_nominal.findViewById(R.id.tv_label_text)
        loanPeriodValueTV = spinner_value_nominal.findViewById(R.id.tv_label_text)
        widgetAddRemove = view.findViewById(R.id.widget_add_remove)

        widgetAddRemove.setButtonClickListener(this)

        loanAmountWarning = widgetAddRemove.findViewById(R.id.tv_warning)

        loanPeriodLabelTV.text = getString(R.string.il_loan_period_type_label)
        spinner_label_nominal.setOnClickListener {

            loanPeriodLabelTV.error = null
            val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodTypeList, null)
            startActivityForResult(intent, LOAN_PERIOD_TYPE)
        }

        loanPeriodValueTV.text = getString(R.string.il_loan_period_value_label)

        spinner_value_nominal.setOnClickListener {
            if (!::selectedLoanPeriodType.isInitialized) {
                loanPeriodLabelTV.error = ""
            } else if (selectedLoanPeriodType.value.equals(DEFAULT_YEAR_VALUE, true)) {
                val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodYearList, null)
                startActivityForResult(intent, LOAN_PERIOD_YEAR)
            } else if (selectedLoanPeriodType.value.equals(DEFAULT_MONTH_VALUE, true)) {
                val intent: Intent = SelectLoanParamActivity.createInstance(context!!, loanPeriodMonthList, null)
                startActivityForResult(intent, LOAN_PERIOD_MONTH)
            }
        }

        view.findViewById<View>(R.id.button_search_pinjaman).setOnClickListener { view1 ->

            if (!presenter.isUserLoggedIn()) {
                navigateToLoginPage()
            } else {
                searchLoanOnline()
            }
        }

    }


    private fun sendCariPinjamanClickEvent() {
        val eventLabel = screenName + " - " + loanPeriodValueTV.tag as String
        instantLoanAnalytics.eventCariPinjamanClick(eventLabel)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn) {
                searchLoanOnline()
            } else {
                showToastMessage(resources.getString(R.string.login_to_proceed), Toast.LENGTH_SHORT)
            }
        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_TYPE) {

            selectedLoanPeriodType = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            if (!loanPeriodLabelTV.text.equals(selectedLoanPeriodType.label)) {

                for (loanPeriodType in loanPeriodTypeList) {
                    loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodType.id
                }
                loanPeriodLabelTV.text = selectedLoanPeriodType.label
                loanPeriodLabelTV.error = null
                resetLoanValueData()
            }

        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_MONTH) {

            selectedLoanPeriodMonth = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            for (loanPeriodType in loanPeriodMonthList) {
                loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodMonth.id
            }
            loanPeriodValueTV.text = selectedLoanPeriodMonth.label
            loanPeriodValueTV.tag = selectedLoanPeriodMonth.value

        } else if (resultCode == RESULT_OK && requestCode == LOAN_PERIOD_YEAR) {

            selectedLoanPeriodYear = data!!.extras?.getParcelable(SelectLoanParamActivity.EXTRA_SELECTED_NAME)!!

            for (loanPeriodType in loanPeriodYearList) {
                loanPeriodType.isSelected = loanPeriodType.id == selectedLoanPeriodYear.id
            }
            loanPeriodValueTV.text = selectedLoanPeriodYear.label
            loanPeriodValueTV.tag = selectedLoanPeriodYear.value

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

        loanPeriodValueTV.text = getString(R.string.il_loan_period_value_label)
    }

    override fun setFilterDataForOnlineLoan(gqlFilterData: GqlFilterData) {

        prepareLoanPeriodListMonth(gqlFilterData.gqlLoanPeriodResponse.loanMonth.min, gqlFilterData.gqlLoanPeriodResponse.loanMonth.max)
        prepareLoanPeriodListYear(gqlFilterData.gqlLoanPeriodResponse.loanYear.min, gqlFilterData.gqlLoanPeriodResponse.loanYear.max)

        loanAmountList = gqlFilterData.gqlLoanAmountResponse

        widgetAddRemove.setMinQuantity(0)
        widgetAddRemove.setMaxQuantity(loanAmountList.size)

        loan_amount_limit.text = "(${loanAmountList.first().label} - ${loanAmountList.last().label})"
        loan_amount_limit.visibility = View.VISIBLE

        widgetAddRemove.setText(loanAmountList.first().label)
        widgetAddRemove.setLoanValue(loanAmountList.first().value.toLong())

    }

    private fun prepareLoanPeriodTypeList() {

        loanPeriodTypeList = ArrayList()
        var loanPeriodType = LoanPeriodType(DEFAULT_MONTH_VALUE, DEFAULT_MONTH_LABEL, 1)
        loanPeriodTypeList.add(loanPeriodType)

        loanPeriodType = LoanPeriodType(DEFAULT_YEAR_VALUE, DEFAULT_YEAR_LABEL, 2)
        loanPeriodTypeList.add(loanPeriodType)

    }

    private fun prepareLoanPeriodListYear(min: Int, max: Int) {

        var loanPeriodYear: LoanPeriodType
        var i = 0
        for (value in min..max) {
            loanPeriodYear = LoanPeriodType(value.toString(), "$value $DEFAULT_YEAR_LABEL", i++)
            loanPeriodYearList.add(loanPeriodYear)
        }

    }

    private fun prepareLoanPeriodListMonth(min: Int, max: Int) {

        var loamPeriodMonth: LoanPeriodType
        var i = 0
        for (value in min..max) {
            loamPeriodMonth = LoanPeriodType(value.toString(), "$value $DEFAULT_MONTH_LABEL", i++)
            loanPeriodMonthList.add(loamPeriodMonth)
        }
    }

    override fun onDecreaseButtonClicked(currentQuantity: Int) {
        if (currentQuantity in 0 until loanAmountList.size) {
            loanAmountWarning.visibility = View.INVISIBLE
            widgetAddRemove.setText(loanAmountList[currentQuantity].label)
            widgetAddRemove.setLoanValue(loanAmountList[currentQuantity].value.toLong())
        } else {
            loanAmountWarning.visibility = View.VISIBLE
            loanAmountWarning.text = String.format(getString(R.string.il_min_loan_amount_warning), loanAmountList[0].label)
        }
    }

    override fun onIncreaseButtonClicked(currentQuantity: Int) {


        if (currentQuantity in 0 until loanAmountList.size) {
            loanAmountWarning.visibility = View.INVISIBLE
            widgetAddRemove.setText(loanAmountList[currentQuantity].label)
            widgetAddRemove.setLoanValue(loanAmountList[currentQuantity].value.toLong())
        } else if (!loanAmountList.isNullOrEmpty()) {
            loanAmountWarning.visibility = View.VISIBLE
            loanAmountWarning.text = String.format(getString(R.string.il_max_loan_amount_warning), loanAmountList.last().label)
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

    override fun navigateToLoginPage() {
        if (activity != null && activity!!.application is InstantLoanRouter) {
            startActivityForResult((activity!!.application as InstantLoanRouter).getLoginIntent(getContext()), LOGIN_REQUEST_CODE)
        }
    }

    override fun showToastMessage(message: String, duration: Int) {
        Toast.makeText(getContext(), message, duration).show()
    }

    override fun openWebView(url: String) {
        RouteManager.route(context!!, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun searchLoanOnline() {
        if (!::selectedLoanPeriodType.isInitialized) {
            loanPeriodLabelTV.error = ""

        } else if (!::selectedLoanPeriodType.isInitialized) {
            loanPeriodLabelTV.error = ""

        } else {
            sendCariPinjamanClickEvent()

            startActivity((activity!!
                    .application as InstantLoanRouter).getWebviewActivityWithIntent(context,
                    URLEncoder.encode(WEB_LINK_NO_COLLATERAL + String.format(LOAN_AMOUNT_QUERY_PARAM,
                            widgetAddRemove.getLoanValue().toString(),
                            selectedLoanPeriodType.value?.toLowerCase(),
                            loanPeriodValueTV.tag as String), "UTF-8")))

        }
    }

    companion object {

        private val TAB_POSITION = "tab_position"

        private val DEFAULT_MONTH_VALUE = "Month"
        private val DEFAULT_MONTH_LABEL = "Bulan"
        private val DEFAULT_YEAR_VALUE = "Year"
        private val DEFAULT_YEAR_LABEL = "Tahun"

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
