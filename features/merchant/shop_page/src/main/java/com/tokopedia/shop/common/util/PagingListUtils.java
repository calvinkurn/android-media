package com.tokopedia.shop.common.util;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.PagingList;

/**
 * Created by nathan on 2/25/18.
 */

public class PagingListUtils {

    public static boolean checkNextPage(PagingList shopProductList) {
        return shopProductList.getPaging() != null &&
                !TextUtils.isEmpty(shopProductList.getPaging().getUriNext()) &&
                !"0".equals(shopProductList.getPaging().getUriNext());
    }
}
