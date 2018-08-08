package com.tokopedia.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.product.edit.view.listener.ProductScoringDetailView;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public abstract class ProductScoringDetailPresenter extends BaseDaggerPresenter<ProductScoringDetailView> {

    public abstract void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel);
}
