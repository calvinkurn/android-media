package com.tokopedia.logisticcart.schedule_slot.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.databinding.BottomsheetScheduleShippingInfoBinding
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotAdapter
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

class ScheduleInfoBottomSheet(private val data: BottomSheetUiModel)
    : BottomSheetUnify() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        clearContentPadding = true
        val view = BottomsheetScheduleShippingInfoBinding.inflate(layoutInflater).apply {
            // todo
            tvScheduleShippingInfo.text = ""
            // todo
            imgScheduleShippingInfo.loadImage("")
        }
        // todo title
        setTitle("")
        setChild(view.root)
    }

    companion object {

        fun show(fm: FragmentManager, data: BottomSheetUiModel): ScheduleInfoBottomSheet {
            val bottomsheet = ScheduleInfoBottomSheet(data)
            bottomsheet.show(fm, "")
            return bottomsheet
        }
    }
}
