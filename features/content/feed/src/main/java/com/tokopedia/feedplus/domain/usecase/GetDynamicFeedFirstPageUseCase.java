package com.tokopedia.feedplus.domain.usecase;

import android.text.TextUtils;

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel;
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase;
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author by milhamj on 07/01/19.
 */
public class GetDynamicFeedFirstPageUseCase extends UseCase<DynamicFeedFirstPageDomainModel> {
    private GetDynamicFeedUseCase getDynamicFeedUseCase;
    private GetWhitelistUseCase getWhitelistUseCase;
    private GetWhitelistUseCase getWhitelistInterestUseCase;

    @Inject
    public GetDynamicFeedFirstPageUseCase(GetDynamicFeedUseCase getDynamicFeedUseCase,
                                          GetWhitelistUseCase getWhitelistUseCase,
                                          GetWhitelistUseCase getWhitelistInterestUseCase) {
        this.getDynamicFeedUseCase = getDynamicFeedUseCase;
        this.getWhitelistUseCase = getWhitelistUseCase;
        this.getWhitelistInterestUseCase = getWhitelistInterestUseCase;
    }

    @Override
    public Observable<DynamicFeedFirstPageDomainModel> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getDynamicFeed(requestParams),
                getCreatePostWhitelist(),
                getInterestWhitelist(),
                DynamicFeedFirstPageDomainModel::new
        );
    }

    private Observable<DynamicFeedDomainModel> getDynamicFeed(RequestParams requestParams) {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io());
    }

    private Observable<WhitelistDomain> getCreatePostWhitelist() {
        getWhitelistUseCase.clearRequest();
        getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_CONTENT_USER))
        );
        return getWhitelistUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .map(mapCreatePostWhitelist());
    }

    private Func1<GraphqlResponse, WhitelistDomain> mapCreatePostWhitelist() {
        return graphqlResponse -> {
            WhitelistQuery query = graphqlResponse.getData(WhitelistQuery.class);
            return getWhitelistDomain(query);
        };
    }

    private WhitelistDomain getWhitelistDomain(WhitelistQuery query) {
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
            return domain;
        }
    }

    private Observable<Boolean> getInterestWhitelist() {
        getWhitelistInterestUseCase.clearRequest();
        getWhitelistInterestUseCase.addRequest(getWhitelistInterestUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_INTEREST))
        );
        return getWhitelistInterestUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .map(mapInterestWhitelist());
    }

    private Func1<GraphqlResponse, Boolean> mapInterestWhitelist() {
        return graphqlResponse -> {
            WhitelistQuery whitelistQuery = graphqlResponse.getData(WhitelistQuery.class);
            return whitelistQuery != null && whitelistQuery.getWhitelist() != null
                    && TextUtils.isEmpty(whitelistQuery.getWhitelist().getError())
                    && whitelistQuery.getWhitelist().isWhitelist();
        };
    }
}
