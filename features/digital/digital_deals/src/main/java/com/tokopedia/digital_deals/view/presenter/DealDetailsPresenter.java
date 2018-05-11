package com.tokopedia.digital_deals.view.presenter;


import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;

import javax.inject.Inject;

import rx.Subscriber;


public class DealDetailsPresenter extends BaseDaggerPresenter<DealDetailsContract.View>
        implements DealDetailsContract.Presenter {

    private GetDealDetailsUseCase getBrandDetailsUseCase;
    private DealsDetailsViewModel dealsDetailsViewModel;
    public static final String HOME_DATA = "home_data";
    public final String TAG = "url";
    String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";


    @Inject
    public DealDetailsPresenter(GetDealDetailsUseCase getDealDetailsUseCase) {
        this.getBrandDetailsUseCase = getDealDetailsUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getBrandDetailsUseCase.unsubscribe();
    }

    public void getDealDetails() {

        getView().showProgressBar();
        getView().hideCollapsingHeader();
        getBrandDetailsUseCase.execute(getView().getParams(), new Subscriber<DealsDetailsDomain>() {

            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                getView().hideCollapsingHeader();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getDealDetails();
                    }
                });
            }

            @Override
            public void onNext(DealsDetailsDomain dealEntity) {
                getView().hideProgressBar();
                getView().showShareButton();
                getView().showCollapsingHeader();
                dealsDetailsViewModel = Utils.getSingletonInstance()
                        .convertIntoDealDetailsViewModel(dealEntity);

                getView().renderBrandDetails(dealsDetailsViewModel);
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_share) {

        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }


}
