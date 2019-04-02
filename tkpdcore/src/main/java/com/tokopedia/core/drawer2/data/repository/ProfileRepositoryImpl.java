package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileSourceFactory profileSourceFactory;

    public ProfileRepositoryImpl(ProfileSourceFactory profileSourceFactory) {
        this.profileSourceFactory = profileSourceFactory;
    }

    @Override
    public Observable<ProfileModel> getProfileFromNetwork(TKPDMapParam<String, Object> parameters) {
        return profileSourceFactory.createCloudPeopleSource().getProfile(parameters);
    }

    @Override
    public Observable<ProfileModel> getProfileFromLocal() {
        return profileSourceFactory.createLocalPeopleSource().getProfile();
    }
}
