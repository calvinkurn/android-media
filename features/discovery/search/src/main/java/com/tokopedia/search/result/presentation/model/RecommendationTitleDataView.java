package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class RecommendationTitleDataView implements Parcelable , Visitable<ProductListTypeFactory> {
    private String title;
    private String seeMoreUrl;
    private String pageName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public RecommendationTitleDataView(String title, String seeMoreUrl, String pageName) {
        this.title = title;
        this.seeMoreUrl = seeMoreUrl;
        this.pageName = pageName;
    }

    public String getSeeMoreUrl() {
        return seeMoreUrl;
    }

    public void setSeeMoreUrl(String seeMoreUrl) {
        this.seeMoreUrl = seeMoreUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    protected RecommendationTitleDataView(Parcel in) {
        this.title = in.readString();
    }

    public static final Creator<RecommendationTitleDataView> CREATOR = new Creator<RecommendationTitleDataView>() {
        @Override
        public RecommendationTitleDataView createFromParcel(Parcel in) {
            return new RecommendationTitleDataView(in);
        }

        @Override
        public RecommendationTitleDataView[] newArray(int size) {
            return new RecommendationTitleDataView[size];
        }
    };

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
