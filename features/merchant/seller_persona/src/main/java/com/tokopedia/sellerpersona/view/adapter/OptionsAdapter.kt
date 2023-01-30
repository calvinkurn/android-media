package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.ItemQuestionOptionBinding
import com.tokopedia.sellerpersona.view.model.OptionUiModel

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    companion object {
        private const val OPTION_VALUE_FORMAT = "%s."
    }

    private val options: MutableList<OptionUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionOptionBinding.inflate(inflater, parent, false)
        return OptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = options.size

    fun setItems(options: List<OptionUiModel>) {
        this.options.clear()
        this.options.addAll(options)
    }

    inner class OptionsViewHolder(
        private val binding: ItemQuestionOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                val option = options[absoluteAdapterPosition]
                tvSpOptionValue.text = String.format(OPTION_VALUE_FORMAT, option.value)
                tvSpOptionTitle.text = option.title

                setBackground(option.isSelected)
            }
        }

        private fun setBackground(isSelected: Boolean) {
            val background = if (isSelected) {
                R.drawable.sp_bg_question_active
            } else {
                R.drawable.sp_bg_question_inactive
            }
            binding.containerSpOption.setBackgroundResource(background)
        }
    }
}