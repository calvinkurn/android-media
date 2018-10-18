package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

import java.util.List;

public interface DigitalCartDealsContract {

    interface View extends CustomerView{

        void showGetCategoriesLoading();

        void hideDealsPage();

        void showGetCategoriesError(String message);

        void hideGetCategoriesLoading();

        void renderDealsTab(List<DealCategoryViewModel> dealCategoryViewModels);

        void showDealsPage();

    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();
    }
}
