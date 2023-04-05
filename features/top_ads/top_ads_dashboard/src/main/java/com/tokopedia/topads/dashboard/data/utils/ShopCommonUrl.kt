package com.tokopedia.topads.dashboard.data.utils

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

open class ShopCommonUrl {
    companion object {
        @JvmField
        var BASE_URL = getInstance().TOME
        @JvmField
        var BASE_WS_URL = getInstance().WS
    }
}
