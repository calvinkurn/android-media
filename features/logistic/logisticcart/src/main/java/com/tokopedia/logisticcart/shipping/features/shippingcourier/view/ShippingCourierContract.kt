package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
interface ShippingCourierContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
    }
}
