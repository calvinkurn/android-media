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
import com.tokopedia.topads.common.data.internal.ParamObject
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
import com.tokopedia.topads.edit.utils.Constants.BID_TYPE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
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
    private var editedSearchBid: Int = 0
    private var maxBid: String = "0"
    private var minBid: String = "0"
    private var editedDailyBudget = "0"
    private var performanceData: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> = mutableListOf()
    private var editedRecomBid: String = ""
    private var bidSuggestion: String = "0"
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

    private var groupId: String = ""

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
            putStringArrayList("productIdList", ArrayList(productListIds))
            putString("groupId", groupId)
            putString("isAutoBid", Utils.getGroupStrategy(isBidAutomatic))
            putParcelableArrayList("bidList", ArrayList(bidSettingsList))
            putStringArrayList("potentialPerformanceList", arrayListOf(performanceData.firstOrNull()?.retention,
                performanceData.getOrNull(1)?.retention))
            putStringArrayList("minMaxBids", arrayListOf(minBid, maxBid))
            putFloat("dailyBudgetInput", getDailyBudgetInput())

        }
        return BaseEditKeywordFragment.newInstance(bundle)
    }

    private fun getBidSettingListAfterModeChange(): MutableList<TopAdsBidSettingsModel> {
        val bidSettingsList: MutableList<TopAdsBidSettingsModel> = mutableListOf()
        if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == "product_auto_search"){
            bidSettingsList.addAll(bidSettingsListManual)
        }else{
            groupInfoResponse?.bidSettings?.forEachIndexed { index, it->
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
        viewModel.getAds(1,
            arguments?.getString(Constants.GROUP_ID),
            "android.topads_edit")
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
        suggestionsDefault.add(DataSuggestions("product", productListIds))
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
            list.add(3, CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                requireActivity().getString(R.string.edit_ad_item_title_ads_search),
                subtitle = requireActivity().getString(
                    R.string.ads_search_item_template,
                    formatFloatWithSeparators(bidSuggestion.toFloat()),
                    totalKeywords
                )

            ) { openSearchBidFragment() })
            list.add(4, CreateEditAdGroupItemUiModel(
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
            updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, if (editedDailyBudget == "0") data.dailyBudget.toString() else editedDailyBudget)

        }
        priceBids.clear()
        priceBids.addAll(data.bidSettings.map { it.priceBid })
        setDividerOnRecyclerView()
        isDailyBudgetActive = groupInfoResponse?.dailyBudget != 0f
    }

    private fun getGroupData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        try {
            dataMap[ParamObject.ACTION_TYPE] = ParamObject.ACTION_EDIT
            dataMap[ParamObject.GROUP_NAME] = groupInfoResponse?.groupName
            dataMap[Constants.DAILY_BUDGET] = if (editedDailyBudget == "0") groupInfoResponse?.dailyBudget else editedDailyBudget
            dataMap[ParamObject.GROUPID] = groupId
            dataMap[Constants.NAME_EDIT] = groupInfoResponse?.groupName != groupName
        } catch (_: NumberFormatException) {
        }
        return dataMap
    }

    private fun updateAdsSearchItem() {
        if (!isBidAutomatic) {
            val totalKeywords = countDataItemsResponse?.get(0)?.totalKeywords
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
        return countDataItemsResponse?.get(0)?.totalKeywords.toString()
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
                    createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.SETTING_MODE, "Iklan diatur manual")
                    showToast("Pengaturan iklanmu berhasil diubah ke manual.")
                } else {
                    createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.SETTING_MODE, "Iklan diatur sistem Topads")
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.ADS_SEARCH)
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)
                    createEditAdGroupAdapter.removeItem(CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE)
                    showToast("Pengaturan iklanmu berhasil diubah ke sistem TopAds. Kamu cukup atur anggaran harian, ya.")
                }

            }

            CreateEditAdGroupItemTag.NAME -> {
                groupName?.let { createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.NAME, it) }
                showToast("Nama grup iklan berhasil diubah.")
            }

            CreateEditAdGroupItemTag.PRODUCT -> {
                val addedCount = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(Constants.ADDED_PRODUCTS_NEW)?.size
                    ?: 0
                val deleteCount = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(Constants.DELETED_PRODUCTS_NEW)?.size
                    ?: 0
                val count = productListIds.size + addedCount - deleteCount
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.PRODUCT, getProductText(count))
                showToast("Produk berhasil disimpan.")
            }

            CreateEditAdGroupItemTag.ADS_SEARCH -> {
                val added = (dataKeyword[POSITIVE_CREATE] as? ArrayList<*>)?.size ?: 0
                val deleted = (dataKeyword[POSITIVE_DELETE] as? ArrayList<*>)?.size ?: 0
                val count = (countDataItemsResponse?.get(0)?.totalKeywords ?: 0) + added - deleted
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.ADS_SEARCH,
                    requireActivity().getString(R.string.ads_search_item_template,
                        getNewPriceBidSearch(dataKeyword),
                        count.toString()))
                showToast("Pengaturan Iklan di Pencarian berhasil diterapkan.")
                updateDailyBudgetAfterBidChange()
                val bids = getPriceBidAfterSearchMutation(dataKeyword)
                priceBids.clear()
                priceBids.addAll(bids)
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
            }


            CreateEditAdGroupItemTag.ADS_RECOMMENDATION -> {
                createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.ADS_RECOMMENDATION, editedRecomBid)
                showToast("Biaya Iklan di Rekomendasi berhasil diterapkan.")
                val searchBid = priceBids.firstOrNull()
                priceBids.clear()
                priceBids.addAll(mutableListOf(searchBid, editedRecomBid.removeCommaRawString().toFloatOrZero()))
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
                updateDailyBudgetAfterBidChange()
            }

            CreateEditAdGroupItemTag.DAILY_BUDGET -> {
                editedDailyBudget = dataGroup[Constants.DAILY_BUDGET].toString()
                dataGroup[Constants.DAILY_BUDGET]?.let { createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, it.toString()) }
                showToast("Perubahan anggaran harian berhasil diterapkan.")
                viewModel.getPerformanceData(productListIds, priceBids, getDailyBudgetInput())
            }

            else -> {}
        }

    }

    private fun getDailyBudgetInput(): Float {
        return if (!isDailyBudgetActive) 0f else if (editedDailyBudget == "0") groupInfoResponse?.dailyBudget
            ?: 0f else editedDailyBudget.toFloatOrZero()
    }

    private fun updateDailyBudgetAfterBidChange() {
        editedDailyBudget = dataGroup[Constants.DAILY_BUDGET].toString()
        createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, editedDailyBudget)
    }

    private fun updateDailyBudget() {
        val searchBidInitial =if (editedSearchBid == 0){
            groupInfoResponse?.bidSettings?.getOrNull(0)?.priceBid ?: 0f
        }else {
            editedSearchBid.toFloat()
        }
        val searchBidFinal = if (searchBidInitial == 0f) bidSuggestion.toFloatOrZero() else searchBidInitial
        val browseBid = if (editedRecomBid.isEmpty()) {
            groupInfoResponse?.bidSettings?.getOrNull(1)?.priceBid ?: 0f
        } else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        val suggestedDailyBudget = if (browseBid > searchBidFinal) browseBid * 40 else searchBidFinal * 40
        val formattedBudget = suggestedDailyBudget.toInt().toString()
        if (dataGroup[Constants.DAILY_BUDGET].toString().toFloatOrZero().toInt() != Int.ZERO){
            dataGroup[Constants.DAILY_BUDGET] = formattedBudget
            editedDailyBudget = formattedBudget
        }

    }

    private fun getPriceBidAfterSearchMutation(dataKeyword: HashMap<String, Any?>): MutableList<Float?> {
        val s = getNewPriceBidSearch(dataKeyword).removeCommaRawString().toFloatOrZero()
        val b = if (editedRecomBid.isEmpty()) priceBids.getOrNull(Int.ONE) else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        return mutableListOf(s, b)
    }

    private fun getNewPriceBidSearch(bidSettings: HashMap<String, Any?>): String {
        val bids: ArrayList<TopAdsBidSettingsModel>? = bidSettings[BID_TYPE] as? ArrayList<TopAdsBidSettingsModel>
        return CurrencyFormatHelper.convertToRupiah(bids?.firstOrNull()?.priceBid?.toInt().toString())
    }

    private fun showToast(msg: String) {
        Toaster.build(requireView(), msg
            ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun openAdGroupSettingModeBottomSheet() {
        groupInfoResponse?.let {
            EditAdGroupSettingModeBottomSheet.newInstance(isBidAutomatic)
                .show(parentFragmentManager) { isAutomatic ->
                    isBidAutomatic = isAutomatic
                    if (isAutomatic) {
                        dataKeyword.clear()
                        strategies.add("auto_bid")
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
                groupInfoResponse?.bidSettings?.firstOrNull()?.priceBid else bidSuggestion.toFloat()))
            bidTypeData.add(
                TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, it.removeCommaRawString().toFloat())
            )
            dataKeyword[Constants.BID_TYPE] = bidTypeData
            updateDailyBudget()
            submitEditGroup(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)

        }
    }

    private fun getRecomBid(): Float? {
        return if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == "product_auto_search") {
            bidSuggestion.toFloat()
        } else if (editedRecomBid.isEmpty()) groupInfoResponse?.bidSettings?.getOrNull(CONST_1)?.priceBid
        else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
    }

    private fun openAdGroupDailyBudgetBottomSheet() {
        val searchBidInitial = if (editedSearchBid == Int.ZERO)groupInfoResponse?.bidSettings?.getOrNull(Int.ZERO)?.priceBid ?: 0f else editedSearchBid.toFloat()
        val searchBid = if (searchBidInitial == 0f) bidSuggestion.toFloatOrZero() else searchBidInitial
        val browseBid = if (editedRecomBid.isEmpty()) {
            groupInfoResponse?.bidSettings?.getOrNull(1)?.priceBid ?: 0f
        } else CurrencyFormatHelper.convertRupiahToDouble(editedRecomBid).toFloat()
        val suggestedDailyBudget = if (browseBid > searchBid) browseBid * 40 else searchBid * 40
        val formattedBudget = CurrencyFormatHelper.convertToRupiah(suggestedDailyBudget.toInt().toString())
        var dailyBudget: String = if (editedDailyBudget == "0") groupInfoResponse?.dailyBudget?.toInt().toString() else editedDailyBudget.removeCommaRawString().toIntOrZero().toString()
        if (dataGroup[Constants.DAILY_BUDGET] == "0") dailyBudget = "0"
        val formattedDailyBudget = CurrencyFormatHelper.convertToRupiah(dailyBudget)
        formattedDailyBudget.let {
            EditAdGroupDailyBudgetBottomSheet.newInstance(it, formattedBudget, productListIds, priceBids, isBidAutomatic).show(parentFragmentManager) { dailyBudget, isToggleOn ->
                this.isDailyBudgetActive = isToggleOn
                if (isToggleOn)
                    dataGroup[Constants.DAILY_BUDGET] = dailyBudget
                else
                    dataGroup[Constants.DAILY_BUDGET] = "0"
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
        dataGroup[STRATEGIES] = if (isBidAutomatic) mutableListOf("auto_bid") else mutableListOf()
        submitEditGroup(CreateEditAdGroupItemTag.PRODUCT)

    }

    private fun getBidTypeData(): ArrayList<TopAdsBidSettingsModel> {
        val bidTypeData: ArrayList<TopAdsBidSettingsModel> = arrayListOf()
        if (groupInfoResponse?.bidSettings?.firstOrNull()?.bidType == "product_auto_search") {
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, bidSuggestion.toFloatOrZero()))
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, bidSuggestion.toFloatOrZero()))
        } else {
            bidTypeData.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, groupInfoResponse?.bidSettings?.firstOrNull()?.priceBid))
            bidTypeData.add(
                TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE,
                    if (editedRecomBid.isEmpty()) groupInfoResponse?.bidSettings?.getOrNull(1)?.priceBid
                    else editedRecomBid.toFloatOrZero())
            )
        }
        return bidTypeData
    }

    fun sendKeywordData(data: HashMap<String, Any?>) {
        dataKeyword = data
        this.editedSearchBid = data["price_bid"] as? Int?:Int.ZERO
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
