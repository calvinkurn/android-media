package com.tokopedia.sellerhomedrawer.domain.datamanager

import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerGetUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.sellerhomedrawer.presentation.view.subscriber.SellerTokoCashSubscriber
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession

class SellerDrawerDataManagerImpl(val viewListener: SellerDrawerDataListener,
                                  val sellerTokoCashUseCase: SellerTokoCashUseCase,
                                  val uaUseCase: SellerGetUserAttributesUseCase,
                                  val userAttributesUseCase: GetSellerHomeUserAttributesUseCase): SellerDrawerDataManager {

    companion object {
        @JvmStatic
        val TAG = SellerDrawerDataManagerImpl::class.java.simpleName
    }

    override fun getTokoCash() {
        sellerTokoCashUseCase.execute(RequestParams.EMPTY, SellerTokoCashSubscriber(viewListener))
    }

    override fun unsubscribe() {
        sellerTokoCashUseCase.unsubscribe()
    }

    override fun getUserAttributes(sessionHandler: UserSession) {

    }

    override fun getSellerUserAttributes(sessionHandler: UserSession) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}