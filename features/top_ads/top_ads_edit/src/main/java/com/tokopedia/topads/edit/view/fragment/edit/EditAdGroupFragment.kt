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
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.databinding.TopadsEditFragmentEditAdGroupBinding
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupAdapter
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupTypeFactory
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemState
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemTag
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemUiModel
import com.tokopedia.topads.edit.view.sheet.EditAdGroupDailyBudgetBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupNameBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupRecommendationBidBottomSheet
import com.tokopedia.topads.edit.view.sheet.EditAdGroupSettingModeBottomSheet
import com.tokopedia.topads.edit.view.viewholder.CustomDividerItemDecoration
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class EditAdGroupFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<TopadsEditFragmentEditAdGroupBinding>()
    private val editAdGroupAdapter by lazy {
        EditAdGroupAdapter(EditAdGroupTypeFactory())
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditAdGroupViewModel::class.java]
    }

    var response: GroupInfoResponse.TopAdsGetPromoGroup.Data? = null

    //    private var groupId: String = "0"
    private var groupId: String = "24270188"
//    private var groupId: String = "24270187"

    private fun getList(context: Context): MutableList<Visitable<*>> {
        return mutableListOf(
            EditAdGroupItemUiModel(EditAdGroupItemTag.NAME, requireActivity().getString(R.string.edit_ad_item_title_name), hasDivider = true) { openAdGroupNameBottomSheet() },
            EditAdGroupItemUiModel(EditAdGroupItemTag.PRODUCT, requireActivity().getString(R.string.edit_ad_item_title_product), hasDivider = true) { },
            EditAdGroupItemUiModel(EditAdGroupItemTag.SETTING_MODE, requireActivity().getString(R.string.edit_ad_item_title_mode)) { openAdGroupSettingModeBottomSheet() },
            EditAdGroupItemUiModel(EditAdGroupItemTag.ADS_SEARCH, requireActivity().getString(R.string.edit_ad_item_title_ads_search)) { },
            EditAdGroupItemUiModel(EditAdGroupItemTag.ADS_RECOMMENDATION, requireActivity().getString(R.string.edit_ad_item_title_ads_recommendation)) { openAdGroupRecommendationBidBottomSheet() },
            EditAdGroupItemUiModel(EditAdGroupItemTag.DAILY_BUDGET, requireActivity().getString(R.string.edit_ad_item_title_daily_budget), hasDivider = true) { openAdGroupDailyBudgetBottomSheet() },
            EditAdGroupItemAdsPotentialUiModel(
                EditAdGroupItemTag.POTENTIAL_PERFORMANCE,
                requireActivity().getString(R.string.edit_ad_item_title_potential_performance),
                context.getString(R.string.footer_potential_widget_edit_ad_group_text),
                "",
                potentialWidgetList,
                EditAdGroupItemState.LOADING
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
            adapter = editAdGroupAdapter
            activity?.let {
                editAdGroupAdapter.updateList(getList(it))
            }
        }

        arguments?.getString(Constants.GROUP_ID)?.let {
            groupId = it
        }

        viewModel.getGroupInfo(groupId, this::onSuccessGroupInfo)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        val isBidAutomatic = checkBidIsAutomatic(data.strategies)

        editAdGroupAdapter.apply {
            updateValue(EditAdGroupItemTag.NAME, data.groupName)
            updateValue(EditAdGroupItemTag.PRODUCT, getProductText(data.groupTotal))
            updateValue(EditAdGroupItemTag.SETTING_MODE, getSettingModeText(isBidAutomatic))
            if (isBidAutomatic) {
                removeItem(EditAdGroupItemTag.ADS_SEARCH)
                removeItem(EditAdGroupItemTag.ADS_RECOMMENDATION)
            } else {
                updateValue(EditAdGroupItemTag.ADS_SEARCH, getPriceBid(data.bidSettings, BID_SETTINGS_TYPE_SEARCH))
                updateValue(EditAdGroupItemTag.ADS_RECOMMENDATION, getPriceBid(data.bidSettings, BID_SETTINGS_TYPE_BROWSE))
            }
            updateValue(EditAdGroupItemTag.DAILY_BUDGET, data.dailyBudget.toString())
        }

        response = data
        setDividerOnRecyclerView()
    }

    private fun setDividerOnRecyclerView() {
        val dividerPositions = editAdGroupAdapter.list
            .mapIndexedNotNull { index, item ->
                if (item is EditAdGroupItemUiModel && item.hasDivider) {
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

    private fun getProductText(groupTotal: String): String {
        return getString(R.string.selected_product, groupTotal)
    }

    private fun getPriceBid(
        bidSettings: List<GroupInfoResponse.TopAdsGetPromoGroup.TopadsGroupBidSetting>,
        settingsType: String
    ): String {
        val bidSetting = bidSettings.find { it.bidType == settingsType }
        return bidSetting?.priceBid.toString()
    }

    private fun getSettingModeText(bidAutomatic: Boolean): String {
        activity?.let {
            if (bidAutomatic) return it.getString(R.string.top_ads_edit_ad_group_item_mode_automatic)
            return it.getString(R.string.top_ads_edit_ad_group_item_mode_manual)
        }
        return ""
    }

    private fun openAdGroupNameBottomSheet() {
        response?.let {
            EditAdGroupNameBottomSheet.newInstance(it.groupName).show(parentFragmentManager)
        }
    }
    private fun openAdGroupSettingModeBottomSheet() {
        response?.let {
            val isBidAutomatic = checkBidIsAutomatic(it.strategies)
            EditAdGroupSettingModeBottomSheet.newInstance(isBidAutomatic).show(parentFragmentManager)
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

        private val potentialWidgetList: MutableList<EditAdGroupItemAdsPotentialWidgetUiModel> = mutableListOf(
            EditAdGroupItemAdsPotentialWidgetUiModel("Di Pencarian", "400x/minggu", "+12% meningkat"),
            EditAdGroupItemAdsPotentialWidgetUiModel("Di Rekomendasi", "400x/minggu", "+12% meningkat"),
            EditAdGroupItemAdsPotentialWidgetUiModel("Total Tampil", "800x/minggu", "+12% meningkat")
        )
    }
}
