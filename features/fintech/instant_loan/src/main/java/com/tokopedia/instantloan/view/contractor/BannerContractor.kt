package com.tokopedia.instantloan.view.contractor

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.BannerEntity

/**
 * Created by lavekush on 22/03/18.
 */

interface BannerContractor {

    interface View : CustomerView {
        fun renderUserList(banners: List<BannerEntity>?)

        fun nextBanner()

        fun previousBanner()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadBanners()
    }
}
