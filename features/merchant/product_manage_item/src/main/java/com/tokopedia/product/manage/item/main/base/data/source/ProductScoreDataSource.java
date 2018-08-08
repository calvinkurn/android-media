package com.tokopedia.product.manage.item.data.source;

import com.tokopedia.product.manage.item.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.product.manage.item.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.product.manage.item.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreDataSource {

    private final ProductScoreDataSourceCache productScoreDataSourceCache;

    public ProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache) {
        this.productScoreDataSourceCache = productScoreDataSourceCache;
    }

    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        return productScoreDataSourceCache.getValidationScore(valueIndicatorScoreModel);
    }
}
