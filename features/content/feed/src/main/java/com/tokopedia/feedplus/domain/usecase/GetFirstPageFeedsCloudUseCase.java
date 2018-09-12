package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    private GetWhitelistUseCase getWhitelistUseCase;

    @Inject
    public GetFirstPageFeedsCloudUseCase(FeedRepository feedRepository,
                                         GetWhitelistUseCase getWhitelistUseCase) {
        super(feedRepository);
        this.getWhitelistUseCase = getWhitelistUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.zip(
                getFeedPlus(requestParams),
                getWhitelist(),
                (feedResult, response) -> {
                    feedResult.getFeedDomain().setWhitelist(response != null ? mappingWhitelist(response) : null);
                    return feedResult;
                }
        );
    }

    private Observable<FeedResult> getFeedPlus(RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);
    }

    private Observable<GraphqlResponse> getWhitelist() {
        getWhitelistUseCase.clearRequest();
        getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_CONTENT_USER))
        );
        return getWhitelistUseCase.createObservable(RequestParams.EMPTY);
    }

    private static WhitelistDomain mappingWhitelist(GraphqlResponse response) {
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
