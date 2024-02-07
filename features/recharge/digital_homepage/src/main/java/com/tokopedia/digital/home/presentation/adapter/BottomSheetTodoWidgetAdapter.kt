package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.ViewBottomsheetRechargeHomepageTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

class BottomSheetTodoWidgetAdapter :
    RecyclerView.Adapter<BottomSheetTodoWidgetAdapter.BottomSheetTodoWidgetViewHolder>() {

    private var optionButtons: List<RechargeHomepageSections.OptionButton> = emptyList()
    private var listener: BottomSheetAdapterTodoWidgetListener? = null

    override fun getItemCount(): Int = optionButtons.size

    override fun onBindViewHolder(holder: BottomSheetTodoWidgetViewHolder, position: Int) {
        holder.bind(optionButtons[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetTodoWidgetViewHolder {
        val view = ViewBottomsheetRechargeHomepageTodoWidgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BottomSheetTodoWidgetViewHolder(view)
    }

    fun setData(listOptionButton: List<RechargeHomepageSections.OptionButton>) {
        this.optionButtons = listOptionButton
        notifyItemRangeChanged(0, optionButtons.size)
    }

    fun setListener(listener: BottomSheetAdapterTodoWidgetListener) {
        this.listener = listener
    }

    inner class BottomSheetTodoWidgetViewHolder(
        private val binding: ViewBottomsheetRechargeHomepageTodoWidgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(optionButton: RechargeHomepageSections.OptionButton) {
            binding.tgTodoWidgetAction.text = optionButton.button
            binding.root.setOnClickListener {
                listener?.onClickBottomSheetTodoWidget(optionButton)
            }
            binding.root.addOnImpressionListener(optionButton) {
                listener?.onImpressBottomSheetTodoWidget(optionButton)
            }
        }
    }

    interface BottomSheetAdapterTodoWidgetListener {
        fun onClickBottomSheetTodoWidget(optionButton: RechargeHomepageSections.OptionButton)
        fun onImpressBottomSheetTodoWidget(optionButton: RechargeHomepageSections.OptionButton)
    }
}
