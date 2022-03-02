package com.tokopedia.logisticorder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticorder.databinding.BottomsheetRescheduleOptionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.utils.lifecycle.autoCleared

class RescheduleOptionBottomSheet(
    private val options: List<String>,
    private val listener: ChooseOptionListener
) : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetRescheduleOptionBinding>()

    init {
        // todo ini di customize
        setTitle("Pilih Waktu")
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    interface ChooseOptionListener {
        fun onOptionChosen(option: String)
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
        binding = BottomsheetRescheduleOptionBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val listWidgetData = ArrayList<ListItemUnify>().apply {
            addAll(options.map { day -> ListItemUnify(title = day, description = "") })
        }

        binding.rvDay.run {
            setData(listWidgetData)
            onLoadFinish {
                setOnItemClickListener { adapterView, view, index, l ->
                    listener.onOptionChosen(options[index])
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