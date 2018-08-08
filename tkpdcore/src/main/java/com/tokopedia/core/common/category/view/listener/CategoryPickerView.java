package com.tokopedia.core.common.category.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public interface CategoryPickerView extends CustomerView {
    void showLoadingDialog();

    void dismissLoadingDialog();

    void renderCategory(List<CategoryViewModel> map, long categoryId);

    void renderCategoryFromSelected(List<CategoryLevelViewModel> categoryLevelDomainModels);

    void showRetryEmpty();

    void hideRetryEmpty();
}
