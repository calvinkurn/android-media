package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.CustomViewPager
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.KeySharedModel
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.response.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.TopadsAutoBidSwitchPartialLayout
import com.tokopedia.topads.common.view.adapter.viewpager.KeywordEditPagerAdapter
import com.tokopedia.topads.common.view.sheet.BidInfoBottomSheet
import com.tokopedia.topads.common.view.sheet.CreatePotentialPerformanceSheet
import com.tokopedia.topads.common.view.sheet.InfoBottomSheet
import com.tokopedia.topads.common.view.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.BID_LIST
import com.tokopedia.topads.edit.utils.Constants.BID_TYPE
import com.tokopedia.topads.edit.utils.Constants.DAILY_BUDGET_INPUT
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.IS_BID_AUTOMATIC
import com.tokopedia.topads.edit.utils.Constants.MIN_MAX_BIDS
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.POTENTIAL_PERFORMANCE_LIST
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID_LIST
import com.tokopedia.topads.edit.utils.Constants.STRATEGIES
import com.tokopedia.topads.edit.view.activity.EditAdGroupActivity
import com.tokopedia.topads.edit.view.activity.OnKeywordAction
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.edit.R as topadseditR

private const val CLICK_KATA_KUNCI_POSITIF = "click - kata kunci positif"
private const val CLICK_KATA_KUNCI_NEGATIF = "click - kata kunci negatif"
private const val CLICK_BID_TYPE_SELECT = "click - mode pengaturan"
private const val MANUAL_LAYOUT_EVENT_LABEL = "mode pengaturan atur manual"
private const val OTOMATIS_LAYOUT_EVENT_LABEL = "mode pengaturan atur otomatis"
private const val OTOMATIS_LEARN_MORE_LINK = "https://seller.tokopedia.com/edu/topads-otomatis/"

class BaseEditKeywordFragment : BaseDaggerFragment(), EditKeywordsFragment.ButtonAction {

    private var dailyBudgetInput: Float = Int.ZERO.toFloat()
    private var minMaxBids: MutableList<String>? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val isBidAutomatic: Boolean get() = autoBidSwitch?.isBidAutomatic ?: false
    private var autoBidSwitch: TopadsAutoBidSwitchPartialLayout? = null
    private var keywordGroup: LinearLayout? = null
    private var autoBidTicker: Ticker? = null
    private var txtInfo: Typography? = null
    private var budget: TextFieldUnify2? = null
    private var btnNextSearch: UnifyButton? = null
    private var headerUnify: HeaderUnify? = null
    private var potentialPerformanceIconUnify: IconUnify? = null

    private var viewPager: CustomViewPager? = null

