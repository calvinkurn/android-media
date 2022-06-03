package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemOrderNoteLayoutBinding

class OrderNoteInputViewHolder(private val binding: TokofoodItemOrderNoteLayoutBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null

    init {
        context = binding.root.context
    }

    fun bindData(orderNote: String) {
        binding.catatanInput.setLabel(orderNote)
    }
}