package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.search.result.presentation.presenter.subscriber.InitiateSearchSubscriber;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SearchPresenterTest {

    private static class MockInitiateSearchUseCase extends UseCase<InitiateSearchModel> {
        @Override
        public Observable<InitiateSearchModel> createObservable(RequestParams requestParams) {
            return null;
        }
    }

    private SearchContract.View searchContractView;
    private UseCase<InitiateSearchModel> initiateSearchModelUseCase;

    private SearchPresenter searchPresenter;

    @Before
    public void setUp() {
        searchPresenter = spy(new SearchPresenter());
        searchContractView = mock(SearchContract.View.class);
        initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);

        mockDependencyInjection();
    }

    private void mockDependencyInjection() {
        doAnswer(invocation -> {
            searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
            return null;
        }).when(searchPresenter).initInjector(searchContractView);
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NotInjected_ShouldThrowError() {
        searchPresenter.initiateSearch(anyMapOf(String.class, Object.class), anyBoolean());
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NoListener_ShouldThrowError() {
        searchPresenter.initInjector(searchContractView);
        searchPresenter.initiateSearch(anyMapOf(String.class, Object.class), anyBoolean());
    }

    @Test
    public void initiateSearch_GivenNulls_ShouldNotExecuteUseCase() {
        searchPresenter.initInjector(searchContractView);
        searchPresenter.setInitiateSearchListener(mock(InitiateSearchListener.class));
        searchPresenter.initiateSearch(eq(null), anyBoolean());

        verify(initiateSearchModelUseCase, never()).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @Test
    public void initiateSearch_GivenAnyParams_ShouldExecuteUseCase() {
        searchPresenter.initInjector(searchContractView);
        searchPresenter.setInitiateSearchListener(mock(InitiateSearchListener.class));
        doNothing().when(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));

        searchPresenter.initiateSearch(anyMapOf(String.class, Object.class), anyBoolean());

        verify(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @After
    public void tearDown() {
        searchContractView = null;
        initiateSearchModelUseCase = null;
        searchPresenter = null;
    }
}