package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.dashboard.databinding.ListBottomsheetLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.dashboard.recommendation.utils.ScheduleSlotListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.ItemListAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

class ListBottomSheet:
    BottomSheetUnify(), ScheduleSlotListener {

    private var itemList: List<ItemListUiModel> = listOf()

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

        val binding = ListBottomsheetLayoutBinding.inflate(layoutInflater).apply {
            rvScheduleSlot.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                this.adapter = adapterItemList
                adapterItemList.setData(itemList)
            }

            rvScheduleSlot.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        setChild(binding.root)

    }

    companion object {
        fun show(fm: FragmentManager, title:String, list: List<ItemListUiModel>): ListBottomSheet {
            val bottomSheet = ListBottomSheet().apply {
                setTitle(title)
                itemList = list
            }
            bottomSheet.show(fm, "")
            return bottomSheet
        }
    }

    override fun onClickItemListener(title: String) {
        TODO("Not yet implemented")
    }
}
