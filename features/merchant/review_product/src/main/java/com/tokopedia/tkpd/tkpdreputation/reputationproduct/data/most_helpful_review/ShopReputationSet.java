
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopReputationSet implements Parcelable{

    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;
    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;

    protected ShopReputationSet(Parcel in) {
        minBadgeScore = in.readInt();
        tooltip = in.readString();
        score = in.readString();
        reputationScore = in.readString();
        reputationBadge = (ReputationBadge) in.readValue(ReputationBadge.class.getClassLoader());
    }

    public static final Creator<ShopReputationSet> CREATOR = new Creator<ShopReputationSet>() {
        @Override
        public ShopReputationSet createFromParcel(Parcel in) {
            return new ShopReputationSet(in);
        }

        @Override
        public ShopReputationSet[] newArray(int size) {
            return new ShopReputationSet[size];
        }
    };

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
     *     The score
     */
    public String getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    public void setScore(String score) {
        this.score = score;
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
        dest.writeInt(minBadgeScore);
        dest.writeString(tooltip);
        dest.writeString(score);
        dest.writeString(reputationScore);
        dest.writeValue(reputationBadge);
    }
}
