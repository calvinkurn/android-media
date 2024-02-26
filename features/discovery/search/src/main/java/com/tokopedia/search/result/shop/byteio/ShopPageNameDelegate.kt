package com.tokopedia.search.result.shop.byteio

import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.search.AppLogSearch

class ShopPageNameDelegate: AppLogInterface {

    override fun getPageName(): String = AppLogSearch.ParamValue.STORE_SEARCH

    override fun isEnterFromWhitelisted(): Boolean = true
}
