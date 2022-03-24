package com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRescheduleReasonBinding
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleReasonOptionModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleReasonBottomSheet(
    private val reasonOptionModels: List<RescheduleReasonOptionModel>,
    private val listener: ChooseReasonListener
) : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetRescheduleReasonBinding>()

    init {
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseReasonListener {
        fun onReasonChosen(reasonChosen: RescheduleReasonOptionModel)
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
        setTitle(getString(R.string.title_reschedule_reason_bottomsheet))
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(reasonOptionModels.map { reason -> ListItemUnify(title = reason.reason, description = "") })
        }

        binding.rvReason.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener.onReasonChosen(reasonOptionModels[index])
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