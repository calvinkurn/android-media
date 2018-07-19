package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.home.data.TopBotStatus;

import rx.Observable;

/**
 * Created by sandeepgoyal on 24/04/18.
 */

public interface ITopBotRepository {
    Observable<TopBotStatus> getTopBotStatus();
}
