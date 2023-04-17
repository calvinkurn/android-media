package com.tokopedia.product.detail.tracking

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.user.session.UserSessionInterface

data class CommonTracker(
    private val productInfo: DynamicProductInfoP1,
    @Deprecated("Not need user id, please passing userSession")
    val userId: String,
    private val userSession: UserSessionInterface? = null
) {
    private val productBasic by lazy { productInfo.basic }
    private val basicCategory by lazy { productBasic.category }

    val shopId by lazy { productBasic.shopID }
    val layoutName by lazy { productInfo.layoutName }
    val categoryName by lazy { basicCategory.name }
    val categoryId by lazy { basicCategory.id }
    val productId by lazy { productBasic.productID }
    val shopType by lazy { productInfo.shopTypeString }
    val categoryChildId by lazy { basicCategory.detail.lastOrNull()?.id ?: "" }
    val isLoggedIn get() = userSession?.isLoggedIn.orFalse()
    val userID get() = userSession?.userId.orEmpty()
}
