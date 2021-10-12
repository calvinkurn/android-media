package com.tokopedia.attachproduct.domain.util;

/**
 * Created by Hendri on 14/02/18.
 */

public class DomainModelToViewModelConverter {
    public static AttachProductItemUiModel convertProductDomainModel(DataProductResponse product) {
        return new AttachProductItemUiModel(
                product.getProductUrl(),
                product.getProductName(),
                product.getProductId(),
                product.getProductImageFull(),
                product.getProductImage(),
                product.getProductPrice(),
                product.getShop().getShopName(),
                product.getOriginalPrice(),
                product.getDiscountPercentage()
        );
    }
}
