package com.tokopedia.tkpd.home.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * @author kulomady on 12/8/16.
 */

public class GetDataFeedCacheUseCase extends UseCase<DataFeed> {
    private final FeedRepository mFeedRepository;


    public GetDataFeedCacheUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread);
        mFeedRepository = feedRepository;
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
        return mFeedRepository.getRecentViewProductFromCache();
    }

    private Observable<Feed> getFeedObservable() {
        return mFeedRepository.getFeedCache();
    }

    private Observable<List<TopAds>> getTopAdsObservable() {
        return mFeedRepository.getTopAdsCache();
    }


}
