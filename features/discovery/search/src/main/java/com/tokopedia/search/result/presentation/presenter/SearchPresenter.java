package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchSubscriber;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

final class SearchPresenter extends BaseDaggerPresenter<SearchContract.View> implements SearchContract.Presenter {

    @Inject
    GraphqlUseCase graphqlUseCase;

    @Inject
    GqlSpecification gqlInitiateSearchSpec;

    SearchPresenter(GraphqlUseCase graphqlUseCase, GqlSpecification gqlInitiateSearchSpec) {
        this.graphqlUseCase = graphqlUseCase;
        this.gqlInitiateSearchSpec = gqlInitiateSearchSpec;
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
    public void initiateSearch(Map<String, Object> searchParameter, InitiateSearchListener initiateSearchListener) {
        if(searchParameter == null || initiateSearchListener == null) return;

        RequestParams requestParams = createInitiateSearchRequestParams(searchParameter);

        GqlSearchHelper.initiateSearch(
                gqlInitiateSearchSpec.getQuery(),
                requestParams,
                graphqlUseCase,
                new InitiateSearchSubscriber(initiateSearchListener)
        );
    }

    private RequestParams createInitiateSearchRequestParams(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(searchParameter);

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putBoolean(SearchApiConst.RELATED, true);

        return requestParams;
    }
}
