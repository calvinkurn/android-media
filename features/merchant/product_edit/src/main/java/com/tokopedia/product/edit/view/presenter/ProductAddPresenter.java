package com.tokopedia.product.edit.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;
import com.tokopedia.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.product.edit.view.listener.ProductAddView;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddPresenter<T extends ProductAddView> {
    void saveDraft(ProductViewModel viewModel, boolean isUploading);
    void getShopInfo();
}
