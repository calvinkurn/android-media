package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.databinding.ListBottomsheetLayoutBinding
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.utils.OnItemSelectChangeListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.ItemListAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

class ListBottomSheet:
    BottomSheetUnify(), OnItemSelectChangeListener {

    private var binding : ListBottomsheetLayoutBinding? = null
    private var itemList: List<ItemListUiModel> = listOf()
    private var bottomsheetType: Int = 1
    private var itemChangeListener: OnItemSelectChangeListener? = null
    private var currentAdType : Int? = null
    private var currentGroupId : String? = ""
    private var newAdType : Int? = null
    private var newGroupId : String? = ""
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

    private fun initView(){
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

            searchGroup.showWithCondition(bottomsheetType == CHOOSE_AD_GROUP_BOTTOMSHEET)

            saveCtaButton.setOnClickListener{
                newAdType?.let { itemChangeListener?.onClickItemListener(it, newGroupId ?: "", selectedGroupName ?: "") }
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
        fun show(fm: FragmentManager, title:String, list: List<ItemListUiModel>, type: Int, listener: OnItemSelectChangeListener, adtype: Int?, groupId:String?): ListBottomSheet {
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

        val CHOOSE_AD_TYPE_BOTTOMSHEET = 1
        val CHOOSE_AD_GROUP_BOTTOMSHEET = 2
    }

    override fun onClickItemListener(adType: Int, groupId: String, groupName: String) {
        itemList.forEach{
            it.isSelected = it.adType == adType && it.groupId == groupId
        }
        newAdType = adType
        newGroupId = groupId
        selectedGroupName = groupName
        adapterItemList.setData(itemList)
        showCtaButton()
    }

    private fun showCtaButton() {
        binding?.saveCtaButton?.showWithCondition(newAdType != currentAdType || newGroupId != currentGroupId)
    }

    private fun updateAdGroups(text: String){
        val list = itemList.filter {
            it.title.lowercase().contains(text.trim().lowercase())
        }
        adapterItemList.setData(list)
    }
}
