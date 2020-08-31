package com.tokopedia.shop.common.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.common.constant.ShopHomeType.WEBVIEW;
import static com.tokopedia.shop.common.constant.ShopHomeType.NATIVE;
import static com.tokopedia.shop.common.constant.ShopHomeType.NONE;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({NATIVE, WEBVIEW, NONE})
public @interface ShopHomeType {
    String NATIVE= "native";
    String WEBVIEW = "webview";
    String NONE = "none";
}
