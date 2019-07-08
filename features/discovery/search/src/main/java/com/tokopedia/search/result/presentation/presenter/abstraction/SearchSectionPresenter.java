package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

public abstract class SearchSectionPresenter<T extends SearchSectionContract.View>
        extends BaseDaggerPresenter<T>
        implements SearchSectionContract.Presenter<T> {

    @Inject
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    public UseCase<DynamicFilterModel> getDynamicFilterUseCase;
    @Inject
    public SearchLocalCacheHandler searchLocalCacheHandler;

    protected void enrichWithAdditionalParams(RequestParams requestParams,
                                                       Map<String, String> additionalParams) {
        requestParams.putAllString(additionalParams);
    }

    protected Subscriber<DynamicFilterModel> getDynamicFilterSubscriber(final boolean shouldSaveToLocalDynamicFilterDb) {
        return new Subscriber<DynamicFilterModel>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                if(e != null) {
                    e.printStackTrace();
                }

                getView().renderFailRequestDynamicFilter();
            }

            @Override
            public void onNext(DynamicFilterModel dynamicFilterModel) {
                if(dynamicFilterModel == null) {
                    getView().renderFailRequestDynamicFilter();
                    return;
                }

                if(shouldSaveToLocalDynamicFilterDb && searchLocalCacheHandler != null) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(getView().getScreenNameId(), dynamicFilterModel);
                }

                getView().renderDynamicFilter(dynamicFilterModel);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        if(getDynamicFilterUseCase != null) getDynamicFilterUseCase.unsubscribe();
    }
}
