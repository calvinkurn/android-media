package com.tokopedia.core.referral.data;

import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public class ReferralDataStoreFactory {

    public final ReferralApi referralApi;

    public ReferralDataStoreFactory(ReferralApi referralApi) {
        this.referralApi = referralApi;
    }

    public ReferralDataStore createCloudPlaceDataStore(){
        return new CloudReferralDataStore(this.referralApi);
    }
}
