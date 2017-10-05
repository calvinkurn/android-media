package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/8/16.
 */

public class LoadMoreFeedUseCase extends UseCase<DataFeed> {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private FeedRepository feedRepository;
    private final GetListShopIdUseCase getListShopIdUseCase;


    public LoadMoreFeedUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               FeedRepository feedRepository,
                               GetListShopIdUseCase getListShopIdUseCase) {

        super(threadExecutor, postExecutionThread);

        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;
        this.getListShopIdUseCase = getListShopIdUseCase;
    }

    @Override
    public Observable<DataFeed> createObservable(RequestParams requestParams) {
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

    private DataFeed getInvalidDataFeed() {
        DataFeed dataFeed = new DataFeed();
        dataFeed.setValid(false);
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


}
