package com.tokopedia.digital_deals.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital_deals.domain.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.view.contractor.DealDetailsAllRedeemLocationsContract;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

import javax.inject.Inject;

public class DealDetailsAllLocationsRedeemPresenter extends BaseDaggerPresenter<DealDetailsAllRedeemLocationsContract.View>
        implements DealDetailsAllRedeemLocationsContract.Presenter {



    @Inject
    public DealDetailsAllLocationsRedeemPresenter() {
    }


    @Override
    public void initialize(List<OutletViewModel> outletViewModelList) {
        getView().renderBrandDetails(outletViewModelList);
    }


    @Override
    public void onDestroy() {

    }
}
