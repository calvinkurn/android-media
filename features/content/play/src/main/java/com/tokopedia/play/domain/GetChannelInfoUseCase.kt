package com.tokopedia.play.domain

import com.tokopedia.play.data.Channel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by mzennis on 2019-12-03.
 */

class GetChannelInfoUseCase : UseCase<Channel>() {

    override fun createObservable(p0: RequestParams?): Observable<Channel> {
        return Observable.empty()
    }
}
