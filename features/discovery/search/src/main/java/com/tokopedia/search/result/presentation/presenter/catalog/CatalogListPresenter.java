package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.search.result.presentation.presenter.subscriber.RefreshCatalogSubscriber;
import com.tokopedia.search.result.presentation.presenter.subscriber.RequestDynamicFilterSubscriber;
import com.tokopedia.search.result.presentation.presenter.subscriber.SearchCatalogLoadMoreSubscriber;
import com.tokopedia.search.result.presentation.presenter.subscriber.SearchCatalogSubscriber;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

final class CatalogListPresenter
        extends SearchSectionPresenter<CatalogListSectionContract.View>
        implements CatalogListSectionContract.Presenter {
    @Inject
    @Named(SearchConstant.SearchCatalog.SEARCH_CATALOG_USE_CASE)
    UseCase<SearchCatalogModel> searchCatalogUseCase;
    @Inject
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> getDynamicFilterUseCase;
    @Inject
    UserSessionInterface userSession;

    private RequestDynamicFilterListener requestDynamicFilterListener;

    @Override
    public void initInjector(CatalogListSectionContract.View view) {
        CatalogListPresenterComponent component = DaggerCatalogListPresenterComponent.builder()
                .baseAppComponent(getView().getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Override
    public void requestCatalogList() {
        RequestParams requestParams = generateParamSearchCatalog();
        getView().initTopAdsParamsByQuery(requestParams);
        searchCatalogUseCase.execute(requestParams, new SearchCatalogSubscriber(getView()));
    }

    private RequestParams generateParamSearchCatalog() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameterMap());
        requestParams.putString(SearchApiConst.Q, getView().getQueryKey());

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    private void setRequestParamsDefaultValues(RequestParams requestParams) {
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(SearchApiConst.START, String.valueOf(getView().getStartFrom()));
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.TERMS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_TERM);
        requestParams.putString(SearchApiConst.BREADCRUMB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_BREADCRUMB);
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.OB, requestParams.getString(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT));
    }

    @Override
    public void requestCatalogLoadMore() {
        searchCatalogUseCase.execute(generateParamSearchCatalog(), new SearchCatalogLoadMoreSubscriber(getView()));
    }

    @Override
    public void refreshSort() {
        if (getView().getDepartmentId() != null && !getView().getDepartmentId().isEmpty()) {
            searchCatalogUseCase.execute(
                    generateParamSearchCatalog(getView().getDepartmentId()),
                    new RefreshCatalogSubscriber(getView())
            );
        } else {
            searchCatalogUseCase.execute(
                    generateParamSearchCatalog(),
                    new RefreshCatalogSubscriber(getView())
            );
        }
    }

    private RequestParams generateParamSearchCatalog(String departmentId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameterMap());
        requestParams.putString(SearchApiConst.SC, departmentId);

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    @Override
    public void requestCatalogList(String departmentId) {
        if (getView() == null) {
            return;
        }
        RequestParams requestParams = generateParamSearchCatalog(departmentId);
        getView().initTopAdsParamsByCategory(requestParams);
        searchCatalogUseCase.execute(requestParams, new SearchCatalogSubscriber(getView()));
    }

    @Override
    public void requestCatalogLoadMore(String departmentId) {
        searchCatalogUseCase.execute(generateParamSearchCatalog(departmentId), new SearchCatalogLoadMoreSubscriber(getView()));
    }

    @Override
    public void requestDynamicFilter() {
        checkViewAttached();

        RequestParams requestParams = createRequestDynamicFilterParams();

        getDynamicFilterUseCase.execute(requestParams, new RequestDynamicFilterSubscriber(requestDynamicFilterListener));
    }

    private RequestParams createRequestDynamicFilterParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(generateParamsNetwork(requestParams));
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_CATALOG);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

        if (getView().getDepartmentId() != null && !getView().getDepartmentId().isEmpty()) {
            requestParams.putString(SearchApiConst.SC, getView().getDepartmentId());
        } else {
            requestParams.putString(SearchApiConst.Q, getView().getQueryKey());
            requestParams.putString(SearchApiConst.SC, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SC);
        }

        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);

        return requestParams;
    }

    private HashMap<String, String> generateParamsNetwork(RequestParams requestParams) {
        return new HashMap<>(
                AuthUtil.generateParamsNetwork(userSession.getUserId(),
                        userSession.getDeviceId(),
                        requestParams.getParamsAllValueInString()));
    }

    @Override
    public void detachView() {
        super.detachView();
        searchCatalogUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }
}
