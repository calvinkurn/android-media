package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokopointsDrawer implements Parcelable {

    @SerializedName("redirectURL")
    private String redirectURL;

    @SerializedName("iconImageURL")
    private String iconImageURL;

    @SerializedName("sectionContent")
    private List<SectionContentItem> sectionContent;

    @SerializedName("offFlag")
    private boolean offFlag;

    @SerializedName("redirectAppLink")
    private String redirectAppLink;

    @Expose(serialize = false, deserialize = false)
    private String mainPageTitle;

    protected TokopointsDrawer(Parcel in) {
        redirectURL = in.readString();
        iconImageURL = in.readString();
        offFlag = in.readByte() != 0;
        redirectAppLink = in.readString();
    }

    public static final Creator<TokopointsDrawer> CREATOR = new Creator<TokopointsDrawer>() {
        @Override
        public TokopointsDrawer createFromParcel(Parcel in) {
            return new TokopointsDrawer(in);
        }

        @Override
        public TokopointsDrawer[] newArray(int size) {
            return new TokopointsDrawer[size];
        }
    };

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setIconImageURL(String iconImageURL) {
        this.iconImageURL = iconImageURL;
    }

    public String getIconImageURL() {
        return iconImageURL;
    }

    public void setSectionContent(List<SectionContentItem> sectionContent) {
        this.sectionContent = sectionContent;
    }

    public List<SectionContentItem> getSectionContent() {
        return sectionContent;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }

    public boolean isOffFlag() {
        return offFlag;
    }

    public void setRedirectAppLink(String redirectAppLink) {
        this.redirectAppLink = redirectAppLink;
    }

    public String getRedirectAppLink() {
        return redirectAppLink;
    }

    @Override
    public String toString() {
        return
                "TokopointsDrawer{" +
                        "redirectURL = '" + redirectURL + '\'' +
                        ",iconImageURL = '" + iconImageURL + '\'' +
                        ",sectionContent = '" + sectionContent + '\'' +
                        ",offFlag = '" + offFlag + '\'' +
                        ",redirectAppLink = '" + redirectAppLink + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(redirectURL);
        dest.writeString(iconImageURL);
        dest.writeByte((byte) (offFlag ? 1 : 0));
        dest.writeString(redirectAppLink);
    }

    public String getMainPageTitle() {
        return mainPageTitle;
    }

    public void setMainPageTitle(String mainPageTitle) {
        this.mainPageTitle = mainPageTitle;
    }
}