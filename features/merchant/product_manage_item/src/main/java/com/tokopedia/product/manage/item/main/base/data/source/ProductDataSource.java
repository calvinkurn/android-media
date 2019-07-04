package com.tokopedia.product.manage.item.main.base.data.source;

import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.ProductCloud;
import com.tokopedia.product.manage.item.main.base.domain.mapper.ProductUploadMapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductDataSource {

    private final ProductCloud productCloud;
    private final ProductUploadMapper productUploadMapper;

    @Inject
    public ProductDataSource(ProductCloud productCloud, ProductUploadMapper productUploadMapper) {
        this.productCloud = productCloud;
        this.productUploadMapper = productUploadMapper;
    }

    public Observable<Integer> addProductSubmit(ProductViewModel productViewModel) {
        return productCloud.addProductSubmit(productUploadMapper.removeUnusedParam(productViewModel, true));
    }

    public Observable<Integer> editProduct(ProductViewModel productViewModel) {
        //when edit, we don;t remove empty object, because we need to delete the object.
        return productCloud.editProduct(productViewModel.getProductId(), productUploadMapper.removeUnusedParam(productViewModel, false));
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return productCloud.getProductDetail(productId);
    }
}
