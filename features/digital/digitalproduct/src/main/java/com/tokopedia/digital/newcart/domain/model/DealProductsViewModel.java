package com.tokopedia.digital.newcart.domain.model;

import java.util.List;

public class DealProductsViewModel {
    private List<DealProductViewModel> products;
    private String nextUrl;

    public DealProductsViewModel() {
    }

    public List<DealProductViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<DealProductViewModel> products) {
        this.products = products;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
