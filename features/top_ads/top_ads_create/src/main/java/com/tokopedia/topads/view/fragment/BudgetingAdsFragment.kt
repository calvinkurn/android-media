package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.DEFAULT_NEW_KEYWORD_VALUE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.KeywordSuggestionActivity
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapter
import com.tokopedia.topads.view.adapter.bidinfo.BidInfoAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.model.BudgetingAdsViewModel
import com.tokopedia.topads.view.sheet.ChooseKeyBottomSheet
import com.tokopedia.topads.view.sheet.InfoBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.topads.dashboard.R as topadsdashboardR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_TIPS_BIAYA_IKLAN = "click-tips biaya iklan"
private const val CLICK_ATUR_BIAYA_IKLAN = "click-atur biaya iklan"
private const val CLICK_BUDGET = "click - biaya non kata kunci box"
private const val EVENT_CLICK_BUDGET = "biaya yang diinput"
private const val CLICK_SETUP_KEY = "click - setup keyword"
private const val CLICK_DAILY_BUDGET_BOX = "click - box biaya iklan manual"
private const val CLICK_EDIT_KEYWORD_TYPE = "click - button edit luas pencarian"
private const val CLICK_EDIT_KEYWORD_BID = "click - edit bid kata kunci"
private const val CLICK_EDIT_KEYWORD_DELETE = "click - delete button kata kunci"
private const val CLICK_ADDED_KEYWORD = "click - tambah kata kunci"

const val PARAM_MODEL = "model"
const val COUNT_TO_BE_SHOWN = 5

class BudgetingAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var ticker: Ticker
    private lateinit var buttonNext: UnifyButton
    private lateinit var addKeyword: Typography
    private lateinit var budget: TextFieldUnify2
    private lateinit var selectedkeyword: Typography

    //    private lateinit var tipButton: FloatingButtonUnify
    private lateinit var loading: LoaderUnify

    //    private lateinit var info1: ImageUnify
    private lateinit var info2: ImageUnify
    private lateinit var bidList: RecyclerView
    private lateinit var txtInfo: Typography
    private lateinit var impressionPerformanceValueSearch: Typography
