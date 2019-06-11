package com.tokopedia.search.result.presentation.presenter.product;

import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.Subscriber;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// TODO:: Revamp this Test class. It should execute SearchProductUseCase and verify view methods is called.
// TODO:: See ShopListPresenterTest for comparison
public class ProductListPresenterTest {

    private static class MockSearchProductModelUseCase extends UseCase<SearchProductModel> {
        @Override
        public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
            return null;
        }
    }

    private static class MockSearchProductModelSubscriber extends Subscriber<SearchProductModel> {
        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) { }

        @Override
        public void onNext(SearchProductModel searchProductModel) { }
    }

    private UseCase<SearchProductModel> searchProductFirstPageUseCase = mock(MockSearchProductModelUseCase.class);
    private UseCase<SearchProductModel> searchProductLoadMoreUseCase = mock(MockSearchProductModelUseCase.class);
    private ProductListPresenter productListPresenter;

    @Before
    public void setUp() {
        productListPresenter = new ProductListPresenter();

        productListPresenter.attachView(mock(ProductListSectionContract.View.class));
        productListPresenter.searchProductFirstPageUseCase = searchProductFirstPageUseCase;
        productListPresenter.searchProductLoadMoreUseCase = searchProductLoadMoreUseCase;
    }

    @Test
    public void loadMoreData_givenNulls_shouldNotExecuteUseCase() {
        productListPresenter.loadMoreData(null, null);

        verify(searchProductLoadMoreUseCase, never()).execute(any(RequestParams.class), any(ProductListPresenterTest.MockSearchProductModelSubscriber.class));
    }

    @Test
    public void loadData_givenNulls_shouldNotExecuteUseCase() {
        productListPresenter.loadData(null, null, false);

        verify(searchProductFirstPageUseCase, never()).execute(any(RequestParams.class), any(ProductListPresenterTest.MockSearchProductModelSubscriber.class));
    }

    @After
    public void tearDown() {
        searchProductFirstPageUseCase = null;
        searchProductLoadMoreUseCase = null;
        productListPresenter = null;
    }
}