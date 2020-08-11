package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.unifycomponents.Label;

import java.util.Objects;

public class LabelledMenuListUiModel extends MenuListViewModel {
    private String label;
    private int labelType = Label.Companion.getGENERAL_LIGHT_GREEN();
    private boolean showRightButton;

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

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public boolean isShowRightButton() {
        return showRightButton;
    }

    public void setShowRightButton(boolean showRightButton) {
        this.showRightButton = showRightButton;
    }

    public LabelledMenuListUiModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMenu());
        dest.writeString(getMenuDescription());
        dest.writeString(getLabel());
        dest.writeInt(getLabelType());
        dest.writeString(getApplink());
        dest.writeInt(getCount());
        dest.writeString(getTitleTrack());
        dest.writeString(getSectionTrack());
        dest.writeInt((showRightButton) ? 1 : 0);
        dest.writeByte((byte) (isUseSeparator() ? 1 : 0));
    }

    protected LabelledMenuListUiModel(Parcel in) {
        this.setMenu(in.readString());
        this.setMenuDescription(in.readString());
        this.setLabel(in.readString());
        this.setLabelType(in.readInt());
        this.setApplink(Objects.requireNonNull(in.readString()));
        this.setCount(in.readInt());
        this.setTitleTrack(in.readString());
        this.setSectionTrack(in.readString());
        this.setShowRightButton(in.readInt() == 1);
        this.setUseSeparator(in.readByte() != 0);
    }

    public static final Creator<LabelledMenuListUiModel> CREATOR = new Creator<LabelledMenuListUiModel>() {
        @Override
        public LabelledMenuListUiModel createFromParcel(Parcel source) {
            return new LabelledMenuListUiModel(source);
        }

        @Override
        public LabelledMenuListUiModel[] newArray(int size) {
            return new LabelledMenuListUiModel[size];
        }
    };
}