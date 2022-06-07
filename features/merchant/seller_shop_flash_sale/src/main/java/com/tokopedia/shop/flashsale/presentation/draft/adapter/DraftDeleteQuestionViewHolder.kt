package com.tokopedia.shop.flashsale.presentation.draft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemDeleteQuestionBinding
import com.tokopedia.utils.view.binding.viewBinding

class DraftDeleteQuestionViewHolder(
    itemView: View,
    private val selectionChangedListener: ((String, Int) -> Unit)? = null
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.ssfs_item_delete_question, parent, false)
    }

    private val binding: SsfsItemDeleteQuestionBinding? by viewBinding()

    fun bind(item: String, isChecked: Boolean) {
        binding?.apply {
            radioQuestion.isChecked = isChecked
            radioQuestion.text = item
            radioQuestion.setOnClickListener {
                selectionChangedListener?.invoke(item, adapterPosition)
            }
        }
    }
}