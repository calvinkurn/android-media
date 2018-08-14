package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func3;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    private GetRecentViewUseCase getRecentProductUseCase;
    private GetWhitelistUseCase getWhitelistUseCase;

    @Inject
    public GetFirstPageFeedsCloudUseCase(FeedRepository feedRepository,
                                         GetRecentViewUseCase getRecentProductUseCase,
                                         GetWhitelistUseCase getWhitelistUseCase) {
        super(feedRepository);
        this.getRecentProductUseCase = getRecentProductUseCase;
        this.getWhitelistUseCase = getWhitelistUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.zip(
                getFeedPlus(requestParams),
                getRecentView(requestParams),
                getWhitelist(),
                new Func3<FeedResult, List<RecentViewProductDomain>, GraphqlResponse, FeedResult>() {
                    @Override
                    public FeedResult call(FeedResult feedResult, List<RecentViewProductDomain> recentViewProductDomains, GraphqlResponse response) {
                        feedResult.getFeedDomain().setRecentProduct(recentViewProductDomains);
                        feedResult.getFeedDomain().setWhitelist(response != null ? mappingWhitelist(response) : null);
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

    private Observable<GraphqlResponse> getWhitelist() {
        getWhitelistUseCase.clearRequest();
        getWhitelistUseCase.setRequest(getWhitelistUseCase.getRequest());
        return getWhitelistUseCase.createObservable(RequestParams.EMPTY);
    }

    public static WhitelistDomain mappingWhitelist(GraphqlResponse response) {
        WhitelistQuery query = response.getData(WhitelistQuery.class);
        return getWhitelistDomain(query);
    }

    public static WhitelistDomain getWhitelistDomain(WhitelistQuery query) {
        if (query == null) return null;
        else {
            WhitelistDomain domain = new WhitelistDomain();
            domain.setError(query.getWhitelist().getError());
            domain.setUrl(query.getWhitelist().getUrl());
            domain.setWhitelist(query.getWhitelist().isWhitelist());
            domain.setTitle(query.getWhitelist().getTitle());
            domain.setDesc(query.getWhitelist().getDescription());
            domain.setTitleIdentifier(query.getWhitelist().getTitleIdentifier());
            domain.setPostSuccessMessage(query.getWhitelist().getPostSuccessMessage());
            return domain;
        }
    }
}
