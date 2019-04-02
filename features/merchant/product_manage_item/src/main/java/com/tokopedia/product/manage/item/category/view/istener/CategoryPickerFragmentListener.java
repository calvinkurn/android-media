package com.tokopedia.product.manage.item.category.view.istener;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public interface CategoryPickerFragmentListener {
    void selectSetCategory(List<CategoryViewModel> listCategory);
}
