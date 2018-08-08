package com.tokopedia.core.referral.data;

import com.tokopedia.core.referral.domain.ReferralRepository;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public class ReferralDataRepository implements ReferralRepository {

    private final ReferralDataStoreFactory referralDataStoreFactory;

    public ReferralDataRepository(ReferralDataStoreFactory referralDataStoreFactory) {
        this.referralDataStoreFactory = referralDataStoreFactory;
    }

    @Override
    public Observable<ReferralCodeEntity> getReferralVoucherCode(String  param) {
        return referralDataStoreFactory.createCloudPlaceDataStore()
                .getReferralVoucherCode(param)
                .map(new Func1<String, ReferralCodeEntity>() {
                    @Override
                    public ReferralCodeEntity call(String tkpdResponseResponse) {
                        TkpdReferralResponse factory = TkpdReferralResponse.factory(tkpdResponseResponse);
                        ReferralCodeEntity referralCodeEntity = new ReferralCodeEntity();
                        if (factory.isError() && factory.getErrorMessages() != null && factory.getErrorMessages().size() > 0) {
                            referralCodeEntity.setErorMessage(factory.getErrorMessages().get(0));
                        } else {
                            referralCodeEntity = factory.convertToObj(ReferralCodeEntity.class);
                        }
                        return referralCodeEntity;
                    }
                });
    }
}