//    private lateinit var bottomLayout: ConstraintLayout
//    private lateinit var tipLayout: ConstraintLayout


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BudgetingAdsViewModel
    private lateinit var bidInfoAdapter: BidInfoAdapter

    private var maxBid = "0"
    private var minBid = "0"
    private var minSuggestKeyword = "0"
    private var maxSuggestKeyword = "0"
    private var suggestBidPerClick = 0
    private var suggestedKeywordBid = "0"
    private var keywordBudget = "0"
    private var isEnable = false
    private var userID: String = ""
    private var shopID = ""
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    companion object {
        private const val REQ_CODE_MODEL = 47
        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        private const val FROM_CREATE = "fromCreate"
        private const val KEYWORD_BUDGET = "keywordBudget"
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
        ticker = view.findViewById(R.id.ticker)
        buttonNext = view.findViewById(R.id.btnNextSearch)
        addKeyword = view.findViewById(R.id.addKeyword)
        budget = view.findViewById(R.id.budget)
        selectedkeyword = view.findViewById(R.id.selectedKeyword)
//        tipButton = view.findViewById(com.tokopedia.topads.common.R.id.tip_btn)
        loading = view.findViewById(R.id.loading)
//        info1 = view.findViewById(com.tokopedia.topads.common.R.id.info1)
        info2 = view.findViewById(R.id.info2)
        bidList = view.findViewById(R.id.bid_list)
        txtInfo = view.findViewById(R.id.txtInfo)
        impressionPerformanceValueSearch = view.findViewById(R.id.impressionPerformanceValueSearch)
//        bottomLayout = view.findViewById(com.tokopedia.topads.common.R.id.bottom)
//        tipLayout = view.findViewById(com.tokopedia.topads.common.R.id.tipView)
//        view.findViewById<TextFieldUnify>(com.tokopedia.topads.common.R.id.biaya_pencarian).hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BudgetingAdsViewModel::class.java)
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(::onDeleteItem, this::onEditBudget, ::onEditType))
    }

    private fun onDeleteItem(position: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_EDIT_KEYWORD_DELETE, "")
        ticker.gone()
        bidInfoAdapter.items.removeAt(position)
        bidInfoAdapter.notifyItemRemoved(position)
        updateString()
        setCount()
        view?.let {
            Toaster.build(it, getString(topadscommonR.string.topads_keyword_common_del_toaster),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL).show()
        }
    }

    private fun prepareView() {
//        if (stepperModel?.redirectionToSummary == true) {
//            buttonNext.text = getString(com.tokopedia.topads.common.R.string.topads_common_save_butt)
//        }

        addKeyword.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_ADDED_KEYWORD, "")
            val intent = Intent(context, KeywordSuggestionActivity::class.java)
            stepperModel?.selectedKeywordStage = getItemSelected()
            intent.putExtra(PARAM_MODEL, stepperModel)
            startActivityForResult(intent, REQ_CODE_MODEL)
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

    private fun actionEnable(isEnable: Boolean) {
        buttonNext.isEnabled = isEnable
    }

    private fun onEditBudget(pos: Int, budget: String) {
        this.keywordBudget = budget
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_EDIT_KEYWORD_BID, "")
        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
        sheet.show(childFragmentManager, "")
        sheet.onSaved = { bid, position ->
            (bidInfoAdapter.items[position] as BidInfoItemViewModel).data.bidSuggest = bid
            bidInfoAdapter.notifyItemChanged(position)
        }
    }

    private fun onEditType(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_EDIT_KEYWORD_TYPE, "")
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
        bundle.putString(MAX_BID, maxSuggestKeyword)
        bundle.putString(MIN_BID, minSuggestKeyword)
        bundle.putString(SUGGESTION_BID, suggestedKeywordBid)
        bundle.putBoolean(FROM_CREATE, true)
        bundle.putString(KEYWORD_BUDGET, keywordBudget)
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
            stepperModel?.finalSearchBidPerClick = budget.editText.text.toString().removeCommaRawString().toIntOrZero()
            stepperModel?.finalRecommendationBidPerClick = budget.editText.text.toString().removeCommaRawString().toIntOrZero()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        stepperModel?.suggestedBidPerClick = suggestBidPerClick.toString()
        stepperModel?.maxBid = maxBid
        stepperModel?.minBid = minBid
        stepperModel?.minSuggestBidKeyword = minSuggestKeyword
        stepperModel?.selectedKeywordStage = getItemSelected()
//        if (stepperModel?.redirectionToSummary == false) {
////            stepperModel?.goToSummary = true
//            stepperListener?.goToNextPage(stepperModel)
//        } else {
//            stepperModel?.redirectionToSummary = false
        stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_4, stepperModel)
