package com.tokopedia.product.manage.item.main.base.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.product.manage.item.common.data.source.cloud.ProductSubmitResp;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductCloud {
    private final TomeProductApi tomeProductApi;

    public static final int SHOW_VARIANT = 1;

    @Inject
    public ProductCloud(TomeProductApi tomeProductApi) {
        this.tomeProductApi = tomeProductApi;
    }

    public Observable<Integer> addProductSubmit(String productViewModel) {
        return tomeProductApi.addProductSubmit(productViewModel)
                .map(dataResponse -> dataResponse.body().getData().getProductId());
    }

    public Observable<Integer> editProduct(String productId, String productViewModel) {
        return tomeProductApi.editProductSubmit(productId, productViewModel)
                .map(dataResponse -> dataResponse.body().getData().getProductId());
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return tomeProductApi.getProductDetail(productId, SHOW_VARIANT)
                .map(new DataResponseMapper<ProductViewModel>())
                .map(new Func1<ProductViewModel, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel) {
                        ProductVariantViewModel productVariantViewModel = productViewModel.getProductVariant();
                        if (productVariantViewModel != null) {
                            List<ProductVariantOptionParent> productVariantOptionParentList = productVariantViewModel.getVariantOptionParent();
                            Collections.sort(productVariantOptionParentList, new Comparator<ProductVariantOptionParent>() {
                                @Override
                                public int compare(ProductVariantOptionParent o1, ProductVariantOptionParent o2) {
                                    return o1.getPosition() - o2.getPosition();
                                }
                            });
                        }
                        return productViewModel;
                    }
                });
    }
}
