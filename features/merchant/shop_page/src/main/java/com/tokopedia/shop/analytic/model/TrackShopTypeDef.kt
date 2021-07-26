package com.tokopedia.shop.analytic.model

import androidx.annotation.StringDef
import com.tokopedia.shop.analytic.model.TrackShopTypeDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
annotation class TrackShopTypeDef {
    companion object {
        var OFFICIAL_STORE = "official_store"
        var GOLD_MERCHANT = "gold_merchant"
        var REGULAR_MERCHANT = "regular"
    }
}