package com.tokopedia.core.drawer2.domain;

import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface ProfileRepository {
    Observable<ProfileModel> getProfileFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<ProfileModel> getProfileFromLocal();
}
