package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemViewModel implements Visitable<DashboardItemTypeFactory> {

    private String imageUrl;
    private String title;
    private String value;
    private String itemClicked;
    private String itemSold;
    private boolean isActive;

    public DashboardItemViewModel(String imageUrl,
                                  String title,
                                  String value,
                                  String itemClicked,
                                  String itemSold,
                                  boolean isActive) {
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.title = title;
        this.value = value;
        this.itemClicked = itemClicked;
        this.itemSold = itemSold;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getItemClicked() {
        return itemClicked;
    }

    public void setItemClicked(String itemClicked) {
        this.itemClicked = itemClicked;
    }

    public String getItemSold() {
        return itemSold;
    }

    public void setItemSold(String itemSold) {
        this.itemSold = itemSold;
    }

    @Override
    public int type(DashboardItemTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
