package com.tokopedia.product.manage.item.domain;

import com.tokopedia.product.manage.item.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.product.manage.item.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public interface ProductScoreRepository {

    Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel);
}
