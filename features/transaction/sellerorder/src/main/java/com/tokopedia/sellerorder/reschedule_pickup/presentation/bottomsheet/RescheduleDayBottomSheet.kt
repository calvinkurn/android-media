package com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRescheduleDayBinding
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleDayBottomSheet(
    private val dayOptions: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption>,
    private val listener: ChooseDayListener
) : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetRescheduleDayBinding>()

    init {
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseDayListener {
        fun onDayChosen(dayChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        binding = BottomsheetRescheduleDayBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setTitle(getString(R.string.title_reschedule_day_bottomsheet))
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(dayOptions.map { day -> ListItemUnify(title = formatDay(day.day), description = "") })
        }

        binding.rvDay.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener.onDayChosen(dayOptions[index])
                    dismiss()
                }
            }
        }
    }

    private fun formatDay(day: String) : String {
        return DateUtil.formatDate("yyyy-MM-dd", "EEEE, dd MMM yyyy", day)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, "")
        }
    }
}