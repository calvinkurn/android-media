package com.tokopedia.core.common.category.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.common.category.view.listener.CategoryPickerView;

/**
 * @author sebastianuskh on 4/3/17.
 */

public abstract class CategoryPickerPresenter extends BaseDaggerPresenter<CategoryPickerView> {
    public abstract void getCategoryLiteTree();

    public abstract void getCategoryChild(long categoryId);

    public abstract void unsubscribe();
}
