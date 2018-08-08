package com.tokopedia.discovery.categorynav.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public interface CategoryNavigationContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void emptyState();

        void renderRootCategory(CategoryNavDomainModel categoryNavDomainModel);

        void renderCategoryLevel2(String categoryId, List<Category> children);

        void renderCategoryLevel3(String categoryId, List<Category> children);

    }

    interface Presenter extends CustomerPresenter<CategoryNavigationContract.View> {

        void getRootCategory(String departementId);

        void getChildren(int level, String departementId);

        void setOnDestroyView();
    }


}
