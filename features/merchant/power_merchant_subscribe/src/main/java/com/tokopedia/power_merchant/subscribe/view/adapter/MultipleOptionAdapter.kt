package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PMCancellationQuestionnaireMultipleOptionModel

class MultipleOptionAdapter(
        private val multipleOptionAdapterListener: MultipleOptionAdapterListener
) : RecyclerView.Adapter<MultipleOptionAdapter.ViewHolder>() {

    var listOption = listOf<PMCancellationQuestionnaireMultipleOptionModel.OptionModel>()

    interface MultipleOptionAdapterListener {
        fun onOptionChecked(isChecked: Boolean, optionValue: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.pm_cancellation_multiple_option_question_checkbox_layout,
                parent
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = listOption.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkboxOption.isChecked = listOption[position].isChecked
        holder.checkboxOption.setOnClickListener {
            multipleOptionAdapterListener.onOptionChecked(
                    (it as AppCompatCheckBox).isChecked,
                    listOption[position].value
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxOption: AppCompatCheckBox = itemView.findViewById(R.id.check_box_option)
    }
}