package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.DefaultParams;
import com.tokopedia.core.base.UseCaseWithParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetTopAdsUseCase
        extends UseCaseWithParams<GetTopAdsUseCase.RequestParams, List<TopAds>> {

    private static final String TOPADS_PAGE_DEFAULT_VALUE = "1";
    private static final String TOPADS_ITEM_DEFAULT_VALUE = "4";
    private static final String SRC_PRODUCT_FEED = "fav_product";
    private static final String KEY_ITEM = "item";
    private static final String KEY_SRC = "src";
    private static final String KEY_PAGE = "page";

    private FeedRepository mFeedRepository;


    public GetTopAdsUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        mFeedRepository = feedRepository;
    }

    @Override
    protected Observable<List<TopAds>> createObservable(RequestParams requestParams) {
        return mFeedRepository.getTopAds(requestParams.getValues());
    }

    static final class RequestParams implements DefaultParams {
        private final TKPDMapParam<String, String> values;


        RequestParams() {
            this.values = new TKPDMapParam<>();
            this.values.put(KEY_PAGE, TOPADS_PAGE_DEFAULT_VALUE);
            this.values.put(KEY_ITEM, TOPADS_ITEM_DEFAULT_VALUE);
            this.values.put(KEY_SRC, SRC_PRODUCT_FEED);
        }

        public TKPDMapParam<String, String> getValues() {
            return values;
        }
    }
}
