package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.addedit.databinding.ItemVariantTypeSelectedBinding
import com.tokopedia.utils.view.binding.viewBinding

class VariantTypeSelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding by viewBinding<ItemVariantTypeSelectedBinding>()

    fun bindData(text: String, isCustomVariant: Boolean) {
        val enabledColor = MethodChecker.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val disabledColor = MethodChecker.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN300)

        binding?.variantTypeName?.text = text
        if (isCustomVariant) {
            binding?.iconEdit?.setImage(
                newLightEnable = enabledColor,
                newDarkEnable = enabledColor
            )
        } else {
            binding?.iconEdit?.setImage(
                newLightEnable = disabledColor,
                newDarkEnable = disabledColor
            )
        }
    }
}