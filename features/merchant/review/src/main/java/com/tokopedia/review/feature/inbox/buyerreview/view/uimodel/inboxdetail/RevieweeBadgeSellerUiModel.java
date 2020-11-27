package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/30/17.
 */

public class RevieweeBadgeSellerUiModel implements Parcelable {

    private String tooltip;
    private String reputationScore;
    private int score;
    private int minBadgeScore;
    private String reputationBadgeUrl;
    private ReputationBadgeUiModel reputationBadge;
    private int isFavorited = -1;

    public RevieweeBadgeSellerUiModel(String tooltip, String reputationScore, int score,
                                      int minBadgeScore, String reputationBadgeUrl,
                                      ReputationBadgeUiModel reputationBadge, int isFavorited) {
        this.tooltip = tooltip;
        this.reputationScore = reputationScore;
        this.score = score;
        this.minBadgeScore = minBadgeScore;
        this.reputationBadgeUrl = reputationBadgeUrl;
        this.reputationBadge = reputationBadge;
        this.isFavorited = isFavorited;
    }

    protected RevieweeBadgeSellerUiModel(Parcel in) {
        tooltip = in.readString();
        reputationScore = in.readString();
        score = in.readInt();
        minBadgeScore = in.readInt();
        reputationBadgeUrl = in.readString();
        reputationBadge = in.readParcelable(ReputationBadgeUiModel.class.getClassLoader());
        isFavorited = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tooltip);
        dest.writeString(reputationScore);
        dest.writeInt(score);
        dest.writeInt(minBadgeScore);
        dest.writeString(reputationBadgeUrl);
        dest.writeParcelable(reputationBadge, flags);
        dest.writeInt(isFavorited);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RevieweeBadgeSellerUiModel> CREATOR = new Creator<RevieweeBadgeSellerUiModel>() {
        @Override
        public RevieweeBadgeSellerUiModel createFromParcel(Parcel in) {
            return new RevieweeBadgeSellerUiModel(in);
        }

        @Override
        public RevieweeBadgeSellerUiModel[] newArray(int size) {
            return new RevieweeBadgeSellerUiModel[size];
        }
    };

    public String getTooltip() {
        return tooltip;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public int getScore() {
        return score;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public String getReputationBadgeUrl() {
        return reputationBadgeUrl;
    }

    public ReputationBadgeUiModel getReputationBadge() {
        return reputationBadge;
    }

    public int getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(int isFavorited) {
        this.isFavorited = isFavorited;
    }
}
