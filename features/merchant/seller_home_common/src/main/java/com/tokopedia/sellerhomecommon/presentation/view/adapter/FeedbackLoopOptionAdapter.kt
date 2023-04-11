package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.databinding.ShcItemFeedbackLoopOptionBinding
import com.tokopedia.sellerhomecommon.presentation.model.FeedbackLoopOptionUiModel

/**
 * Created by @ilhamsuaib on 22/08/22.
 */

class FeedbackLoopOptionAdapter(
    private val items: List<FeedbackLoopOptionUiModel>,
    private val onCheckedListener: (FeedbackLoopOptionUiModel) -> Unit
) : RecyclerView.Adapter<FeedbackLoopOptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShcItemFeedbackLoopOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onCheckedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ShcItemFeedbackLoopOptionBinding,
        private val onCheckedListener: (FeedbackLoopOptionUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedbackLoopOptionUiModel) = binding.run {
            cbShcFeedbackOption.isChecked = item.isSelected
            tvShcFeedbackOption.text = item.title
            edtShcFeedbackOption.gone()

            root.setOnClickListener {
                cbShcFeedbackOption.isChecked = !cbShcFeedbackOption.isChecked
            }
            cbShcFeedbackOption.setOnCheckedChangeListener { _, _ ->
                item.isSelected = !item.isSelected
                if (item is FeedbackLoopOptionUiModel.Other) {
                    setOnOtherClicked(item)
                }
                onCheckedListener(item)
            }
        }

        private fun setOnOtherClicked(item: FeedbackLoopOptionUiModel.Other) {
            binding.run {
                if (item.isSelected) {
                    edtShcFeedbackOption.visible()
                    edtShcFeedbackOption.textFieldInput.afterTextChanged { text ->
                        item.value = text
                        onCheckedListener(item)
                    }
                } else {
                    edtShcFeedbackOption.gone()
                    item.value = String.EMPTY
                }
            }
        }
    }
}