//        }
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
        return inflater.inflate(R.layout.topads_create_fragment_budget_list2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dummyId: MutableList<String> = mutableListOf()
        val productIds = stepperModel?.selectedProductIds?.map {
            it
        }
        val suggestions = ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("group", dummyId))
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions("product", productIds))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onDefaultSuccessSuggestion)
        productIds?.let { viewModel.getProductBid(it) }
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
        setObservers()
    }

    private fun setObservers() {
        observeBidData()
        observePerformancePrediction()

    }

    private fun observePerformancePrediction() {
        viewModel.performanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    stepperModel?.searchPrediction = it.data.umpGetImpressionPrediction.impressionPredictionData.impression.finalImpression
                    impressionPerformanceValueSearch.text = String.format("%sx", it.data.umpGetImpressionPrediction.impressionPredictionData.impression.finalImpression)
                }
                else -> {
                    Log.e("BidsInfo", "Inside setObservers $it")
                }
            }
        }
    }

    private fun observeBidData() {
        viewModel.bidProductData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessBidProductData(it.data.topAdsGetBidSuggestionByProductIDs.bidData)
                }

                else -> {

                }
            }
        }
    }

    private fun onSuccessBidProductData(bidData: TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs.BidData) {
        suggestBidPerClick = bidData.bidSuggestion
//            maxBid = it.maxBid
//            minBid = it.minBid


//        budget.textFieldInput.setText(suggestBidPerClick)
        budget.editText.setText(suggestBidPerClick.toString())

//        if (budget.editText.text.toString().removeCommaRawString().toDouble() > minBid.toDouble() && budget.editText.text.toString().removeCommaRawString().toDouble() < maxBid.toDouble()) {
//            setMessageErrorField(getString(com.tokopedia.topads.common.R.string.recommendated_bid_message_new), suggestBidPerClick, false)
//            isEnable = true
//            actionEnable()
//        }
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
        ticker.setTextDescription(MethodChecker.fromHtml(String.format(getString(topadscommonR.string.topads_common_added_key_ticker_text), bidInfoAdapter.itemCount)))
    }

    private fun setCount() {
        if (bidInfoAdapter.items.count() == 1 && bidInfoAdapter.items[0] is BidInfoEmptyViewModel) {
            selectedkeyword.text = String.format(getString(topadscommonR.string.topads_common_selected_keyword), 0)
        } else {
            selectedkeyword.text = String.format(getString(topadscommonR.string.topads_common_selected_keyword), bidInfoAdapter.items.count())
        }
    }

    private fun setRestoreValue() {
        ticker.gone()
        setCount()
        if (stepperModel?.selectedKeywordStage?.isEmpty() == true) {
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
//            suggestBidPerClick = it.suggestionBid
            maxBid = it.maxBid
            minBid = it.minBid
        }

//        budget.textFieldInput.setText(suggestBidPerClick)
        budget.editText.setText(suggestBidPerClick)

//        if (budget.editText.text.toString().removeCommaRawString().toDouble() > minBid.toDouble() && budget.editText.text.toString().removeCommaRawString().toDouble() < maxBid.toDouble()) {
//            setMessageErrorField(getString(com.tokopedia.topads.common.R.string.recommendated_bid_message_new), suggestBidPerClick, false)
//            isEnable = true
//            actionEnable(true)
//        }
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
            suggestedKeywordBid = it.minBid
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
        buttonNext.show()
        buttonNext.setOnClickListener {
            gotoNextPage()
        }
//        info1.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.INFORMATION))
        info2.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.INFORMATION))
//        info1.setOnClickListener {
//            InfoBottomSheet.newInstance().show(childFragmentManager, 0)
//        }
        info2.setOnClickListener {
            InfoBottomSheet.newInstance().show(childFragmentManager, 1)
        }
        txtInfo.text = MethodChecker.fromHtml(getString(R.string.top_ads_create_text_info_search_bid))
        setClicksOnViews()

