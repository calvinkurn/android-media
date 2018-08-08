
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InboxReputationPojo {

    @SerializedName("inbox_reputation")
    @Expose
    private List<InboxReputation> inboxReputation = null;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    public List<InboxReputation> getInboxReputation() {
        return inboxReputation;
    }

    public void setInboxReputation(List<InboxReputation> inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
