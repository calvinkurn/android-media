package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_3
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_4
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.RECOMMENDATION_BUDGET_MULTIPLIER
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_AUTO_SEARCH
import com.tokopedia.topads.common.data.internal.ParamObject.SUGGESTION_BID_SETTINGS
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.databinding.TopadsEditFragmentEditAdGroupBinding
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupAdapter
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemState
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemTag
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemUiModel
import com.tokopedia.topads.edit.view.sheet.EditAdGroupDailyBudgetBottomSheet
import com.tokopedia.topads.common.view.sheet.CreateEditAdGroupNameBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupRecommendationBidBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupSettingModeBottomSheet
import com.tokopedia.topads.common.view.adapter.createedit.decorator.CustomDividerItemDecoration
import com.tokopedia.topads.edit.utils.Constants.BID_LIST
import com.tokopedia.topads.edit.utils.Constants.BID_TYPE
import com.tokopedia.topads.edit.utils.Constants.DAILY_BUDGET_INPUT
import com.tokopedia.topads.edit.utils.Constants.EDIT_SOURCE
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.IS_AUTO_BID
import com.tokopedia.topads.edit.utils.Constants.IS_BID_AUTOMATIC
import com.tokopedia.topads.edit.utils.Constants.MIN_MAX_BIDS
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POTENTIAL_PERFORMANCE_LIST
import com.tokopedia.topads.edit.utils.Constants.PRODUCT
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID_LIST
import com.tokopedia.topads.edit.utils.Constants.STRATEGIES
import com.tokopedia.topads.edit.utils.Utils
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.collections.HashMap

class EditAdGroupFragment : BaseDaggerFragment() {

    private var isDailyBudgetActive: Boolean = false
    private var editedSearchBid: Int = Int.ZERO
    private var maxBid: String = Int.ZERO.toString()
    private var minBid: String = Int.ZERO.toString()
    private var editedDailyBudget = Int.ZERO.toString()
    private var performanceData: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> = mutableListOf()
    private var editedRecomBid: String = String.EMPTY
    private var bidSuggestion: String = Int.ZERO.toString()
    private var binding by autoClearedNullable<TopadsEditFragmentEditAdGroupBinding>()
    private val createEditAdGroupAdapter by lazy {
        CreateEditAdGroupAdapter(CreateEditAdGroupTypeFactory())
    }
    private val productListIds by lazy { mutableListOf<String>() }
    private val priceBids: MutableList<Float?> by lazy { mutableListOf() }
    private var isBidAutomatic: Boolean = false

