package com.tokopedia.posapp.domain.model.product;

import com.tokopedia.posapp.domain.model.shop.ShopInfoDomain;

import java.util.List;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductDomain {
    private ProductInfoDomain productInfo;
    private ShopInfoDomain shopInfo;
    private List<ProductImageDomain> productImages;

    public ProductInfoDomain getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfoDomain productInfo) {
        this.productInfo = productInfo;
    }

    public ShopInfoDomain getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoDomain shopInfo) {
        this.shopInfo = shopInfo;
    }

    public List<ProductImageDomain> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageDomain> productImages) {
        this.productImages = productImages;
    }
}
