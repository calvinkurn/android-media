package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireOptionItemBinding
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireOptionAdapter(
    private val items: List<QuestionnaireOptionUiModel>,
    private val onAnswerSelected: () -> Unit
) : RecyclerView.Adapter<QuestionnaireOptionAdapter.QuestionnaireOptionViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireOptionViewHolder {
        val binding = ItemPmQuestionnaireOptionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionnaireOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionnaireOptionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onAnswerSelected)
    }

    override fun getItemCount(): Int = items.size

    inner class QuestionnaireOptionViewHolder(private val binding: ItemPmQuestionnaireOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionnaireOptionUiModel, onAnswerSelected: () -> Unit) {
            with(binding) {
                tvPmQuestionnaireItem.text = item.text
                cbPmQuestionnaireOption.isChecked = item.isChecked

                cbPmQuestionnaireOption.setOnCheckedChangeListener { _, isChecked ->
                    item.isChecked = isChecked
                    onAnswerSelected()
                }

                root.setOnClickListener {
                    cbPmQuestionnaireOption.isChecked = !cbPmQuestionnaireOption.isChecked
                    onAnswerSelected()
                }
            }
        }
    }
}