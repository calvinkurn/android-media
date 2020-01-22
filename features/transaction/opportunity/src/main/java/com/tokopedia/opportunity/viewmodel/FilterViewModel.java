package com.tokopedia.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nisie on 3/6/17.
 */

public class FilterViewModel implements Parcelable {
    private String name;
    private SearchViewModel searchViewModel;
    private ArrayList<OptionViewModel> listChild;
    private boolean isSelected;
    private boolean isActive;
    private int position;

    public FilterViewModel() {
        this.listChild = new ArrayList<>();
    }


    protected FilterViewModel(Parcel in) {
        name = in.readString();
        searchViewModel = in.readParcelable(SearchViewModel.class.getClassLoader());
        listChild = in.createTypedArrayList(OptionViewModel.CREATOR);
        isSelected = in.readByte() != 0;
        isActive = in.readByte() != 0;
        position = in.readInt();
    }

    public static final Creator<FilterViewModel> CREATOR = new Creator<FilterViewModel>() {
        @Override
        public FilterViewModel createFromParcel(Parcel in) {
            return new FilterViewModel(in);
        }

        @Override
        public FilterViewModel[] newArray(int size) {
            return new FilterViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(searchViewModel, flags);
        dest.writeTypedList(listChild);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(position);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchViewModel getSearchViewModel() {
        return searchViewModel;
    }

    public void setSearchViewModel(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }

    public ArrayList<OptionViewModel> getListChild() {
        return listChild;
    }

    public void setListChild(ArrayList<OptionViewModel> listChild) {
        this.listChild = listChild;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
