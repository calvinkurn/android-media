package com.tokopedia.topads.auto.view.fragment

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.seller.common.widget.PrefixEditText
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.data.entity.BidInfoData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.widget.Range
import com.tokopedia.topads.auto.view.widget.RangeSeekBar
import com.tokopedia.topads.common.constant.TopAdsAddingOption
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.layout_setting_daily_budget_ads.*
import kotlinx.android.synthetic.main.layout_start_daily_budget.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 07,May,2019
 */
abstract class DailyBudgetFragment : BaseDaggerFragment() {
    lateinit var seekBar: RangeSeekBar
    lateinit var priceRange: TextView
    lateinit var priceEditText: PrefixEditText
    lateinit var budgetViewModel: DailyBudgetViewModel
    lateinit var budgetInputLayout: TextInputLayout
    lateinit var progressBar: ProgressBar
    lateinit var cardView: CardView

    val requestType = "auto_ads"
    val source = "update_auto_ads"

    @Inject
    lateinit var factory: DailyBudgetViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    abstract fun getLayoutId(): Int

    abstract fun setUpView(view: View)

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
        budgetViewModel.getBudgetInfo(userSession.shopId.toInt(), requestType, source)
        budgetViewModel.budgetInfoData.observe(this@DailyBudgetFragment, Observer {
            val data = it!!.get(0)
            estimateImpression(data, data.minDailyBudget)
            seekBar.range = Range(data.minDailyBudget, data.maxDailyBudget, 1000)
            seekBar.value = data.minDailyBudget
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    estimateImpression(data, progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
            priceEditText.addTextChangedListener(object : NumberTextWatcher(priceEditText, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val error = budgetViewModel.checkBudget(number, data.minDailyBudget.toDouble(), data.maxDailyBudget.toDouble())
                    if (!TextUtils.isEmpty(error)) {
                        budgetInputLayout.error = error
                    } else {
                        budgetInputLayout.error = null
                    }
                }
            })
            hideLoading()
        })
    }

    private fun estimateImpression(data: BidInfoData, progress: Int) {
        priceRange.text = budgetViewModel.getPotentialImpression(data.estimation.minBid.toDouble(),
                data.estimation.maxBid.toDouble(), progress.toDouble())
        priceEditText.setText(progress.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        priceRange = view.findViewById(R.id.price_range)
        budgetInputLayout = view.findViewById(R.id.input_layout_budget_price)
        priceEditText = view.findViewById(R.id.edit_text_max_price)
        seekBar = view.findViewById(R.id.seekbar)
        cardView = view.findViewById(R.id.card_layout)
        progressBar = view.findViewById(R.id.loading)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AD_OPTION) {
            if (data != null) {

                when (data.getIntExtra(SELECTED_OPTION, -1)) {
                    TopAdsAddingOption.GROUP_OPT -> (activity as AutoAdsBaseActivity).onSummaryGroupClicked()
                    TopAdsAddingOption.PRODUCT_OPT -> (activity as AutoAdsBaseActivity).gotoCreateProductAd()
                    TopAdsAddingOption.KEYWORDS_OPT -> (activity as AutoAdsBaseActivity).gotoCreateKeyword()
                }
                activity!!.finish()
            }
        }
    }

    fun showLoading(){
        cardView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        post_btn.isEnabled = false
    }

    fun hideLoading(){
        progressBar.visibility = View.GONE
        cardView.visibility = View.VISIBLE
        post_btn.isEnabled = true
    }

    fun activatedAds(){
        budgetViewModel.postAutoAdsStatus(AutoAdsParam(AutoAdsParam.Input(
                "toggle_on",
                "topchat",
                priceEditText.textWithoutPrefix.replace(",", "").toInt(),
                userSession.shopId.toInt(),
                "one-click-promo"
        )))
    }

    fun deactivedAds(){
        budgetViewModel.postAutoAdsStatus(AutoAdsParam(AutoAdsParam.Input(
                "toggle_off",
                "topchat",
                priceEditText.textWithoutPrefix.replace(",", "").toInt(),
                userSession.shopId.toInt(),
                "one-click-promo"
        )))
    }

    companion object {

        val REQUEST_CODE_AD_OPTION = 3
        val SELECTED_OPTION = "selected_option"
    }
}
