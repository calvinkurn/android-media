package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class ProductDataDomain {
    private int productId;
    private String productName;
    private String productImageUrl;
    private String productPageUrl;
    private int shopId;
    private int productStatus;

    public ProductDataDomain(int productId, String productName,
                             String productImageUrl, String productPageUrl, int shopId, int productStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPageUrl = productPageUrl;
        this.shopId = shopId;
        this.productStatus = productStatus;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public int getShopId() {
        return shopId;
    }

    public int getProductStatus() {
        return productStatus;
    }
}
