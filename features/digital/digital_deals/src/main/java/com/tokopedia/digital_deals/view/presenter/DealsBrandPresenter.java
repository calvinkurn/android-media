package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetBrandDetailsUseCase;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsBrandDetailsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class DealsBrandPresenter extends BaseDaggerPresenter<DealsBrandDetailsContract.View>
        implements DealsBrandDetailsContract.Presenter {

    private GetBrandDetailsUseCase getBrandDetailsUseCase;
    private List<CategoryItemsViewModel> categoryViewModels;
    private BrandViewModel brandViewModel;
    public final String TAG="url";
    public final static String BRAND_DATA="brand_data";
    String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";


    @Inject
    public DealsBrandPresenter(GetBrandDetailsUseCase getBrandDetailsUseCase) {
        this.getBrandDetailsUseCase = getBrandDetailsUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getBrandDetailsUseCase.unsubscribe();
    }



    public void getBrandDetails() {

        getView().showProgressBar();
        getView().hideCollapsingHeader();
        getBrandDetailsUseCase.execute(getView().getParams(), new Subscriber<BrandDetailsDomain>() {

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
                        getBrandDetails();
                    }
                });
            }

            @Override
            public void onNext(BrandDetailsDomain dealEntity) {
                getView().hideProgressBar();
                getView().showCollapsingHeader();
                categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsViewModel(dealEntity.getDealItems());
                brandViewModel =Utils.getSingletonInstance().convertIntoBrandViewModel(dealEntity.getDealBrand());
                getView().renderBrandDetails(categoryViewModels, brandViewModel);
                CommonUtils.dumper("enter onNext");
            }
        });
    }



}
