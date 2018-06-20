package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistUseCase extends UseCase<WhitelistDomain> {

    protected FeedRepository feedRepository;

    @Inject
    public GetWhitelistUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<WhitelistDomain> createObservable(RequestParams requestParams) {
        return feedRepository.getWhitelist(requestParams);
    }

}
