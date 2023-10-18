package com.tokopedia.tokofood.stub.purchase.presentation.fragment

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.stub.purchase.util.TokoFoodPurchaseComponentStubInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class TokoFoodPurchaseFragmentStub: TokoFoodPurchaseFragment() {

    fun getCurrentAdapter(): TokoFoodPurchaseAdapter? {
        return rvAdapter
    }

    override fun initInjector() {
        context?.applicationContext?.let {
            TokoFoodPurchaseComponentStubInstance.getTokoFoodPurchaseComponentStub(it).inject(this)
        }
    }

    override fun getLocalCacheModel(): LocalCacheModel {
        return ChooseAddressUtils.setLocalizingAddressData(
            addressId = "1",
            cityId = "",
            districtId = "",
            lat = "1",
            long = "2",
            label = "",
            postalCode = "",
            warehouseId = "",
            shopId = "",
            warehouses = listOf(),
            serviceType = ""
        )
    }

    companion object {
        @JvmStatic
        fun createInstance(): TokoFoodPurchaseFragmentStub {
            return TokoFoodPurchaseFragmentStub()
        }
    }



}
