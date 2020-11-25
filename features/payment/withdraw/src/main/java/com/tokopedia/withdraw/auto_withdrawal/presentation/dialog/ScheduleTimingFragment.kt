package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
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
    private var currentSelectedSchedule: Schedule? = null
    private var adapter: ScheduleAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            if (containsKey(ARG_SCHEDULE_LIST)) {
                scheduleArrayList = getParcelableArrayList(ARG_SCHEDULE_LIST)
                if (containsKey(ARG_CURRENT_SELECTED_SCHEDULE_LIST)) {
                    currentSelectedSchedule = getParcelable(ARG_CURRENT_SELECTED_SCHEDULE_LIST)
                }
            } else
                this@ScheduleTimingFragment.dismiss()
        }
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_schedule_timings, null, false)
        rootView.apply {
            setChild(this)
            findViewById<View>(R.id.btnUpdateSchedule).setOnClickListener { updateSchedule() }
            recyclerView = findViewById(R.id.rvScheduleTiming)
            initRecyclerAdapter()
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

    private fun initRecyclerAdapter() {
        recyclerView?.apply {
            scheduleArrayList?.let {
                this@ScheduleTimingFragment.adapter = ScheduleAdapter(it, currentSelectedSchedule,
                        this@ScheduleTimingFragment)
                layoutManager = LinearLayoutManager(context)
                adapter = this@ScheduleTimingFragment.adapter
                adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {

        const val TAG_BOTTOM_SHEET = "ScheduleTimingFragment"
        const val ARG_SCHEDULE_LIST = "arg_schedule_list"
        const val ARG_CURRENT_SELECTED_SCHEDULE_LIST = "arg_current_selected_schedule_list"

        fun show(context: Context?, fragmentManager: FragmentManager,
                        scheduleList: ArrayList<Schedule>, currentSelectedSchedule: Schedule?) {
            val bottomSheet = ScheduleTimingFragment().apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(ARG_SCHEDULE_LIST, scheduleList)
                currentSelectedSchedule?.let {
                    bundle.putParcelable(ARG_CURRENT_SELECTED_SCHEDULE_LIST, currentSelectedSchedule)
                }
                arguments = bundle
            }
            context?.let {
                bottomSheet.setTitle(context.getString(R.string.swd_withdrawal_schedule))
            }
            bottomSheet.show(fragmentManager, TAG_BOTTOM_SHEET)
        }
    }

    override fun onScheduleSelected(schedule: Schedule) {
        recyclerView?.post {
            scheduleArrayList?.forEach {
                if (it == schedule)
                    it.status = 1
                else
                    it.status = 0
            }
            adapter?.notifyDataSetChanged()
        }
    }


}