package com.tokopedia.product.manage.item.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.common.model.variantbyprdold.ProductVariantByPrdModel;
import com.tokopedia.product.manage.item.common.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hendry on 5/18/17.
 */

public class ProductVariantCloud {

    private TomeProductApi tomeProductApi;

    @Inject
    public ProductVariantCloud(TomeProductApi tomeProductApi) {
        this.tomeProductApi = tomeProductApi;
    }

    public Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> fetchProductVariantByCat(long categoryId) {
        return tomeProductApi.getProductVariantByCat(categoryId);
    }

    public Observable<Response<DataResponse<ProductVariantByPrdModel>>> fetchProductVariantByPrd(long productId) {
        return tomeProductApi.getProductVariantByPrd(productId);
    }

}
