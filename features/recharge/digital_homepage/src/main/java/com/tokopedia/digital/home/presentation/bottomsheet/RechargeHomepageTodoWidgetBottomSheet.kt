package com.tokopedia.digital.home.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.digital.home.databinding.BottomsheetRechargeHomepageTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.BottomSheetTodoWidgetAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RechargeHomepageTodoWidgetBottomSheet : BottomSheetUnify(), BottomSheetTodoWidgetAdapter.BottomSheetAdapterTodoWidgetListener {

    private var binding by autoClearedNullable<BottomsheetRechargeHomepageTodoWidgetBinding>()
    private var optionButtons: List<RechargeHomepageSections.OptionButton>? = null
    private var listener: BottomSheetTodoWidgetListener? = null

    fun setOptionButtons(optionButtons: List<RechargeHomepageSections.OptionButton>) {
        this.optionButtons = optionButtons
    }

    fun setListener(listener: BottomSheetTodoWidgetListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onClickBottomSheetTodoWidget(applink: String) {
        listener?.onClickBottomSheetTodoWidget(applink)
        dismiss()
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true

        binding = BottomsheetRechargeHomepageTodoWidgetBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            optionButtons?.let { optionButtons ->
                val adapter = BottomSheetTodoWidgetAdapter()
                adapter.setListener(this@RechargeHomepageTodoWidgetBottomSheet)
                rvBottomsheetTodoWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                rvBottomsheetTodoWidget.adapter = adapter
                adapter.setData(optionButtons)
            }
        }

        setChild(binding?.root)
    }

    interface BottomSheetTodoWidgetListener {
        fun onClickBottomSheetTodoWidget(applink: String)
    }
}
