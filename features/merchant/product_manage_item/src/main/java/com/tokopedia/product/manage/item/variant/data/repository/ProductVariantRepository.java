package com.tokopedia.product.manage.item.common.repository;

import com.tokopedia.product.manage.item.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.common.model.variantbyprdold.ProductVariantByPrdModel;

import java.util.List;

import rx.Observable;

/**
 * Created by hendry on 8/14/2017.
 */

public interface ProductVariantRepository {
    Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId);
    Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId);
}
