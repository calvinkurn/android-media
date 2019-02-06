
package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopReputation implements Parcelable {

    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("score")
    @Expose
    private int score;
    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;

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
     *     The tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * 
     * @param tooltip
     *     The tooltip
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
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

    /**
     * 
     * @return
     *     The score
     */
    public int getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 
     * @return
     *     The minBadgeScore
     */
    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    /**
     * 
     * @param minBadgeScore
     *     The min_badge_score
     */
    public void setMinBadgeScore(int minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.reputationBadge, flags);
        dest.writeString(this.tooltip);
        dest.writeString(this.reputationScore);
        dest.writeInt(this.score);
        dest.writeInt(this.minBadgeScore);
    }

    public ShopReputation() {
    }

    protected ShopReputation(Parcel in) {
        this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
        this.tooltip = in.readString();
        this.reputationScore = in.readString();
        this.score = in.readInt();
        this.minBadgeScore = in.readInt();
    }

    public static final Creator<ShopReputation> CREATOR = new Creator<ShopReputation>() {
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
