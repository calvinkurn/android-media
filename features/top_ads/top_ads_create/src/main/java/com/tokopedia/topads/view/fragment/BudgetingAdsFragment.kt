package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.DataSuggestions
import com.tokopedia.topads.data.response.ResponseBidInfo
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapter
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.model.BudgetingAdsViewModel
import com.tokopedia.topads.view.sheet.InfoSheetBudgetList
import com.tokopedia.topads.view.sheet.TipSheetBudgetList
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.budget
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.tip_btn
import java.lang.NumberFormatException
import java.util.ArrayList
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
class BudgetingAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BudgetingAdsViewModel
    private lateinit var bidInfoAdapter: BidInfoAdapter
    private var maxBid = 0
    private var minBid = 0
    private var minSuggestKeyword = 0
    private var maxSuggestKeyword = 0
    private var suggestBidPerClick = 0
    private var bidMap = mutableMapOf<String, Int>()

    companion object {
        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        fun createInstance(): Fragment {
            val fragment = BudgetingAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BudgetingAdsViewModel::class.java)
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(stepperModel!!.selectedKeywords, stepperModel!!.selectedSuggestBid, this::onClickCloseButton, this::onEdit, this::actionEnable))
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    private fun actionEnable(flag: Boolean) {
        btn_next.isEnabled = flag
    }

    private fun onEdit(): MutableMap<String, Int> {
        bidMap[MIN_BID] = minSuggestKeyword
        bidMap[MAX_BID] = maxSuggestKeyword
        return bidMap
    }

    private fun onClickCloseButton(pos: Int) {
        bidInfoAdapter.items.removeAt(pos)
        stepperModel?.selectedKeywords?.removeAt(pos)
        stepperModel?.selectedSuggestBid?.removeAt(pos)
        bidInfoAdapter.notifyDataSetChanged()
        updateString()
    }


    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        try {
            stepperModel?.finalBidPerClick = Integer.parseInt(budget.textFieldInput.text.toString().replace(",", ""))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        stepperModel?.suggestedBidPerClick = suggestBidPerClick
        stepperModel?.maxBid = maxBid
        stepperModel?.minBid = minBid
        stepperModel?.minSuggestBidKeyword = minSuggestKeyword
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
    }

    override fun getScreenName(): String {
        return BudgetingAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_budget_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dummyId: MutableList<Int> = mutableListOf()
        val productIds = stepperModel?.selectedProductIds
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("group", dummyId))
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions("product", productIds))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onDefaultSuccessSuggestion, this::onErrorSuggestion, this::oneDefaultEmptySuggestion)
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion, this::onErrorSuggestion, this::onEmptySuggestion)

    }

    private fun oneDefaultEmptySuggestion() {
    }

    private fun onDefaultSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        suggestBidPerClick = data[0].suggestionBid
        maxBid = data[0].maxBid
        minBid = data[0].minBid
        if (stepperModel?.finalBidPerClick != -1)
            budget.textFieldInput.setText(stepperModel?.finalBidPerClick.toString())
        else
            budget.textFieldInput.setText(suggestBidPerClick.toString())

        if (budget.textFieldInput.text.toString().replace(",", "").toInt() in (minBid + 1) until maxBid) {
            setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
            actionEnable(true)
        }
    }

    private fun onEmptySuggestion() {
        bidInfoAdapter.items.add(BidInfoEmptyViewModel())
        bidInfoAdapter.notifyDataSetChanged()
    }

    private fun onErrorSuggestion(throwable: Throwable) {

    }

    private fun onSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        stepperModel?.selectedKeywords?.forEach { _ ->
            bidInfoAdapter.items.add(BidInfoItemViewModel(data[0]))
        }
        minSuggestKeyword = data[0].minBid
        maxSuggestKeyword = data[0].maxBid
        bidInfoAdapter.notifyDataSetChanged()
        loading.visibility = View.GONE
        updateString()
    }

    private fun updateString() {

        selected_keyword.text = String.format(getString(R.string.kata_kunci_count), bidInfoAdapter.items.count())
        if (bidInfoAdapter.items.count() == 0)
            onEmptySuggestion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.visibility = View.VISIBLE
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            TipSheetBudgetList.newInstance(it.context).show()
        }
        btn_info.setOnClickListener {
            InfoSheetBudgetList.newInstance(it.context).show()
        }

        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                stepperModel?.finalBidPerClick = result
                when {
                    result < minBid -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        actionEnable(false)
                    }
                    result > maxBid -> {
                        actionEnable(false)
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }
                    else -> {
                        actionEnable(true)
                        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
                    }
                }
            }
        })
        bid_list.adapter = bidInfoAdapter
        bid_list.layoutManager = LinearLayoutManager(context)
    }


    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(String.format(error, bid))

    }

    override fun updateToolBar() {
        (activity as StepperActivity).updateToolbarTitle(getString(R.string.bid_info_step))

    }
}