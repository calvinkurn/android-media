package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.domain.usecase.TestErrorUseCase;
import com.tokopedia.search.result.domain.usecase.TestUseCase;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SearchPresenterTest {

    private static abstract class MockInitiateSearchUseCase extends UseCase<InitiateSearchModel> { }
    private static abstract class MockInitiateSearchSubscriber extends Subscriber<InitiateSearchModel> { }

    private SearchContract.View searchView;
    private SearchPresenter searchPresenter;

    @Before
    public void setUp() {
        searchPresenter = new SearchPresenter();
        searchView = mock(SearchContract.View.class);
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NotInjected_ShouldThrowError() {
        searchPresenter.initiateSearch(new HashMap<>());
    }

    @Test(expected = RuntimeException.class)
    public void initiateSearch_NotAttachedView_ShouldThrowError() {
        searchPresenter.initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);
        searchPresenter.initiateSearch(new HashMap<>());
    }

    private void searchPresenterInjectDependencies(UseCase<InitiateSearchModel> initiateSearchModelUseCase) {
        searchPresenter.initiateSearchModelUseCase = initiateSearchModelUseCase;
        searchPresenter.attachView(searchView);
    }

    @Test
    public void initiateSearch_GivenNulls_ShouldNotExecuteUseCase() {
        UseCase<InitiateSearchModel> initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);
        searchPresenterInjectDependencies(initiateSearchModelUseCase);

        searchPresenter.initiateSearch(null);

        verify(initiateSearchModelUseCase, never()).execute(any(RequestParams.class), any(MockInitiateSearchSubscriber.class));
    }

    @Test
    public void initiateSearch_GivenAnyParams_ShouldExecuteUseCase() {
        UseCase<InitiateSearchModel> initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);
        searchPresenterInjectDependencies(initiateSearchModelUseCase);
        doNothing().when(initiateSearchModelUseCase).execute(any(RequestParams.class), any(MockInitiateSearchSubscriber.class));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(initiateSearchModelUseCase).execute(any(RequestParams.class), any(MockInitiateSearchSubscriber.class));
    }

    @Test
    public void initiateSearchNull_CallListenerHandleResponseError() {
        searchPresenterInjectDependencies(new TestUseCase<>(null));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleResponseError();
    }

    @Test
    public void initiateSearchSuccessWithNullSearchProduct_ShouldHandleResponseSearchWithHasCatalogFalse() {
        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(null);

        initiateSearchAndVerifyHandleResponseSearchWithHasCatalogFalse(initiateSearchModel);
    }

    @Test
    public void initiateSearchSuccessWithNullRedirectionAndCatalogList_ShouldHandleResponseSearchWithHasCatalogFalse() {
        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(
                new InitiateSearchModel.SearchProduct(null, null)
        );

        initiateSearchAndVerifyHandleResponseSearchWithHasCatalogFalse(initiateSearchModel);
    }

    @Test
    public void initiateSearchSuccessWithNullRedirectApplink_ShouldHandleReponseSearchWithHasCatalogFalse() {
        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(
                new InitiateSearchModel.SearchProduct(
                    new InitiateSearchModel.SearchProduct.Redirection(null),
                        null
                )
        );

        initiateSearchAndVerifyHandleResponseSearchWithHasCatalogFalse(initiateSearchModel);
    }

    @Test
    public void initiateSearchSuccessWithEmptyCatalogs_ShouldHandleResponseSearchWithHasCatalogFalse() {
        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(
                new InitiateSearchModel.SearchProduct(null, new ArrayList<>())
        );

        initiateSearchAndVerifyHandleResponseSearchWithHasCatalogFalse(initiateSearchModel);
    }

    private void initiateSearchAndVerifyHandleResponseSearchWithHasCatalogFalse(InitiateSearchModel initiateSearchModel) {
        searchPresenterInjectDependencies(new TestUseCase<>(initiateSearchModel));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleResponseSearch(false);
    }

    @Test
    public void initiateSearchSuccessWithSomeCatalogs_ShouldHandleResponseSearchWithHasCatalogTrue() {
        List<InitiateSearchModel.SearchProduct.Catalog> catalogList = new ArrayList<>();
        catalogList.add(new InitiateSearchModel.SearchProduct.Catalog());

        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(
                new InitiateSearchModel.SearchProduct(
                null, catalogList
        ));

        searchPresenterInjectDependencies(new TestUseCase<>(initiateSearchModel));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleResponseSearch(true);
    }

    @Test
    public void initiateSearchSuccessWithRandomApplinkString_ShouldHandleReponseApplink() {
        String redirectApplink = "some_random_string_or_could_also_be_real_applink";
        InitiateSearchModel initiateSearchModel = new InitiateSearchModel(
            new InitiateSearchModel.SearchProduct(
                new InitiateSearchModel.SearchProduct.Redirection(redirectApplink), null
            )
        );

        searchPresenterInjectDependencies(new TestUseCase<>(initiateSearchModel));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleApplink(redirectApplink);
    }

    @Test
    public void initiateSearchWithNullError_ShouldHandleResponseError() {
        searchPresenterInjectDependencies(new TestErrorUseCase<>(null));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleResponseError();
    }

    @Test
    public void initiateSearchWithException_ShouldHandleResponseError() {
        Exception exception = new Exception("Mock exception, should be handled in Subscriber onError");
        searchPresenterInjectDependencies(new TestErrorUseCase<>(exception));

        searchPresenter.initiateSearch(new HashMap<>());

        verify(searchView).initiateSearchHandleResponseError();
    }

    @Test
    public void onPause_NotInjected_ShouldNotError() {
        searchPresenter.onPause();
    }

    @Test
    public void onPause_AfterInjectUseCase_ShouldUnsubscribeAnyUseCase() {
        UseCase<InitiateSearchModel> initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);
        searchPresenterInjectDependencies(initiateSearchModelUseCase);
        doNothing().when(initiateSearchModelUseCase).unsubscribe();

        searchPresenter.onPause();

        verify(initiateSearchModelUseCase).unsubscribe();
    }

    @Test
    public void detachView_NotInjected_ShouldNotError() {
        searchPresenter.detachView();

        assert searchPresenter.getView() == null;
    }

    @Test
    public void detachView_AfterInjectUseCase_ShouldUnsubscribeAllUseCases() {
        UseCase<InitiateSearchModel> initiateSearchModelUseCase = mock(MockInitiateSearchUseCase.class);
        searchPresenterInjectDependencies(initiateSearchModelUseCase);
        doNothing().when(initiateSearchModelUseCase).unsubscribe();

        searchPresenter.detachView();

        assert searchPresenter.getView() == null;
        verify(initiateSearchModelUseCase).unsubscribe();
    }

    @Test
    public void onResume_NotInjected_ShouldNotError() {
        searchPresenter.onResume();
    }

    @Test
    public void onDestroy_NotInjected_ShouldNotError() {
        searchPresenter.onDestroy();
    }

    @After
    public void tearDown() {
        searchView = null;
        searchPresenter = null;
    }
}