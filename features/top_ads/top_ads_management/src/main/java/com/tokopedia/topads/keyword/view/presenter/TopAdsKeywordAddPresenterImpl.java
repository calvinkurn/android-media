package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsRemoveSourceTaggingUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordAddPresenterImpl extends TopAdsKeywordAddPresenter {

    private KeywordAddUseCase keywordAddUseCase;
    private TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;
    private TopAdsRemoveSourceTaggingUseCase topAdsRemoveSourceTaggingUseCase;

    @Inject
    public TopAdsKeywordAddPresenterImpl(KeywordAddUseCase keywordAddUseCase,
                                         TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                         TopAdsRemoveSourceTaggingUseCase topAdsRemoveSourceTaggingUseCase) {
        this.keywordAddUseCase = keywordAddUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
        this.topAdsRemoveSourceTaggingUseCase = topAdsRemoveSourceTaggingUseCase;
    }

    public void addKeyword (final String groupId,
                            final int keywordType,
                            final ArrayList<String> keywordList){
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
                keywordAddUseCase.execute(
                        KeywordAddUseCase.createRequestParam(groupId, keywordType,
                                keywordList, source),
                        getAddKeywordSubscriber());
            }
        });
    }

    private Subscriber<AddKeywordDomainModel> getAddKeywordSubscriber(){
        return new Subscriber<AddKeywordDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onFailedSaveKeyword(e);
                }
            }

            @Override
            public void onNext(AddKeywordDomainModel addKeywordDomainModel) {
                getView().onSuccessSaveKeyword();
            }
        };
    }

    public void unsubscribe(){
        keywordAddUseCase.unsubscribe();
    }
}
