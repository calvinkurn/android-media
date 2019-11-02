package com.tokopedia.core.drawer2.data.source

import android.content.Context
import com.tokopedia.core.database.manager.GlobalCacheManager
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel
import com.tokopedia.core.network.apiservices.user.PeopleService
import com.tokopedia.core.util.SessionHandler
import rx.Observable

class CloudProfileSource(
        context: Context,
        peopleService: PeopleService,
        profileMapper: ProfileMapper,
        peopleCache: GlobalCacheManager,
        sessionHandler: SessionHandler
) {

    fun getProfile(map: Map<String, String>): Observable<ProfileModel>? {
        return Observable.just(ProfileModel())
    }

}