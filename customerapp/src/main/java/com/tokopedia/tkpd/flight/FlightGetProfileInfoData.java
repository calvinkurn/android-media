package com.tokopedia.tkpd.flight;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 1/23/18.
 */

public class FlightGetProfileInfoData {
    private GetUserInfoUseCase getUserInfoUseCase;

    public FlightGetProfileInfoData(Context context) {
        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(context);
        String authKey = sessionHandler.getAccessToken(context);
        authKey = sessionHandler.getTokenType(context) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);

        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        context,
                        accountsService,
                        new GetUserInfoMapper(),
                        null,
                        sessionHandler
                );
        getUserInfoUseCase = new GetUserInfoUseCase(
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );
    }

    public Observable<ProfileInfo> getProfileInfoPrefillBooking() {
        return getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam()).map(new Func1<GetUserInfoDomainModel, ProfileInfo>() {
            @Override
            public ProfileInfo call(GetUserInfoDomainModel getUserInfoDomainModel) {
                ProfileInfo profileInfo = new ProfileInfo();
                profileInfo.setFullname(getUserInfoDomainModel.getGetUserInfoDomainData().getFullName());
                profileInfo.setPhoneNumber(getUserInfoDomainModel.getGetUserInfoDomainData().getPhone());
                profileInfo.setEmail(getUserInfoDomainModel.getGetUserInfoDomainData().getEmail());
                return profileInfo;
            }
        });
    }
}
