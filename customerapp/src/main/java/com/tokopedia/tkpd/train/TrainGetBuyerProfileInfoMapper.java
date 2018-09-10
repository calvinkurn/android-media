package com.tokopedia.tkpd.train;

import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 28/06/18.
 */
public class TrainGetBuyerProfileInfoMapper implements Func1<ProfileInfo, ProfileBuyerInfo> {

    public TrainGetBuyerProfileInfoMapper() {
    }

    @Override
    public ProfileBuyerInfo call(ProfileInfo profileInfo) {
        ProfileBuyerInfo profileBuyerInfo = new ProfileBuyerInfo();
        profileBuyerInfo.setFullname(profileInfo.getFullname());
        profileBuyerInfo.setEmail(profileInfo.getEmail());
        profileBuyerInfo.setBday(profileInfo.getBday());
        profileBuyerInfo.setGender(profileInfo.getGender());
        profileBuyerInfo.setPhoneNumber(profileInfo.getPhoneNumber());
        return profileBuyerInfo;
    }
}
