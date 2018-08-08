package com.tokopedia.topads.dashboard.domain.model;

import java.util.List;

/**
 * @author normansyahputa on 3/3/17.
 */
public class ProductListDomain {
    List<ProductDomain> productDomains;
    boolean eof;
    int page = -1;

    public ProductListDomain() {
    }

    public ProductListDomain(List<ProductDomain> productDomains, boolean eof) {
        this.productDomains = productDomains;
        this.eof = eof;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ProductDomain> getProductDomains() {
        return productDomains;
    }

    public void setProductDomains(List<ProductDomain> productDomains) {
        this.productDomains = productDomains;
    }

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }
}
