package com.tokopedia.search.result.presentation.model;

import androidx.annotation.DrawableRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;

public class EmptySearchViewModel implements Visitable<SearchSectionTypeFactory> {

    @DrawableRes
    private int imageRes;
    private String title;
    private String content;
    private String buttonText;
    private boolean bannerAdsAllowed = true;

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

    public boolean isBannerAdsAllowed() {
        return bannerAdsAllowed;
    }

    public void setBannerAdsAllowed(boolean bannerAdsAllowed) {
        this.bannerAdsAllowed = bannerAdsAllowed;
    }

    @Override
    public int type(SearchSectionTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
