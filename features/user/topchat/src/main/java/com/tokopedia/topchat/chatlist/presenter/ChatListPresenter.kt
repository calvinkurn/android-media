package com.tokopedia.topchat.chatlist.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ChatListPresenter @Inject constructor(
        private var userSession: UserSessionInterface,
        private val tkpdAuthInterceptor: TkpdAuthInterceptor,
        private val fingerprintInterceptor: FingerprintInterceptor)
    : BaseDaggerPresenter<ChatListContract.View>()
        , ChatListContract.Presenter<ChatListContract.View>{

    private var mSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>

    init {
        mSubscription = CompositeSubscription()
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
    }
}
