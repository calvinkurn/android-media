package com.tokopedia.topchat.attachproduct.domain.util;

import com.tokopedia.topchat.attachproduct.data.model.DataProductResponse;
import com.tokopedia.topchat.attachproduct.view.viewmodel.AttachProductItemViewModel;

/**
 * Created by Hendri on 14/02/18.
 */

public class DomainModelToViewModelConverter {
    public static AttachProductItemViewModel convertProductDomainModel(DataProductResponse product){
        return new AttachProductItemViewModel(product.getProductUrl(),
                product.getProductName(),
                product.getProductId(),
                product.getProductImageFull(),
                product.getProductImage(),
                product.getProductPrice());
    }
}
