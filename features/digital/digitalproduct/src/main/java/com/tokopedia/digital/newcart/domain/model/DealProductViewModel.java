package com.tokopedia.digital.newcart.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;

import javax.annotation.Nullable;

public class DealProductViewModel implements Visitable<DigitalDealsAdapterTypeFactory> {
    private long salesPriceNumeric;
    private long savePriceNumeric;
    private String title;
    private String brandName;
    private String imageUrl;
    private long id;
    private boolean selected;

    public DealProductViewModel() {
    }


    public long getSalesPriceNumeric() {
        return salesPriceNumeric;
    }

    public void setSalesPriceNumeric(long salesPriceNumeric) {
        this.salesPriceNumeric = salesPriceNumeric;
    }

    public long getSavePriceNumeric() {
        return savePriceNumeric;
    }

    public void setSavePriceNumeric(long savePriceNumeric) {
        this.savePriceNumeric = savePriceNumeric;
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
}
