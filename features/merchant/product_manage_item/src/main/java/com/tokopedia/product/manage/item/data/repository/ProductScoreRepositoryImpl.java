package com.tokopedia.product.manage.item.data.repository;

import com.tokopedia.product.manage.item.data.source.ProductScoreDataSource;
import com.tokopedia.product.manage.item.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.product.manage.item.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.product.manage.item.domain.ProductScoreRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreRepositoryImpl implements ProductScoreRepository {

    private final ProductScoreDataSource productScoreDataSource;

    public ProductScoreRepositoryImpl(ProductScoreDataSource productScoreDataSource) {
        this.productScoreDataSource = productScoreDataSource;
    }

    @Override
    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        return productScoreDataSource.getValidationScore(valueIndicatorScoreModel);
    }
}
