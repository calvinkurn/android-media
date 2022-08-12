package com.tokopedia.kyc_centralized

object KycConstant {

    const val EXTRA_USE_CROPPING = "useCropping"
    const val EXTRA_USE_COMPRESSION = "useCompression"

    const val PADDING_16 = 16
    const val PADDING_ZERO = 0
    const val PADDING_0_5F = 0.5f

    enum class KycCompressionQuality(val type: Float){
        Q_100(100F),
        Q_70(70F),
        Q_50(50F),
        Q_40(40F),
        Q_30(30F),
        Q_20(20F)
    }
}