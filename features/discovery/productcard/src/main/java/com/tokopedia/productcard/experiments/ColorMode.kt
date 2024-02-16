package com.tokopedia.productcard.experiments

import com.tokopedia.productcard.R
import com.tokopedia.unifyprinciples.ColorMode

enum class ColorMode : ProductCardColor {
    LIGHT {
        override val cardBackgroundColor: Int
            get() = android.R.color.transparent
        override val productNameTextColor: Int
            get() = R.color.dms_static_light_NN950_96
        override val priceTextColor: Int
            get() = R.color.dms_static_light_NN950_96
        override val slashPriceTextColor: Int
            get() = R.color.dms_static_light_NN400
        override val soldCountTextColor: Int
            get() = R.color.dms_static_light_NN950_68
        override val discountTextColor: Int
            get() = R.color.dms_static_light_RN500
        override val ratingTextColor: Int
            get() = R.color.dms_static_light_NN950_68
        override val buttonColorMode: ColorMode
            get() = ColorMode.LIGHT_MODE
        override val labelBenefitViewColor: ProductCardColor.LabelBenefitViewColor
            get() = ProductCardColor.LabelBenefitViewColor(cutoutFillColor = "")
    },

    DARK {
        override val cardBackgroundColor: Int
            get() = android.R.color.transparent
        override val productNameTextColor: Int
            get() = R.color.dms_static_dark_NN950_96
        override val priceTextColor: Int
            get() = R.color.dms_static_dark_NN950_96
        override val slashPriceTextColor: Int
            get() = R.color.dms_static_dark_NN400
        override val soldCountTextColor: Int
            get() = R.color.dms_static_dark_NN950_68
        override val discountTextColor: Int
            get() = R.color.dms_static_dark_RN500
        override val ratingTextColor: Int
            get() = R.color.dms_static_dark_NN950_68
        override val buttonColorMode: ColorMode
            get() = ColorMode.DARK_MODE
        override val labelBenefitViewColor: ProductCardColor.LabelBenefitViewColor
            get() = ProductCardColor.LabelBenefitViewColor(cutoutFillColor = "")
    }
}
