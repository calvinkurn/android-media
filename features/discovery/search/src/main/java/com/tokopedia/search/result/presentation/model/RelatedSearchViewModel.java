package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import java.util.List;

public class RelatedSearchViewModel implements Parcelable, Visitable<ProductListTypeFactory> {

    private String relatedKeyword;
    private List<OtherRelated> otherRelated;

    public String getRelatedKeyword() {
        return relatedKeyword;
    }

    public void setRelatedKeyword(String relatedKeyword) {
        this.relatedKeyword = relatedKeyword;
    }

    public List<OtherRelated> getOtherRelated() {
        return otherRelated;
    }

    public void setOtherRelated(List<OtherRelated> otherRelated) {
        this.otherRelated = otherRelated;
    }

    public static class OtherRelated implements Parcelable {
        private String keyword;
        private String url;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.keyword);
            dest.writeString(this.url);
        }

        public OtherRelated() {
        }

        protected OtherRelated(Parcel in) {
            this.keyword = in.readString();
            this.url = in.readString();
        }

        public static final Creator<OtherRelated> CREATOR = new Creator<OtherRelated>() {
            @Override
            public OtherRelated createFromParcel(Parcel source) {
                return new OtherRelated(source);
            }

            @Override
            public OtherRelated[] newArray(int size) {
                return new OtherRelated[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.relatedKeyword);
        dest.writeTypedList(this.otherRelated);
    }

    public RelatedSearchViewModel() {
    }

    protected RelatedSearchViewModel(Parcel in) {
        this.relatedKeyword = in.readString();
        this.otherRelated = in.createTypedArrayList(OtherRelated.CREATOR);
    }

    public static final Creator<RelatedSearchViewModel> CREATOR = new Creator<RelatedSearchViewModel>() {
        @Override
        public RelatedSearchViewModel createFromParcel(Parcel source) {
            return new RelatedSearchViewModel(source);
        }

        @Override
        public RelatedSearchViewModel[] newArray(int size) {
            return new RelatedSearchViewModel[size];
        }
    };

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
