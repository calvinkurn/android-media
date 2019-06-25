package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.mapper.CatalogViewModelMapper;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

final class CatalogListPresenter
        extends SearchSectionPresenter<CatalogListSectionContract.View>
        implements CatalogListSectionContract.Presenter {

    @Inject
    @Named(SearchConstant.SearchCatalog.SEARCH_CATALOG_USE_CASE)
    UseCase<SearchCatalogModel> searchCatalogUseCase;
    @Inject
    CatalogViewModelMapper catalogViewModelMapper;
    @Inject
    UserSessionInterface userSession;

    private boolean isSearchCatalogReturnedNull = false;

    @Override
    public void initInjector(CatalogListSectionContract.View view) {
        CatalogListPresenterComponent component = DaggerCatalogListPresenterComponent.builder()
                .baseAppComponent(getView().getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Override
    public void requestCatalogList() {
        checkViewAttached();

        RequestParams requestParams = generateParamSearchCatalog();
        getView().initTopAdsParams(requestParams);

        searchCatalogUseCase.unsubscribe();
        searchCatalogUseCase.execute(requestParams, getSearchCatalogSubscriber());
    }

    private RequestParams generateParamSearchCatalog() {
        RequestParams requestParams = RequestParams.create();

        setRequestParamsDefaultValues(requestParams);
        requestParams.putAll(getView().getSearchParameterMap());
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

    private Subscriber<SearchCatalogModel> getSearchCatalogSubscriber() {
        return new Subscriber<SearchCatalogModel>() {
            @Override
            public void onStart() {
                searchCatalogSubscriberOnStart();
            }

            @Override
            public void onNext(SearchCatalogModel searchCatalogModel) {
                searchCatalogSubscriberOnNext(searchCatalogModel);
            }

            @Override
            public void onCompleted() {
                searchCatalogSubscriberOnCompleted();
            }

            @Override
            public void onError(Throwable e) {
                searchCatalogSubscriberOnError(e);
            }
        };
    }

    private void searchCatalogSubscriberOnStart() {
        getView().setTopAdsEndlessListener();
        getView().showRefreshLayout();
    }

    private void searchCatalogSubscriberOnNext(SearchCatalogModel searchCatalogModel) {
        if(searchCatalogModel == null) {
            isSearchCatalogReturnedNull = true;
            getView().renderRetryInit();
            return;
        }

        getView().renderListView(catalogViewModelMapper.mappingCatalogViewModelWithHeader(searchCatalogModel));
        getView().renderShareURL(searchCatalogModel.shareURL);
        getView().setHasNextPage(isHasNextPage(searchCatalogModel.paging.uriNext));
        if (!isHasNextPage(searchCatalogModel.paging.uriNext)) {
            getView().unSetTopAdsEndlessListener();
        }
    }

    private boolean isHasNextPage(String uriNext) {
        return uriNext != null && !uriNext.isEmpty();
    }

    private void searchCatalogSubscriberOnCompleted() {
        if(!isSearchCatalogReturnedNull) requestDynamicFilter(getView().getSearchParameterMap());
        getView().hideRefreshLayout();
    }

    private void searchCatalogSubscriberOnError(Throwable e) {
        if(e == null) {
            getView().renderUnknown();
        }
        else {
            e.printStackTrace();

            if (e instanceof MessageErrorException || e instanceof RuntimeException) {
                getView().renderErrorView(e.getMessage());
            } else if (e instanceof IOException) {
                getView().renderRetryInit();
            } else {
                getView().renderUnknown();
            }
        }

        getView().hideRefreshLayout();
    }

    @Override
    public void requestCatalogLoadMore() {
        searchCatalogUseCase.unsubscribe();

        searchCatalogUseCase.execute(generateParamSearchCatalog(), getSearchCatalogLoadMoreSubscriber());
    }

    private Subscriber<SearchCatalogModel> getSearchCatalogLoadMoreSubscriber() {
        return new Subscriber<SearchCatalogModel>() {
            @Override
            public void onNext(SearchCatalogModel searchCatalogModel) {
                searchCatalogLoadMoreSubscriberOnNext(searchCatalogModel);
            }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                searchCatalogLoadMoreSubscriberOnError(e);
            }
        };
    }

    private void searchCatalogLoadMoreSubscriberOnNext(SearchCatalogModel searchCatalogModel) {
        getView().renderNextListView(catalogViewModelMapper.mappingCatalogViewModelWithoutHeader(searchCatalogModel));
        getView().setHasNextPage(isHasNextPage(searchCatalogModel.paging.uriNext));
        if (!isHasNextPage(searchCatalogModel.paging.uriNext)) {
            getView().unSetTopAdsEndlessListener();
        }
    }

    private void searchCatalogLoadMoreSubscriberOnError(Throwable e) {
        if (e instanceof MessageErrorException) {
            getView().renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeException) {
            getView().renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            getView().renderRetryInit();
        } else {
            getView().renderUnknown();
            e.printStackTrace();
        }
        getView().hideRefreshLayout();
    }

    @Override
    public void refreshSort() {
        searchCatalogUseCase.unsubscribe();
        searchCatalogUseCase.execute(generateParamSearchCatalog(), getRefreshCatalogSubscriber());
    }

    private Subscriber<SearchCatalogModel> getRefreshCatalogSubscriber() {
        return new Subscriber<SearchCatalogModel>() {
            @Override
            public void onStart() {
                searchCatalogSubscriberOnStart();
            }

            @Override
            public void onNext(SearchCatalogModel searchCatalogModel) {
                refreshCatalogSubscriberOnNext(searchCatalogModel);
            }

            @Override
            public void onCompleted() {
                searchCatalogSubscriberOnCompleted();
            }

            @Override
            public void onError(Throwable e) {
                searchCatalogSubscriberOnError(e);
            }
        };
    }

    private void refreshCatalogSubscriberOnNext(SearchCatalogModel searchCatalogModel) {
        if(searchCatalogModel == null) {
            isSearchCatalogReturnedNull = true;
            getView().renderRetryInit();
            return;
        }

        getView().successRefreshCatalog(catalogViewModelMapper.mappingCatalogViewModelWithHeader(searchCatalogModel));
        getView().renderShareURL(searchCatalogModel.shareURL);
        getView().setHasNextPage(isHasNextPage(searchCatalogModel.paging.uriNext));
        if (!isHasNextPage(searchCatalogModel.paging.uriNext)) {
            getView().unSetTopAdsEndlessListener();
        }
    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameterMap) {
        checkViewAttached();

        RequestParams requestParams = createRequestDynamicFilterParams(searchParameterMap);

        getDynamicFilterUseCase.execute(requestParams, getDynamicFilterSubscriber(true));
    }

    private RequestParams createRequestDynamicFilterParams(Map<String, Object> searchParameterMap) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(searchParameterMap);
        requestParams.putAllString(generateParamsNetwork(requestParams));
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_CATALOG);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

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
        if(searchCatalogUseCase != null) searchCatalogUseCase.unsubscribe();
    }
}
