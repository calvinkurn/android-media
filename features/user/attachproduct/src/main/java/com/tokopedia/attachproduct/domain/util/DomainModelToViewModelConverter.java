package com.tokopedia.attachproduct.domain.util;

import com.tokopedia.attachproduct.data.model.DataProductResponse;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

/**
 * Created by Hendri on 14/02/18.
 */

public class DomainModelToViewModelConverter {
    public static AttachProductItemViewModel convertProductDomainModel(DataProductResponse product) {
        return new AttachProductItemViewModel(product.getProductUrl(),
                product.getProductName(),
                product.getProductId(),
                product.getProductImageFull(),
                product.getProductImage(),
                product.getProductPrice(),
                product.getShop().getShopName());
    }
}
