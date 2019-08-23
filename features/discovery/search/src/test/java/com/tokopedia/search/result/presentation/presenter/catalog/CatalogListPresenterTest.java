package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.domain.usecase.TestErrorUseCase;
import com.tokopedia.search.result.domain.usecase.TestUseCase;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.mapper.CatalogViewModelMapper;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatalogListPresenterTest {

    private static abstract class MockSearchCatalogUseCase extends UseCase<SearchCatalogModel> { }
    private static abstract class MockDynamicFilterModelUseCase extends UseCase<DynamicFilterModel> { }

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
        searchCatalogPresenterInitInjector(new TestUseCase<>(null));

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
        searchCatalogPresenterInitInjector(new TestUseCase<>(searchCatalogModel));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewRenderingResult(dummyListVisitable, "",false);
        verifyViewFailing(false, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenNullError_ShouldRenderUnknown() {
        searchCatalogPresenterInitInjector(new TestErrorUseCase<>(null));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, false, true);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenRuntimeException_ShouldRenderErrorView() {
        RuntimeException runtimeException = new RuntimeException("Mock Runtime exception, should be handled in Subscriber onError");
        searchCatalogPresenterInitInjector(new TestErrorUseCase<>(runtimeException));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(true, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenMessageErrorException_ShouldRenderErrorView() {
        MessageErrorException messageErrorException = new MessageErrorException("Mock Message Error exception, should be handled in Subscriber onError");
        searchCatalogPresenterInitInjector(new TestErrorUseCase<>(messageErrorException));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(true, false, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenMessageIOException_ShouldRenderRetryInit() {
        IOException ioException = new IOException("Mock IO exception, should be handled in Subscriber onError");
        searchCatalogPresenterInitInjector(new TestErrorUseCase<>(ioException));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, true, false);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void requestCatalogList_GivenMessageAnyException_ShouldRenderUnknown() {
        Exception exception = new Exception("Mock exception, should be handled in Subscriber onError");
        searchCatalogPresenterInitInjector(new TestErrorUseCase<>(exception));

        searchCatalogPresenter.requestCatalogList();

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, false, true);
        verifyViewFinishing();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void detachView_NotInjected_ShouldNotError() {
        searchCatalogPresenter.detachView();

        assert searchCatalogPresenter.getView() == null;
    }

    @Test
    public void detachView_AfterInjectUseCase_ShouldUnsubscribeAllUseCases() {
        UseCase<SearchCatalogModel> searchCatalogUseCase = mock(MockSearchCatalogUseCase.class);
        searchCatalogPresenterInitInjector(searchCatalogUseCase);

        searchCatalogPresenter.detachView();

        assert searchCatalogPresenter.getView() == null;
        verify(searchCatalogUseCase).unsubscribe();
        verify(dynamicFilterModelUseCase).unsubscribe();
    }
}