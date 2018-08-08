package com.tokopedia.core.referral.data;

import rx.Observable;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public interface ReferralDataStore {

    Observable<String > getReferralVoucherCode(String param);

}
