package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.keyword.domain.interactor.EditTopAdsKeywordDetailUseCase;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.view.mapper.TopAdsKeywordEditDetailMapper;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;

import rx.Subscriber;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailPresenterImpl extends TopAdsKeywordEditDetailPresenter {

    private final EditTopAdsKeywordDetailUseCase editTopadsKeywordDetailUseCase;
    private final TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;

    public TopAdsKeywordEditDetailPresenterImpl(EditTopAdsKeywordDetailUseCase editTopadsKeywordDetailUseCase,
                                                TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        this.editTopadsKeywordDetailUseCase = editTopadsKeywordDetailUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
    }

    @Override
    public void editTopAdsKeywordDetail(final KeywordAd model) {
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
                if (topAdsSourceTaggingModel != null){
                    source = topAdsSourceTaggingModel.getSource();
                }
                TopAdsKeywordEditDetailInputDomainModel domainModel = TopAdsKeywordEditDetailMapper.mapViewToDomain(model, source);
                RequestParams params = EditTopAdsKeywordDetailUseCase.generateRequestParam(domainModel);
                editTopadsKeywordDetailUseCase.execute(params, new EditTopAdsKeywordDetailSubscriber());
            }
        });
    }

    @Override
    public void unSubscribe() {
        editTopadsKeywordDetailUseCase.unsubscribe();
        topAdsGetSourceTaggingUseCase.unsubscribe();
    }

    private class EditTopAdsKeywordDetailSubscriber extends Subscriber<EditTopAdsKeywordDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showError(e);
            }
        }

        @Override
        public void onNext(EditTopAdsKeywordDetailDomainModel domainModel) {
            if (isViewAttached()) {
                KeywordAd viewModel = TopAdsKeywordEditDetailMapper.mapDomainToView(domainModel);
                getView().onSuccessEditTopAdsKeywordDetail(viewModel);
            }
        }
    }
}
