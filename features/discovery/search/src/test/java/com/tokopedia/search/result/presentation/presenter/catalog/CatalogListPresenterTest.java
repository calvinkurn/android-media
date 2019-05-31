package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.domain.usecase.TestUseCase;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.mapper.CatalogViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatalogListPresenterTest {
    private static abstract class MockDynamicFilterModelUseCase extends UseCase<DynamicFilterModel> { }

    private class TestSuccessSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.just(searchCatalogModel);
        }
    }

    private class TestNullSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.just(null);
        }
    }

    private class TestRuntimeExceptionSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.error(new RuntimeException("Mock Runtime exception, should be handled in Subscriber onError"));
        }
    }

    private class TestMessageErrorExceptionSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.error(new MessageErrorException("Mock Message Error exception, should be handled in Subscriber onError"));
        }
    }

    private class TestIOExceptionSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.error(new IOException("Mock IO exception, should be handled in Subscriber onError"));
        }
    }

    private class TestErrorSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.error(new Exception("Mock exception, should be handled in Subscriber onError"));
        }
    }

    private class TestNullErrorSearchCatalogUseCase extends TestUseCase<SearchCatalogModel> {
        @Override
        public Observable<SearchCatalogModel> createObservable(RequestParams requestParams) {
            return Observable.error(null);
        }
    }

    private CatalogListSectionContract.View catalogListSectionView = mock(CatalogListSectionContract.View.class);
    private CatalogViewModelMapper catalogViewModelMapper = mock(CatalogViewModelMapper.class);
    private UseCase<DynamicFilterModel> dynamicFilterModelUseCase = mock(MockDynamicFilterModelUseCase.class);
    private UserSessionInterface userSession = mock(UserSessionInterface.class);
    private SearchCatalogModel searchCatalogModel = new SearchCatalogModel();

    private CatalogListPresenter searchCatalogPresenter = new CatalogListPresenter();

    private void searchCatalogPresenterInitInjector(UseCase<SearchCatalogModel> searchCatalogUseCase) {
        searchCatalogPresenter.attachView(catalogListSectionView);
        searchCatalogPresenter.searchCatalogUseCase = searchCatalogUseCase;
        searchCatalogPresenter.catalogViewModelMapper = catalogViewModelMapper;
        searchCatalogPresenter.getDynamicFilterUseCase = dynamicFilterModelUseCase;
        searchCatalogPresenter.userSession = userSession;
    }

    private void verifyViewStartLoading() {
        verify(catalogListSectionView).setTopAdsEndlessListener();
        verify(catalogListSectionView).showRefreshLayout();
    }

    private void verifyViewNotRenderingResult() {
        verify(catalogListSectionView, never()).renderListView(any());
        verify(catalogListSectionView, never()).renderShareURL(anyString());
        verify(catalogListSectionView, never()).setHasNextPage(anyBoolean());
        verify(catalogListSectionView, never()).unSetTopAdsEndlessListener();
    }

    private void verifyViewRenderingResult(List<Visitable> visitables, String shareUrl, boolean hasNextPage) {
        verify(catalogListSectionView).renderListView(visitables);
        verify(catalogListSectionView).renderShareURL(shareUrl);
        verify(catalogListSectionView).setHasNextPage(hasNextPage);
        verify(catalogListSectionView, !hasNextPage ? times(1) : never()).unSetTopAdsEndlessListener();
    }

    private void verifyViewFailing(boolean isErrorView, boolean isRetryInit, boolean isUnknown) {
        verify(catalogListSectionView, isErrorView ? times(1) : never()).renderErrorView(anyString());
        verify(catalogListSectionView, isRetryInit ? times(1) : never()).renderRetryInit();
        verify(catalogListSectionView, isUnknown ? times(1) : never()).renderUnknown();
    }

    private void verifyViewFinishing() {
        verify(catalogListSectionView).hideRefreshLayout();
    }

    @Test
    public void requestCatalogList_GivenNulls_ShouldRenderRetryInit() {
        searchCatalogPresenterInitInjector(new TestNullSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, true, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenSearchCatalogModel_ShouldRenderResultWithoutError() {
        List<Visitable> dummyListVisitable = new ArrayList<>();
        when(catalogViewModelMapper.mappingCatalogViewModelWithHeader(searchCatalogModel)).thenReturn(dummyListVisitable);
        searchCatalogPresenterInitInjector(new TestSuccessSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewRenderingResult(dummyListVisitable, "",false);
        verifyViewFailing(false, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenNullError_ShouldRenderUnknown() {
        searchCatalogPresenterInitInjector(new TestNullErrorSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, false, true);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void subscribe_GivenRuntimeException_ShouldRenderErrorView() {
        searchCatalogPresenterInitInjector(new TestRuntimeExceptionSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(true, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void subscribe_GivenMessageErrorException_ShouldRenderErrorView() {
        searchCatalogPresenterInitInjector(new TestMessageErrorExceptionSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(true, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void subscribe_GivenMessageIOException_ShouldRenderRetryInit() {
        searchCatalogPresenterInitInjector(new TestIOExceptionSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, true, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void subscribe_GivenMessageAnyException_ShouldRenderUnknown() {
        searchCatalogPresenterInitInjector(new TestErrorSearchCatalogUseCase());

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, false, true);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }
}