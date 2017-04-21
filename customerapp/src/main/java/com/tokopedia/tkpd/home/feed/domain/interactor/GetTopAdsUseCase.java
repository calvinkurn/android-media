package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetTopAdsUseCase
        extends UseCase<List<TopAds>> {

    public static final String TOPADS_PAGE_DEFAULT_VALUE = "1";
    public static final String TOPADS_ITEM_DEFAULT_VALUE = "4";
    public static final String SRC_PRODUCT_FEED = "fav_product";
    public static final String KEY_ITEM = "item";
    public static final String KEY_SRC = "src";
    public static final String KEY_PAGE = "page";

    private FeedRepository feedRepository;


    public GetTopAdsUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }


    @Override
    public Observable<List<TopAds>> createObservable(RequestParams requestParams) {
        return feedRepository.getTopAds(requestParams.getParameters());
    }
}
