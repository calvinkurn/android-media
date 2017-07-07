package com.tokopedia.discovery.categorynav.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;

/**
 * @author by alifa on 7/6/17.
 */

public interface CategoryNavigationContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void emptyState();

        void renderRootCategory(CategoryNavDomainModel categoryNavDomainModel);

    }

    interface Presenter extends CustomerPresenter<CategoryNavigationContract.View> {

        void getRootCategory(String departementId);

    }


}
