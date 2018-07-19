package com.tokopedia.home.account.presentation.view.categorygridview.model;

import java.util.List;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryGrid {
    private String title;
    private String linkText;
    private String linkUrl;
    private List<CategoryItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public List<CategoryItem> getItems() {
        return items;
    }

    public void setItems(List<CategoryItem> items) {
        this.items = items;
    }
}
