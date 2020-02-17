package com.tokopedia.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nisie on 4/26/17.
 */

public class OptionViewModel implements Parcelable{
    private String name;
    private String key;
    private String value;
    private int parent;
    private int isHidden;
    private int treeLevel;
    private String identifier;
    private ArrayList<OptionViewModel> listChild;
    private boolean isSelected;
    private boolean isExpanded;

    public OptionViewModel() {
    }

    protected OptionViewModel(Parcel in) {
        name = in.readString();
        key = in.readString();
        value = in.readString();
        parent = in.readInt();
        isHidden = in.readInt();
        treeLevel = in.readInt();
        identifier = in.readString();
        listChild = in.createTypedArrayList(OptionViewModel.CREATOR);
        isSelected = in.readByte() != 0;
        isExpanded = in.readByte() != 0;
    }

    public static final Creator<OptionViewModel> CREATOR = new Creator<OptionViewModel>() {
        @Override
        public OptionViewModel createFromParcel(Parcel in) {
            return new OptionViewModel(in);
        }

        @Override
        public OptionViewModel[] newArray(int size) {
            return new OptionViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(value);
        dest.writeInt(parent);
        dest.writeInt(isHidden);
        dest.writeInt(treeLevel);
        dest.writeString(identifier);
        dest.writeTypedList(listChild);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isExpanded ? 1 : 0));
    }
}
