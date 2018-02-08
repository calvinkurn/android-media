package com.tokopedia.core.common.category.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.common.category.view.listener.CategoryPickerView;

/**
 * @author sebastianuskh on 4/3/17.
 */

public abstract class CategoryPickerPresenter extends BaseDaggerPresenter<CategoryPickerView>{
    public abstract void fetchCategoryLevelOne();

    public abstract void fetchCategoryChild(long categoryId);

    public abstract void fetchCategoryFromSelected(long initSelected);

    public abstract void unsubscribe();
}
