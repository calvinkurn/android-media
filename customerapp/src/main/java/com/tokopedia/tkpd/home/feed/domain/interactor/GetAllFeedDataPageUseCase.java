package com.tokopedia.tkpd.home.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author kulomady on 12/8/16.
 */

public class GetAllFeedDataPageUseCase extends UseCase<DataFeed> {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final FeedRepository feedRepository;
    private final GetRecentProductUsecase getRecentProductUsecase;
    private final GetListShopIdUseCase getListShopIdUseCase;

    public GetAllFeedDataPageUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     FeedRepository feedRepository,
                                     GetRecentProductUsecase getRecentProductUsecase,
                                     GetListShopIdUseCase getListShopIdUseCase) {

        super(threadExecutor, postExecutionThread);
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;
        this.getRecentProductUsecase = getRecentProductUsecase;
        this.getListShopIdUseCase = getListShopIdUseCase;
    }


    @Override
    public Observable<DataFeed> createObservable(RequestParams requestParams) {
        return Observable.zip(getRecentProductObservable(), getFeedObservable(),
                new Func2<List<ProductFeed>, Feed, DataFeed>() {
            @Override
            public DataFeed call(List<ProductFeed> productFeeds, Feed feed) {
                return getValidDataFeed(productFeeds, feed);
            }
        }).onErrorReturn(new Func1<Throwable, DataFeed>() {
            @Override
            public DataFeed call(Throwable throwable) {
                return getInvalidDataFeed();
            }
        });
    }

    @NonNull
    private DataFeed getValidDataFeed(List<ProductFeed> products, Feed feed) {
        DataFeed dataFeed = new DataFeed();
        if (products == null) {
            dataFeed.setRecentProductError(true);
            dataFeed.setRecentProductList(Collections.<ProductFeed>emptyList());
        } else {
            dataFeed.setRecentProductList(products);
        }
        if (feed == null) {
            dataFeed.setFeedError(true);
            dataFeed.setFeed(new Feed());
        } else {
            dataFeed.setFeed(feed);
        }
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
        return getRecentProductUsecase.createObservable(RequestParams.EMPTY)
                .onErrorReturn(new Func1<Throwable, List<ProductFeed>>() {
                    @Override
                    public List<ProductFeed> call(Throwable throwable) {
                        return null;
                    }
                });
    }

    private Observable<Feed> getFeedObservable() {
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

                        return new GetFeedUseCase(threadExecutor,
                                postExecutionThread,
                                feedRepository)
                                .createObservable(getFeedRequestParams(shopIdListInString))
                                .onErrorReturn(new Func1<Throwable, Feed>() {
                                    @Override
                                    public Feed call(Throwable throwable) {
                                        return null;
                                    }
                                });

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

    @NonNull
    private RequestParams getFeedRequestParams(String shopIdListInString) {
        RequestParams params = RequestParams.create();
        params.putString(GetFeedUseCase.KEY_ROWS,GetFeedUseCase.ROW_VALUE_DEFAULT);
        params.putString(GetFeedUseCase.KEY_START, GetFeedUseCase.START_VALUE_DEFAULT);
        params.putString((GetFeedUseCase.KEY_DEVICE), GetFeedUseCase.DEVICE_VALUE_DEFAULT);
        params.putString(GetFeedUseCase.KEY_OB, GetFeedUseCase.OB_VALUE_DEFAULT);
        params.putString(GetFeedUseCase.KEY_SHOP_ID,shopIdListInString);
        params.putBoolean(GetFeedUseCase.KEY_IS_FIRST_PAGE,true);
        return params;
    }



}
