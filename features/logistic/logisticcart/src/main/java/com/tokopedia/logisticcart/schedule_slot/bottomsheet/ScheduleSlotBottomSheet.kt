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
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotAdapter
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class ScheduleSlotBottomSheet(private val data: BottomSheetUiModel)
    : BottomSheetUnify(), ScheduleSlotListener {

    private var listener: (() -> BaseScheduleSlotUiModel<out Any>)? = null

    private val adapterScheduleSlot: ScheduleSlotAdapter by lazy {
        ScheduleSlotAdapter(ScheduleSlotTypeFactory(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setListener(onClick: () -> BaseScheduleSlotUiModel<out Any>) {
        listener = onClick
    }

    private fun initView() {
        clearContentPadding = true
        val view = View.inflate(context, com.tokopedia.logisticcart.R.layout.layout_bottomsheet_schedule_slot, null).apply {
            findViewById<RecyclerView>(com.tokopedia.logisticcart.R.id.rv_schedule_slot).apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                this.adapter = adapterScheduleSlot
                adapterScheduleSlot.setData(data.date)
            }
        }
        setChild(view)
    }

    override fun onClickDateListener(data: ButtonDateUiModel) {
    }

    override fun onClickTimeListener(data: BaseScheduleSlotUiModel<out Any>) {
        Handler().postDelayed({
            dismiss()
            listener?.invoke()
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
