package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.newcart.domain.DigitalDealsGetCategoriesUseCase;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsContract;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class DigitalCartDealsPresenter extends BaseDaggerPresenter<DigitalCartDealsContract.View> implements DigitalCartDealsContract.Presenter {
    private DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase;

    @Inject
    public DigitalCartDealsPresenter(DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase) {
        this.digitalDealsGetCategoriesUseCase = digitalDealsGetCategoriesUseCase;
    }

    @Override
    public void onViewCreated() {
        getView().showGetCategoriesLoading();
        getView().hideDealsPage();
        digitalDealsGetCategoriesUseCase.execute(new Subscriber<List<DealCategoryViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showGetCategoriesError(e.getMessage());
                    getView().hideGetCategoriesLoading();
                }
            }

            @Override
            public void onNext(List<DealCategoryViewModel> dealCategoryViewModels) {
                getView().hideGetCategoriesLoading();
                getView().showDealsPage();
                getView().renderDealsTab(dealCategoryViewModels);
            }
        });
    }
}
