package com.tokopedia.sellerhomedrawer.presentation.view.subscriber

import android.content.Context
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.sellerhomedrawer.domain.util.SellerTokoCashUtil
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import rx.Subscriber

class SellerTokoCashSubscriber(val context: Context,
                               val viewListener: SellerDrawerDataListener): Subscriber<SellerTokoCashData>() {

    override fun onNext(sellerTokoCashData: SellerTokoCashData?) {
        if (sellerTokoCashData != null)
            viewListener.onGetTokoCash(
                SellerTokoCashUtil.convertToViewModel(sellerTokoCashData))
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(context, e))
    }
}