//        tipButton.visibility = View.VISIBLE
//        tipButton.addItem(tooltipView)
//        tipButton.setOnClickListener {
//            val tipsList: ArrayList<TipsUiModel> = ArrayList()
//            tipsList.apply {
//                add(TipsUiRowModel(R.string.biaya_iklan_tip_1, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
//                add(TipsUiRowModel(R.string.biaya_iklan_tip_2, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
//                add(TipsUiRowModel(R.string.biaya_iklan_tip_3, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
//            }
//            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
//            tipsListSheet?.showHeader = true
//            tipsListSheet?.showKnob = false
//            tipsListSheet?.isDragable = false
//            tipsListSheet?.isHideable = false
//            tipsListSheet?.setTitle(getString(R.string.tip_biaya_iklan_title))
//            tipsListSheet?.show(childFragmentManager, "")
//            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_BIAYA_IKLAN, shopID, userID)
//        }

        budget.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val eventLabel = "$shopID - $EVENT_CLICK_BUDGET"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET, eventLabel, userID)
            }
        }

        budget.editText.addTextChangedListener(object : NumberTextWatcher(budget.editText) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(
                    CLICK_DAILY_BUDGET_BOX, "")
                stepperModel?.finalSearchBidPerClick = result
                when {
                    result >= suggestBidPerClick -> {
                        setMessageErrorField("Biaya optimal ✔️", "0", false)
                        stepperModel?.selectedProductIds?.let {
                            viewModel.getPerformanceData(it, result.toFloat(), -1f, -1f)
                        }
                        actionEnable(true)
                    }

                    else -> {
//                        setMessageErrorField(getString(com.tokopedia.topads.dashboard.R.string.topads_insight_recommended_bid_apply), "550", true)
                        budget.setMessage(getClickableString(suggestBidPerClick))
                        actionEnable(true)
                    }
//                    result < minBid.toDouble() -> {
//                        setMessageErrorField(getString(com.tokopedia.topads.common.R.string.min_bid_error_new), minBid, true)
//                        isEnable = false
//                    }
//                    result > maxBid.toDouble() -> {
//                        isEnable = false
//                        setMessageErrorField(getString(com.tokopedia.topads.common.R.string.max_bid_error_new), maxBid, true)
//                    }
//
//                    result % (FACTOR.toInt()) != 0 -> {
//                        isEnable = false
//                        setMessageErrorField(getString(com.tokopedia.topads.common.R.string.topads_common_error_multiple_50), FACTOR, true)
//                    }
//                    else -> {
//                        isEnable = true
//                        setMessageErrorField(getString(com.tokopedia.topads.common.R.string.recommendated_bid_message_new), suggestBidPerClick, false)
//                    }
                }

            }
        })
        bidList.adapter = bidInfoAdapter
        bidList.layoutManager = LinearLayoutManager(context)
    }

    private fun setClicksOnViews() {
        txtInfo.setOnClickListener {
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle("Biaya Iklan di Pencarian")
                it.setDescription(
                    "Biaya iklan baru akan dikenakan setiap ada calon pembeli yang klik iklan produkmu baik itu secara organik ataupun saat iklanmu tampil sesuai dengan kata kunci pilihanmu.\n" +
                        "\n" +
                        "Tips:\n" +
                        "Semakin tinggi biaya iklanmu, maka semakin tinggi peluang iklanmu ditampilkan."
                )
            }.show(childFragmentManager)
        }
    }

    private fun getClickableString(
        bid: Int,
    ): SpannableString {
        val msg = String.format(
            getString(topadsdashboardR.string.topads_insight_recommended_bid_apply),
            bid
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                budget.editText.setText(bid.toString())
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                if (context != null) {
                    ds.color = ContextCompat.getColor(
                        context!!,
                        unifyprinciplesR.color.Unify_GN500
                    )
                }

                ds.isFakeBoldText = true
            }
        }
        ss.setSpan(cs, msg.length - 8, msg.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_MODEL) {
            if (resultCode == Activity.RESULT_OK) {
                stepperModel = data?.getParcelableExtra(PARAM_MODEL)
                resetView()
            }
        }
    }

    private fun resetView() {
        bidInfoAdapter.items.clear()
        if (stepperModel?.selectedKeywordStage?.isNotEmpty() == true) {
            stepperModel?.selectedKeywordStage?.forEach {
                if (it.bidSuggest == "0")
                    it.bidSuggest = DEFAULT_NEW_KEYWORD_VALUE
                bidInfoAdapter.items.add(BidInfoItemViewModel(it))
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendKeywordAddEvent(
                CLICK_ADDED_KEYWORD, "", stepperModel?.selectedKeywordStage!!
            )
            bidInfoAdapter.notifyDataSetChanged()
        } else {
            onEmptySuggestion()
        }
        setCount()
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
//        budget.setError(bool)
        budget.isInputError = bool
        budget.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

}
