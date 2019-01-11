package com.tokopedia.common.travel.presentation.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 21/08/18.
 */
public class SearchSortModel implements Parcelable {

    private int idSort;
    private String sortName;
    private boolean isSelected;

    public SearchSortModel() {
    }

    protected SearchSortModel(Parcel in) {
        idSort = in.readInt();
        sortName = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<SearchSortModel> CREATOR = new Creator<SearchSortModel>() {
        @Override
        public SearchSortModel createFromParcel(Parcel in) {
            return new SearchSortModel(in);
        }

        @Override
        public SearchSortModel[] newArray(int size) {
            return new SearchSortModel[size];
        }
    };

    public int getIdSort() {
        return idSort;
    }

    public void setIdSort(int idSort) {
        this.idSort = idSort;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idSort);
        parcel.writeString(sortName);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
