package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountViewModel {
    private List<Visitable> buyerViewModels;
    private List<Visitable> sellerViewModels;

    public List<Visitable> getBuyerViewModels() {
        return buyerViewModels;
    }

    public void setBuyerViewModels(List<Visitable> buyerViewModels) {
        this.buyerViewModels = buyerViewModels;
    }

    public List<Visitable> getSellerViewModels() {
        return sellerViewModels;
    }

    public void setSellerViewModels(List<Visitable> sellerViewModels) {
        this.sellerViewModels = sellerViewModels;
    }
}
