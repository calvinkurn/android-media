package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class SuggestionDataView implements Parcelable, Visitable<ProductListTypeFactory> {
    private String suggestionText = "";
    private String suggestedQuery = "";
    private String suggestion = "";

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getSuggestedQuery() {
        return suggestedQuery;
    }

    public void setSuggestedQuery(String suggestedQuery) {
        this.suggestedQuery = suggestedQuery;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestion() {
        return this.suggestion;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public SuggestionDataView() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suggestionText);
        dest.writeString(this.suggestedQuery);
        dest.writeString(this.suggestion);
    }

    protected SuggestionDataView(Parcel in) {
        this.suggestionText = in.readString();
        this.suggestedQuery = in.readString();
        this.suggestion = in.readString();
    }

    public static final Creator<SuggestionDataView> CREATOR = new Creator<SuggestionDataView>() {
        @Override
        public SuggestionDataView createFromParcel(Parcel source) {
            return new SuggestionDataView(source);
        }

        @Override
        public SuggestionDataView[] newArray(int size) {
            return new SuggestionDataView[size];
        }
    };
}
