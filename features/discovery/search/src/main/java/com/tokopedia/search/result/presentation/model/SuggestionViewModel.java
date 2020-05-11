package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class SuggestionViewModel implements Parcelable, Visitable<ProductListTypeFactory> {
    private String suggestionText = "";
    private String suggestionCurrentKeyword = "";
    private String suggestedQuery = "";
    private String formattedResultCount = "";
    private String suggestion = "";

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getSuggestionCurrentKeyword() {
        return suggestionCurrentKeyword;
    }

    public void setSuggestionCurrentKeyword(String suggestionCurrentKeyword) {
        this.suggestionCurrentKeyword = suggestionCurrentKeyword;
    }

    public String getSuggestedQuery() {
        return suggestedQuery;
    }

    public void setSuggestedQuery(String suggestedQuery) {
        this.suggestedQuery = suggestedQuery;
    }

    public String getFormattedResultCount() {
        return formattedResultCount;
    }

    public void setFormattedResultCount(String formattedResultCount) {
        this.formattedResultCount = formattedResultCount;
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

    public SuggestionViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suggestionText);
        dest.writeString(this.suggestionCurrentKeyword);
        dest.writeString(this.suggestedQuery);
        dest.writeString(this.formattedResultCount);
        dest.writeString(this.suggestion);
    }

    protected SuggestionViewModel(Parcel in) {
        this.suggestionText = in.readString();
        this.suggestionCurrentKeyword = in.readString();
        this.suggestedQuery = in.readString();
        this.formattedResultCount = in.readString();
        this.suggestion = in.readString();
    }

    public static final Creator<SuggestionViewModel> CREATOR = new Creator<SuggestionViewModel>() {
        @Override
        public SuggestionViewModel createFromParcel(Parcel source) {
            return new SuggestionViewModel(source);
        }

        @Override
        public SuggestionViewModel[] newArray(int size) {
            return new SuggestionViewModel[size];
        }
    };
}
