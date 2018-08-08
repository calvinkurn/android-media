
package com.tokopedia.challenges.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmissionCounts {

    @SerializedName("Approved")
    @Expose
    private int approved;
    @SerializedName("Waiting")
    @Expose
    private int waiting;
    @SerializedName("Declined")
    @Expose
    private int declined;

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public int getDeclined() {
        return declined;
    }

    public void setDeclined(int declined) {
        this.declined = declined;
    }

}
