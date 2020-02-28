package com.tokopedia.sellerhomedrawer.presentation.view

import android.content.Context
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus

class SellerHomeDashboardContract {

    interface View {
        fun onSuccessGetFlashSaleSellerStatus(isVisible: Boolean)
        fun onSuccessGetShopInfo(goldGetPmOsStatus: GoldGetPmOsStatus)
    }

    interface Presenter {

    }

}