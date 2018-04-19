package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.common.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl extends TopAdsDetailEditProductPresenterImpl<TopAdsDetailEditView> implements TopAdsDetailNewProductPresenter {

    TopAdsCreateDetailProductListUseCase topAdsSaveDetailProductListUseCase;
    TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                               TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                               TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                               TopAdsProductListUseCase topAdsProductListUseCase, TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                               TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
        this.topAdsSaveDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
    }

    @Override
    public void saveAd(final TopAdsDetailProductViewModel detailAd, final ArrayList<TopAdsProductViewModel> topAdsProductList, final String source) {
        topAdsGetSourceTaggingUseCase.execute(new Subscriber<TopAdsSourceTaggingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                topAdsSaveDetailProductListUseCase.execute(TopAdsCreateDetailProductListUseCase.createRequestParams(
                        TopAdDetailProductMapper.convertViewToDomainList(detailAd, topAdsProductList),
                        topAdsSourceTaggingModel.getSource()), getSubscriberSaveListAd());
            }
        });
    }

    private Subscriber<TopAdsDetailProductDomainModel> getSubscriberSaveListAd() {
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
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(topAdsDetailProductDomainModel));
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailProductListUseCase.unsubscribe();
    }
}