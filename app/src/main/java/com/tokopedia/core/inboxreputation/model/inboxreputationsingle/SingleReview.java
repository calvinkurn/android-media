
package com.tokopedia.core.inboxreputation.model.inboxreputationsingle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;

@org.parceler.Parcel
public class SingleReview {

    @SerializedName("list")
    @Expose
    InboxReputationDetailItem inboxReputationDetailItem;


    /**
     * @return The list
     */
    public InboxReputationDetailItem getInboxReputationDetailItem() {
        return inboxReputationDetailItem;
    }

    /**
     * @param inboxReputationDetailItem The list
     */
    public void setInboxReputationDetailItem(InboxReputationDetailItem inboxReputationDetailItem) {
        this.inboxReputationDetailItem = inboxReputationDetailItem;
    }

}
