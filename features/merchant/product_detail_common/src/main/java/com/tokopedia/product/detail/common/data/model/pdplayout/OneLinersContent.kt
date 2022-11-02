package com.tokopedia.product.detail.common.data.model.pdplayout

import com.tokopedia.kotlin.extensions.view.EMPTY

data class OneLinersContent(
    val productID: String = String.EMPTY,
    val content: String = String.EMPTY,
    val linkText: String = String.EMPTY,
    var color: String = String.EMPTY,
    var applink: String = String.EMPTY,
    val separator: String = String.EMPTY,
    var icon: String = String.EMPTY,
    val isVisible: Boolean = true,
    val eduLink: EduLinkData = EduLinkData()
)
