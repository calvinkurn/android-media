package com.tokopedia.sellerhomedrawer.presentation.view.subscriber

import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.util.SellerTokoCashUtil
import rx.Subscriber

class SellerTokoCashSubscriber(val viewListener: SellerDrawerDataListener): Subscriber<SellerTokoCashData>() {

    override fun onNext(sellerTokoCashData: SellerTokoCashData?) {
        if (sellerTokoCashData != null)
            viewListener.onGetTokoCash(
                SellerTokoCashUtil.convertToViewModel(sellerTokoCashData))
    }

    override fun onCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}