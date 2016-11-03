
package com.tokopedia.tkpd.inboxreputation.model.inboxreputation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

@org.parceler.Parcel
public class InboxReputation{

    @SerializedName("navs_tab")
    @Expose
    NavsTab navsTab;
    @SerializedName("list")
    @Expose
    java.util.List<InboxReputationItem> list = new ArrayList<InboxReputationItem>();
    @SerializedName("paging")
    @Expose
    PagingHandler.PagingHandlerModel paging;

    public NavsTab getNavsTab() {
        return navsTab;
    }

    public void setNavsTab(NavsTab navsTab) {
        this.navsTab = navsTab;
    }

    public List<InboxReputationItem> getList() {
        return list;
    }

    public void setList(List<InboxReputationItem> list) {
        this.list = list;
    }

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }
}
