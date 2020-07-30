package com.tokopedia.tkpd.home.favorite.view;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.data.FavoriteDataRepository;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.DataFavoriteMapper;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * @author madi on 3/27/17.
 */
public class FavoritePresenterTest {

//    @Rule
//    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();


//    @Mock
//    GetAllFeedDataPageUseCase getAllFeedDataPageUseCase;
//    @Mock
//    LoadMoreFeedUseCase loadMoreFeedUseCase;
//    @Mock
//    FeedContract.View view;
//    @Mock
//    FeedRepository feedRepository;
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//    @Test
//    public void refreshDataFeed() throws Exception {
//        PostExecutionThread postExecutionThread = new PostExecutionThread() {
//            @Override
//            public Scheduler getScheduler() {
//                return Schedulers.immediate();
//            }
//        };
//        ThreadExecutor threadExecutor = new ThreadExecutor() {
//            @Override
//            public void execute(@NonNull Runnable command) {
//                command.run();
//            }
//        };
//        Feed feed = new Feed();
//        doReturn(Observable.just(feed)).when(feedRepository).getFeedCache();
//        List<ProductFeed> productFeedList = new ArrayList<>();
//        doReturn(Observable.just(productFeedList)).when(feedRepository).getRecentViewProductFromCache();
//        List<TopAds> topAdsList = new ArrayList<>();
//        doReturn(Observable.just(topAdsList)).when(feedRepository).getTopAdsCache();
//        GetDataFeedCacheUseCase getDataFeedCacheUseCase =
//                new GetDataFeedCacheUseCase(threadExecutor, postExecutionThread,
//                        feedRepository);
//        FeedPresenter feedPresenter = new FeedPresenter(getAllFeedDataPageUseCase,
//                getDataFeedCacheUseCase, loadMoreFeedUseCase);
//        feedPresenter.attachView(view);
//        feedPresenter.initializeDataFeed();
//        verify(view).hideContentView();
//        verify(view).hideRefreshLoading();
//    }
//}

    @Mock
    private Context context;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private DataFavoriteMapper favoriteMapper;
    @Mock
    private FavoriteContract.View view;
    @Mock
    private FavoriteDataRepository repository;
    @Mock
    private RequestParams requestParams;
    @Mock
    private FavoriteShop mockFavoriteShop;
    @Mock
    private TopAdsShop mockTopAdsShop;
    @Mock
    private Resources resources;

    private GetTopAdsShopUseCase getTopAdsShopUseCase;
    private GetInitialDataPageUsecase getInitialDataPageUsecase;
    private ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
    private GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;
    private GetFavoriteShopUsecase getFavoriteShopUsecase;

    private FavoritePresenter favoritePresenter;


    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
        initializeMock();
        favoritePresenter = new FavoritePresenter(
                getInitialDataPageUsecase,
                getTopAdsShopUseCase,
                toggleFavouriteShopUseCase,
                getAllDataFavoriteUseCase,
                getFavoriteShopUsecase,
                favoriteMapper);
        favoritePresenter.attachView(view);
    }

    private void initializeMock() {
        MockitoAnnotations.initMocks(this);
        when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
        JobExecutor jobExecutor = new JobExecutor();

        getTopAdsShopUseCase
                = new GetTopAdsShopUseCase(jobExecutor, postExecutionThread, repository);

        toggleFavouriteShopUseCase
                = new ToggleFavouriteShopUseCase(new GraphqlUseCase(), resources);

        getFavoriteShopUsecase
                = new GetFavoriteShopUsecase(jobExecutor, postExecutionThread, repository);

        getAllDataFavoriteUseCase = new GetAllDataFavoriteUseCase(context,
                jobExecutor, postExecutionThread, getFavoriteShopUsecase,
                getTopAdsShopUseCase);

        getInitialDataPageUsecase = new GetInitialDataPageUsecase(context, jobExecutor, postExecutionThread,
                getFavoriteShopUsecase, getTopAdsShopUseCase);

        getInitialDataPageUsecase = new GetInitialDataPageUsecase(context, jobExecutor, postExecutionThread,
                getFavoriteShopUsecase, getTopAdsShopUseCase);
    }

}