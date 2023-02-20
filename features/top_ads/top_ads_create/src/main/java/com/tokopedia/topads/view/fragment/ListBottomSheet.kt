package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.create.databinding.LayoutBottomsheetListBinding
import com.tokopedia.topads.view.activity.SeePerformanceTopadsActivity
import com.tokopedia.topads.view.adapter.ItemListAdapter
import com.tokopedia.topads.view.adapter.ItemListTypeFactory
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.topads.view.utils.ScheduleSlotListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class ListBottomSheet(private val title: String, private val itemList: List<ItemListUiModel>) :
    BottomSheetUnify(), ScheduleSlotListener {


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

        val binding = LayoutBottomsheetListBinding.inflate(layoutInflater).apply {
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
        setTitle(title)
        setChild(binding.root)
    }

    companion object {

        fun show(fm: FragmentManager, title:String, list: List<ItemListUiModel>): ListBottomSheet {
            val bottomSheet = ListBottomSheet(title, list)
            bottomSheet.show(fm, "")
            return bottomSheet
        }
    }

    override fun onClickItemListener() {
        this.dismiss()
        (activity as SeePerformanceTopadsActivity).openCalendar()
    }
}
