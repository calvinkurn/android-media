package com.tokopedia.tkpd.home.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * @author kulomady on 12/8/16.
 */

public class GetAllFeedDataPageUseCase extends UseCase<DataFeed> {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final FeedRepository feedRepository;
    private final GetRecentProductUsecase getRecentProductUsecase;
    private final GetListShopIdUseCase getListShopIdUseCase;
    private final GetTopAdsUseCase getTopAdsUseCase;

    public GetAllFeedDataPageUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     FeedRepository feedRepository,
                                     GetRecentProductUsecase getRecentProductUsecase,
                                     GetListShopIdUseCase getListShopIdUseCase,
                                     GetTopAdsUseCase getTopAdsUseCase) {

        super(threadExecutor, postExecutionThread);
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;
        this.getRecentProductUsecase = getRecentProductUsecase;
        this.getListShopIdUseCase = getListShopIdUseCase;
        this.getTopAdsUseCase = getTopAdsUseCase;
    }

    @Override
    public Observable<DataFeed> createObservable() {

        return Observable.zip(
                getRecentProductObservable(),
                getFeedObservable(),
                getTopAdsObservable(),
                new Func3<List<ProductFeed>, Feed, List<TopAds>, DataFeed>() {
                    @Override
                    public DataFeed call(List<ProductFeed> products,
                                         Feed feed, List<TopAds> topAds) {

                        return getValidDataFeed(products, feed, topAds);
                    }
                }
        ).onErrorReturn(new Func1<Throwable, DataFeed>() {
            @Override
            public DataFeed call(Throwable throwable) {
                return getInvalidDataFeed();
            }
        });
    }


    @NonNull
    private DataFeed getValidDataFeed(List<ProductFeed> products, Feed feed, List<TopAds> topAds) {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setFeed(feed);
        dataFeed.setRecentProductList(products);
        dataFeed.setTopAds(topAds);
        dataFeed.setValid(true);
        return dataFeed;
    }

    @NonNull
    private DataFeed getInvalidDataFeed() {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setValid(false);
        return dataFeed;
    }

    private Observable<List<ProductFeed>> getRecentProductObservable() {
        return getRecentProductUsecase.execute()
                .onErrorReturn(new Func1<Throwable, List<ProductFeed>>() {
                    @Override
                    public List<ProductFeed> call(Throwable throwable) {
                        return Collections.emptyList();
                    }
                });
    }

    private Observable<Feed> getFeedObservable() {
        final String emptyString = "";
        return getListShopIdUseCase
                .execute()
                .map(new Func1<List<String>, String>() {
                    @Override
                    public String call(List<String> shopIdInList) {
                        return StringUtils.convertListToStringDelimiter(shopIdInList, ",");
                    }
                })
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        return emptyString;
                    }
                })
                .flatMap(new Func1<String, Observable<Feed>>() {
                    @Override
                    public Observable<Feed> call(String shopIdListInString) {

                        return new GetFeedUseCase(threadExecutor,
                                postExecutionThread,
                                feedRepository)
                                .execute(getFeedRequestParams(shopIdListInString));

                    }
                })
                .onErrorReturn(new Func1<Throwable, Feed>() {
                    @Override
                    public Feed call(Throwable throwable) {
                        Feed feed = new Feed();
                        feed.setIsValid(false);
                        return feed;
                    }
                });
    }

    private Observable<List<TopAds>> getTopAdsObservable() {
        return getTopAdsUseCase.execute(new GetTopAdsUseCase.RequestParams())
                .onErrorReturn(new Func1<Throwable, List<TopAds>>() {
                    @Override
                    public List<TopAds> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Collections.emptyList();
                    }
                });
    }

    @NonNull
    private GetFeedUseCase.RequestParams getFeedRequestParams(String shopIdListInString) {
        TKPDMapParam<String, String> params = GetFeedUseCase.RequestParams.defaultParamsValue();
        params.put(GetFeedUseCase.RequestParams.KEY_SHOP_ID, shopIdListInString);
        return new GetFeedUseCase.RequestParams(params);
    }







}
