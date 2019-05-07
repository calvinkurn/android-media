package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.presentation.presenter.subscriber.InitiateSearchSubscriber;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {

    @Mock
    UseCase<InitiateSearchModel> initiateSearchModelUseCase;

    private SearchPresenter searchPresenter;

    @Before
    public void setUp() throws Exception {
        searchPresenter = new SearchPresenter(initiateSearchModelUseCase);
    }

    @Test
    public void initiateSearch_GivenNulls_ShouldNotExecuteUseCase() {
        searchPresenter.initiateSearch(null, false, null);

        verify(initiateSearchModelUseCase, never()).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @Test
    public void initiateSearch_GivenAnyParams_ShouldExecuteUseCase() {
        doNothing().when(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));

        searchPresenter.initiateSearch(new HashMap<>(), false, any(InitiateSearchListener.class));

        verify(initiateSearchModelUseCase).execute(any(RequestParams.class), any(InitiateSearchSubscriber.class));
    }

    @After
    public void tearDown() throws Exception {
        initiateSearchModelUseCase = null;
        searchPresenter = null;
    }
}