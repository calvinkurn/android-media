package com.tokopedia.shop.analytic.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.analytic.model.TrackShopTypeDef.GOLD_MERCHANT;
import static com.tokopedia.shop.analytic.model.TrackShopTypeDef.OFFICIAL_STORE;
import static com.tokopedia.shop.analytic.model.TrackShopTypeDef.REGULAR_MERCHANT;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({OFFICIAL_STORE, GOLD_MERCHANT, REGULAR_MERCHANT})
public @interface TrackShopTypeDef {
    String OFFICIAL_STORE = "official_store";
    String GOLD_MERCHANT = "gold_merchant";
    String REGULAR_MERCHANT = "regular";
}
