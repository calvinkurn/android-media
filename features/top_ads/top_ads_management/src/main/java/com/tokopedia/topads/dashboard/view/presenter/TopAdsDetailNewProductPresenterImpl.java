package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsBalanceCheckUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl extends TopAdsDetailEditProductPresenterImpl<TopAdsDetailEditView> implements TopAdsDetailNewProductPresenter {

    private TopAdsCreateDetailProductListUseCase topAdsSaveDetailProductListUseCase;
    private TopAdsBalanceCheckUseCase topAdsBalanceCheckUseCase;
    private UserSessionInterface sessionInterface;

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                               TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                               TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                               TopAdsProductListUseCase topAdsProductListUseCase,
                                               TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                               TopAdsBalanceCheckUseCase topAdsBalanceCheckUseCase,
                                               TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                               TopAdsMinimumBidUseCase minimumBidUseCase,
                                               UserSessionInterface sessionInterface) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsProductListUseCase,
                topAdsGetSuggestionUseCase, topAdsBalanceCheckUseCase, topAdsGetSourceTaggingUseCase,
                minimumBidUseCase, sessionInterface);
        this.topAdsSaveDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
        this.topAdsBalanceCheckUseCase = topAdsBalanceCheckUseCase;
        this.sessionInterface = sessionInterface;
    }

    public void getBalance(Resources resources) {
        topAdsBalanceCheckUseCase.setQuery(resources);
        topAdsBalanceCheckUseCase.execute(TopAdsBalanceCheckUseCase.createRequestParams(Integer.parseInt(sessionInterface.getShopId())), new Subscriber<TopAdsDepositResponse.Data>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(TopAdsDepositResponse.Data topAdsDepositResponse) {
                getView().onBalanceCheck(topAdsDepositResponse);
            }
        });

    }

    @Override
    public void saveAd(final TopAdsDetailProductViewModel detailAd, final ArrayList<TopAdsProductViewModel> topAdsProductList, final String source, TopAdsDepositResponse.Data topAdsDepositResponse) {
        topAdsGetSourceTaggingUseCase.execute(new Subscriber<TopAdsSourceTaggingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                String source = TopAdsNetworkConstant.VALUE_SOURCE_ANDROID;
                if (topAdsSourceTaggingModel != null) {
                    source = topAdsSourceTaggingModel.getSource();
                }
                topAdsSaveDetailProductListUseCase.execute(TopAdsCreateDetailProductListUseCase.createRequestParams(
                        TopAdDetailProductMapper.convertViewToDomainList(detailAd, topAdsProductList),
                        source), getSubscriberSaveListAd(topAdsDepositResponse));
            }
        });
    }

    private Subscriber<TopAdsDetailProductDomainModel> getSubscriberSaveListAd(TopAdsDepositResponse.Data topAdsDepositResponse) {
        return new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel) {
                if (topAdsDepositResponse.getTopadsDashboardDeposits().getData().getAmount() > 0)
                    topAdsDetailProductDomainModel.setEnoughDeposit(true);
                else
                    topAdsDetailProductDomainModel.setEnoughDeposit(false);
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(topAdsDetailProductDomainModel));
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailProductListUseCase.unsubscribe();
        topAdsGetSourceTaggingUseCase.unsubscribe();
    }
}