package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireChipOptionItemBinding
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class QuestionnaireChipOptionAdapter(
    private val items: List<QuestionnaireOptionUiModel>,
    private val onAnswerSelected: () -> Unit
): RecyclerView.Adapter<QuestionnaireChipOptionAdapter.QuestionnaireChipOptionViewHolder>() {

    fun updateChips(isSelected: Boolean, position: Int) {
        items.mapIndexed { index, item ->
            if (index == position) {
                item.isChecked = isSelected
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireChipOptionViewHolder {
        val binding = ItemPmQuestionnaireChipOptionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionnaireChipOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionnaireChipOptionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onAnswerSelected)
    }

    override fun getItemCount(): Int = items.size

    inner class QuestionnaireChipOptionViewHolder(private val binding: ItemPmQuestionnaireChipOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionnaireOptionUiModel, onAnswerSelected: () -> Unit) {
            with(binding) {
                chipsQuestionnaireOption.run {
                    centerText = true
                    chip_image_icon.loadImage(item.imageURL) {
                        listener(onSuccess = { _, _ ->
                            chip_image_icon.show()
                        })
                    }
                    chipText = item.text
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    toggle(item.isChecked)

                    setOnClickListener {
                        updateChips(item.isChecked.not(), bindingAdapterPosition)
                        onAnswerSelected()
                    }
                }
            }
        }

        private fun ChipsUnify.toggle(isSelected: Boolean) {
            chipType = if (isSelected) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
        }
    }
}
