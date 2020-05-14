package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfo
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.internal.AutoAdsStatus
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment.Companion.CHANNEL
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment.Companion.TOGGLE_ON
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.widget.Range
import com.tokopedia.topads.auto.view.widget.TipSheetBudgetList
import com.tokopedia.topads.common.activity.NoCreditActivity
import com.tokopedia.topads.common.activity.SuccessActivity
import com.tokopedia.topads.common.constant.TopAdsReasonOption
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_autoads_create_auto_ad_layout.*
import javax.inject.Inject

class CreateAutoAdsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var factory: DailyBudgetViewModelFactory
    lateinit var budgetViewModel: DailyBudgetViewModel
    val REQUEST_CODE_CONFIRMATION = 8903
    val KEY_DAILY_BUDGET = "BUDGET"
    val KEY_AUTOADS_STATUS = "AUTOADS_STATUS"

    @Inject
    lateinit var userSession: UserSessionInterface
    val requestType = "auto_ads"
    val source = "sellerapp_autoads_creation"
    var shopStatus = 0
    val SOURCE = "sellerapp_autoads_creation"
    private var MORE_INFO = " Info Selengkapnya"
    private var lowImpression = 0.0
    private var highImpression = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_autoads_create_auto_ad_layout, container, false)
        budgetViewModel = ViewModelProviders.of(this, factory).get(DailyBudgetViewModel::class.java)
        return view
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.visibility = View.VISIBLE

        budgetViewModel.getBudgetInfo(userSession.shopId.toInt(), requestType, source, this::onSuccessBudgetInfo, this::onErrorBudgetInfo)
        btn_submit.setOnClickListener {
            val budget = budgetEditText.textWithoutPrefix.replace(",", "").toInt()
            budgetViewModel.postAutoAds(AutoAdsParam(AutoAdsParam.Input(
                    TOGGLE_ON,
                    CHANNEL,
                    budget,
                    userSession.shopId.toInt(),
                    SOURCE
            )))
        }
        budgetViewModel.autoAdsData.observe(this, Observer {
            when (it!!.adsInfo.reason) {
                TopAdsReasonOption.INSUFFICIENT_CREDIT -> insufficientCredit(it.adsInfo.message)
                TopAdsReasonOption.ELIGIBLE -> eligible()
                TopAdsReasonOption.NOT_ELIGIBLE -> notEligible()
                else -> activity!!.finish()
            }
        })

        tip_btn.setOnClickListener {
            TipSheetBudgetList.newInstance(it.context).show()

        }
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(resources.getColor(com.tokopedia.design.R.color.tkpd_main_green), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))

            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text.movementMethod = LinkMovementMethod.getInstance()
        info_text.append(spannableText)

    }

    fun onSuccessPotentialEstimation(data: EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem) {
        lowImpression = data.lowImpMultiplier
        highImpression = data.highImpMultiplier
        price_range.text = getPotentialReach()
        loading.visibility = View.GONE
    }

    private fun notEligible() {
    }

    private fun eligible() {
        val intent = Intent(context, SuccessActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun insufficientCredit(message: String) {

        val intent = Intent(context, NoCreditActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    fun onSuccessBudgetInfo(response: TopadsBidInfo.Response) {
        val data = response.bidInfo.data[0]
        var budget = data.minDailyBudget
        val status = arguments!!.getInt(KEY_AUTOADS_STATUS, 0)
        if (status == AutoAdsStatus.STATUS_ACTIVE || status == AutoAdsStatus.STATUS_NOT_DELIVERED) {
            budget = arguments!!.getInt(KEY_DAILY_BUDGET, 0)
        }
        range_start.text = data.minDailyBudgetFmt
        range_end.text = data.maxDailyBudgetFmt
        budgetEditText.setText(data.minDailyBudgetFmt.replace(".",",").replace("Rp",""))
        shopStatus = data.shopStatus
        seekbar.range = Range(data.minDailyBudget, data.maxDailyBudget, 1000)
        seekbar.value = budget
        budgetViewModel.topadsStatisticsEstimationPotentialReach(this::onSuccessPotentialEstimation, userSession.shopId, source)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                estimateImpression(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        budgetEditText.addTextChangedListener(object : NumberTextWatcher(budgetEditText, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val error = budgetViewModel.checkBudget(number, data.minDailyBudget.toDouble(), data.maxDailyBudget.toDouble())
                if (!TextUtils.isEmpty(error)) {
                    error_text.visibility = View.VISIBLE
                    error_text.text = error
                    btn_submit.isEnabled = false
                } else {
                    error_text.visibility = View.GONE
                    btn_submit.isEnabled = true
                }
            }
        })
    }

    private fun getPotentialReach(): CharSequence? {
        return budgetViewModel.getPotentialImpressionGQL(budgetEditText.textWithoutPrefix.replace(",", "").toDouble()
                , highImpression)
    }

    fun onErrorBudgetInfo() {

    }

    private fun estimateImpression(progress: Int) {
        budgetEditText.setText(progress.toString())
        price_range.text = getPotentialReach()
    }

    override fun getScreenName(): String? {
        return CreateAutoAdsFragment::class.java.name
    }

    companion object {

        fun newInstance(): CreateAutoAdsFragment {

            val args = Bundle()
            val fragment = CreateAutoAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}