package com.tokopedia.topads.auto.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.TextView

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.seller.common.widget.PrefixEditText
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.factory.DailyBudgetViewModelFactory
import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModel
import com.tokopedia.topads.auto.view.widget.Range
import com.tokopedia.topads.auto.view.widget.RangeSeekBar
import com.tokopedia.topads.common.constant.TopAdsAddingOption
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

    @Inject
    lateinit var factory: DailyBudgetViewModelFactory


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

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                priceRange.text = budgetViewModel.getPotentialImpression(10000.toDouble(),
                        20000.toDouble(), progress.toDouble())
                priceEditText.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
//        budgetViewModel.bidInfo.observe(this, Observer { topadsBidInfo ->
//            val bidInfo = topadsBidInfo!!.data[0]
//            seekBar.range = Range(bidInfo.minBid, bidInfo.maxBid, bidInfo.minBid)
//            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                    priceRange.text = budgetViewModel.getPotentialImpression(bidInfo.minBid.toDouble(),
//                            bidInfo.maxBid.toDouble(), progress.toDouble())
//                    priceEditText.setText(progress.toString())
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar) {
//
//                }
//
//                override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//                }
//            })
//        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        priceRange = view.findViewById(R.id.price_range)
        budgetInputLayout = view.findViewById(R.id.input_layout_budget_price)
        priceEditText = view.findViewById(R.id.edit_text_max_price)
        seekBar = view.findViewById(R.id.seekbar)
        setUpView(view)
        setListener()
        return view
    }

    open fun setListener() {
        seekBar.range = Range(MIN_BUDGET, MAX_BUDGET, 1000)
        seekBar.value = 15000
        priceEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val imm = activity!!
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        priceEditText.addTextChangedListener(object : NumberTextWatcher(priceEditText, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val error = budgetViewModel.checkBudget(number, MIN_BUDGET.toDouble(), MAX_BUDGET.toDouble())
                if (!TextUtils.isEmpty(error)) {
                    budgetInputLayout.error = error
                } else {
                    budgetInputLayout.error = null
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AD_OPTION) {
            if (data != null) {
                when (data.getIntExtra(SELECTED_OPTION, -1)) {
                    TopAdsAddingOption.GROUP_OPT -> onSummaryGroupClicked()
                    TopAdsAddingOption.PRODUCT_OPT -> gotoCreateProductAd()
                    TopAdsAddingOption.KEYWORDS_OPT -> gotoCreateKeyword()
                }
                activity!!.finishAffinity()
            }
        }
    }

    private fun gotoCreateProductAd() {
        val router = activity!!.application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsGroupNewPromoIntent(activity!!))
        } else {
            router.openTopAdsDashboardApplink(activity!!)
        }
    }

    private fun onSummaryGroupClicked() {
        val router = activity!!.application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsGroupAdListIntent(activity!!))
        } else {
            router.openTopAdsDashboardApplink(activity!!)
        }
    }

    private fun gotoCreateKeyword() {
        val router = activity!!.application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsKeywordNewChooseGroupIntent(activity!!, true, null))
        } else {
            router.openTopAdsDashboardApplink(activity!!)
        }
    }

    companion object {

        val REQUEST_CODE_AD_OPTION = 3
        val SELECTED_OPTION = "selected_option"
        val MIN_BUDGET = 10000
        val MAX_BUDGET = 20000
    }
}