    private var dataProduct = Bundle()
    private var dataKeyword = HashMap<String, Any?>()
    private var dataGroup = HashMap<String, Any?>()
    private var groupName: String? = null
    private val strategies: ArrayList<String> = arrayListOf()
    private var bidSettingsListManual: MutableList<TopAdsBidSettingsModel> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditAdGroupViewModel::class.java]
    }

    private var groupInfoResponse: GroupInfoResponse.TopAdsGetPromoGroup.Data? = null
    private var countDataItemsResponse: List<CountDataItem>? = null

    private var groupId: String = String.EMPTY

    private var settingChangeType: CreateEditAdGroupItemTag? = null

    private fun getList(context: Context): MutableList<Visitable<*>> {
        return mutableListOf(
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.NAME,
                requireActivity().getString(R.string.edit_ad_item_title_name),
                hasDivider = true
            ) { openAdGroupNameBottomSheet() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.PRODUCT,
                requireActivity().getString(R.string.edit_ad_item_title_product),
                hasDivider = true
            ) { openProductFragment() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.SETTING_MODE,
                requireActivity().getString(R.string.edit_ad_item_title_mode)
            ) { openAdGroupSettingModeBottomSheet() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                requireActivity().getString(R.string.edit_ad_item_title_ads_search)
            ) { openSearchBidFragment() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                requireActivity().getString(R.string.edit_ad_item_title_ads_recommendation)
            ) { openAdGroupRecommendationBidBottomSheet() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.DAILY_BUDGET,
                requireActivity().getString(R.string.edit_ad_item_title_daily_budget),
                hasDivider = true
            ) { openAdGroupDailyBudgetBottomSheet() },
            CreateEditAdGroupItemAdsPotentialUiModel(
                CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE,
                requireActivity().getString(R.string.edit_ad_item_title_potential_performance),
                context.getString(R.string.footer_potential_widget_edit_ad_group_text),
                "",
                potentialWidgetList,
                state = CreateEditAdGroupItemState.LOADING
            )
        )
    }

    private fun openSearchBidFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.parent_view, getSearchFragment()).commit()
            addToBackStack(null)
        }
    }

    private fun getSearchFragment(): Fragment {
        val bidSettingsList = getBidSettingListAfterModeChange()
        val bundle = Bundle(arguments).apply {
            putStringArrayList(PRODUCT_ID_LIST, ArrayList(productListIds))
            putString(GROUP_ID, groupId)
            putString(IS_AUTO_BID, Utils.getGroupStrategy(isBidAutomatic))
            putParcelableArrayList(BID_LIST, ArrayList(bidSettingsList))
            putStringArrayList(POTENTIAL_PERFORMANCE_LIST, arrayListOf(performanceData.firstOrNull()?.retention,
                performanceData.getOrNull(1)?.retention))
            putStringArrayList(MIN_MAX_BIDS, arrayListOf(minBid, maxBid))
            putFloat(DAILY_BUDGET_INPUT, getDailyBudgetInput())

        }
        return BaseEditKeywordFragment.newInstance(bundle)
    }

    private fun getBidSettingListAfterModeChange(): MutableList<TopAdsBidSettingsModel> {
        val bidSettingsList: MutableList<TopAdsBidSettingsModel> = mutableListOf()
        if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == PRODUCT_AUTO_SEARCH) {
            bidSettingsList.addAll(bidSettingsListManual)
        } else {
            groupInfoResponse?.bidSettings?.forEachIndexed { index, it ->
                val topAdsBidSettingsModel = TopAdsBidSettingsModel()
                topAdsBidSettingsModel.bidType = it.bidType
                topAdsBidSettingsModel.priceBid = getPriceBidAfterUpdate(it.priceBid, index)
                bidSettingsList.add(topAdsBidSettingsModel)
            }
        }
        return bidSettingsList
    }

    private fun getPriceBidAfterUpdate(priceBid: Float?, index: Int): Float? {
        return if (index == Int.ZERO) {
            if (editedSearchBid == Int.ZERO) priceBid else editedSearchBid.toFloat()
        } else {
            if (editedRecomBid.isEmpty()) priceBid else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        }
    }

    private fun openProductFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.parent_view, getFragment()).commit()
            addToBackStack(null)
        }
    }

    private fun getFragment(): Fragment {
        val fragment = EditProductFragment()
        fragment.arguments = this.arguments
        return fragment
    }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopadsEditFragmentEditAdGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.header?.setNavigationOnClickListener { activity?.onBackPressed() }

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = createEditAdGroupAdapter
            activity?.let {
                createEditAdGroupAdapter.updateList(getList(it))
            }
        }
        arguments?.getString(Constants.GROUP_ID)?.let {
            groupId = it
        }

        viewModel.getGroupInfo(groupId, this::onSuccessGroupInfo)
        viewModel.getTotalAdsAndKeywordsCount(groupId)
        fetchProductData()
        observeLiveData()
        groupInfoResponse?.strategies?.let { isBidAutomatic = checkBidIsAutomatic(it) }

    }

    private fun onDefaultSuccessSuggestion(dataItems: List<TopadsBidInfo.DataItem>) {
        this.maxBid = dataItems.firstOrNull()?.maxBid ?: ""
        this.minBid = dataItems.firstOrNull()?.minBid ?: ""
    }

    private fun fetchProductData() {
        viewModel.getAds(Int.ONE,
            arguments?.getString(Constants.GROUP_ID),
            EDIT_SOURCE)
    }

    private fun onSuccessGetAds(dataItems: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) {

        dataProduct.putParcelableArrayList(Constants.ADDED_PRODUCTS, toArrayList(dataItems))
        productListIds.clear()
        productListIds.addAll(dataItems.map { it.itemID })
        viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
        viewModel.getProductBid(productListIds)
        getDefaultBid()
    }

    private fun getDefaultBid() {
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions(PRODUCT, productListIds))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onDefaultSuccessSuggestion)
    }

    private fun toArrayList(dataItems: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>): ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> {
        val list: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
            arrayListOf()
        dataItems.forEach {
            list.add(it)
        }
        return list
    }

    private fun observeLiveData() {

        viewModel.adsData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetAds(it.data.topadsGetListProductsOfGroup.data)
                }

                else -> {}
            }
        }

        viewModel.adsKeywordCount.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessTotalAdsAndKeywordsCount(it.data.data)
                }

                else -> {}
            }
        }

        viewModel.performanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessSearchPerformanceData(it.data)
                }

                else -> {}
            }
        }

        viewModel.bidProductData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessBidSuggestion(it.data.topAdsGetBidSuggestionByProductIDs.bidData.bidSuggestion)
                }

                else -> {}
            }
        }

    }

    private fun onSuccessBidSuggestion(bidSuggestion: Int) {
        this.bidSuggestion = bidSuggestion.toString().removeCommaRawString()
        bidSettingsListManual = mutableListOf(TopAdsBidSettingsModel(
            ParamObject.PRODUCT_SEARCH, bidSuggestion.toFloat()),
            TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, bidSuggestion.toFloat()))
    }

    private fun onSuccessKeyword(keywordsItems: List<GetKeywordResponse.KeywordsItem>, s: String) {
        val totalKeywords = keywordsItems.count().toString()
        context?.let { it1 ->
            val list = createEditAdGroupAdapter.list
            list.add(CONST_3, CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                requireActivity().getString(R.string.edit_ad_item_title_ads_search),
                subtitle = requireActivity().getString(
                    R.string.ads_search_item_template,
                    formatFloatWithSeparators(bidSuggestion.toFloat()),
                    totalKeywords
                )

            ) { openSearchBidFragment() })
            list.add(CONST_4, CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                requireActivity().getString(R.string.edit_ad_item_title_ads_recommendation),
                subtitle = formatFloatWithSeparators(bidSuggestion.toFloat())
            ) { openAdGroupRecommendationBidBottomSheet() })
            list.add(CreateEditAdGroupItemAdsPotentialUiModel(
                CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE,
                requireActivity().getString(R.string.edit_ad_item_title_potential_performance),
                it1.getString(R.string.footer_potential_widget_edit_ad_group_text),
                "",
                potentialWidgetList,
                state = CreateEditAdGroupItemState.LOADING
            ))

            createEditAdGroupAdapter.updateList(list.toMutableList())
            createEditAdGroupAdapter.updatePotentialWidget(performanceData)
        }
    }

    private fun onSuccessSearchPerformanceData(data: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>) {
        this.performanceData = data
        createEditAdGroupAdapter.updatePotentialWidget(data)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {

        isBidAutomatic = checkBidIsAutomatic(data.strategies)
        groupInfoResponse = data
        dataGroup = getGroupData()

        val bidSettingsList: MutableList<TopAdsBidSettingsModel> = mutableListOf()
        data.bidSettings.forEach {
            val topAdsBidSettingsModel = TopAdsBidSettingsModel()
            topAdsBidSettingsModel.bidType = it.bidType
            topAdsBidSettingsModel.priceBid = it.priceBid
            bidSettingsList.add(topAdsBidSettingsModel)
        }

        createEditAdGroupAdapter.apply {
            updateValue(CreateEditAdGroupItemTag.NAME, data.groupName)
            updateValue(CreateEditAdGroupItemTag.SETTING_MODE, getSettingModeText(isBidAutomatic))
            if (isBidAutomatic) {
                removeItem(CreateEditAdGroupItemTag.ADS_SEARCH)
                removeItem(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)
                removeItem(CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE)
            } else {
                updateAdsSearchItem()
                updateValue(
                    CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                    editedRecomBid.ifEmpty { getPriceBid(data.bidSettings, BID_SETTINGS_TYPE_BROWSE) }
                )
            }
            updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, if (editedDailyBudget == Int.ZERO.toString()) data.dailyBudget.toString() else editedDailyBudget)

        }
        priceBids.clear()
        priceBids.addAll(data.bidSettings.map { it.priceBid })
        setDividerOnRecyclerView()
        isDailyBudgetActive = groupInfoResponse?.dailyBudget != Int.ZERO.toFloat()
    }

    private fun getGroupData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        try {
            dataMap[ParamObject.ACTION_TYPE] = ParamObject.ACTION_EDIT
            dataMap[ParamObject.GROUP_NAME] = groupInfoResponse?.groupName
            dataMap[Constants.DAILY_BUDGET] = if (editedDailyBudget == Int.ZERO.toString()) groupInfoResponse?.dailyBudget else editedDailyBudget
            dataMap[ParamObject.GROUPID] = groupId
            dataMap[Constants.NAME_EDIT] = groupInfoResponse?.groupName != groupName
        } catch (_: NumberFormatException) {
        }
        return dataMap
    }

    private fun updateAdsSearchItem() {
        if (!isBidAutomatic) {
            val totalKeywords = countDataItemsResponse?.firstOrNull()?.totalKeywords
            val priceBid = groupInfoResponse?.bidSettings?.let {
                getPriceBid(it, BID_SETTINGS_TYPE_SEARCH)
            }
            if (totalKeywords != null && priceBid != null) {
                createEditAdGroupAdapter.updateValue(
                    CreateEditAdGroupItemTag.ADS_SEARCH,
                    requireActivity().getString(
                        R.string.ads_search_item_template,
                        getPriceBidSearch(),
                        getSearchKeywords()
                    )
                )
            }
        }

    }

    private fun getSearchKeywords(): String {
        return countDataItemsResponse?.firstOrNull()?.totalKeywords.toString()
    }

    private fun getPriceBidSearch(): String? {
        return groupInfoResponse?.bidSettings?.let {
            getPriceBid(it, BID_SETTINGS_TYPE_SEARCH)
        }
    }


    private fun onSuccessTotalAdsAndKeywordsCount(countDataItems: List<CountDataItem>) {
        countDataItemsResponse = countDataItems
        countDataItemsResponse?.get(0)?.totalProducts?.let {
            createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.PRODUCT, getProductText(it))
        }

        updateAdsSearchItem()

    }

    private fun setDividerOnRecyclerView() {
        val dividerPositions = createEditAdGroupAdapter.list
            .mapIndexedNotNull { index, item ->
                if (item is CreateEditAdGroupItemUiModel && item.hasDivider) {
                    index
                } else {
                    null
                }
            }

        val itemDecoration = CustomDividerItemDecoration(dividerPositions)
        binding?.recyclerView?.apply {
            addItemDecoration(itemDecoration)
        }
    }

    private fun checkBidIsAutomatic(strategies: List<String>): Boolean {
        return strategies.contains(ParamObject.AUTO_BID_STATE)
    }

    private fun getProductText(groupTotal: Int): String {
        return String.format(getString(R.string.selected_product), groupTotal)
    }

    private fun getPriceBid(
        bidSettings: List<GroupInfoResponse.TopAdsGetPromoGroup.TopadsGroupBidSetting>,
        settingsType: String
    ): String {
        val bidSetting = bidSettings.find { it.bidType == settingsType }
        return CurrencyFormatHelper.convertToRupiah(bidSetting?.priceBid?.toInt().toString())
    }

    private fun getSettingModeText(bidAutomatic: Boolean): String {
        activity?.let {
            if (bidAutomatic) return it.getString(R.string.top_ads_edit_ad_group_item_mode_automatic)
            return it.getString(R.string.top_ads_edit_ad_group_item_mode_manual)
        }
        return ""
    }

    private fun openAdGroupNameBottomSheet() {
        groupInfoResponse?.let {
            CreateEditAdGroupNameBottomSheet.newInstance(it.groupName, groupId)
                .show(parentFragmentManager) { groupName ->
                    viewModel.validateGroup(groupName, ::onValidateNameSuccess, ::onValidateNameFailure)
                }
        }
    }

    private fun onValidateNameFailure(error: String?) {
        Toaster.build(requireView(), error
            ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun onValidateNameSuccess(topAdsGroupValidateNameV2: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        autoFillGroupName(topAdsGroupValidateNameV2.data.groupName)
    }

    private fun autoFillGroupName(groupName: String) {
        dataKeyword.clear()
        dataProduct.clear()
        this.groupName = groupName
        dataGroup[ParamObject.GROUP_NAME] = groupName
        dataGroup[Constants.NAME_EDIT] = groupInfoResponse?.groupName != groupName
        submitEditGroup(CreateEditAdGroupItemTag.NAME)

    }

    private fun submitEditGroup(settingChangeType: CreateEditAdGroupItemTag) {
        this.settingChangeType = settingChangeType
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup,
            ::onSuccessGroupEdited, ::onErrorEdit)

    }

    private fun onErrorEdit(errorMessage: String?) {
        Toaster.build(requireView(), errorMessage
            ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun onSuccessGroupEdited() {
        when (settingChangeType) {
            CreateEditAdGroupItemTag.SETTING_MODE -> {
                if (!isBidAutomatic) {
                    viewModel.getAdKeyword(groupId.toIntOrZero(), "", this::onSuccessKeyword)
                    createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.SETTING_MODE, getString(R.string.top_ads_search_browse_item_settings_subtitle_manual))
                    showToast(getString(R.string.top_ads_search_browse_item_settings_toast_manual))
                } else {
                    createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.SETTING_MODE, getString(R.string.top_ads_search_browse_item_settings_subtitle_auto))
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.ADS_SEARCH)
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE)
                    showToast(getString(R.string.top_ads_search_browse_item_settings_toast_auto))
                }

            }

            CreateEditAdGroupItemTag.NAME -> {
                groupName?.let { createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.NAME, it) }
                showToast(getString(R.string.top_ads_name_change_toast))
            }

            CreateEditAdGroupItemTag.PRODUCT -> {
                val addedCount = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(Constants.ADDED_PRODUCTS_NEW)?.size
                    ?: 0
                val deleteCount = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(Constants.DELETED_PRODUCTS_NEW)?.size
                    ?: 0
                val count = productListIds.size + addedCount - deleteCount
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.PRODUCT, getProductText(count))
                showToast(getString(R.string.top_ads_product_add_delete_toast))
            }

            CreateEditAdGroupItemTag.ADS_SEARCH -> {
                val added = (dataKeyword[POSITIVE_CREATE] as? ArrayList<*>)?.size ?: 0
                val deleted = (dataKeyword[POSITIVE_DELETE] as? ArrayList<*>)?.size ?: 0
                val count = (countDataItemsResponse?.get(0)?.totalKeywords ?: 0) + added - deleted
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.ADS_SEARCH,
                    requireActivity().getString(R.string.ads_search_item_template,
                        getNewPriceBidSearch(dataKeyword),
                        count.toString()))
                showToast(getString(R.string.top_ads_search_bid_change_toast))
                updateDailyBudgetAfterBidChange()
                val bids = getPriceBidAfterSearchMutation(dataKeyword)
                priceBids.clear()
                priceBids.addAll(bids)
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
            }


            CreateEditAdGroupItemTag.ADS_RECOMMENDATION -> {
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.ADS_RECOMMENDATION, editedRecomBid)
                showToast(getString(R.string.top_ads_browse_bid_change_toast))
                val searchBid = priceBids.firstOrNull()
                priceBids.clear()
                priceBids.addAll(mutableListOf(searchBid, editedRecomBid.removeCommaRawString().toFloatOrZero()))
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
                updateDailyBudgetAfterBidChange()
            }

            CreateEditAdGroupItemTag.DAILY_BUDGET -> {
                editedDailyBudget = dataGroup[Constants.DAILY_BUDGET].toString()
                dataGroup[Constants.DAILY_BUDGET]?.let { createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, it.toString()) }
                showToast(getString(R.string.top_ads_daily_budget_change_toast))
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
            }

            else -> {}
        }

    }

    private fun getDailyBudgetInput(): Float {
        return if (!isDailyBudgetActive) 0f else if (editedDailyBudget == Int.ZERO.toString()) groupInfoResponse?.dailyBudget
            ?: 0f else editedDailyBudget.toFloatOrZero()
    }

    private fun updateDailyBudgetAfterBidChange() {
        editedDailyBudget = dataGroup[Constants.DAILY_BUDGET].toString()
        createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, editedDailyBudget)
    }

    private fun updateDailyBudget() {
        val searchBidInitial = if (editedSearchBid == Int.ZERO) {
            groupInfoResponse?.bidSettings?.firstOrNull()?.priceBid ?: Int.ZERO.toFloat()
        } else {
            editedSearchBid.toFloat()
        }
        val searchBidFinal = if (searchBidInitial == Int.ZERO.toFloat()) bidSuggestion.toFloatOrZero() else searchBidInitial
        val browseBid = if (editedRecomBid.isEmpty()) {
            groupInfoResponse?.bidSettings?.getOrNull(Int.ONE)?.priceBid ?: Int.ZERO.toFloat()
        } else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        val suggestedDailyBudget = if (browseBid > searchBidFinal) browseBid * RECOMMENDATION_BUDGET_MULTIPLIER else searchBidFinal * RECOMMENDATION_BUDGET_MULTIPLIER
        val formattedBudget = suggestedDailyBudget.toInt().toString()
        if (dataGroup[Constants.DAILY_BUDGET].toString().toFloatOrZero().toInt() != Int.ZERO) {
            dataGroup[Constants.DAILY_BUDGET] = formattedBudget
            editedDailyBudget = formattedBudget
        }

    }

    private fun getPriceBidAfterSearchMutation(dataKeyword: HashMap<String, Any?>): MutableList<Float?> {
        val search = getNewPriceBidSearch(dataKeyword).removeCommaRawString().toFloatOrZero()
        val browse = if (editedRecomBid.isEmpty()) priceBids.getOrNull(Int.ONE) else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        return mutableListOf(search, browse)
    }

    private fun getNewPriceBidSearch(bidSettings: HashMap<String, Any?>): String {
        val bids: ArrayList<TopAdsBidSettingsModel>? = bidSettings[BID_TYPE] as? ArrayList<TopAdsBidSettingsModel>
        return CurrencyFormatHelper.convertToRupiah(bids?.firstOrNull()?.priceBid?.toInt().toString())
    }

    private fun showToast(msg: String) {
        Toaster.build(requireView(), msg, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun openAdGroupSettingModeBottomSheet() {
        groupInfoResponse?.let {
            EditAdGroupSettingModeBottomSheet.newInstance(isBidAutomatic)
                .show(parentFragmentManager) { isAutomatic ->
                    isBidAutomatic = isAutomatic
                    if (isAutomatic) {
                        dataKeyword.clear()
                        strategies.add(IS_BID_AUTOMATIC)
                        dataGroup[STRATEGIES] = strategies
                        submitEditGroup(CreateEditAdGroupItemTag.SETTING_MODE)
                    } else {
                        dataGroup[BID_TYPE] = bidSettingsListManual
                        dataKeyword[SUGGESTION_BID_SETTINGS] = bidSettingsListManual
                        dataGroup[STRATEGIES] = mutableListOf<String>()
                        submitEditGroup(CreateEditAdGroupItemTag.SETTING_MODE)
                    }
                }
        }

    }

    private fun openAdGroupRecommendationBidBottomSheet() {
        EditAdGroupRecommendationBidBottomSheet.newInstance(getRecomBid(), productListIds, minBid, maxBid, performanceData, getDailyBudgetInput()).show(parentFragmentManager) {
            dataKeyword.clear()
            dataProduct.clear()
            this.editedRecomBid = it
            val bidTypeData: ArrayList<TopAdsBidSettingsModel> = arrayListOf()
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType != "product_auto_search")
                groupInfoResponse?.bidSettings?.firstOrNull()?.priceBid else bidSuggestion.toFloatOrZero()))
            bidTypeData.add(
                TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, it.removeCommaRawString().toFloatOrZero())
            )
            dataKeyword[Constants.BID_TYPE] = bidTypeData
            updateDailyBudget()
            submitEditGroup(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)

        }
    }

    private fun getRecomBid(): Float? {
        return if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == PRODUCT_AUTO_SEARCH) {
            bidSuggestion.toFloat()
        } else if (editedRecomBid.isEmpty()) groupInfoResponse?.bidSettings?.getOrNull(CONST_1)?.priceBid
        else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
    }

    private fun openAdGroupDailyBudgetBottomSheet() {
        val searchBidInitial = if (editedSearchBid == Int.ZERO) groupInfoResponse?.bidSettings?.getOrNull(Int.ZERO)?.priceBid
            ?: 0f else editedSearchBid.toFloat()
        val searchBid = if (searchBidInitial == 0f) bidSuggestion.toFloatOrZero() else searchBidInitial
        val browseBid = if (editedRecomBid.isEmpty()) {
            groupInfoResponse?.bidSettings?.getOrNull(1)?.priceBid ?: Int.ZERO.toFloat()
        } else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        val suggestedDailyBudget = if (browseBid > searchBid) browseBid * RECOMMENDATION_BUDGET_MULTIPLIER else searchBid * RECOMMENDATION_BUDGET_MULTIPLIER
        val formattedBudget = CurrencyFormatHelper.convertToRupiah(suggestedDailyBudget.toInt().toString())
        var dailyBudget: String = if (editedDailyBudget == Int.ZERO.toString()) groupInfoResponse?.dailyBudget?.toInt().toString() else editedDailyBudget.removeCommaRawString().toIntOrZero().toString()
        if (dataGroup[Constants.DAILY_BUDGET] == Int.ZERO.toString()) dailyBudget = Int.ZERO.toString()
        val formattedDailyBudget = CurrencyFormatHelper.convertToRupiah(dailyBudget)
        formattedDailyBudget.let {
            EditAdGroupDailyBudgetBottomSheet.newInstance(it, formattedBudget, productListIds, priceBids, isBidAutomatic).show(parentFragmentManager) { dailyBudget, isToggleOn ->
                this.isDailyBudgetActive = isToggleOn
                if (isToggleOn)
                    dataGroup[Constants.DAILY_BUDGET] = dailyBudget
                else
                    dataGroup[Constants.DAILY_BUDGET] = Int.ZERO.toString()
                dataProduct.clear()
                submitEditGroup(CreateEditAdGroupItemTag.DAILY_BUDGET)
            }
        }

    }

    fun sendData(items: Bundle) {
        dataProduct.clear()
        dataKeyword.clear()
        dataProduct = items
        dataGroup[BID_TYPE] = getBidTypeData()
        dataKeyword[SUGGESTION_BID_SETTINGS] = bidSettingsListManual
        dataGroup[STRATEGIES] = if (isBidAutomatic) mutableListOf(IS_BID_AUTOMATIC) else mutableListOf()
        submitEditGroup(CreateEditAdGroupItemTag.PRODUCT)

    }

    private fun getBidTypeData(): ArrayList<TopAdsBidSettingsModel> {
        val bidTypeData: ArrayList<TopAdsBidSettingsModel> = arrayListOf()
        if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == PRODUCT_AUTO_SEARCH) {
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, bidSuggestion.toFloatOrZero()))
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, bidSuggestion.toFloatOrZero()))
        } else {
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, groupInfoResponse?.bidSettings?.firstOrNull()?.priceBid))
            bidTypeData.add(
                TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE,
                    if (editedRecomBid.isEmpty()) groupInfoResponse?.bidSettings?.getOrNull(1)?.priceBid
                    else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat())
            )
        }
        return bidTypeData
    }

    fun sendKeywordData(data: HashMap<String, Any?>) {
        dataKeyword = data
        this.editedSearchBid = data[PARAM_PRICE_BID] as? Int ?: Int.ZERO
        updateDailyBudget()
        submitEditGroup(CreateEditAdGroupItemTag.ADS_SEARCH)
    }

    companion object {
        const val BID_SETTINGS_TYPE_SEARCH = "product_search"
        const val BID_SETTINGS_TYPE_BROWSE = "product_browse"
        fun newInstance(): EditAdGroupFragment {
            return EditAdGroupFragment()
        }

        private val potentialWidgetList: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> =
            mutableListOf(
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel()
            )

        fun formatFloatWithSeparators(value: Float?): String {
            val formatter = DecimalFormat("###,###.###")
            return "Rp. " + formatter.format(value)
        }
    }
}
