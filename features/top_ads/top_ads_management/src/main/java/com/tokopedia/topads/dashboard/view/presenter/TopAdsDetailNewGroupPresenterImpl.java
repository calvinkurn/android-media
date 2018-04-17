package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

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

    public TopAdsDetailNewGroupPresenterImpl(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                             TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                             TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                             TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                             TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        super(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
        this.topAdsCreateNewGroupUseCase = topAdsCreateNewGroupUseCase;
        this.topAdsCreateDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
    }

    @Override
    public void saveAdExisting(String groupId, final List<TopAdsProductViewModel> topAdsProductViewModelList, final String source) {
        super.getDetailAd(groupId, new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
                // get the latest domain from API, then pass it to re-save it
                topAdsCreateDetailProductListUseCase.execute(
                        TopAdsCreateDetailProductListUseCase.createRequestParams(
                                topAdsDetailGroupDomainModel,
                                topAdsProductViewModelList,
                                source
                        ),
                        getSaveProductSubscriber()
                );
            }
        });
    }


    protected Subscriber<TopAdsDetailProductDomainModel> getSaveProductSubscriber(){
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
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
                getView().goToGroupDetail(domainModel.getGroupId());
            }
        };
    }


    @Override
    public void saveAdNew(String groupName,
                          TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                          List<TopAdsProductViewModel> topAdsProductViewModelList, String source) {
        topAdsCreateNewGroupUseCase.execute(
                TopAdsCreateNewGroupUseCase.createRequestParams(groupName, topAdsDetailProductViewModel, topAdsProductViewModelList, source),
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
                        getView().onSaveAdSuccess(topAdsDetailGroupViewModel);
                        getView().goToGroupDetail(String.valueOf(topAdsDetailGroupViewModel.getGroupId()));
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsCreateNewGroupUseCase.unsubscribe();
        topAdsCreateDetailProductListUseCase.unsubscribe();
    }

}