package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewModel implements Visitable<AccountTypeFactory> {

    private String name;
    private String imageUrl;
    private String tokopoint;
    private String coupons;
    private int progress;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTokopoint() {
        return tokopoint;
    }

    public void setTokopoint(String tokopoint) {
        this.tokopoint = tokopoint;
    }

    public String getVoucher() {
        return coupons;
    }

    public void setVoucher(String coupons) {
        this.coupons = coupons;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
