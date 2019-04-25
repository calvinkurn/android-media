package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchSubscriber;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchHelper;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchQueryHelper;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.usecase.RequestParams;

final class SearchPresenter extends BaseDaggerPresenter<SearchContract.View> implements SearchContract.Presenter {

    private GraphqlUseCase graphqlUseCase;
    private GqlSearchQueryHelper gqlSearchQueryHelper;

    SearchPresenter(GraphqlUseCase graphqlUseCase, GqlSearchQueryHelper gqlSearchQueryHelper) {
        this.graphqlUseCase = graphqlUseCase;
        this.gqlSearchQueryHelper = gqlSearchQueryHelper;
    }

    @Override
    public void onPause() {
        graphqlUseCase.unsubscribe();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void initiateSearch(SearchParameter searchParameter, boolean isForceSearch, InitiateSearchListener initiateSearchListener) {
        checkViewAttached();

        RequestParams requestParams = createInitiateSearchRequestParams(searchParameter, isForceSearch);

        GqlSearchHelper.initiateSearch(
                gqlSearchQueryHelper.getInitiateSearchQuery(),
                requestParams,
                graphqlUseCase,
                new InitiateSearchSubscriber(initiateSearchListener)
        );
    }

    private RequestParams createInitiateSearchRequestParams(SearchParameter searchParameter, boolean isForceSearch) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(searchParameter.getSearchParameterMap());

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putBoolean(SearchApiConst.REFINED, isForceSearch);
        requestParams.putBoolean(SearchApiConst.RELATED, true);

        return requestParams;
    }
}
