package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel

class AddOnInfoViewHolder(private val binding: TokofoodItemAddOnInfoLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null

    init {
        context = binding.root.context
    }

    fun bindData(addOnUiModel: AddOnUiModel) {
        context?.run {
            binding.tpgAddOnName.text = this.getString(R.string.text_add_on_name, addOnUiModel.name)
            binding.tpgAddOnValue.text = addOnUiModel.selectedAddOns.joinToString()
        }
    }
}