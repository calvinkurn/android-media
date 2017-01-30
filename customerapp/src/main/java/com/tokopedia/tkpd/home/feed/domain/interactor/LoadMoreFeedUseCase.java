package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.DefaultParams;
import com.tokopedia.core.base.UseCaseWithParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author Kulomady on 12/8/16.
 */

public class LoadMoreFeedUseCase
        extends UseCaseWithParams<GetFeedUseCase.RequestParams, DataFeed> {


    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private FeedRepository mFeedRepository;
    private final GetListShopIdUseCase mGetListShopIdUseCase;
    private GetTopAdsUseCase mGetTopAdsUseCase;

    public LoadMoreFeedUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               FeedRepository feedRepository,
                               GetListShopIdUseCase getListShopIdUseCase,
                               GetTopAdsUseCase GetTopAdsUseCase) {

        super(threadExecutor, postExecutionThread);

        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mFeedRepository = feedRepository;
        mGetListShopIdUseCase = getListShopIdUseCase;
        mGetTopAdsUseCase = GetTopAdsUseCase;
    }


    @Override
    protected Observable<DataFeed> createObservable(GetFeedUseCase.RequestParams requestParams) {
        if (requestParams.isIncludeWithTopAds()) {
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

    private Observable<Feed> getFeedObservable(final GetFeedUseCase.RequestParams requestParams) {
        final String emptyString = "";
        return mGetListShopIdUseCase
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
                        requestParams.getValues()
                                .put(GetFeedUseCase.RequestParams.KEY_SHOP_ID, shopIdListInString);

                        return new GetFeedUseCase(mThreadExecutor,
                                mPostExecutionThread,
                                mFeedRepository)
                                .execute(requestParams);

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
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(TopAdsApi.PAGE, LoadMoreFeedUseCase.RequestParams.PARAM_TOPADS_PAGE_DEFAULT);
        params.put(TopAdsApi.ITEM, LoadMoreFeedUseCase.RequestParams.PARAM_TOPADS_ITEM_DEFAULT);
        params.put(TopAdsApi.SRC, TopAdsApi.SRC_PRODUCT_FEED);
        return mGetTopAdsUseCase.execute(new GetTopAdsUseCase.RequestParams())
                .onErrorReturn(new Func1<Throwable, List<TopAds>>() {
                    @Override
                    public List<TopAds> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Collections.emptyList();
                    }
                });
    }

    public static final class RequestParams implements DefaultParams {
        private final HashMap<String, String> values;
        private static final String PARAM_TOPADS_PAGE_DEFAULT = "1";
        private static final String PARAM_TOPADS_ITEM_DEFAULT = "4";

        public RequestParams(HashMap<String, String> values) {
            this.values = values;
        }

        public HashMap<String, String> getValues() {
            return values;
        }
    }


}
