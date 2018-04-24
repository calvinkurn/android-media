package com.tokopedia.tkpd.tkpdcontactus.home.domain;

import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.data.TopBotStatus;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 24/04/18.
 */

public interface ITopBotRepository {
    Observable<TopBotStatus> getTopBotStatus();
}
