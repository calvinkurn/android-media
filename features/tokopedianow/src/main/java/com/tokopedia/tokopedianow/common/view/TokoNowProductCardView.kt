package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCardViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class TokoNowProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    private var binding: LayoutTokopedianowProductCardViewBinding

    init {
        binding = LayoutTokopedianowProductCardViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            imageView.loadImage("https://slack-imgs.com/?c=1&o1=ro&url=https%3A%2F%2Fimages.tokopedia.net%2Fimg%2Fandroid%2Fnow%2FPN-RICH.jpg")
            mainPriceTypography.text = "Rp.1.500.000"
            discountLabel.setLabel("100%")
            slashedPriceTypography.text = "9999999"
            productNameTypography.text = "hello"
            categoryInfoTypography.text = "100gr"
            ratingTypography.text = "4.5"
        }
    }

}
