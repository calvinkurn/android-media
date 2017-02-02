package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetFeedUseCase extends UseCase<Feed> {

    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_ROWS = "rows";
    public static final String KEY_START = "start";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_OB = "ob";
    public static final String KEY_IS_FIRST_PAGE = "isFirstPage";
    public static final String ROW_VALUE_DEFAULT = "12";
    public static final String START_VALUE_DEFAULT = "0";
    public static final String DEVICE_VALUE_DEFAULT = "android";
    public static final String OB_VALUE_DEFAULT = "10";
    private final FeedRepository feedRepository;

    GetFeedUseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread,
                   FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;

    }

    @Override
    public Observable<Feed> createObservable(RequestParams requestParams) {
        boolean isFirstPage = requestParams.getBoolean(KEY_IS_FIRST_PAGE, false);
        requestParams.clearValue(KEY_IS_FIRST_PAGE);
        return feedRepository.getFeed(isFirstPage,requestParams.getParameters());
    }

}
