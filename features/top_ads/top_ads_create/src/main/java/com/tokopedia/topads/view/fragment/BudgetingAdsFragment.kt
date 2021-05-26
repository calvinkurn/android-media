package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.KeywordSuggestionActivity
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapter
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.model.BudgetingAdsViewModel
import com.tokopedia.topads.common.view.sheet.ChooseKeyBottomSheet
import com.tokopedia.topads.common.view.sheet.InfoBottomSheet
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
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


const val COUNT_TO_BE_SHOWN = 5

class BudgetingAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var ticker: Ticker
    private lateinit var buttonNext: UnifyButton
    private lateinit var addKeyword: Typography
    private lateinit var budget: TextFieldUnify
    private lateinit var selectedkeyword: Typography
    private lateinit var tipButton: FloatingButtonUnify
    private lateinit var loading: LoaderUnify
    private lateinit var info1: ImageUnify
    private lateinit var info2: ImageUnify
    private lateinit var bidList: RecyclerView
    private lateinit var bottomLayout: ConstraintLayout
    private lateinit var tipLayout: ConstraintLayout


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BudgetingAdsViewModel
    private lateinit var bidInfoAdapter: BidInfoAdapter
    private var maxBid = "0"
    private var minBid = "0"
    private var minSuggestKeyword = "0"
    private var maxSuggestKeyword = "0"
    private var suggestBidPerClick = "0"
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
        private const val FACTOR = "50"
        fun createInstance(): Fragment {
            val fragment = BudgetingAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupView(view: View) {
        ticker = view.findViewById(com.tokopedia.topads.common.R.id.ticker)
        buttonNext = view.findViewById(com.tokopedia.topads.common.R.id.btn_next)
        addKeyword = view.findViewById(com.tokopedia.topads.common.R.id.addKeyword)
        budget = view.findViewById(com.tokopedia.topads.common.R.id.budget)
        selectedkeyword = view.findViewById(com.tokopedia.topads.common.R.id.selectedKeyword)
        tipButton = view.findViewById(com.tokopedia.topads.common.R.id.tip_btn)
        loading = view.findViewById(com.tokopedia.topads.common.R.id.loading)
        info1 = view.findViewById(com.tokopedia.topads.common.R.id.info1)
        info2 = view.findViewById(com.tokopedia.topads.common.R.id.info2)
        bidList = view.findViewById(com.tokopedia.topads.common.R.id.bid_list)
        bottomLayout = view.findViewById(com.tokopedia.topads.common.R.id.bottom)
        tipLayout = view.findViewById(com.tokopedia.topads.common.R.id.tipView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BudgetingAdsViewModel::class.java)
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(::onDeleteItem, this::onEditBudget, ::onEditType))
    }

    private fun onDeleteItem(position: Int) {
        ticker.gone()
        bidInfoAdapter.items.removeAt(position)
        bidInfoAdapter.notifyItemRemoved(position)
        updateString()
        setCount()
        view?.let {
            Toaster.build(it, getString(com.tokopedia.topads.common.R.string.topads_keyword_common_del_toaster),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL).show()
        }
    }

    private fun prepareView() {
        if (stepperModel?.redirectionToSummary == true) {
            buttonNext.text = getString(R.string.topads_common_save_butt)
        }

        addKeyword.setOnClickListener {
            val intent = Intent(context, KeywordSuggestionActivity::class.java)
            stepperModel?.selectedKeywordStage = getItemSelected()
            intent.putExtra("model", stepperModel)
            startActivityForResult(intent, 47)
        }
    }

    private fun getItemSelected(): MutableList<KeywordDataItem> {
        val list: MutableList<KeywordDataItem> = mutableListOf()
        bidInfoAdapter.items.forEach {
            if (it is BidInfoItemViewModel) {
                list.add(it.data)
            }
        }
        return list
    }

    private fun actionEnable() {
        buttonNext.isEnabled = isEnable
    }

    private fun onEditBudget(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
        sheet.show(childFragmentManager, "")
        sheet.onSaved = { bid, position ->
            (bidInfoAdapter.items[position] as BidInfoItemViewModel).data.bidSuggest = bid
            bidInfoAdapter.notifyItemChanged(position)
        }
    }

    private fun onEditType(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        val sheet = ChooseKeyBottomSheet.newInstance()
        val type = (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.keywordType
        val typeInt = if (type == BROAD_TYPE)
            BROAD_POSITIVE
        else
            EXACT_POSITIVE
        sheet.show(childFragmentManager, typeInt)
        sheet.onSelect = { typeKey ->
            (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.keywordType = typeKey
            bidInfoAdapter.notifyItemChanged(pos)
        }
    }

    private fun prepareBundle(pos: Int): Bundle {
        val bundle = Bundle()
        bundle.putString("type", "create")
        bundle.putString(MAX_BID, maxBid)
        bundle.putString(MIN_BID, minBid)
        bundle.putString(SUGGESTION_BID, (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.bidSuggest)
        bundle.putString(KEYWORD_NAME, (bidInfoAdapter.items[pos] as BidInfoItemViewModel).data.keyword)
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
        stepperModel?.selectedKeywordStage = getItemSelected()
        if (stepperModel?.redirectionToSummary == false) {
            stepperModel?.goToSummary = true
            stepperListener?.goToNextPage(stepperModel)
        } else {
            stepperModel?.redirectionToSummary = false
            stepperListener?.getToFragment(4, stepperModel)
        }
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_ATUR_BIAYA_IKLAN, "")
    }


    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.topads_create_budget_title))
    }

    override fun getScreenName(): String {
        return BudgetingAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.topads.common.R.layout.topads_create_fragment_budget_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dummyId: MutableList<Long> = mutableListOf()
        val productIds = stepperModel?.selectedProductIds?.map {
            it.toLong()
        }
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("group", dummyId))
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions("product", productIds))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onDefaultSuccessSuggestion)
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion, this::onEmptySuggestion)
        val list: MutableList<String>? = stepperModel?.selectedProductIds
        val productId = list?.joinToString(",")
        if (stepperModel?.goToSummary == true) {
            setRestoreValue()
        } else {
            viewModel.getSuggestionKeyword(productId
                    ?: "", 0, this::onSuccessSuggestionKeyword, this::onEmptySuggestion)
        }
        setCount()
    }

    private fun onSuccessSuggestionKeyword(keywords: List<KeywordData>) {
        val keyList: MutableList<KeywordDataItem> = mutableListOf()
        var count = 0
        keywords.forEach { key ->
            if (count == COUNT_TO_BE_SHOWN)
                return@forEach
            key.keywordData.forEachIndexed { index, it ->
                count++
                if (count >= COUNT_TO_BE_SHOWN) {
                    return@forEachIndexed
                }
                bidInfoAdapter.items.add(BidInfoItemViewModel(it))
                keyList.add(it)
            }
        }
        bidInfoAdapter.notifyDataSetChanged()
        setCount()
        ticker.setTextDescription(MethodChecker.fromHtml(String.format(getString(com.tokopedia.topads.common.R.string.topads_common_added_key_ticker_text), bidInfoAdapter.itemCount)))
    }

    private fun setCount() {
        if (bidInfoAdapter.items.count() == 1 && bidInfoAdapter.items[0] is BidInfoEmptyViewModel) {
            selectedkeyword.text = String.format(getString(R.string.topads_common_selected_keyword), 0)
        } else {
            selectedkeyword.text = String.format(getString(R.string.topads_common_selected_keyword), bidInfoAdapter.items.count())
        }
    }

    private fun setRestoreValue() {
        ticker?.gone()
        setCount()
        if(stepperModel?.selectedKeywordStage?.isEmpty() == true) {
            onEmptySuggestion()
        } else {
            stepperModel?.selectedKeywordStage?.forEach { it ->
                bidInfoAdapter.items.add(BidInfoItemViewModel(it))
            }
            bidInfoAdapter.notifyDataSetChanged()
        }
    }

    private fun onDefaultSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            suggestBidPerClick = it.suggestionBid
            maxBid = it.maxBid
            minBid = it.minBid
        }

        if (stepperModel?.finalBidPerClick != -1 && stepperModel?.finalBidPerClick != 0)
            budget.textFieldInput.setText(stepperModel?.finalBidPerClick.toString())
        else
            budget.textFieldInput.setText(suggestBidPerClick)

        if (budget.textFieldInput.text.toString().removeCommaRawString().toDouble() > minBid.toDouble() && budget.textFieldInput.text.toString().removeCommaRawString().toDouble() < maxBid.toDouble()) {
            setMessageErrorField(getString(R.string.recommendated_bid_message_new), suggestBidPerClick, false)
            isEnable = true
            actionEnable()
        }
    }

    private fun onEmptySuggestion() {
        ticker.gone()
        bidInfoAdapter.items.add(BidInfoEmptyViewModel())
        bidInfoAdapter.notifyDataSetChanged()
    }

    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            minSuggestKeyword = it.minBid
            maxSuggestKeyword = it.maxBid
        }
        bidInfoAdapter.setMinimumBid(minSuggestKeyword)
        bidInfoAdapter.notifyDataSetChanged()
        loading.visibility = View.GONE
    }

    private fun updateString() {
        if (bidInfoAdapter.items.count() == 0)
            onEmptySuggestion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        prepareView()
        loading.visibility = View.VISIBLE
        userID = UserSession(view.context).userId
        shopID = UserSession(view.context).shopId
        buttonNext.setOnClickListener {
            gotoNextPage()
        }
        info1.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.INFORMATION))
        info2.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.INFORMATION))
        info1.setOnClickListener {
            InfoBottomSheet.newInstance().show(childFragmentManager, 0)
        }
        info2.setOnClickListener {
            InfoBottomSheet.newInstance().show(childFragmentManager, 1)
        }
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.tip_biaya_iklan)
            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_ic_tips))
        }

        tipButton.addItem(tooltipView)
        tipButton.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.biaya_iklan_tip_1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.biaya_iklan_tip_2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.biaya_iklan_tip_3, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.isDragable = false
            tipsListSheet?.isHideable = false
            tipsListSheet?.setTitle(getString(R.string.tip_biaya_iklan_title))
            tipsListSheet?.show(childFragmentManager, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_BIAYA_IKLAN, shopID, userID)
        }

        budget.textFieldInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val eventLabel = "$shopID - $EVENT_CLICK_BUDGET"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET, eventLabel, userID)
            }
        }

        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                stepperModel?.finalBidPerClick = result
                when {
                    result < minBid.toDouble() -> {
                        setMessageErrorField(getString(R.string.min_bid_error_new), minBid, true)
                        isEnable = false
                    }
                    result > maxBid.toDouble() -> {
                        isEnable = false
                        setMessageErrorField(getString(R.string.max_bid_error_new), maxBid, true)
                    }

                    result % (FACTOR.toInt()) != 0 -> {
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
        bidList.adapter = bidInfoAdapter
        bidList.layoutManager = LinearLayoutManager(context)
        tipLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        bottomLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val heightButton = tipLayout.measuredHeight
        val bottomHeight = bottomLayout.measuredHeight
        val height = (heightButton + bottomHeight)
        bidList.setPadding(0, 0, 0, height)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47) {
            if (resultCode == Activity.RESULT_OK) {
                stepperModel = data?.getParcelableExtra("model")
                resetView()
            }
        }
    }

    private fun resetView() {
        if (bidInfoAdapter.items.size == 0) {
            onEmptySuggestion()
        } else {
            bidInfoAdapter.items.clear()
            stepperModel?.selectedKeywordStage?.forEach {
                if(it.bidSuggest == "0")
                    it.bidSuggest = minSuggestKeyword
                bidInfoAdapter.items.add(BidInfoItemViewModel(it))
            }
            bidInfoAdapter.notifyDataSetChanged()
        }
        setCount()
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

}
