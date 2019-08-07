package com.tokopedia.shop.open.view.listener

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.shop.open.data.model.PostalCodeViewModel

interface PostalCodeChooserContract {

    interface View : BaseListViewListener<PostalCodeViewModel> {

        fun showLoading()

        fun hideLoading()

        override fun showGetListError(throwable: Throwable)

        fun showNoResultMessage()

        fun showInitialLoadMessage()
    }
}