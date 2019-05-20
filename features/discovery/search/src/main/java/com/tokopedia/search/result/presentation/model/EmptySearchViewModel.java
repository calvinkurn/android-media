package com.tokopedia.search.result.presentation.model;

import android.support.annotation.DrawableRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;

public class EmptySearchViewModel implements Visitable<SearchSectionTypeFactory> {

    @DrawableRes
    private int imageRes;
    private String title;
    private String content;
    private String buttonText;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public int type(SearchSectionTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
