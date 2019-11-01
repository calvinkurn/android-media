package com.tokopedia.shop.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.common.constant.ShopStatusLevelDef.LEVEL_GOLD;
import static com.tokopedia.shop.common.constant.ShopStatusLevelDef.LEVEL_OFFICIAL_STORE;
import static com.tokopedia.shop.common.constant.ShopStatusLevelDef.LEVEL_REGULAR;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({LEVEL_REGULAR, LEVEL_GOLD, LEVEL_OFFICIAL_STORE})
public @interface ShopStatusLevelDef {
    int LEVEL_REGULAR = 0;
    int LEVEL_GOLD = 1;
    int LEVEL_OFFICIAL_STORE = 2;
}
