package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.DefaultParams;
import com.tokopedia.core.base.UseCaseWithParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetFeedUseCase extends UseCaseWithParams<GetFeedUseCase.RequestParams, Feed> {

    private final FeedRepository mFeedRepository;


    GetFeedUseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread,
                   FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        mFeedRepository = feedRepository;

    }

    @Override
    protected Observable<Feed> createObservable(RequestParams requestValue) {
        return mFeedRepository.getFeed(requestValue.getValues());
    }

    public static class RequestParams implements DefaultParams {

        static final String KEY_SHOP_ID = "shop_id";
        static final String KEY_ROWS = "rows";
        public static final String KEY_START = "start";
        static final String KEY_DEVICE = "device";
        static final String KEY_OB = "ob";

        static final String ROW_VALUE_DEFAULT = "12";
        static final String STAR_VALUE_DEFAULT = "0";
        static final String DEVICE_VALUE_DEFAULT = "android";
        static final String OB_VALUE_DEFAULT = "10";
        private boolean isIncludeWithTopAds = false;
        private final TKPDMapParam<String, String> values;


        public static TKPDMapParam<String, String> defaultParamsValue() {
            TKPDMapParam<String, String> params = new TKPDMapParam<>();
            params.put(KEY_ROWS, ROW_VALUE_DEFAULT);
            params.put(KEY_START, STAR_VALUE_DEFAULT);
            params.put(KEY_DEVICE, DEVICE_VALUE_DEFAULT);
            params.put(KEY_OB, OB_VALUE_DEFAULT);
            return params;
        }

        public RequestParams(TKPDMapParam<String, String> values) {
            this.values = values;
        }

        public TKPDMapParam<String, String> getValues() {
            return values;
        }


        boolean isIncludeWithTopAds() {
            return isIncludeWithTopAds;
        }

        public void setIncludeWithTopAds(boolean includeWithTopAds) {
            isIncludeWithTopAds = includeWithTopAds;
        }

    }

}
