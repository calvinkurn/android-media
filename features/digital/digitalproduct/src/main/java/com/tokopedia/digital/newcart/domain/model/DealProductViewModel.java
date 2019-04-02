package com.tokopedia.digital.newcart.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;

import javax.annotation.Nullable;

public class DealProductViewModel implements Visitable<DigitalDealsAdapterTypeFactory> {
    private String categoryName;
    private long salesPriceNumeric;
    private long beforePriceNumeric;
    private String title;
    private String brandName;
    private String imageUrl;
    private long id;
    private boolean selected;
    private String url;

    public DealProductViewModel() {
    }


    public long getSalesPriceNumeric() {
        return salesPriceNumeric;
    }

    public void setSalesPriceNumeric(long salesPriceNumeric) {
        this.salesPriceNumeric = salesPriceNumeric;
    }

    public long getBeforePrice() {
        return beforePriceNumeric;
    }

    public void setBeforePriceNumeric(long beforePriceNumeric) {
        this.beforePriceNumeric = beforePriceNumeric;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof DealProductViewModel && ((DealProductViewModel) obj).getId() == getId();
    }

    @Override
    public int type(DigitalDealsAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
