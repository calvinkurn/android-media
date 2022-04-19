package com.tokopedia.shop.analytic.model

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
annotation class ShopTrackProductTypeDef {
    companion object {
        var PRODUCT = 1
        var FEATURED = 2
        var ETALASE_HIGHLIGHT = 3
    }
}