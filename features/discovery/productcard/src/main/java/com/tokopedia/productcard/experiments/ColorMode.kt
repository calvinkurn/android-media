package com.tokopedia.productcard.experiments

import com.tokopedia.productcard.R

enum class ColorMode : ProductCardColor {
    LIGHT {
        override val cardBackgroundColor: Int
            get() = android.R.color.transparent
        override val productNameColor: Int
            get() = R.color.dms_static_light_NN950_96
        override val productPriceColor: Int
            get() = R.color.dms_static_light_NN950_96
        override val productSlashPriceColor: Int
            get() = R.color.dms_static_light_NN950_44
        override val productSoldCountColor: Int
            get() = R.color.dms_static_light_NN950_68
        override val productDiscountColor: Int
            get() = R.color.dms_static_light_RN500
        override val productRatingColor: Int
            get() = R.color.dms_static_light_NN950_68
    },
    
    DARK {
        override val cardBackgroundColor: Int
            get() = android.R.color.transparent
        override val productNameColor: Int
            get() = R.color.dms_static_dark_NN950_96
        override val productPriceColor: Int
            get() = R.color.dms_static_dark_NN950_96
        override val productSlashPriceColor: Int
            get() = R.color.dms_static_dark_NN950_44
        override val productSoldCountColor: Int
            get() = R.color.dms_static_dark_NN950_68
        override val productDiscountColor: Int
            get() = R.color.dms_static_dark_RN500
        override val productRatingColor: Int
            get() = R.color.dms_static_dark_NN950_68
    }
}
