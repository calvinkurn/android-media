package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhomedrawer.data.ShopStatusModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-07
 */

class SharedViewModel @Inject constructor() : ViewModel() {

    val getShopStatus = MutableLiveData<ShopStatus>()

    fun setShopStatus(shopStatus: ShopStatusModel) {
        when {
            shopStatus.isOfficialStore() -> {
                getShopStatus.value = ShopStatus.OFFICIAL_STORE
            }
            shopStatus.isPowerMerchantActive() -> {
                getShopStatus.value = ShopStatus.POWER_MERCHANT
            }
            else -> {
                getShopStatus.value = ShopStatus.REGULAR_MERCHANT
            }
        }
    }
}