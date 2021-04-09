package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
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
import com.tokopedia.topads.view.sheet.ChooseKeyBottomSheet
import com.tokopedia.topads.view.sheet.InfoBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
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


const val COUNT_TO_BE_SHOWN = 5

class BudgetingAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

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
        const val SPECIFIC_TYPE = "Spesifik"
        const val BROAD_TYPE = "Luas"
        const val EXACT_POSITIVE = 21
        const val BROAD_POSITIVE = 11
        private const val FACTOR = "50"
        fun createInstance(): Fragment {
            val fragment = BudgetingAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BudgetingAdsViewModel::class.java)
        bidInfoAdapter = BidInfoAdapter(BidInfoAdapterTypeFactoryImpl(::onDeleteItem, this::onEditBudget, ::onEditType))
    }

    private fun onDeleteItem(position: Int) {
        ticker?.gone()
        bidInfoAdapter.items.removeAt(position)
        bidInfoAdapter.notifyItemRemoved(position)
        updateString()
        setCount()
    }

    private fun prepareView() {
        if (stepperModel?.redirectionToSummary == true) {
            btn_next?.text = getString(R.string.topads_common_save_butt)
        }

        addKeyword?.setOnClickListener {
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
        btn_next.isEnabled = isEnable
    }

    private fun onEditBudget(pos: Int) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SETUP_KEY, shopID, userID)
        val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(pos))
        sheet.show(childFragmentManager, "")
        sheet.onSaved = { bid, _, position ->
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
        stepperModel?.selectedKeywordStage = getItemSelected()
        if (stepperModel?.redirectionToSummary == false)
            stepperListener?.goToNextPage(stepperModel)
        else {
            stepperModel?.redirectionToSummary = false
            stepperListener?.getToFragment(3, stepperModel)
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
        return inflater.inflate(R.layout.topads_create_fragment_budget_list, container, false)
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
        if (stepperModel?.selectedKeywordStage?.isNotEmpty() != false) {
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
        ticker?.setTextDescription(MethodChecker.fromHtml(String.format(getString(R.string.topads_create_key_ticker_text), bidInfoAdapter.itemCount)))
    }

    private fun setCount() {
        selectedKeyword?.text = String.format(getString(R.string.topads_create_selected_keyword), bidInfoAdapter.items.count())
    }

    private fun setRestoreValue() {
        ticker?.gone()
        setCount()
        stepperModel?.selectedKeywordStage?.forEach { it ->
            bidInfoAdapter.items.add(BidInfoItemViewModel(it))
        }
        bidInfoAdapter.notifyDataSetChanged()
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
        prepareView()
        loading?.visibility = View.VISIBLE
        userID = UserSession(view.context).userId
        shopID = UserSession(view.context).shopId
        btn_next?.setOnClickListener {
            gotoNextPage()
        }
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

        tip_btn?.addItem(tooltipView)
        tip_btn?.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.makin_tinggi_biaya_iklan_makin_besar_kemungkinan_produkmu_tampil_di_halaman_pencarian, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.gunakan_rekomendasi_biaya_agar_produkmu_lebih_bersaing, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.bayar_hanya_jika_iklan_produkmu_di_klik_dari_hasil_pencarian_kata_kunci_pilihan, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.isDragable = false
            tipsListSheet?.isHideable = false
            tipsListSheet?.setTitle(getString(R.string.tip_biaya_iklan))
            tipsListSheet?.show(childFragmentManager, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_BIAYA_IKLAN, shopID, userID)
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
                    result < minBid.toDouble() -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        isEnable = false
                    }
                    result > maxBid.toDouble() -> {
                        isEnable = false
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
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
        bid_list.adapter = bidInfoAdapter
        bid_list.layoutManager = LinearLayoutManager(context)
        tipView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        bottom?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        if (tipView != null && bottom != null) {
            val heightButton = tipView.measuredHeight
            val bottomHeight = bottom.measuredHeight
            val height = (heightButton + bottomHeight)
            bid_list?.setPadding(0, 0, 0, height)
        }
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
        bidInfoAdapter.items.clear()
        stepperModel?.selectedKeywordStage?.forEach {
            bidInfoAdapter.items.add(BidInfoItemViewModel(it))
        }
        bidInfoAdapter.notifyDataSetChanged()
        setCount()
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

}
