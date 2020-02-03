package com.tokopedia.sellerhomedrawer.domain.repository;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface ProfileRepository {
    Observable<ProfileModel> getProfileFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<ProfileModel> getProfileFromLocal();
}
