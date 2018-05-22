package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    private GetRecentViewUseCase getRecentProductUseCase;

    @Inject
    public GetFirstPageFeedsCloudUseCase(FeedRepository feedRepository,
                                         GetRecentViewUseCase getRecentProductUseCase) {
        super(feedRepository);
        this.getRecentProductUseCase = getRecentProductUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getFeedPlus(requestParams),
                getRecentView(requestParams),
                new Func2<FeedResult, List<RecentViewProductDomain>, FeedResult>() {
                    @Override
                    public FeedResult call(FeedResult feedResult, List<RecentViewProductDomain> recentViewProductDomains) {
                        feedResult.getFeedDomain().setRecentProduct(recentViewProductDomains);
                        return feedResult;
                    }
                }
        );
    }

    private Observable<FeedResult> getFeedPlus(RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);
    }

    private Observable<List<RecentViewProductDomain>> getRecentView(RequestParams requestParams) {
        return getRecentProductUseCase.createObservable(requestParams);
    }
}
