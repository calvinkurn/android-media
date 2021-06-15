package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.ChipsUnify

class TypoCorrectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var chipsTypo: ChipsUnify = itemView.findViewById(R.id.chips_typo)

    fun bindData(productName: Pair<String, String>) {
        itemView.context.apply {
            val colorBlack = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N400)
            val alpha = MethodChecker.getColor(this, R.color.color_alpha)

            chipsTypo.chip_text.text = getString(
                    R.string.label_product_title_validation_typo_correction,
                    productName.first,
                    productName.second,
                    Integer.toHexString(colorBlack and alpha)
            ).parseAsHtml()
        }
    }
}