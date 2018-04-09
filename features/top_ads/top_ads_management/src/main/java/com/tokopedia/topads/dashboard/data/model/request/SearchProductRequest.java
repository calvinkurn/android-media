package com.tokopedia.topads.dashboard.data.model.request;

import android.text.TextUtils;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

import java.util.HashMap;

/**
 * Created by Nathaniel on 12/21/2016.
 */

public class SearchProductRequest {

    private String shopId;
    private String keyword;
    private int etalaseId;
    private int sortBy;
    private int start;
    private int rows;

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setEtalaseId(int etalaseId) {
        this.etalaseId = etalaseId;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        if (!TextUtils.isEmpty(keyword)) {
            params.put(TopAdsNetworkConstant.PARAM_KEYWORD, keyword);
        }
        if (etalaseId > 0) {
            params.put(TopAdsNetworkConstant.PARAM_ETALASE, String.valueOf(etalaseId));
        }
        if (sortBy > 0) {
            params.put(TopAdsNetworkConstant.PARAM_SORT_BY, String.valueOf(sortBy));
        }
        params.put(TopAdsNetworkConstant.PARAM_START, String.valueOf(start));
        if (rows > 0) {
            params.put(TopAdsNetworkConstant.PARAM_ROWS, String.valueOf(rows));
        }
        return params;
    }
}
