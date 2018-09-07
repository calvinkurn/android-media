package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/30/17.
 */

public class ReputationBadgeViewModel implements Parcelable {

    private int level;
    private int set;

    public ReputationBadgeViewModel(int level, int set) {
        this.level = level;
        this.set = set;
    }

    protected ReputationBadgeViewModel(Parcel in) {
        level = in.readInt();
        set = in.readInt();
    }

    public static final Creator<ReputationBadgeViewModel> CREATOR = new Creator<ReputationBadgeViewModel>() {
        @Override
        public ReputationBadgeViewModel createFromParcel(Parcel in) {
            return new ReputationBadgeViewModel(in);
        }

        @Override
        public ReputationBadgeViewModel[] newArray(int size) {
            return new ReputationBadgeViewModel[size];
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
