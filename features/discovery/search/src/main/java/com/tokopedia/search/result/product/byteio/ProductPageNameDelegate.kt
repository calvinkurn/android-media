package com.tokopedia.search.result.product.byteio

import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.search.AppLogSearch

class ProductPageNameDelegate: AppLogInterface {

    override fun getPageName(): String = AppLogSearch.ParamValue.GOODS_SEARCH

    override fun isEnterFromWhitelisted(): Boolean = true
}
