package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var suggestBidPerClick = 0

    companion object {

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
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(stepperModel!!.selectedKeywords, stepperModel!!.selectedSuggestBid, this::onClickCloseButton, this::onEdit))
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    private fun onEdit(): Int {
        return minSuggestKeyword
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
            stepperModel?.finalBidPerClick = Integer.parseInt(budget.text.toString())
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
        recom_txt.text = String.format(getString(R.string.recommendated_bid_message), suggestBidPerClick)
        if (stepperModel?.finalBidPerClick != -1)
            budget.setText(stepperModel?.finalBidPerClick.toString())
        else
            budget.setText(suggestBidPerClick.toString())
        maxBid = data[0].maxBid
        minBid = data[0].minBid
        error_text.text = String.format(getString(R.string.min_bid_error), minBid)

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
        bidInfoAdapter.notifyDataSetChanged()
        updateString()
    }

    private fun updateString() {

        selected_keyword.text = String.format(getString(R.string.kata_kunci_count), bidInfoAdapter.items.count())
        if (bidInfoAdapter.items.count() == 0)
            onEmptySuggestion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            TipSheetBudgetList.newInstance(it.context).show()
        }
        btn_info.setOnClickListener {
            InfoSheetBudgetList.newInstance(it.context).show()
        }
        budget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val result = Integer.parseInt(budget.text.toString())
                    stepperModel?.finalBidPerClick = result
                    if (result < minBid) {
                        error_text.visibility = View.VISIBLE
                        recom_txt.visibility = View.GONE
                    } else {
                        error_text.visibility = View.GONE
                        recom_txt.visibility = View.VISIBLE
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()

                }

            }
            override fun afterTextChanged(s: Editable?) {

            }

        })

        bid_list.adapter = bidInfoAdapter
        bid_list.layoutManager = LinearLayoutManager(context)
    }

    override fun updateToolBar() {
        (activity as StepperActivity).updateToolbarTitle(getString(R.string.bid_info_step))

    }
}