package com.tokopedia.core.referral.data;

import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;

import rx.Observable;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public class CloudReferralDataStore implements ReferralDataStore {
    private final ReferralApi referralApi;

    public CloudReferralDataStore(ReferralApi referralApi) {
        this.referralApi = referralApi;
    }


    @Override
    public Observable<String> getReferralVoucherCode(String param) {
        return referralApi.getReferralVoucherCode(param);
    }
}
