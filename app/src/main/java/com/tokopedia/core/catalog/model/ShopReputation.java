package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class ShopReputation implements Parcelable {

    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("badge_level")
    @Expose
    private int badgeLevel;
    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;

    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    public String getScore() {
        return score;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public String getTooltip() {
        return tooltip;
    }

    public int getBadgeLevel() {
        return badgeLevel;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.reputationBadge, flags);
        dest.writeString(this.score);
        dest.writeString(this.reputationScore);
        dest.writeString(this.tooltip);
        dest.writeInt(this.badgeLevel);
        dest.writeInt(this.minBadgeScore);
    }

    public ShopReputation() {
    }

    protected ShopReputation(Parcel in) {
        this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
        this.score = in.readString();
        this.reputationScore = in.readString();
        this.tooltip = in.readString();
        this.badgeLevel = in.readInt();
        this.minBadgeScore = in.readInt();
    }

    public static final Parcelable.Creator<ShopReputation> CREATOR = new Parcelable.Creator<ShopReputation>() {
        @Override
        public ShopReputation createFromParcel(Parcel source) {
            return new ShopReputation(source);
        }

        @Override
        public ShopReputation[] newArray(int size) {
            return new ShopReputation[size];
        }
    };
}
