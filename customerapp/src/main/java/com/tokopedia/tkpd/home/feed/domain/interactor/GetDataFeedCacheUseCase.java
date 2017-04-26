package com.tokopedia.tkpd.home.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
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

public class GetDataFeedCacheUseCase extends UseCase<DataFeed> {
    private final FeedRepository feedRepository;


    public GetDataFeedCacheUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<DataFeed> createObservable(RequestParams requestParams) {

        return Observable.zip(
                getRecentProductObservable(),
                getFeedObservable(),
                new Func2<List<ProductFeed>, Feed, DataFeed>() {
                    @Override
                    public DataFeed call(List<ProductFeed> products,
                                         Feed feed) {

                        return getValidDataFeed(products, feed);
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
    private DataFeed getValidDataFeed(List<ProductFeed> products, Feed feed) {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setFeed(feed);
        dataFeed.setRecentProductList(products);
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
        return feedRepository.getRecentViewProductFromCache().onErrorReturn(historyErrorValue());
    }

    private Func1<Throwable, List<ProductFeed>> historyErrorValue() {
        return new Func1<Throwable, List<ProductFeed>>() {
            @Override
            public List<ProductFeed> call(Throwable throwable) {
                return Collections.emptyList();
            }
        };
    }

    private Observable<Feed> getFeedObservable() {
        return feedRepository.getFeedCache().onErrorReturn(feedErrorReturn());
    }

    private Func1<Throwable, Feed> feedErrorReturn() {
        return new Func1<Throwable, Feed>() {
            @Override
            public Feed call(Throwable throwable) {
                Feed feed = new Feed();
                feed.setIsValid(false);
                return feed;
            }
        };
    }

}
