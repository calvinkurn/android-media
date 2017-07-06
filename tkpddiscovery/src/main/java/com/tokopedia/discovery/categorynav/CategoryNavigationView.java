package com.tokopedia.discovery.categorynav;

import com.tokopedia.core.network.entity.categories.Category;
import com.tokopedia.core.presenter.BaseView;

import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public interface CategoryNavigationView extends BaseView {

    void setUpRootCategory(List<Category> categories);
}
