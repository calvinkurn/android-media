package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.search.result.presentation.presenter.subscriber.InitiateSearchSubscriber;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

class SearchPresenter extends BaseDaggerPresenter<SearchContract.View> implements SearchContract.Presenter {

    @Inject
    UseCase<InitiateSearchModel> initiateSearchModelUseCase;

    private InitiateSearchListener initiateSearchListener;

    SearchPresenter() { }

    @Override
    public void initInjector(SearchContract.View view) {
        SearchPresenterComponent component = DaggerSearchPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Override
    public void setInitiateSearchListener(InitiateSearchListener initiateSearchListener) {
        this.initiateSearchListener = initiateSearchListener;
    }

    @Override
    public void onPause() {
        if(initiateSearchModelUseCase != null) initiateSearchModelUseCase.unsubscribe();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void initiateSearch(Map<String, Object> searchParameter, boolean isForceSearch) {
        initiateSearchCheckForNulls();
        if(searchParameter == null) return;

        RequestParams requestParams = createInitiateSearchRequestParams(searchParameter, isForceSearch);

        initiateSearchModelUseCase.execute(requestParams, new InitiateSearchSubscriber(initiateSearchListener));
    }

    private void initiateSearchCheckForNulls() {
        if(initiateSearchModelUseCase == null) throw new RuntimeException("UseCase<InitiateSearchModel> is not injected.");
        if(initiateSearchListener == null) throw new RuntimeException("InitiateSearchListener is not set.");
    }

    private RequestParams createInitiateSearchRequestParams(Map<String, Object> searchParameter, boolean isForceSearch) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(searchParameter);

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putBoolean(SearchApiConst.REFINED, isForceSearch);
        requestParams.putBoolean(SearchApiConst.RELATED, true);

        return requestParams;
    }
}
