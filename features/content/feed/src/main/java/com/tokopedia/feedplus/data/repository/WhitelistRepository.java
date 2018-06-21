package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by yfsx on 21/06/18.
 */
public interface WhitelistRepository {

    Observable<WhitelistDomain> getWhitelist(RequestParams requestParams);
}
