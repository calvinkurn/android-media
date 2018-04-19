package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.topads.common.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.common.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordAddPresenterImpl extends TopAdsKeywordAddPresenter {

    private KeywordAddUseCase keywordAddUseCase;
    private TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;

    @Inject
    public TopAdsKeywordAddPresenterImpl(KeywordAddUseCase keywordAddUseCase,
                                         TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        this.keywordAddUseCase = keywordAddUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
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
                keywordAddUseCase.execute(
                        KeywordAddUseCase.createRequestParam(groupId, keywordType,
                                keywordList, topAdsSourceTaggingModel.getSource()),
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
