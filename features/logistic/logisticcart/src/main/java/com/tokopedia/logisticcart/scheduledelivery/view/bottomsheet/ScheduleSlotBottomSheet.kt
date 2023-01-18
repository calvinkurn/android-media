package com.tokopedia.logisticcart.scheduledelivery.view.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.LayoutBottomsheetScheduleSlotBinding
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotAdapter
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseTimeUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ScheduleSlotBottomSheet(private val data: BottomSheetUiModel) :
    BottomSheetUnify(),
    ScheduleSlotListener {

    interface ScheduleSlotBottomSheetListener {
        fun onChooseTimeListener(timeId: Long, dateId: String)
    }

    private var listener: ScheduleSlotBottomSheetListener? = null
    private var bottomSheetInfo: ScheduleInfoBottomSheet? = null

    private val adapterScheduleSlot: ScheduleSlotAdapter by lazy {
        ScheduleSlotAdapter(ScheduleSlotTypeFactory(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        setTitle(getString(R.string.bottomsheet_schedule_slot_title))
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
        adapterScheduleSlot.setTimeSlot(this.data)
    }

    override fun onClickTimeListener(data: ChooseTimeUiModel) {
        this.data.date.content.forEach {
            it.availableTime.forEach { time ->
                time.isSelected = data.dateId == time.dateId && data.timeId == time.timeId
            }
        }
        adapterScheduleSlot.setData(this.data)
        listener?.onChooseTimeListener(data.timeId, data.dateId)
        dismiss()
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
