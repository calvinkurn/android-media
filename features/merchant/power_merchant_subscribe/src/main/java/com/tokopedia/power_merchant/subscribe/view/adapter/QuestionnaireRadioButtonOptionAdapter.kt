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

    fun updateRadioButton(position: Int) {
        items.mapIndexed { index, item ->
            if (item.isChecked) {
                item.isChecked = false
                notifyItemChanged(index, PAYLOAD_TOGGLE_RB)
            } else if (index == position) {
                item.isChecked = true
                notifyItemChanged(index, PAYLOAD_TOGGLE_RB)
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

    override fun onBindViewHolder(
        holder: QuestionnaireRadioButtonOptionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val keyPayload = payloads.firstOrNull() as? Int
            if (keyPayload == PAYLOAD_TOGGLE_RB) {
                holder.bindPayload(items[position])
            }
        }
    }

    inner class QuestionnaireRadioButtonOptionViewHolder(private val binding: ItemPmQuestionnaireRadiobuttonOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionnaireOptionUiModel) {
            with(binding) {
                rbQuestionnaireOption.run {
                    setOnCheckedChangeListener(null)
                    text = item.text
                    isChecked = item.isChecked
                    skipAnimation()

                    setOnClickListener {
                        updateRadioButton(bindingAdapterPosition)
                        onAnswerSelected()
                    }
                }
            }
        }

        fun bindPayload(item: QuestionnaireOptionUiModel) {
            with(binding) {
                rbQuestionnaireOption.isChecked = item.isChecked
            }
        }
    }

    companion object {
        const val PAYLOAD_TOGGLE_RB = 565
    }
}
