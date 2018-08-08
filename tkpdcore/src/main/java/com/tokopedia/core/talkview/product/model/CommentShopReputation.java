
package com.tokopedia.core.talkview.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentShopReputation implements Parcelable{

    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;

    /**
     * 
     * @return
     *     The reputationScore
     */
    public String getReputationScore() {
        return reputationScore;
    }

    /**
     * 
     * @param reputationScore
     *     The reputation_score
     */
    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    /**
     * 
     * @return
     *     The reputationBadge
     */
    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    /**
     * 
     * @param reputationBadge
     *     The reputation_badge
     */
    public void setReputationBadge(ReputationBadge reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reputationScore);
        dest.writeParcelable(this.reputationBadge, flags);
    }

    public CommentShopReputation() {
    }

    protected CommentShopReputation(Parcel in) {
        this.reputationScore = in.readString();
        this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
    }

    public static final Creator<CommentShopReputation> CREATOR = new Creator<CommentShopReputation>() {
        @Override
        public CommentShopReputation createFromParcel(Parcel source) {
            return new CommentShopReputation(source);
        }

        @Override
        public CommentShopReputation[] newArray(int size) {
            return new CommentShopReputation[size];
        }
    };
}
