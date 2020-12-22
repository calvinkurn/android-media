package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapter
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.model.BudgetingAdsViewModel
import com.tokopedia.topads.view.sheet.TipSheetBudgetList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.*
import java.util.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_TIPS_BIAYA_IKLAN = "click-tips biaya iklan"
private const val CLICK_ATUR_BIAYA_IKLAN = "click-atur biaya iklan"
private const val CLICK_BUDGET = "click - biaya non kata kunci box"
private const val EVENT_CLICK_BUDGET = "biaya yang diinput"
private const val CLICK_SETUP_KEY = "click - setup keyword"

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
    private var isEnable = false
    private var userID: String = ""
    private var shopID = ""
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null
    companion object {
        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        private const val SUGGESTION_BID = "suggest"
        private const val KEYWORD_NAME = "keywordName"
        private const val CURRENT_KEY_TYPE = "currentKeyType"
        private const val ITEM_POSITION = "pos"
        const val SPECIFIC_TYPE = "Spesifik"
        const val BROAD_TYPE = "Luas"
        private const val EXACT_POSITIVE = 21
        const val BROAD_POSITIVE = 11
        private const val FACTOR = 50
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
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(stepperModel?.selectedKeywords, stepperModel?.selectedSuggestBid, this::onClickItem))
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun actionEnable() {
        btn_next.isEnabled = isEnable
    }

    private fun onClickItem(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
        sheet.show(childFragmentManager, "")
        sheet.onSaved = { bid, type, position ->
            bidInfoAdapter.typeList[position] = type
            (bidInfoAdapter.items[position] as BidInfoItemViewModel).data.suggestionBid = bid.toInt()
            bidInfoAdapter.notifyDataSetChanged()
            stepperModel?.selectedSuggestBid?.set(position, bid.toInt())
        }
        sheet.onDelete = { position ->
            stepperModel?.selectedKeywords?.removeAt(position)
            stepperModel?.selectedSuggestBid?.removeAt(position)
            bidInfoAdapter.typeList.removeAt(position)
            bidInfoAdapter.items.removeAt(position)
            bidInfoAdapter.notifyItemRemoved(position)
        }
        updateString()
    }

    private fun prepareBundle(pos: Int): Bundle {
        val bundle = Bundle()
        bundle.putInt(MAX_BID, (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.maxBid)
        bundle.putInt(MIN_BID, (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.minBid)
        bundle.putInt(SUGGESTION_BID, stepperModel?.selectedSuggestBid?.get(pos) ?: 0)
        bundle.putString(KEYWORD_NAME, stepperModel?.selectedKeywords?.get(pos))
        if ((bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.keywordType == SPECIFIC_TYPE) {
            bundle.putInt(CURRENT_KEY_TYPE, EXACT_POSITIVE)
        } else {
            bundle.putInt(CURRENT_KEY_TYPE, BROAD_POSITIVE)
        }
        bundle.putInt(ITEM_POSITION, pos)
        return bundle
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
        stepperModel?.selectedKeywordType = getKeywordType()
        stepperListener?.goToNextPage(stepperModel)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_ATUR_BIAYA_IKLAN, "")
    }

    private fun getKeywordType(): MutableList<Int> {
        val list: MutableList<Int> = mutableListOf()
        bidInfoAdapter.typeList.forEach {
            list.add(it)
        }
        return list
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.topads_common_keyword_list_step))
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
        if (stepperModel?.selectedKeywordType?.isNotEmpty() != false) {
            setRestoreValue()
        }
        val dummyId: MutableList<Int> = mutableListOf()
        val productIds = stepperModel?.selectedProductIds
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("group", dummyId))
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions("product", productIds))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onDefaultSuccessSuggestion)
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion, this::onEmptySuggestion)

    }

    private fun setRestoreValue() {
        val list: MutableList<Int> = mutableListOf()
        stepperModel?.selectedKeywordType?.forEach { it ->
            list.add(it)
        }
        bidInfoAdapter.setType(list)
    }

    private fun onDefaultSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        suggestBidPerClick = data[0].suggestionBid
        maxBid = data[0].maxBid
        minBid = data[0].minBid
        val list: MutableList<Int> = mutableListOf()
        stepperModel?.selectedKeywords?.forEach { _ ->
            list.add(EXACT_POSITIVE)
        }
        bidInfoAdapter.setType(list)
        if (stepperModel?.finalBidPerClick != -1 && stepperModel?.finalBidPerClick != 0)
            budget.textFieldInput.setText(stepperModel?.finalBidPerClick.toString())
        else
            budget.textFieldInput.setText(suggestBidPerClick.toString())

        if (budget.textFieldInput.text.toString().replace(",", "").toInt() in (minBid + 1) until maxBid) {
            setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
            isEnable = true
            actionEnable()
        }
    }

    private fun onEmptySuggestion() {
        headlineList?.visibility = View.GONE
        bidInfoAdapter.items.add(BidInfoEmptyViewModel())
        bidInfoAdapter.notifyDataSetChanged()
    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        stepperModel?.selectedKeywords?.forEach { _ ->
            bidInfoAdapter.items.add(BidInfoItemViewModel(data[0]))
        }
        minSuggestKeyword = data[0].minBid
        maxSuggestKeyword = data[0].maxBid
        bidInfoAdapter.setMinimumBid(minSuggestKeyword)
        bidInfoAdapter.notifyDataSetChanged()
        loading.visibility = View.GONE
        updateString()
    }

    private fun updateString() {
        if (bidInfoAdapter.items.count() == 0)
            onEmptySuggestion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headlineList?.visibility = View.VISIBLE
        loading?.visibility = View.VISIBLE
        userID = UserSession(view.context).userId
        shopID = UserSession(view.context).shopId
        btn_next?.setOnClickListener {
            gotoNextPage()
        }
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.tip_biaya_iklan)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_ic_tips))
        }

        tip_btn?.addItem(tooltipView)
        tip_btn?.setOnClickListener {
            val tipSheetBugList = TipSheetBudgetList.newInstance()
            tipSheetBugList.show(fragmentManager!!, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_BIAYA_IKLAN, shopID,userID)
        }

        budget?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val eventLabel = "$shopID - $EVENT_CLICK_BUDGET"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET, eventLabel, userID)
            }
        }

        budget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                stepperModel?.finalBidPerClick = result
                when {
                    result < minBid -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        isEnable = false
                    }
                    result > maxBid -> {
                        isEnable = false
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }

                    result % FACTOR != 0 -> {
                        isEnable = false
                        setMessageErrorField(getString(R.string.topads_common_error_multiple_50), FACTOR, true)
                    }
                    else -> {
                        isEnable = true
                        setMessageErrorField(getString(R.string.recommendated_bid_message_new), suggestBidPerClick, false)
                    }
                }
                actionEnable()
            }
        })
        bid_list.adapter = bidInfoAdapter
        bid_list.layoutManager = LinearLayoutManager(context)
    }

    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(Html.fromHtml(String.format(error, bid)))
    }

}
