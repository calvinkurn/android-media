package com.tokopedia.topads.keyword.domain.model;

import java.util.List;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardDomain {
    private Page page;
    private List<Datum> data = null;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
