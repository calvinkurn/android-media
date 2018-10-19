package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.domain.DigitalDealsGetCategoriesUseCase;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
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
        digitalDealsGetCategoriesUseCase.execute(
                digitalDealsGetCategoriesUseCase.createRequestParam(
                        getView().getCartPassData().getCategoryId()
                ),
                new Subscriber<List<DealCategoryViewModel>>() {
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
                getView().showCheckoutView(getView().getCartPassData(), getView().getCartInfoData());
            }
        });
    }

    @Override
    public void onSelectDealProduct(DealProductViewModel viewModel) {
        if (getView().getSelectedDeals().size() >= 5) {
            getView().showErrorInRedSnackbar(R.string.digital_deals_maximum_error_message);
        } else {
            viewModel.setSelected(true);
            getView().getSelectedDeals().add(viewModel);
            getView().notifySelectedDeal();
        }
    }
}
