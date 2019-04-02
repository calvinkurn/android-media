package com.tokopedia.feedplus.domain.usecase;

import android.text.TextUtils;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    private GetWhitelistUseCase getWhitelistUseCase;
    private GetWhitelistUseCase getWhitelistInterestUseCase;

    @Inject
    public GetFirstPageFeedsCloudUseCase(FeedRepository feedRepository,
                                         GetWhitelistUseCase getWhitelistUseCase,
                                         GetWhitelistUseCase getWhitelistInterestUseCase) {
        super(feedRepository);
        this.getWhitelistUseCase = getWhitelistUseCase;
        this.getWhitelistInterestUseCase = getWhitelistInterestUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.zip(
                getFeedPlus(requestParams),
                getWhitelist(),
                getInterestWhitelist(),
                (feedResult, response, interestResponse) -> {
                    feedResult.getFeedDomain().setWhitelist(response != null
                            ? mappingWhitelist(response)
                            : null);
                    feedResult.getFeedDomain().setInterestWhitelist(interestResponse != null
                            ? mapInterestWhitelist(interestResponse)
                            : false);
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

    private Observable<GraphqlResponse> getInterestWhitelist() {
        getWhitelistInterestUseCase.clearRequest();
        getWhitelistInterestUseCase.addRequest(getWhitelistInterestUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_INTEREST))
        );
        return getWhitelistInterestUseCase.createObservable(RequestParams.EMPTY);
    }

    private static WhitelistDomain mappingWhitelist(GraphqlResponse response) {
        WhitelistQuery query = response.getData(WhitelistQuery.class);
        return getWhitelistDomain(query);
    }

    public static WhitelistDomain getWhitelistDomain(WhitelistQuery query) {
        if (query == null) return null;
        else {
            WhitelistDomain domain = new WhitelistDomain();
            domain.setError(query.getWhitelist().getError() != null ?
                    query.getWhitelist().getError() :
                    "");
            domain.setUrl(query.getWhitelist().getUrl() != null ?
                    query.getWhitelist().getUrl() :
                    "");
            domain.setWhitelist(query.getWhitelist().isWhitelist());
            domain.setTitle(query.getWhitelist().getTitle() != null ?
                    query.getWhitelist().getTitle() :
                    "");
            domain.setDesc(query.getWhitelist().getDescription() != null ?
                    query.getWhitelist().getDescription() :
                    "");
            domain.setTitleIdentifier(query.getWhitelist().getTitleIdentifier() != null ?
                    query.getWhitelist().getTitleIdentifier() :
                    "");
            domain.setPostSuccessMessage(query.getWhitelist().getPostSuccessMessage() != null ?
                    query.getWhitelist().getPostSuccessMessage() :
                    "");
            domain.setImage(query.getWhitelist().getImageUrl() != null ?
                    query.getWhitelist().getImageUrl() :
                    "");
            domain.setAuthors(query.getWhitelist().getAuthors() != null ?
                    query.getWhitelist().getAuthors() :
                    new ArrayList<>());
            return domain;
        }
    }

    private static Boolean mapInterestWhitelist(GraphqlResponse response) {
        WhitelistQuery whitelistQuery = response.getData(WhitelistQuery.class);
        return whitelistQuery != null && whitelistQuery.getWhitelist() != null
                && TextUtils.isEmpty(whitelistQuery.getWhitelist().getError())
                && whitelistQuery.getWhitelist().isWhitelist();
    }
}
