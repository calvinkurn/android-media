package com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRescheduleDayBinding
import com.tokopedia.sellerorder.databinding.BottomsheetRescheduleReasonBinding
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleReasonBottomSheet(
    private val reasonOptions: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption>,
    private val listener: ChooseReasonListener
) : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetRescheduleReasonBinding>()

    init {
        setTitle(getString(R.string.title_reschedule_reason_bottomsheet))
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseReasonListener {
        fun onReasonChosen(reasonChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption)
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
        binding = BottomsheetRescheduleReasonBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(reasonOptions.map { reason -> ListItemUnify(title = reason.reason, description = "") })
        }

        binding.rvReason.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener.onReasonChosen(reasonOptions[index])
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