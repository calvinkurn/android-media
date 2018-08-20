package com.tokopedia.shop.common.graphql.data.shopnote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopNoteModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("isTerms")
    @Expose
    private Boolean isTerms;
    @SerializedName("updateTime")
    @Expose
    private String updateTime;

    public ShopNoteModel(){}

    protected ShopNoteModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        byte tmpIsTerms = in.readByte();
        isTerms = tmpIsTerms == 0 ? null : tmpIsTerms == 1;
        updateTime = in.readString();
    }

    public static final Creator<ShopNoteModel> CREATOR = new Creator<ShopNoteModel>() {
        @Override
        public ShopNoteModel createFromParcel(Parcel in) {
            return new ShopNoteModel(in);
        }

        @Override
        public ShopNoteModel[] newArray(int size) {
            return new ShopNoteModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Boolean getTerms() {
        return isTerms;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTerms(Boolean terms) {
        isTerms = terms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeByte((byte) (isTerms == null ? 0 : isTerms ? 1 : 2));
        dest.writeString(updateTime);
    }
}
