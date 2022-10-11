package com.tokopedia.logisticcart.schedule_slot.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.logisticcart.databinding.LayoutBottomsheetScheduleSlotBinding
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotAdapter
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class ScheduleSlotBottomSheet(private val data: BottomSheetUiModel)
    : BottomSheetUnify(), ScheduleSlotListener {

    interface ScheduleSlotBottomSheetListener {
        fun onChooseTimeListener(timeId: Long, dateId: String)
    }
    private var listener: ScheduleSlotBottomSheetListener? = null
    private var bottomSheetInfo: ScheduleInfoBottomSheet? = null

    private val adapterScheduleSlot: ScheduleSlotAdapter by lazy {
        ScheduleSlotAdapter(ScheduleSlotTypeFactory(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setListener(listener: ScheduleSlotBottomSheetListener) {
        this.listener = listener
    }

    private fun initView() {
        clearContentPadding = true
        val binding = LayoutBottomsheetScheduleSlotBinding.inflate(layoutInflater).apply {
            rvScheduleSlot.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                this.adapter = adapterScheduleSlot
                adapterScheduleSlot.setData(data)
            }
        }
        setChild(binding.root)
    }

    override fun onClickInfoListener() {
        if (bottomSheetInfo == null) {
            bottomSheetInfo = ScheduleInfoBottomSheet.show(parentFragmentManager, data.infoUiModel)
        } else {
            bottomSheetInfo?.show(parentFragmentManager, "")
        }
    }

    override fun onClickDateListener(data: ButtonDateUiModel) {
        this.data.date.content.forEach {
            it.isSelected = it.id == data.id
        }
        adapterScheduleSlot.setData(this.data)
    }

    override fun onClickTimeListener(data: ChooseTimeUiModel) {
        Handler().postDelayed({
            dismiss()
            listener?.onChooseTimeListener(data.timeId, data.dateId)
        }, 1000)
    }

    override fun dismiss() {
        super.dismiss()
        listener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener = null
    }

    companion object {

        fun show(fm: FragmentManager, data: BottomSheetUiModel): ScheduleSlotBottomSheet {
            val bottomsheet = ScheduleSlotBottomSheet(data)
            bottomsheet.show(fm, "")
            return bottomsheet
        }
    }
}
