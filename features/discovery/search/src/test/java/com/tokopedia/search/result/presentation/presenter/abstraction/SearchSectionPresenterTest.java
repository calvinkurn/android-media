package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.domain.usecase.TestErrorUseCase;
import com.tokopedia.search.result.domain.usecase.TestUseCase;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.junit.Test;

import java.util.HashMap;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SearchSectionPresenterTest {

    private SearchSectionContract.View view = mock(SearchSectionContract.View.class);
    private DynamicFilterModel dynamicFilterModel = new DynamicFilterModel();
    private SearchLocalCacheHandler searchLocalCacheHandler = mock(SearchLocalCacheHandler.class);
    private TestSearchSectionPresenterSubclass searchSectionPresenterSubclass = new TestSearchSectionPresenterSubclass();

    private void searchSectionPresenterInjectDependencies(UseCase<DynamicFilterModel> dynamicFilterModelUseCase) {
        searchSectionPresenterSubclass.attachView(view);
        searchSectionPresenterSubclass.getDynamicFilterUseCase = dynamicFilterModelUseCase;
        searchSectionPresenterSubclass.searchLocalCacheHandler = searchLocalCacheHandler;
    }

    @Test
    public void requestDynamicFilterWithData_ViewShouldRenderDynamicFilter() {
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestUseCase<>(dynamicFilterModel);
        searchSectionPresenterInjectDependencies(dynamicFilterModelUseCase);

        searchSectionPresenterSubclass.requestDynamicFilter(new HashMap<>());

        verify(view).renderDynamicFilter(dynamicFilterModel);
        verify(searchLocalCacheHandler).saveDynamicFilterModelLocally(any(), any());
    }

    @Test
    public void requestDynamicFilterWithNull_ViewShouldRenderFail() {
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestUseCase<>(null);
        searchSectionPresenterInjectDependencies(dynamicFilterModelUseCase);

        searchSectionPresenterSubclass.requestDynamicFilter(new HashMap<>());

        verify(view).renderFailRequestDynamicFilter();
        verify(searchLocalCacheHandler, never()).saveDynamicFilterModelLocally(any(), any());
    }

    @Test
    public void requestDynamicFilterWithError_ViewShouldRenderFail() {
        Exception error = new Exception("Mock exception, should be handled in Subscriber onError");
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestErrorUseCase<>(error);
        searchSectionPresenterInjectDependencies(dynamicFilterModelUseCase);

        searchSectionPresenterSubclass.requestDynamicFilter(new HashMap<>());

        verify(view).renderFailRequestDynamicFilter();
        verify(searchLocalCacheHandler, never()).saveDynamicFilterModelLocally(any(), any());
    }

    @Test
    public void requestDynamicFilterWithNullError_ViewShouldRenderFail() {
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestErrorUseCase<>(null);
        searchSectionPresenterInjectDependencies(dynamicFilterModelUseCase);

        searchSectionPresenterSubclass.requestDynamicFilter(new HashMap<>());

        verify(view).renderFailRequestDynamicFilter();
        verify(searchLocalCacheHandler, never()).saveDynamicFilterModelLocally(any(), any());
    }
}