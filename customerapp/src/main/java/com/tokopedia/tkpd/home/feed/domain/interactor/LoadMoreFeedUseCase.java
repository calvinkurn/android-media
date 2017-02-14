package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author Kulomady on 12/8/16.
 */

public class LoadMoreFeedUseCase
        extends UseCase<DataFeed> {

    public static final String KEY_IS_INCLUDE_TOPADS = "isIncludeTopAds";

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private FeedRepository feedRepository;
    private final GetListShopIdUseCase getListShopIdUseCase;
    private GetTopAdsUseCase getTopAdsUseCase;

    public LoadMoreFeedUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               FeedRepository feedRepository,
                               GetListShopIdUseCase getListShopIdUseCase,
                               GetTopAdsUseCase getTopAdsUseCase) {

        super(threadExecutor, postExecutionThread);

        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;
        this.getListShopIdUseCase = getListShopIdUseCase;
        this.getTopAdsUseCase = getTopAdsUseCase;
    }

    @Override
    public Observable<DataFeed> createObservable(RequestParams requestParams) {
        boolean isIncludeTopAds = requestParams.getBoolean(KEY_IS_INCLUDE_TOPADS,false);
        requestParams.clearValue(KEY_IS_INCLUDE_TOPADS);
        if (isIncludeTopAds) {
            return Observable.zip(
                    getFeedObservable(requestParams),
                    getTopAdsObservable(),
                    new Func2<Feed, List<TopAds>, DataFeed>() {
                        @Override
                        public DataFeed call(Feed feed, List<TopAds> topAds) {
                            return getValidDataFeedWithTopAds(feed, topAds);
                        }
                    })
                    .onErrorReturn(new Func1<Throwable, DataFeed>() {
                        @Override
                        public DataFeed call(Throwable throwable) {
                            return getInvalidDataFeed();
                        }
                    });
        } else {
            return getFeedObservable(requestParams).map(new Func1<Feed, DataFeed>() {
                @Override
                public DataFeed call(Feed feed) {
                    return getValidDataFeed(feed);
                }
            }).onErrorReturn(new Func1<Throwable, DataFeed>() {
                @Override
                public DataFeed call(Throwable throwable) {
                    return getInvalidDataFeed();
                }
            });

        }
    }

    private DataFeed getInvalidDataFeed() {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setValid(false);
        return dataFeed;
    }

    private DataFeed getValidDataFeedWithTopAds(Feed feed, List<TopAds> topAds) {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setFeed(feed);
        dataFeed.setTopAds(topAds);
        dataFeed.setValid(true);
        return dataFeed;
    }

    private DataFeed getValidDataFeed(Feed feed) {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setFeed(feed);
        dataFeed.setValid(true);
        return dataFeed;
    }

    private Observable<Feed> getFeedObservable(final RequestParams requestParams) {
        final String emptyString = "";
        return getListShopIdUseCase
                .createObservable(RequestParams.EMPTY)
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
                        requestParams.getParameters()
                                .put(GetFeedUseCase.KEY_SHOP_ID, shopIdListInString);

                        return new GetFeedUseCase(threadExecutor,
                                postExecutionThread,
                                feedRepository)
                                .createObservable(requestParams);

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
        return getTopAdsUseCase.createObservable(getTopAdsDefaultParams())
                .onErrorReturn(new Func1<Throwable, List<TopAds>>() {
                    @Override
                    public List<TopAds> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Collections.emptyList();
                    }
                });
    }

    private RequestParams getTopAdsDefaultParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetTopAdsUseCase.KEY_PAGE,GetTopAdsUseCase.TOPADS_PAGE_DEFAULT_VALUE);
        params.putString(GetTopAdsUseCase.KEY_ITEM,GetTopAdsUseCase.TOPADS_ITEM_DEFAULT_VALUE);
        params.putString(GetTopAdsUseCase.KEY_SRC,GetTopAdsUseCase.SRC_PRODUCT_FEED);
        return params;
    }


}
