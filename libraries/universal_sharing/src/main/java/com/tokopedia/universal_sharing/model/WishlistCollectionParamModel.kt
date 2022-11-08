package com.tokopedia.universal_sharing.model

data class WishlistCollectionParamModel(
    val collectionName: String = "",
    val collectionOwner: String = "",
    val productCount: Long = 0L,
    val productImage1: String = "",
    val productPrice1: Long = 0L,
    val productImage2: String = "",
    val productPrice2: Long = 0L,
    val productImage3: String = "",
    val productPrice3: Long = 0L,
    val productImage4: String = "",
    val productPrice4: Long = 0L,
    val productImage5: String = "",
    val productPrice5: Long = 0L,
    val productImage6: String = "",
    val productPrice6: Long = 0L
) : ImageGeneratorParamModel()

