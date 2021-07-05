
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserShopReputation implements Parcelable{

    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("min_badge_score")
    @Expose
    private String minBadgeScore;
    @SerializedName("score")
    @Expose
    private String score;

    protected UserShopReputation(Parcel in) {
        reputationBadge = (ReputationBadge) in.readValue(ReputationBadge.class.getClassLoader());
        tooltip = in.readString();
        reputationScore = in.readString();
        minBadgeScore = in.readString();
        score = in.readString();
    }

    public static final Creator<UserShopReputation> CREATOR = new Creator<UserShopReputation>() {
        @Override
        public UserShopReputation createFromParcel(Parcel in) {
            return new UserShopReputation(in);
        }

        @Override
        public UserShopReputation[] newArray(int size) {
            return new UserShopReputation[size];
        }
    };

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

    /**
     * 
     * @return
     *     The minBadgeScore
     */
    public String getMinBadgeScore() {
        return minBadgeScore;
    }

    /**
     * 
     * @param minBadgeScore
     *     The min_badge_score
     */
    public void setMinBadgeScore(String minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(reputationBadge);
        dest.writeString(tooltip);
        dest.writeString(reputationScore);
        dest.writeString(minBadgeScore);
        dest.writeString(score);
    }
}
