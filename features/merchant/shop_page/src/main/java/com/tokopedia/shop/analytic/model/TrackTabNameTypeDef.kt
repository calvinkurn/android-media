package com.tokopedia.shop.analytic.model

import androidx.annotation.StringDef
import com.tokopedia.shop.analytic.model.TrackTabNameTypeDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
annotation class TrackTabNameTypeDef {
    companion object {
        var PRODUCT = "Product"
        var INFO = "Info"
        var FEED = "Feed"
        var SEARCH = "Search"
        var FAVORITE = "favorite"
    }
}