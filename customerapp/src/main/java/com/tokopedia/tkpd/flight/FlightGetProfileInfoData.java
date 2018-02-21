package com.tokopedia.tkpd.flight;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.tkpd.flight.di.FlightConsumerComponent;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 1/23/18.
 */

public class FlightGetProfileInfoData {
    private static FlightGetProfileInfoData instance;
    @Inject
    GetUserInfoUseCase getUserInfoUseCase;
    private FlightConsumerComponent flightConsumerComponent;

    public FlightGetProfileInfoData(FlightConsumerComponent flightConsumerComponent) {
        this.flightConsumerComponent = flightConsumerComponent;
    }

    public static FlightGetProfileInfoData newInstance(FlightConsumerComponent flightConsumerComponent) {
        if (instance == null) {
            instance = new FlightGetProfileInfoData(flightConsumerComponent);
        }
        return instance;
    }

    public FlightGetProfileInfoData inject() {
        if (flightConsumerComponent != null) {
            flightConsumerComponent.inject(this);
        }
        return this;
    }

    public Observable<ProfileInfo> getProfileInfoPrefillBooking() {
        return getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam()).map(new Func1<GetUserInfoDomainModel, ProfileInfo>() {
            @Override
            public ProfileInfo call(GetUserInfoDomainModel getUserInfoDomainModel) {
                ProfileInfo profileInfo = new ProfileInfo();
                profileInfo.setFullname(getUserInfoDomainModel.getGetUserInfoDomainData().getFullName());
                profileInfo.setPhoneNumber(getUserInfoDomainModel.getGetUserInfoDomainData().getPhone());
                profileInfo.setEmail(getUserInfoDomainModel.getGetUserInfoDomainData().getEmail());
                profileInfo.setBday(getUserInfoDomainModel.getGetUserInfoDomainData().getBday());
                profileInfo.setGender(getUserInfoDomainModel.getGetUserInfoDomainData().getGender());
                return profileInfo;
            }
        });
    }
}
