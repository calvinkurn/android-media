package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.domain.model.Schedule
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.ScheduleAdapter
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.ScheduleChangeListener

class ScheduleTimingFragment : BottomSheetUnify(), ScheduleChangeListener {

    private var scheduleArrayList: ArrayList<Schedule>? = null
    private var scheduleChangeListener: ScheduleChangeListener? = null
    private var adapter: ScheduleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_SCHEDULE_LIST)) {
                scheduleArrayList = it.getParcelableArrayList(ARG_SCHEDULE_LIST)
            } else
                dismiss()
        }
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_schedule_timings, null, false)
        rootView.apply {
            setChild(this)
            findViewById<View>(R.id.btnUpdateSchedule).setOnClickListener { updateSchedule() }
            val recyclerView = findViewById<RecyclerView>(R.id.rvScheduleTiming)
            initRecyclerAdapter(recyclerView)
        }
    }

    private fun updateSchedule() {
        var updateSchedule: Schedule? = null
        scheduleArrayList?.forEach {
            if (it.status == 1)
                updateSchedule = it
        }
        updateSchedule?.let {
            scheduleChangeListener?.onScheduleSelected(it)
        }
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ScheduleChangeListener) {
            scheduleChangeListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        scheduleChangeListener = null
    }

    private fun initRecyclerAdapter(recyclerView: RecyclerView?) {
        recyclerView?.apply {
            scheduleArrayList?.let {
                this@ScheduleTimingFragment.adapter = ScheduleAdapter(it,
                        this@ScheduleTimingFragment)
                layoutManager = LinearLayoutManager(context)
                adapter = this@ScheduleTimingFragment.adapter
                adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val ARG_SCHEDULE_LIST = "arg_schedule_list"
        fun getInstance(scheduleList: ArrayList<Schedule>)
                : ScheduleTimingFragment {
            return ScheduleTimingFragment().apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_SCHEDULE_LIST, scheduleList)
                arguments = bundle
            }
        }
    }

    override fun onScheduleSelected(schedule: Schedule) {
        scheduleArrayList?.forEach {
            if (it == schedule)
                schedule.status = 1
            else
                schedule.status = 0
            adapter?.notifyDataSetChanged()
        }
    }


}