package com.tokopedia.product.edit.common.repository;

import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.edit.common.model.variantbyprdold.ProductVariantByPrdModel;

import java.util.List;

import rx.Observable;

/**
 * Created by hendry on 8/14/2017.
 */

public interface ProductVariantRepository {
    Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId);
    Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId);
}
