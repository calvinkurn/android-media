package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/30/17.
 */

public class ReputationBadgeUiModel implements Parcelable {

    private int level;
    private int set;

    public ReputationBadgeUiModel(int level, int set) {
        this.level = level;
        this.set = set;
    }

    protected ReputationBadgeUiModel(Parcel in) {
        level = in.readInt();
        set = in.readInt();
    }

    public static final Creator<ReputationBadgeUiModel> CREATOR = new Creator<ReputationBadgeUiModel>() {
        @Override
        public ReputationBadgeUiModel createFromParcel(Parcel in) {
            return new ReputationBadgeUiModel(in);
        }

        @Override
        public ReputationBadgeUiModel[] newArray(int size) {
            return new ReputationBadgeUiModel[size];
        }
    };

    public int getLevel() {
        return level;
    }

    public int getSet() {
        return set;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(set);
    }
}
