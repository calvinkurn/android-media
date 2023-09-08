package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.common.data.response.GroupInfoResponse
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
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.DecimalFormat
import javax.inject.Inject

class EditAdGroupFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<TopadsEditFragmentEditAdGroupBinding>()
    private val createEditAdGroupAdapter by lazy {
        CreateEditAdGroupAdapter(CreateEditAdGroupTypeFactory())
    }

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

    //    private var groupId: String = "0"
    private var groupId: String = "24270188"
//    private var groupId: String = "24270187"

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
            ) { },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.SETTING_MODE,
                requireActivity().getString(R.string.edit_ad_item_title_mode)
            ) { openAdGroupSettingModeBottomSheet() },
            CreateEditAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                requireActivity().getString(R.string.edit_ad_item_title_ads_search)
            ) { },
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
//        viewModel.getTotalAdsAndKeywordsCount(groupId, this::onSuccessTotalAdsAndKeywordsCount)
    }
    //TODO fix onSuccessGroupInfo and onSuccessTotalAdsAndKeywordsCount,
    // since response succession coming in near time, there's possibility it causing IllegalStateException
    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        val isBidAutomatic = checkBidIsAutomatic(data.strategies)
        groupInfoResponse = data
        createEditAdGroupAdapter.apply {
            updateValue(CreateEditAdGroupItemTag.NAME, data.groupName)
            updateValue(CreateEditAdGroupItemTag.SETTING_MODE, getSettingModeText(isBidAutomatic))
            if (isBidAutomatic) {
                removeItem(CreateEditAdGroupItemTag.ADS_SEARCH)
                removeItem(CreateEditAdGroupItemTag.ADS_RECOMMENDATION)
            } else {
                updateAdsSearchItem()
                updateValue(
                    CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                    getPriceBid(data.bidSettings, BID_SETTINGS_TYPE_BROWSE)
                )
            }
            updateValue(CreateEditAdGroupItemTag.DAILY_BUDGET, data.dailyBudget.toString())
        }


        setDividerOnRecyclerView()
    }

    private fun updateAdsSearchItem() {
        val totalKeywords = countDataItemsResponse?.get(0)?.totalKeywords
        val priceBid = groupInfoResponse?.bidSettings?.let {
            getPriceBid(it, BID_SETTINGS_TYPE_SEARCH)
        }
        if (totalKeywords != null && priceBid != null) {
            createEditAdGroupAdapter.updateValue(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                requireActivity().getString(
                    R.string.ads_search_item_template,
                    priceBid,
                    totalKeywords.toString()
                )
            )
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
        return getString(R.string.selected_product, groupTotal)
    }

    private fun getPriceBid(
        bidSettings: List<GroupInfoResponse.TopAdsGetPromoGroup.TopadsGroupBidSetting>,
        settingsType: String
    ): String {
        val bidSetting = bidSettings.find { it.bidType == settingsType }
        return formatFloatWithSeparators(bidSetting?.priceBid)
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
                .show(parentFragmentManager){}
        }
    }

    private fun openAdGroupSettingModeBottomSheet() {
        groupInfoResponse?.let {
            val isBidAutomatic = checkBidIsAutomatic(it.strategies)
            EditAdGroupSettingModeBottomSheet.newInstance(isBidAutomatic)
                .show(parentFragmentManager)
        }
    }

    private fun openAdGroupRecommendationBidBottomSheet() {
        EditAdGroupRecommendationBidBottomSheet.newInstance().show(parentFragmentManager)
    }

    private fun openAdGroupDailyBudgetBottomSheet() {
        EditAdGroupDailyBudgetBottomSheet.newInstance().show(parentFragmentManager)
    }

    companion object {
        const val BID_SETTINGS_TYPE_SEARCH = "product_search"
        const val BID_SETTINGS_TYPE_BROWSE = "product_browse"
        fun newInstance(): EditAdGroupFragment {
            return EditAdGroupFragment()
        }

        private val potentialWidgetList: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> =
            mutableListOf(
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Di Pencarian",
                    "400x/minggu",
                    "+12% meningkat"
                ),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Di Rekomendasi",
                    "400x/minggu",
                    "+12% meningkat"
                ),
                CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                    "Total Tampil",
                    "800x/minggu",
                    "+12% meningkat"
                )
            )

        fun formatFloatWithSeparators(value: Float?): String {
            val formatter = DecimalFormat("###,###.###")
            return "Rp. " + formatter.format(value)
        }
    }
}
