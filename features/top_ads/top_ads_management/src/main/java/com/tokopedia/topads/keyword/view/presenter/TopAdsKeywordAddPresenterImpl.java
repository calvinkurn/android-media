package com.tokopedia.topads.keyword.view.presenter;

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

    @Inject
    public TopAdsKeywordAddPresenterImpl(KeywordAddUseCase keywordAddUseCase) {
        this.keywordAddUseCase = keywordAddUseCase;
    }

    public void addKeyword (String groupId,
                            int keywordType,
                            ArrayList<String> keywordList){
        keywordAddUseCase.execute(
                KeywordAddUseCase.createRequestParam(groupId, keywordType, keywordList),
                getAddKeywordSubscriber());
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
