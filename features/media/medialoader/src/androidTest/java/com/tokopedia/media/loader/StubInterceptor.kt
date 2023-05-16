package com.tokopedia.media.loader

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class StubInterceptor {

    @Inject
    lateinit var userSession: UserSessionInterface
}
