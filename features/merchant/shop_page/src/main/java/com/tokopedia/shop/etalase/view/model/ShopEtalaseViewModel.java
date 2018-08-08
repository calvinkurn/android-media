package com.tokopedia.shop.etalase.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.etalase.view.adapter.ShopEtalaseAdapterTypeFactory;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewModel implements Visitable<ShopEtalaseAdapterTypeFactory>{

    private long useAce;
    private String etalaseId;
    private String etalaseName;
    private long etalaseNumProduct;
    private long etalaseTotalProduct;
    private String etalaseBadge;

    private boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getUseAce() {
        return useAce;
    }

    public void setUseAce(long useAce) {
        this.useAce = useAce;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public long getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(long etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public long getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(long etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge;
    }

    @Override
    public int type(ShopEtalaseAdapterTypeFactory shopEtalaseAdapterTypeFactory) {
        return shopEtalaseAdapterTypeFactory.type(this);
    }
}
