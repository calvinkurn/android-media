package com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRescheduleTimeBinding
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleTimeBottomSheet(
    private val timeOption: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption>,
    private val listener: ChooseTimeListener
) : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetRescheduleTimeBinding>()

    init {
        setTitle(getString(R.string.title_reschedule_time_bottomsheet))
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseTimeListener {
        fun onTimeChosen(timeChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption)
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
        binding = BottomsheetRescheduleTimeBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(timeOption.map { time -> ListItemUnify(title = "", description = time.time) })
        }

        binding.rvTime.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener.onTimeChosen(timeOption[index])
                    dismiss()
                }
            }
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, "")
        }
    }
}
