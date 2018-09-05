package com.tokopedia.talk.producttalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel

/**
 * @author by Steven
 */

interface ProductTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?

        fun show(viewModel: ProductTalkViewModel)
    }

    interface Presenter : CustomerPresenter<View> {

        fun getProductTalk(productId: String)
    }
}