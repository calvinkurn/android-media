package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.databinding.BottomsheetRescheduleTimeBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleTimeBottomSheet : BottomSheetUnify() {

    private var timeOption: List<RescheduleTimeOptionModel> = listOf()
    private var listener: ChooseTimeListener? = null
    private var binding by autoCleared<BottomsheetRescheduleTimeBinding>()

    init {
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseTimeListener {
        fun onTimeChosen(timeChosen: RescheduleTimeOptionModel)
    }

    fun setTimeOptions(data: List<RescheduleTimeOptionModel>) {
        this.timeOption = data
    }

    fun setListener(listener: ChooseTimeListener) {
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
        binding = BottomsheetRescheduleTimeBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setTitle(getString(R.string.title_reschedule_time_bottomsheet))
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(timeOption.map { time -> ListItemUnify(title = time.formattedTime, description = "") })
        }

        binding.rvTime.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener?.onTimeChosen(timeOption[index])
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
