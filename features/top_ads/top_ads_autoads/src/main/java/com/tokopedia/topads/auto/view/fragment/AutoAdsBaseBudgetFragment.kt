package com.tokopedia.topads.auto.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.widget.Range
import com.tokopedia.topads.auto.view.widget.RangeSeekBar
import com.tokopedia.topads.common.activity.NoCreditActivity
import com.tokopedia.topads.common.activity.SuccessActivity
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.util.Utils.locale
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Author errysuprayogi on 07,May,2019
 */
abstract class AutoAdsBaseBudgetFragment : BaseDaggerFragment() {
    lateinit var seekBar: RangeSeekBar
    lateinit var priceRange: TextView
    lateinit var priceEditText: TextFieldUnify
    lateinit var budgetViewModel: DailyBudgetViewModel
    lateinit var progressBar: LoaderUnify
    lateinit var errorText: TextView
    lateinit var rangeStart: TextView
    lateinit var rangeEnd: TextView
    lateinit var btnSubmit: UnifyButton
    lateinit var tipBtn: FloatingButtonUnify
    private var lowClickDivider = 1
    private var minDailyBudget = 1
    private var maxDailyBudget = 1

    var topAdsDeposit: Int = 0

    val requestType = "auto_ads"
    val source = "update_auto_ads"
    var shopStatus = 0

    @Inject
    lateinit var factory: DailyBudgetViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    abstract fun getLayoutId(): Int

    abstract fun setUpView(view: View)

    abstract fun showLoading()

    abstract fun hideLoading()

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            budgetViewModel = ViewModelProviders.of(this, factory).get(DailyBudgetViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLoading()
        budgetViewModel.getTopAdsDeposit()
        budgetViewModel.topAdsDeposit.observe(viewLifecycleOwner, Observer {
            topAdsDeposit = it
            budgetViewModel.getBudgetInfo(requestType, source, this::onSuccessBudgetInfo)
        })
        budgetViewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            if (topAdsDeposit <= 0) {
                insufficientCredit()
            } else
                eligible()
        })

        priceEditText.textFieldInput.addTextChangedListener(
                object : NumberTextWatcher(priceEditText.textFieldInput, "0") {

                    override fun afterTextChanged(s: Editable) {
                        priceEditText.textFieldInput.removeTextChangedListener(this)
                        var text = replace(s.toString())
                        if (text.isEmpty())
                            text = "0"
                        try {
                            priceEditText.textFieldInput.setText(convertToCurrencyString(text.toLong()))
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }

                        val error = budgetViewModel.checkBudget(text.toDouble(), minDailyBudget.toDouble(), maxDailyBudget.toDouble())
                        if (!TextUtils.isEmpty(error)) {
                            errorText.visibility = View.VISIBLE
                            errorText.text = error
                            btnSubmit.isEnabled = false
                        } else {
                            errorText.visibility = View.GONE
                            btnSubmit.isEnabled = true                                                                                          
                        }
                        priceEditText.textFieldInput.addTextChangedListener(this)
                    }
                })
    }


    private fun onSuccessBudgetInfo(response: ResponseBidInfo.Result) {
        val data = response.topadsBidInfo.data[0]
        var budget = data.minDailyBudget
        val status = arguments!!.getInt(KEY_AUTOADS_STATUS, 0)
        if (status == AutoAdsStatus.STATUS_ACTIVE || status == AutoAdsStatus.STATUS_NOT_DELIVERED) {
            budget = arguments!!.getInt(KEY_DAILY_BUDGET, 0)
        }
        rangeStart.text = data.minDailyBudgetFmt
        rangeEnd.text = data.maxDailyBudgetFmt
        minDailyBudget = data.minDailyBudget
        maxDailyBudget = data.maxDailyBudget
        priceEditText.textFieldInput.setText(data.minDailyBudgetFmt.replace("Rp", ""))
        shopStatus = data.shopStatus
        seekBar.range = Range(data.minDailyBudget, data.maxDailyBudget, 1000)
        seekBar.value = budget
        budgetViewModel.topadsStatisticsEstimationPotentialReach(this::onSuccessPotentialEstimation, userSession.shopId, source)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                estimateImpression(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    fun onSuccessPotentialEstimation(data: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem) {
        lowClickDivider = data.lowClickDivider
        priceRange.text = convertToCurrencyString(replace(getPotentialReach().toString()).toLong())
        hideLoading()
    }

    private fun getPotentialReach(): CharSequence? {
        return budgetViewModel.getPotentialImpressionGQL(replace(priceEditText.textFieldInput.text.toString()).toInt()
                , lowClickDivider)
    }

    fun estimateImpression(progress: Int) {
        priceEditText.textFieldInput.setText(progress.toString())
        priceRange.text = convertToCurrencyString(replace(getPotentialReach().toString()).toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        priceRange = view.findViewById(R.id.price_range)
        priceEditText = view.findViewById(R.id.budgetEditText)
        seekBar = view.findViewById(R.id.seekbar)
        progressBar = view.findViewById(R.id.loading)
        errorText = view.findViewById(R.id.error_text)
        btnSubmit = view.findViewById(R.id.btn_submit)
        rangeStart = view.findViewById(R.id.range_start)
        rangeEnd = view.findViewById(R.id.range_end)
        tipBtn = view.findViewById(R.id.tip_btn)
        setUpView(view)
        setListener()
        return view
    }

    open fun setListener() {
        priceEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val imm = activity!!
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun activatedAds() {
        val budget = replace(priceEditText.textFieldInput.text.toString()).toInt()
        budgetViewModel.postAutoAds(AutoAdsParam(AutoAdsParam.Input(
                TOGGLE_ON,
                CHANNEL,
                budget,
                userSession.shopId.toInt(),
                SOURCE
        )))
    }

    private fun eligible() {
        val intent = Intent(context, SuccessActivity::class.java).apply {
            if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun insufficientCredit() {
        val intent = Intent(context, NoCreditActivity::class.java).apply {
            if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    companion object {
        val KEY_DAILY_BUDGET = "BUDGET"
        val KEY_AUTOADS_STATUS = "AUTOADS_STATUS"
        val TOGGLE_ON = "toggle_on"
        val TOGGLE_OFF = "toggle_off"
        val CHANNEL = "topchat"
        val SOURCE = "sellerapp_autoads_creation"
    }

    fun replace(text: String): String {
        return text.replace(",", "").replace(".", "")
                .replace("Rp", "").trim()
    }


    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value))
    }
}
