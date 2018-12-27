package com.tokopedia.product.share.ekstensions

import com.tokopedia.product.share.ProductData

fun ProductData.getShareContent(shortUrl: String) = "${if (productName.isNotEmpty()) "\n" else ""}$shortUrl"