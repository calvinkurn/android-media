package com.tokopedia.shop.settings.notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopNoteFactory;

public class ShopNoteViewModel implements Parcelable, Visitable<ShopNoteFactory>{
    private String id;
    private String title;
    private String content;
    private Boolean isTerms;
    private String updateTime;

    public ShopNoteViewModel(ShopNoteModel shopNoteModel){
        this.id = shopNoteModel.getId();
        this.title = shopNoteModel.getTitle();
        this.content = shopNoteModel.getContent();
        this.isTerms = shopNoteModel.getTerms();
        this.updateTime = shopNoteModel.getUpdateTime();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeValue(this.isTerms);
        dest.writeString(this.updateTime);
    }

    protected ShopNoteViewModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.isTerms = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.updateTime = in.readString();
    }

    public static final Parcelable.Creator<ShopNoteViewModel> CREATOR = new Parcelable.Creator<ShopNoteViewModel>() {
        @Override
        public ShopNoteViewModel createFromParcel(Parcel source) {
            return new ShopNoteViewModel(source);
        }

        @Override
        public ShopNoteViewModel[] newArray(int size) {
            return new ShopNoteViewModel[size];
        }
    };

    @Override
    public int type(ShopNoteFactory typeFactory) {
        return typeFactory.type(this);
    }
}
