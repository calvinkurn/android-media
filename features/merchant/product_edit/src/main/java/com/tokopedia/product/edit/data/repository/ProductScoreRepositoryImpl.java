package com.tokopedia.product.edit.data.repository;

import com.tokopedia.product.edit.data.source.ProductScoreDataSource;
import com.tokopedia.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.product.edit.domain.ProductScoreRepository;

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
