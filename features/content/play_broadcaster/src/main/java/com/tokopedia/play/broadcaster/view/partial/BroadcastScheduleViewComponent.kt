package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 30/11/20
 */
class BroadcastScheduleViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        listener: Listener
) : ViewComponent(container, idRes) {

    private val clScheduleHeader = findViewById<ConstraintLayout>(R.id.cl_schedule_header)
    private val clScheduleDesc = findViewById<ConstraintLayout>(R.id.cl_schedule_desc)

    private val tvHeader = findViewById<TextView>(R.id.tv_header)
    private val iconHeaderEdit = findViewById<IconUnify>(R.id.icon_header_edit)

    private val tvDescSchedule = findViewById<TextView>(R.id.tv_desc_schedule)
    private val iconDescEdit = findViewById<IconUnify>(R.id.icon_desc_edit)
    private val iconDescDelete = findViewById<IconUnify>(R.id.icon_desc_delete)

    init {
        iconDescEdit.setOnClickListener {
            listener.onEditBroadcastSchedule(this@BroadcastScheduleViewComponent)
        }

        iconHeaderEdit.setOnClickListener {
            listener.onAddBroadcastSchedule(this@BroadcastScheduleViewComponent)
        }

        iconDescDelete.setOnClickListener {
            listener.onDeleteBroadcastSchedule(this@BroadcastScheduleViewComponent)
        }
    }

    fun setSchedule(scheduleModel: BroadcastScheduleUiModel) {
        when (scheduleModel) {
            BroadcastScheduleUiModel.NoSchedule -> setNoSchedule()
            is BroadcastScheduleUiModel.Scheduled -> setScheduledBroadcast(scheduleModel)
        }
    }

    private fun setNoSchedule() {
        iconHeaderEdit.visibility = View.VISIBLE
        tvHeader.text = getString(R.string.play_broadcast_add_schedule_info)

        clScheduleDesc.visibility = View.GONE
    }

    private fun setScheduledBroadcast(model: BroadcastScheduleUiModel.Scheduled) {
        iconHeaderEdit.visibility = View.GONE
        tvHeader.text = getString(R.string.play_broadcast_schedule_set_info)

        clScheduleDesc.visibility = View.VISIBLE
        tvDescSchedule.text = model.formattedTime
    }

    interface Listener {

        fun onAddBroadcastSchedule(view: BroadcastScheduleViewComponent)
        fun onEditBroadcastSchedule(view: BroadcastScheduleViewComponent)
        fun onDeleteBroadcastSchedule(view: BroadcastScheduleViewComponent)
    }
}