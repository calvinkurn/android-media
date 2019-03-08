package com.tokopedia.tkpd.home.favorite.view;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.data.FavoriteDataRepository;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUtil;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.DataFavoriteMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
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
    private PagingHandler pagingHandler;
    @Mock
    private FavoriteContract.View view;
    @Mock
    private FavoriteDataRepository repository;
    @Mock
    private RequestParams requestParams;
    @Mock
    private DomainWishlist mockDomainWishlist;
    @Mock
    private FavoriteShop mockFavoriteShop;
    @Mock
    private TopAdsShop mockTopAdsShop;
    @Mock
    private PagingHandler.PagingHandlerModel mockPagingHandler;
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


    @SuppressWarnings("unchecked")
    @Test
    public void shouldShowMessageFailedWhenWishlistErrorOnInitialData() throws Exception {

        when(mockDomainWishlist.isNetworkError()).thenReturn(true);
        when(mockFavoriteShop.isNetworkError()).thenReturn(false);
        when(mockTopAdsShop.isNetworkError()).thenReturn(false);

        Observable mockWishlistObservableError = Observable.just(mockDomainWishlist);
        Observable mockFavObservable = Observable.just(mockFavoriteShop);
        Observable mocTopAdsObservable = Observable.just(mockTopAdsShop);

        when(repository.getWishlist(any(TKPDMapParam.class))).thenReturn(mockWishlistObservableError);
        when(repository.getFirstPageFavoriteShop(any(TKPDMapParam.class))).thenReturn(mockFavObservable);
        when(repository.getTopAdsShop(any(TKPDMapParam.class))).thenReturn(mocTopAdsObservable);

        favoritePresenter.loadInitialData();

        verify(view).hideRefreshLoading();
        verify(view).showWishlistFailedMessage();
        verify(view).showInitialDataPage(anyList());
    }


    private void initializeMock() {
        MockitoAnnotations.initMocks(this);
        when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
        JobExecutor jobExecutor = new JobExecutor();

        getTopAdsShopUseCase
                = new GetTopAdsShopUseCase(jobExecutor, postExecutionThread, repository);

        GetWishlistUtil getWishlistUtil
                = new GetWishlistUtil(repository);

        toggleFavouriteShopUseCase
                = new ToggleFavouriteShopUseCase(new GraphqlUseCase(), resources);

        getFavoriteShopUsecase
                = new GetFavoriteShopUsecase(jobExecutor, postExecutionThread, repository);

        getAllDataFavoriteUseCase = new GetAllDataFavoriteUseCase(context,
                jobExecutor, postExecutionThread, getFavoriteShopUsecase, getWishlistUtil,
                getTopAdsShopUseCase);

        getInitialDataPageUsecase = new GetInitialDataPageUsecase(context, jobExecutor, postExecutionThread,
                getFavoriteShopUsecase, getWishlistUtil, getTopAdsShopUseCase);

        getInitialDataPageUsecase = new GetInitialDataPageUsecase(context, jobExecutor, postExecutionThread,
                getFavoriteShopUsecase, getWishlistUtil, getTopAdsShopUseCase);
    }

}