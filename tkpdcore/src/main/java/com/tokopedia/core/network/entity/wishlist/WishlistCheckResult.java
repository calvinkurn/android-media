package com.tokopedia.core.network.entity.wishlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryPri on 20/04/17.
 */

public class WishlistCheckResult {
    @SerializedName("data")
    CheckResultIds checkResultIds;

    public CheckResultIds getCheckResultIds() {
        return checkResultIds;
    }

    public void setCheckResultIds(CheckResultIds checkResultIds) {
        this.checkResultIds = checkResultIds;
    }

    public static class CheckResultIds {
        @SerializedName("ids")
        List<String> ids = new ArrayList<>();

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }
    }
}
