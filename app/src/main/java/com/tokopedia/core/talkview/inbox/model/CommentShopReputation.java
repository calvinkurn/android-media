package com.tokopedia.core.talkview.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentShopReputation implements Parcelable {

    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;

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


    protected CommentShopReputation(Parcel in) {
        reputationBadge = (ReputationBadge) in.readValue(ReputationBadge.class.getClassLoader());
        reputationScore = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(reputationBadge);
        dest.writeString(reputationScore);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentShopReputation> CREATOR = new Parcelable.Creator<CommentShopReputation>() {
        @Override
        public CommentShopReputation createFromParcel(Parcel in) {
            return new CommentShopReputation(in);
        }

        @Override
        public CommentShopReputation[] newArray(int size) {
            return new CommentShopReputation[size];
        }
    };
}