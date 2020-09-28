package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsBalanceCheckUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewGroupPresenterImpl<T extends TopAdsDetailNewGroupView>
        extends TopAdsDetailEditGroupPresenterImpl<T>
        implements TopAdsDetailNewGroupPresenter<T> {

    private TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase;
    private TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase;
    private TopAdsBalanceCheckUseCase topAdsBalanceCheckUseCase;
    private UserSessionInterface sessionInterface;

    public TopAdsDetailNewGroupPresenterImpl(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                             TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                             TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                             TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                             TopAdsMinimumBidUseCase minimumBidUseCase,
                                             TopAdsBalanceCheckUseCase topAdsBalanceCheckUseCase,
                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                             UserSessionInterface sessionInterface) {
        super(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase, topAdsProductListUseCase,
                minimumBidUseCase, topAdsGetSourceTaggingUseCase, sessionInterface);
        this.topAdsCreateNewGroupUseCase = topAdsCreateNewGroupUseCase;
        this.topAdsCreateDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
        this.sessionInterface = sessionInterface;
        this.topAdsBalanceCheckUseCase = topAdsBalanceCheckUseCase;
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
    public void saveAdExisting(String groupId, final List<TopAdsProductViewModel> topAdsProductViewModelList, final String source, TopAdsDepositResponse.Data topAdsDepositResponse) {
        super.getDetailAd(groupId, new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(final TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
                // get the latest domain from API, then pass it to re-save it
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
                        topAdsCreateDetailProductListUseCase.execute(
                                TopAdsCreateDetailProductListUseCase.createRequestParams(
                                        topAdsDetailGroupDomainModel,
                                        topAdsProductViewModelList,
                                        source
                                ),
                                getSaveProductSubscriber(topAdsDepositResponse)
                        );
                    }
                });
            }
        });
    }


    private Subscriber<TopAdsDetailProductDomainModel> getSaveProductSubscriber(TopAdsDepositResponse.Data topAdsDepositResponse) {
        return new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                if (topAdsDepositResponse.getTopadsDashboardDeposits().getData().getAmount() > 0)
                    domainModel.setEnoughDeposit(true);
                else
                    domainModel.setEnoughDeposit(false);
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
                getView().goToGroupDetail(domainModel.getGroupId());
            }
        };
    }


    @Override
    public void saveAdNew(final String groupName,
                          final TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                          final List<TopAdsProductViewModel> topAdsProductViewModelList, final String source, String shopId, TopAdsDepositResponse.Data topAdsDepositResponse) {

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
                topAdsCreateNewGroupUseCase.execute(
                        TopAdsCreateNewGroupUseCase.createRequestParams(groupName,
                                topAdsDetailProductViewModel, topAdsProductViewModelList,
                                source, shopId),
                        new Subscriber<TopAdsDetailGroupViewModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
                            }

                            @Override
                            public void onNext(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel) {
                                if (topAdsDepositResponse.getTopadsDashboardDeposits().getData().getAmount() > 0)
                                    topAdsDetailGroupViewModel.setEnoughDeposit(true);
                                else
                                    topAdsDetailGroupViewModel.setEnoughDeposit(false);
                                getView().onSaveAdSuccess(topAdsDetailGroupViewModel);
                                getView().goToGroupDetail(String.valueOf(topAdsDetailGroupViewModel.getGroupId()));
                            }
                        });
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsCreateNewGroupUseCase.unsubscribe();
        topAdsCreateDetailProductListUseCase.unsubscribe();
        topAdsBalanceCheckUseCase.unsubscribe();
        topAdsGetSourceTaggingUseCase.unsubscribe();
    }

}