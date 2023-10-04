package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.domain.GetDefaultChosenAddressUseCase
import javax.inject.Inject

class GetDefaultChosenAddressService : JobIntentServiceX() {

    @Inject
    lateinit var getDefaultChosenAddressUseCase: GetDefaultChosenAddressUseCase

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        application?.let {
            ActivityComponentFactory.instance.createLoginComponent(it).inject(this)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            getDefaultChosenAddressUseCase.getDefaultChosenAddress({
                val address = it.data
                val tokonow = it.tokonow
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    applicationContext,
                    addressId = address.addressId.toString(),
                    cityId = address.cityId.toString(),
                    districtId = address.districtId.toString(),
                    lat = address.latitude,
                    long = address.longitude,
                    label = "${address.addressName} ${address.receiverName}",
                    postalCode = address.postalCode,
                    shopId = tokonow.shopId.toString(),
                    warehouseId = tokonow.warehouseId.toString(),
                    warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(tokonow.warehouses),
                    serviceType = tokonow.serviceType,
                    lastUpdate = tokonow.tokonowLastUpdate
                )
            }, {
                it.printStackTrace()
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val JOB_ID = 998877

        fun startService(context: Context) {
            try {
                val intent = Intent(context, GetDefaultChosenAddressService::class.java)
                enqueueWork(context, GetDefaultChosenAddressService::class.java, JOB_ID, intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
