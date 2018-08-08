package com.tokopedia.product.manage.item.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel;
import com.tokopedia.product.manage.item.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.product.manage.item.view.listener.ProductAddView;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddPresenter<T extends ProductAddView> {
    void saveDraft(ProductViewModel viewModel, boolean isUploading);
    void getShopInfo();
    void fetchProductVariantByCat(long categoryId);
}
