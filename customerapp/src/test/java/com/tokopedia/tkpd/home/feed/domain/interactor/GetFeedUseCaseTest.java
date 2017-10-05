package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.view.FeedContract;
import com.tokopedia.tkpd.home.feed.view.FeedPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kulomady on 2/2/17.
 */
public class GetFeedUseCaseTest {


    @Mock
    GetAllFeedDataPageUseCase getAllFeedDataPageUseCase;
    @Mock
    LoadMoreFeedUseCase loadMoreFeedUseCase;
    @Mock
    FeedContract.View view;
    @Mock
    FeedRepository feedRepository;
    @Mock
    private PostExecutionThread postExecutionThread;

    private ThreadExecutor threadExecutor;
    private GetDataFeedCacheUseCase getDataFeedCacheUseCase;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
        threadExecutor = new JobExecutor();
    }
    @Test
    public void refreshDataFeed() throws Exception {

        doReturn(Observable.just(new Feed())).when(feedRepository).getFeedCache();
        doReturn(Observable.just(new ArrayList<>()))
                .when(feedRepository).getRecentViewProductFromCache();
        doReturn(Observable.just(new ArrayList<>())).when(feedRepository).getTopAdsCache();

        getDataFeedCacheUseCase = new GetDataFeedCacheUseCase(threadExecutor, postExecutionThread,
                feedRepository);

        FeedPresenter feedPresenter = new FeedPresenter(getAllFeedDataPageUseCase,
                getDataFeedCacheUseCase, loadMoreFeedUseCase);

        feedPresenter.attachView(view);
        feedPresenter.initializeDataFeed();
        verify(view).hideContentView();
        verify(view).hideRefreshLoading();
    }
}
