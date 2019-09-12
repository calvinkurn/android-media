package com.tokopedia.shop.analytic.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.analytic.model.TrackTabNameTypeDef.FAVORITE;
import static com.tokopedia.shop.analytic.model.TrackTabNameTypeDef.FEED;
import static com.tokopedia.shop.analytic.model.TrackTabNameTypeDef.INFO;
import static com.tokopedia.shop.analytic.model.TrackTabNameTypeDef.PRODUCT;
import static com.tokopedia.shop.analytic.model.TrackTabNameTypeDef.SEARCH;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({PRODUCT, INFO, FEED, SEARCH, FAVORITE})
public @interface TrackTabNameTypeDef {
    String PRODUCT = "Product";
    String INFO = "Info";
    String FEED = "Feed";
    String SEARCH = "Search";
    String FAVORITE = "favorite";
}
