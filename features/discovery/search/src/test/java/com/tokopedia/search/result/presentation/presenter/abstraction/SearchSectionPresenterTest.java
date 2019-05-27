package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
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
import static org.mockito.Mockito.verify;

public class SearchSectionPresenterTest {

    private class TestSuccessGetDynamicFilterUseCase extends TestUseCase<DynamicFilterModel> {
        @Override
        public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
            return Observable.just(dynamicFilterModel);
        }
    }

    private class TestNullGetDynamicFilterUseCase extends TestUseCase<DynamicFilterModel> {
        @Override
        public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
            return Observable.just(null);
        }
    }

    private class TestErrorGetDynamicFilterUseCase extends TestUseCase<DynamicFilterModel> {
        @Override
        public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
            return Observable.error(new Exception("Mock exception, should be handled in Subscriber onError"));
        }
    }

    private class TestNullErrorGetDynamicFilterUseCase extends TestUseCase<DynamicFilterModel> {
        @Override
        public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
            return Observable.error(null);
        }
    }

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
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestSuccessGetDynamicFilterUseCase();
        searchSectionPresenterInjectDependencies(dynamicFilterModelUseCase);

        searchSectionPresenterSubclass.requestDynamicFilter(new HashMap<>(), true);

        verify(view).renderDynamicFilter(dynamicFilterModel);
        verify(searchLocalCacheHandler).saveDynamicFilterModelLocally(any(), any());
    }

    @Test
    public void requestDynamicFilterWithNull_ViewShouldRenderFail() {
        UseCase<DynamicFilterModel> dynamicFilterModelUseCase = new TestNullGetDynamicFilterUseCase();
    }
}