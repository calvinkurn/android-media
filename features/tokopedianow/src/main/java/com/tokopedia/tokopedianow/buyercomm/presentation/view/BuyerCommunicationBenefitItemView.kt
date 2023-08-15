package com.tokopedia.tokopedianow.buyercomm.presentation.view

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBuyerCommunicationBenefitBinding

class BuyerCommunicationBenefitItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: ItemTokopedianowBuyerCommunicationBenefitBinding? = null

    init {
        binding = ItemTokopedianowBuyerCommunicationBenefitBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(number: String, text: String) {
        binding?.apply {
            val benefitText = SpannableStringBuilder(text)
            val dotIndex = benefitText.indexOf(".")
            val endIndex = benefitText.length - 1
            val boldTextColor = ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
            val normalTextColor = ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )
            val boldSpan = StyleSpan(Typeface.BOLD)

            benefitText.setSpan(boldSpan, 0, dotIndex, 0)
            benefitText.setSpan(ForegroundColorSpan(boldTextColor), 0, dotIndex, 0)
            benefitText.setSpan(ForegroundColorSpan(normalTextColor), dotIndex, endIndex, 0)

            textNumber.text = number
            textDescription.text = benefitText
        }
    }
}
