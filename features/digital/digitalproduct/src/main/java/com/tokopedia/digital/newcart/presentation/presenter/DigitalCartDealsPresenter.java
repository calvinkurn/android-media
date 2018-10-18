package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.newcart.domain.DigitalDealsGetCategoriesUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsContract;

import javax.inject.Inject;

public class DigitalCartDealsPresenter extends BaseDaggerPresenter<DigitalCartDealsContract.View> implements DigitalCartDealsContract.Presenter {
    private DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase;

    @Inject
    public DigitalCartDealsPresenter(DigitalDealsGetCategoriesUseCase digitalDealsGetCategoriesUseCase) {
        this.digitalDealsGetCategoriesUseCase = digitalDealsGetCategoriesUseCase;
    }
}
