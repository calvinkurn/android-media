package com.tokopedia.topads.dashboard.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.data.Meta;
import com.tokopedia.topads.dashboard.data.model.data.Page;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class PageDataResponse<T> extends DataResponse<T> {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("page")
    @Expose
    private Page page;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
