package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.databinding.BottomsheetRescheduleDayBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleDayBottomSheet : BottomSheetUnify() {

    private var dayOptions: List<RescheduleDayOptionModel> = listOf()
    private var listener: ChooseDayListener? = null

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
        fun onDayChosen(dayChosen: RescheduleDayOptionModel)
    }

    fun setDayOptions(data: List<RescheduleDayOptionModel>) {
        this.dayOptions = data
    }

    fun setListener(listener: ChooseDayListener) {
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
            addAll(dayOptions.map { day -> ListItemUnify(title = day.day, description = "") })
        }

        binding.rvDay.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener?.onDayChosen(dayOptions[index])
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
