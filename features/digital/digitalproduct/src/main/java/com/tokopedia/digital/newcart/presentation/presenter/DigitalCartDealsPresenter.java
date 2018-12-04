package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.newcart.domain.DigitalDealsGetCategoriesUseCase;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsContract;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class DigitalCartDealsPresenter extends BaseDaggerPresenter<DigitalCartDealsContract.View> implements DigitalCartDealsContract.Presenter {
    private DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase;
    private DigitalAnalytics digitalAnalytics;

    @Inject
    public DigitalCartDealsPresenter(DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase,
                                     DigitalAnalytics digitalAnalytics) {
        this.digitalDealsGetCategoriesUseCase = digitalDealsGetCategoriesUseCase;
        this.digitalAnalytics = digitalAnalytics;
    }

    @Override
    public void onViewCreated() {
        getView().renderGetCategoriesLoading();
        getView().hideDealsPage();
        digitalAnalytics.eventMulticheckoutDeal(getView().getCartInfoData());
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
                            getView().renderGetCategoriesError(e.getMessage());
                            getView().hideGetCategoriesLoading();
                        }
                    }

                    @Override
                    public void onNext(List<DealCategoryViewModel> dealCategoryViewModels) {
                        getView().hideGetCategoriesLoading();
                        getView().showDealsPage();
                        getView().renderCheckoutView(getView().getCartPassData(), getView().getCartInfoData());
                        getView().renderDealsTab(dealCategoryViewModels);

                        if (!getView().isOnboardAlreadyShown()) {
                            getView().renderOnboarding();
                        }
                    }
                });

    }

    @Override
    public void onSelectDealProduct(DealProductViewModel viewModel, int currentFragmentPosition) {
        if (getView().getSelectedDeals().size() >= 5) {
            digitalAnalytics.eventDealMaximalError();
            getView().renderErrorInRedSnackbar(R.string.digital_deals_maximum_error_message);
        } else {
            viewModel.setSelected(true);
            getView().getSelectedDeals().add(viewModel);
            getView().notifySelectedDeal();
            getView().notifySelectedDealsInCheckout(viewModel);
            getView().updateSelectedDeal(currentFragmentPosition, viewModel);
        }
    }

    @Override
    public void unSelectDealFromCheckoutView(DealProductViewModel viewModel) {
        int indexOf = getView().getSelectedDeals().indexOf(viewModel);
        if (indexOf != -1) {
            viewModel.setSelected(false);
            getView().getSelectedDeals().remove(indexOf);
            Integer fragmentPosition = getView().getSelectedDealsMap().get(viewModel);
            getView().notifyAdapterInSpecifyFragment(fragmentPosition);
            getView().getSelectedDealsMap().remove(viewModel);
        }
    }

    @Override
    public void onDealsTabSelected(CharSequence dealCategory) {
        digitalAnalytics.eventSelectDeal(dealCategory);
    }


}
