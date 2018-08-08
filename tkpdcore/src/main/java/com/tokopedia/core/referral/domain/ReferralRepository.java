package com.tokopedia.core.referral.domain;

import com.tokopedia.core.referral.data.ReferralCodeEntity;

import rx.Observable;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public interface ReferralRepository {
    Observable<ReferralCodeEntity> getReferralVoucherCode(String param);

}