    private var buttonStateCallback: SaveButtonStateCallBack? = null
    private var btnState = true
    private var isAutoBid: Boolean = false
    private var positivekeywordsAll: ArrayList<KeySharedModel>? = arrayListOf()
    private var negativekeywordsAll: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider[EditFormDefaultViewModel::class.java]
    }

    private var autoBidAdvantageDesc: View? = null
    private var tabsUnify: TabsUnify? = null

    private var suggestBidPerClick = Int.ZERO
    private var onKeywordAction: OnKeywordAction? = null
    private var productIds: ArrayList<String> = arrayListOf()
    private var impressionPerformanceValueSearch: Typography? = null
    private var kataKunci: Typography? = null
    private var kataKunciInfo: IconUnify? = null

    companion object {
        fun newInstance(bundle: Bundle?): BaseEditKeywordFragment {
            val fragment = BaseEditKeywordFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            context?.resources?.getLayout(R.layout.topads_edit_keyword_base_layout),
            container,
            false
        )
        initView(view)
        return view
    }

    private fun initView(view: View) {
        keywordGroup = view.findViewById(R.id.keyword_grp)
        txtInfo = view.findViewById(R.id.txtInfo)
        budget = view.findViewById(R.id.budget)
        viewPager = view.findViewById(R.id.view_pager)
        tabsUnify = view.findViewById(R.id.keyword_tabs)
        btnNextSearch = view.findViewById(R.id.btnNextSearch)
        impressionPerformanceValueSearch = view.findViewById(R.id.impressionPerformanceValueSearch)
        kataKunciInfo = view.findViewById(R.id.kataKunciInfo)
        kataKunci = view.findViewById(R.id.kataKunci)
        headerUnify = view.findViewById(R.id.header)
        potentialPerformanceIconUnify = view.findViewById(R.id.potentialPerformanceIconUnify)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productIds = arguments?.getStringArrayList(PRODUCT_ID_LIST)?.let { ArrayList(it) }
            ?: arrayListOf()
        context?.resources?.getString(R.string.topads_edit_auto_bid_ticker_title)?.let { autoBidTicker?.setHtmlDescription(it) }
        this.dailyBudgetInput = arguments?.getFloat(DAILY_BUDGET_INPUT) ?: Int.ZERO.toFloat()
        initListeners()
        renderViewPager()
        setTabs()

        arguments?.getBoolean(IS_BID_AUTOMATIC)?.let { handleInitialAutoBidState(it) }
        setViews()
        val groupId = arguments?.getString(GROUP_ID)
        val suggestionsDefault = java.util.ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions(String.EMPTY, listOf(groupId.toString())))
        viewModel.getSuggestedBid(productIds, this::onBidSuccessSuggestion)
        headerUnify?.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
        val performanceList = arguments?.getStringArrayList(POTENTIAL_PERFORMANCE_LIST)
        potentialPerformanceIconUnify?.setOnClickListener {
            CreatePotentialPerformanceSheet.newInstance(
                performanceList?.firstOrNull().toIntOrZero(),
                performanceList?.getOrNull(Int.ONE).toIntOrZero()
            ).show(childFragmentManager)

        }
        this.minMaxBids = arguments?.getStringArrayList(MIN_MAX_BIDS)

    }

    private fun setViews() {
        txtInfo?.text = MethodChecker.fromHtml(getString(topadscommonR.string.top_ads_common_text_info_search_bid))
        setListeners()
        checkForSearchBid()
        setObservers()
    }

    private fun setObservers() {
        viewModel.performanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    impressionPerformanceValueSearch?.text = String.format(getString(topadscommonR.string.top_ads_performce_count_prefix), it.data.umpGetImpressionPrediction.impressionPredictionData.impression.finalImpression)
                }

                else -> {}
            }
        }
    }

    private fun onBidSuccessSuggestion(data: TopAdsGetBidSuggestionResponse) {
        suggestBidPerClick = data.topAdsGetBidSuggestionByProductIDs.bidData.bidSuggestion
    }

    private fun setListeners() {
        budget?.editText?.addTextChangedListener(object : NumberTextWatcher(budget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                val minBid = minMaxBids?.firstOrNull().toDoubleOrZero()
                val maxBid= minMaxBids?.getOrNull(1).toDoubleOrZero()
                when {
                    result % 50 != 0 -> {
                        setMessageErrorField(getString(topadscommonR.string.topads_ads_error_multiple_fifty), Int.ZERO.toString(), true)
                        actionEnable(false)
                    }
                    result >= suggestBidPerClick -> {
                        if (result > maxBid && maxBid.toInt() != Int.ZERO) {
                            setMessageErrorField(getString(topadscommonR.string.max_bid_error_new), maxBid.toInt().toString(), true)
                            actionEnable(false)
                        } else {
                            setMessageErrorField(getString(topadscommonR.string.topads_ads_optimal_bid), Int.ZERO.toString(), false)
                            productIds.let {
                                viewModel.getPerformanceData(it, result.toFloat(), result.toFloat(), dailyBudgetInput)
                            }
                            actionEnable(true)
                        }
                    }

                    result < minBid && maxBid.toInt() != 0 -> {
                        setMessageErrorField(getString(topadscommonR.string.min_bid_error_new), minBid.toInt().toString(), true)
                        actionEnable(false)
                    }

                    else -> {
                        budget?.isInputError = false
                        budget?.setMessage(getClickableString(suggestBidPerClick))
                        actionEnable(true)
                    }
                }

            }
        })

        setClicksOnViews()
    }

    private fun setClicksOnViews() {
        txtInfo?.setOnClickListener {
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(getString(topadscommonR.string.topads_ads_search_bid_tooltip_title))
                it.setDescription(
                    getString(topadscommonR.string.topads_ads_search_bid_tooltip_description)
                )
            }.show(childFragmentManager)
        }

        btnNextSearch?.setOnClickListener {
            onKeywordAction = activity as? EditAdGroupActivity
            onKeywordAction?.onAction(sendData())
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        kataKunciInfo?.setOnClickListener {
            InfoBottomSheet(
                InfoBottomSheet.TYPE_KATA_KUNCI
            ).show(childFragmentManager)
        }
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget?.isInputError = bool
        budget?.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

    private fun actionEnable(isEnable: Boolean) {
        btnNextSearch?.isEnabled = isEnable
    }

    private fun getClickableString(
        bid: Int,
    ): SpannableString {
        val msg = String.format(
            getString(topadscommonR.string.topads_commom_recommended_bid_apply),
            bid
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                budget?.editText?.setText(bid.toString())
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

    private fun checkForSearchBid() {
        arguments?.getParcelableArrayList<TopAdsBidSettingsModel>(BID_LIST)?.forEach {
            if (it.bidType.equals(ParamObject.PRODUCT_SEARCH)) {
                updateBudgetInputIf(
                    value = (it.priceBid?.toInt() ?: 0).toString(),
                    isBidManual = !isBidAutomatic
                )
            }
        }
    }

    private fun setTabs() {
        tabsUnify?.getUnifyTabLayout()?.removeAllTabs()
        tabsUnify?.customTabMode = TabLayout.MODE_FIXED
        tabsUnify?.addNewTab(getString(topadseditR.string.top_ads_kata_kunci_tab_positive))
        tabsUnify?.addNewTab(getString(topadseditR.string.top_ads_kata_kunci_tab_negative))

    }

    private fun updateBudgetInputIf(value: String, isBidManual: Boolean) {
        if (isBidManual)
            budget?.editText?.setText(value)
    }

    private fun initListeners() {
        autoBidTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(
                    context, ApplinkConstInternalGlobal.WEBVIEW, OTOMATIS_LEARN_MORE_LINK
                )
            }

            override fun onDismiss() {}
        })

        autoBidSwitch?.let {
            it.onCheckBoxStateChanged = { isAutoBid ->
                handleAutoBidState(isAutoBid)
                setVisibilityAutoBidAdvantageDesc(isAutoBid)
            }

            it.onInfoClicked = {
                BidInfoBottomSheet().show(childFragmentManager, "")
            }
        }

    }

    private fun setVisibilityAutoBidAdvantageDesc(autoBid: Boolean) {
        if (autoBid) {
            autoBidAdvantageDesc?.hide()
        } else {
            autoBidAdvantageDesc?.show()
        }
    }

    private fun handleInitialAutoBidState(isAutoBid: Boolean) {
        handleAutoBidState(isAutoBid)
        if (isAutoBid) autoBidSwitch?.switchToAutomatic() else autoBidSwitch?.switchToManual()
    }

    private fun handleAutoBidState(isAutoBid: Boolean) {
        this.isAutoBid = isAutoBid
        if (isAutoBid) {
            keywordGroup?.visibility = View.GONE
            autoBidTicker?.visibility = View.VISIBLE
            viewPager?.visibility = View.GONE
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT, OTOMATIS_LAYOUT_EVENT_LABEL
            )
        } else {
            keywordGroup?.visibility = View.VISIBLE
            autoBidTicker?.visibility = View.GONE
            viewPager?.visibility = View.VISIBLE
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT, MANUAL_LAYOUT_EVENT_LABEL
            )
        }
        setVisibilityAutoBidAdvantageDesc(isAutoBid)
    }

    private fun renderViewPager() {
        viewPager?.adapter = getViewPagerAdapter()
        viewPager?.disableScroll(true)
        viewPager?.let { tabsUnify?.setupWithViewPager(it) }
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(EditKeywordsFragment.newInstance(arguments))
        list.add(EditNegativeKeywordsFragment.newInstance(arguments))
        val adapter = KeywordEditPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    fun getButtonState(): Boolean {
        return btnState
    }

    override fun onAttach(context: Context) {
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }

    override fun buttonDisable(enable: Boolean) {
        btnState = enable
        buttonStateCallback?.setButtonState()
    }

    override fun getScreenName(): String {
        return BaseEditKeywordFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    fun sendData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        val fragments = (viewPager?.adapter as KeywordEditPagerAdapter?)?.list
        var dataNegativeAdded: ArrayList<KeySharedModel>? = arrayListOf()
        var dataNegativeDeleted: ArrayList<KeySharedModel>? = arrayListOf()
        var deletedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        var addedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        var editedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        val strategies: ArrayList<String> = arrayListOf()
        var bidSettings: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
        var bidInfoDataItem: List<GroupEditInput.Group.TopadsSuggestionBidSetting>? = null
        var bidGroup = 0
        val list = arguments?.getParcelableArrayList<TopAdsBidSettingsModel>(BID_LIST)
        list?.getOrNull(0)?.priceBid = CurrencyFormatHelper.convertRupiahToDouble(budget?.editText?.text.toString()).toFloat()


        if (!isAutoBid) {
            if (fragments?.get(0) is EditKeywordsFragment) {
                val bundle: Bundle = (fragments[0] as EditKeywordsFragment).sendData()
                addedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_CREATE)
                deletedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_DELETE)
                editedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_EDIT)
                positivekeywordsAll = bundle.getParcelableArrayList(POSITIVE_KEYWORD_ALL)
                bidGroup = budget?.editText?.text?.toString()?.removeCommaRawString().toIntOrZero()
                bidSettings = list
                bidInfoDataItem = (fragments[0] as EditKeywordsFragment).getSuggestedBidSettings()
            }
            if (fragments?.get(1) is EditNegativeKeywordsFragment) {
                val bundle: Bundle = (fragments[1] as EditNegativeKeywordsFragment).sendData()
                dataNegativeAdded = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_ADDED)
                dataNegativeDeleted = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_DELETED)
                negativekeywordsAll = bundle.getParcelableArrayList(NEGATIVE_KEYWORD_ALL)

            }
        }
        strategies.clear()
        if (autoBidSwitch?.isBidAutomatic == true) {
            strategies.add(ParamObject.AUTO_BID_STATE)
        }
        dataMap[POSITIVE_CREATE] = addedKeywordsPos
        dataMap[POSITIVE_DELETE] = deletedKeywordsPos
        dataMap[POSITIVE_EDIT] = editedKeywordsPos
        dataMap[NEGATIVE_KEYWORDS_ADDED] = dataNegativeAdded
        dataMap[NEGATIVE_KEYWORDS_DELETED] = dataNegativeDeleted
        dataMap[STRATEGIES] = strategies
        dataMap[Constants.PRICE_BID] = bidGroup
        dataMap[BID_TYPE] = bidSettings
        dataMap[ParamObject.SUGGESTION_BID_SETTINGS] = bidInfoDataItem
        return dataMap
    }

    fun getKeywordNameItems(): MutableList<Map<String, Any>> {
        val fragments = (viewPager?.adapter as KeywordEditPagerAdapter?)?.list
            ?: return mutableListOf()

        val items: MutableList<Map<String, Any>> = mutableListOf()
        if (fragments.size > Int.ZERO && fragments[Int.ZERO] is EditKeywordsFragment) {
            positivekeywordsAll?.forEachIndexed { _, it ->
                val map = mapOf(
                    "name" to it.name, "id" to it.id, "type" to "positif"
                )
                items.add(map)
            }
        }
        if (fragments.size > 1 && fragments[Int.ONE] is EditNegativeKeywordsFragment) {
            negativekeywordsAll?.forEach {
                val map = mapOf(
                    "name" to it.tag, "id" to it.keywordId, "type" to "negatif"
                )
                items.add(map)
            }
        }
        return items
    }

}
