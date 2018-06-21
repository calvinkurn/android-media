package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.WhitelistRepository;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistUseCase extends UseCase<WhitelistDomain> {

    private WhitelistRepository whitelistRepository;

    @Inject
    public GetWhitelistUseCase(WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    @Override
    public Observable<WhitelistDomain> createObservable(RequestParams requestParams) {
        return whitelistRepository.getWhitelist(requestParams);
    }

}
