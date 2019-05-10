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

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SearchPresenterTest {

    private static abstract class MockInitiateSearchUseCase extends UseCase<InitiateSearchModel> { }

    private SearchContract.View searchContractView;
    private UseCase<InitiateSearchModel> initiateSearchModelUseCase;

    private SearchPresenter searchPresenter;

    @Before
    public void setUp() {
        searchPresenter = new SearchPresenter();
        searchContractView = mock(SearchContract.View.class);
        initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);

//        mockDependencyInjection();
    }

    private void mockDependencyInjection() {
        doAnswer(invocation -> {
            searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
            return null;
        }).when(searchPresenter).initInjector(searchContractView);
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NotInjected_ShouldThrowError() {
        searchPresenter.initiateSearch(new HashMap<>(), false);
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NoListener_ShouldThrowError() {
        searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
        searchPresenter.initiateSearch(new HashMap<>(), false);
    }

    @Test
    public void initiateSearch_GivenNulls_ShouldNotExecuteUseCase() {
        searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
        searchPresenter.setInitiateSearchListener(mock(InitiateSearchListener.class));
        searchPresenter.initiateSearch(null, false);

        verify(initiateSearchModelUseCase, never()).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @Test
    public void initiateSearch_GivenAnyParams_ShouldExecuteUseCase() {
        searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
        searchPresenter.setInitiateSearchListener(mock(InitiateSearchListener.class));
        doNothing().when(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));

        searchPresenter.initiateSearch(new HashMap<>(), false);

        verify(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @Test
    public void onPause_NotInjected_ShouldNotError() {
        searchPresenter.onPause();

        verify(initiateSearchModelUseCase, never()).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @Test
    public void onPause_AfterInjectUseCase_ShouldUnsubscribeAnyUseCase() {
        searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
        doNothing().when(initiateSearchModelUseCase).unsubscribe();

        searchPresenter.onPause();

        verify(initiateSearchModelUseCase).unsubscribe();
    }

    @After
    public void tearDown() {
        searchContractView = null;
        initiateSearchModelUseCase = null;
        searchPresenter = null;
    }
}