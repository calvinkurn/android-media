package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireRadiobuttonOptionItemBinding
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel

class QuestionnaireRadioButtonOptionAdapter(
    private val items: List<QuestionnaireOptionUiModel>,
    private val onAnswerSelected: () -> Unit
) : RecyclerView.Adapter<QuestionnaireRadioButtonOptionAdapter.QuestionnaireRadioButtonOptionViewHolder>() {

    fun updateRadioButton(newItem: QuestionnaireOptionUiModel, position: Int) {
        items.mapIndexed { index, item ->
            if (item.isChecked) {
                item.isChecked = false
                notifyItemChanged(position)
            } else if (index == position) {
                newItem.isChecked = true
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireRadioButtonOptionViewHolder {
        val binding = ItemPmQuestionnaireRadiobuttonOptionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionnaireRadioButtonOptionViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: QuestionnaireRadioButtonOptionViewHolder, position: Int) {
        if (items.isNotEmpty()) {
            holder.bind(items[position])
        }
    }

    inner class QuestionnaireRadioButtonOptionViewHolder(private val binding: ItemPmQuestionnaireRadiobuttonOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionnaireOptionUiModel) {
            with(binding) {
                rbQuestionnaireOption.run {
                    setOnCheckedChangeListener(null)
                    isChecked = item.isChecked
                    skipAnimation()

                    setOnCheckedChangeListener { _, _ ->
                        updateRadioButton(item, bindingAdapterPosition)
                        onAnswerSelected()
                    }
                    root.setOnClickListener {
                        isChecked = isChecked.not()
                    }
                }
            }
        }
    }
}
