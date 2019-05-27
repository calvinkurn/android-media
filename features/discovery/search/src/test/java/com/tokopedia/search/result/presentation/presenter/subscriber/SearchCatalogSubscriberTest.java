package com.tokopedia.search.result.presentation.presenter.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SearchCatalogSubscriberTest {

    private CatalogListSectionContract.View catalogListSectionView;
    private SearchCatalogSubscriber searchCatalogSubscriber;

    @Before
    public void setUp() {
        catalogListSectionView = mock(CatalogListSectionContract.View.class);
        searchCatalogSubscriber = new SearchCatalogSubscriber(catalogListSectionView);
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

    private void verifyViewFinishing(boolean shouldGetDynamicFilter) {
        verify(catalogListSectionView, shouldGetDynamicFilter ? times(1) : never()).getDynamicFilter();
        verify(catalogListSectionView).hideRefreshLayout();
    }

    @Test
    public void subscribe_GivenNulls_ShouldRenderRetryInit() {
        Observable.<SearchCatalogModel>just(null).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, true, false);
        verifyViewFinishing(false);
    }

    @Test
    public void subscribe_GivenSearchCatalogModelWithoutCatalogList_ShouldRenderResultWithoutError() {
        SearchCatalogModel dummyResult = new SearchCatalogModel();
        dummyResult.catalogList = null;

        Observable.just(dummyResult).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewRenderingResult(new ArrayList<>(), "",false);
        verifyViewFailing(false, false, false);
        verifyViewFinishing(true);
    }

    @Test
    public void subscribe_GivenNullError_ShouldRenderUnknown() {
        Observable.<SearchCatalogModel>error(null).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verifyViewFailing(false, false, true);
        verifyViewFinishing(false);
    }

    @Test
    public void subscribe_GivenRuntimeException_ShouldRenderErrorView() {
        Exception exception = mock(RuntimeException.class);
        doNothing().when(exception).printStackTrace();
        Observable.<SearchCatalogModel>error(exception).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verify(exception).printStackTrace();
        verifyViewFailing(true, false, false);
        verifyViewFinishing(false);
    }

    @Test
    public void subscribe_GivenMessageErrorException_ShouldRenderErrorView() {
        Exception exception = mock(MessageErrorException.class);
        doNothing().when(exception).printStackTrace();
        Observable.<SearchCatalogModel>error(exception).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verify(exception).printStackTrace();
        verifyViewFailing(true, false, false);
        verifyViewFinishing(false);
    }

    @Test
    public void subscribe_GivenMessageIOException_ShouldRenderRetryInit() {
        Exception exception = mock(IOException.class);
        doNothing().when(exception).printStackTrace();
        Observable.<SearchCatalogModel>error(exception).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verify(exception).printStackTrace();
        verifyViewFailing(false, true, false);
        verifyViewFinishing(false);
    }

    @Test
    public void subscribe_GivenMessageAnyException_ShouldRenderUnknown() {
        Exception exception = mock(Exception.class);
        doNothing().when(exception).printStackTrace();
        Observable.<SearchCatalogModel>error(exception).subscribe(searchCatalogSubscriber);

        verifyViewStartLoading();
        verifyViewNotRenderingResult();
        verify(exception).printStackTrace();
        verifyViewFailing(false, false, true);
        verifyViewFinishing(false);
    }
}