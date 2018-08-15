package com.tokopedia.common_digital.product.data.repository;

import android.text.TextUtils;

import com.tokopedia.common_digital.product.data.datasource.CategoryDetailDataSource;
import com.tokopedia.common_digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.common_digital.product.presentation.model.ProductDigitalData;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class DigitalCategoryRepository implements IDigitalCategoryRepository {

    private CategoryDetailDataSource categoryDetailDataSource;

    public DigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        this.categoryDetailDataSource = categoryDetailDataSource;
    }

    @Override
    public Observable<ProductDigitalData> getCategory(String categoryId) {
        return categoryDetailDataSource.getCategory(categoryId);
    }

    @Override
    public Observable<ProductDigitalData> getCategoryWithFavorit(String categoryId, String operatorId,
                                                                 String clientNumber, String productId) {
        if (TextUtils.isEmpty(operatorId) && TextUtils.isEmpty(clientNumber) && TextUtils.isEmpty(productId)) {
            return categoryDetailDataSource.getCategoryDetailWithFavorit(categoryId);
        }

        return categoryDetailDataSource.getCategoryAndFavoritFromCloud(categoryId, operatorId,
                clientNumber, productId);
    }

    @Override
    public Observable<String> getHelpUrl(String categoryId) {
        return categoryDetailDataSource.getHelpUrl(categoryId);
    }

}