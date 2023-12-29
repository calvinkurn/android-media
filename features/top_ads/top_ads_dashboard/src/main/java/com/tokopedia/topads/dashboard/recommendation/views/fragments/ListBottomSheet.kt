package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.databinding.ListBottomsheetLayoutBinding
import com.tokopedia.topads.dashboard.recommendation.common.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ListBottomSheetItemUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.ItemListAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify

class ListBottomSheet :
    BottomSheetUnify(), OnItemSelectChangeListener {

    private var binding: ListBottomsheetLayoutBinding? = null
    private var itemList: List<ListBottomSheetItemUiModel> = listOf()
    private var bottomsheetType: Int = 1
    private var itemChangeListener: OnItemSelectChangeListener? = null
    private var currentAdType: Int? = null
    private var currentGroupId: String? = ""
    private var newAdType: Int? = null
    private var newGroupId: String? = ""
    private var selectedGroupName: String? = ""

    private val adapterItemList: ItemListAdapter by lazy {
        ItemListAdapter(ItemListTypeFactory(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        clearContentPadding = true

        binding = ListBottomsheetLayoutBinding.inflate(layoutInflater).apply {
            rvScheduleSlot.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                this.adapter = adapterItemList
                adapterItemList.setData(itemList)
            }

            rvScheduleSlot.addItemDecoration(
                RecommendationInsightItemDecoration(
                    rvScheduleSlot.context,
                    LinearLayoutManager.VERTICAL
                )
            )

            searchGroup.showWithCondition(bottomsheetType == CHOOSE_AD_GROUP_BOTTOM_SHEET)

            saveCtaButton.setOnClickListener {
                newAdType?.let { itemChangeListener?.onSubmitSelectItemListener(it, newGroupId ?: "", selectedGroupName ?: "") }
                dismiss()
            }

            searchGroup.searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    updateAdGroups(text.toString())
                }
            })
        }
        setChild(binding?.root)
    }

    companion object {
        fun show(fm: FragmentManager, title: String, list: List<ListBottomSheetItemUiModel>, type: Int, listener: OnItemSelectChangeListener, adtype: Int?, groupId: String?): ListBottomSheet {
            val bottomSheet = ListBottomSheet().apply {
                setTitle(title)
                itemList = list
                bottomsheetType = type
                itemChangeListener = listener
                currentAdType = adtype
                currentGroupId = groupId
                newAdType = adtype
                newGroupId = groupId
            }
            bottomSheet.show(fm, "")
            return bottomSheet
        }

        const val CHOOSE_AD_TYPE_BOTTOM_SHEET = 1
        const val CHOOSE_AD_GROUP_BOTTOM_SHEET = 2
    }

    override fun onClickItemListener(adType: Int, groupId: String, groupName: String) {
        itemList.forEach {
            it.isSelected = it.adType == adType && it.groupId == groupId
        }
        newAdType = adType
        newGroupId = groupId
        selectedGroupName = groupName
        showCtaButton()
        if(bottomsheetType == CHOOSE_AD_GROUP_BOTTOM_SHEET){
            updateAdGroups(binding?.searchGroup?.searchBarTextField?.text.toString())
        } else{
            adapterItemList.setData(itemList)
        }
    }

    private fun showCtaButton() {
        binding?.saveCtaButton?.showWithCondition(newAdType != currentAdType || newGroupId != currentGroupId)
    }

    private fun updateAdGroups(text: String) {
        val list = itemList.filter {
            it.title.lowercase().contains(text.trim().lowercase())
        }
        adapterItemList.setData(list)
    }
}
