package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
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
            val addOnName = this.getString(R.string.text_add_on_name, addOnUiModel.name)
            val addOnValues = addOnUiModel.selectedAddOns.joinToString()
            val fullText = addOnName + addOnValues
            val startIndex = addOnName.length
            val endIndex = startIndex + addOnValues.length
            val spannableString = SpannableString(fullText)
            val addOnNameColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            spannableString.setSpan(ForegroundColorSpan(addOnNameColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tpgAddOn.text = spannableString
        }
    }
}