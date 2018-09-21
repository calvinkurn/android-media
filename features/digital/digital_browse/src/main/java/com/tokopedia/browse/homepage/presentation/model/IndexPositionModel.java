package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 07/09/18.
 */

public class IndexPositionModel implements Parcelable {

    private int indexPositionInTab;
    private int indexPositionInList;

    public IndexPositionModel() {
    }

    protected IndexPositionModel(Parcel in) {
        indexPositionInTab = in.readInt();
        indexPositionInList = in.readInt();
    }

    public static final Creator<IndexPositionModel> CREATOR = new Creator<IndexPositionModel>() {
        @Override
        public IndexPositionModel createFromParcel(Parcel in) {
            return new IndexPositionModel(in);
        }

        @Override
        public IndexPositionModel[] newArray(int size) {
            return new IndexPositionModel[size];
        }
    };

    public int getIndexPositionInTab() {
        return indexPositionInTab;
    }

    public void setIndexPositionInTab(int indexPositionInTab) {
        this.indexPositionInTab = indexPositionInTab;
    }

    public int getIndexPositionInList() {
        return indexPositionInList;
    }

    public void setIndexPositionInList(int indexPositionInList) {
        this.indexPositionInList = indexPositionInList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(indexPositionInTab);
        dest.writeInt(indexPositionInList);
    }
}
