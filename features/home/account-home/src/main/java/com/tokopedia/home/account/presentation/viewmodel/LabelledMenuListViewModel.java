package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

import java.util.Objects;

public class LabelledMenuListViewModel extends MenuListViewModel {
    private String label;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LabelledMenuListViewModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMenu());
        dest.writeString(getMenuDescription());
        dest.writeString(getLabel());
        dest.writeString(getApplink());
        dest.writeInt(getCount());
        dest.writeString(getTitleTrack());
        dest.writeString(getSectionTrack());
        dest.writeByte((byte) (isUseSeparator() ? 1 : 0));
    }

    protected LabelledMenuListViewModel(Parcel in) {
        this.setMenu(in.readString());
        this.setMenuDescription(in.readString());
        this.setLabel(in.readString());
        this.setApplink(Objects.requireNonNull(in.readString()));
        this.setCount(in.readInt());
        this.setTitleTrack(in.readString());
        this.setSectionTrack(in.readString());
        this.setUseSeparator(in.readByte() != 0);
    }

    public static final Creator<LabelledMenuListViewModel> CREATOR = new Creator<LabelledMenuListViewModel>() {
        @Override
        public LabelledMenuListViewModel createFromParcel(Parcel source) {
            return new LabelledMenuListViewModel(source);
        }

        @Override
        public LabelledMenuListViewModel[] newArray(int size) {
            return new LabelledMenuListViewModel[size];
        }
    };
}