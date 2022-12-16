package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.databinding.BottomsheetRescheduleReasonBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleReasonBottomSheet : BottomSheetUnify() {

    private var reasonOptionModels: List<RescheduleReasonOptionModel> = listOf()
    private var listener: ChooseReasonListener? = null
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

    fun setOptions(data: List<RescheduleReasonOptionModel>) {
        this.reasonOptionModels = data
    }

    fun setListener(listener: ChooseReasonListener) {
        this.listener = listener
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
            addAll(
                reasonOptionModels.map { reason ->
                    ListItemUnify(
                        title = reason.reason,
                        description = ""
                    )
                }
            )
        }

        binding.rvReason.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener?.onReasonChosen(reasonOptionModels[index])
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

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}
