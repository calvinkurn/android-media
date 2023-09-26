package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.GetDefaultChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.usecase.GetDefaultChosenAddressUseCase
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetDefaultChosenAddressService : JobIntentServiceX(), CoroutineScope {

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
        launch {
            try {
                val address = getDefaultChosenAddressUseCase(GetDefaultChosenAddressParam(latLong = null, source = "login", isTokonow = true)).response
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    applicationContext,
                    addressId = address.data.addressId.toString(),
                    cityId = address.data.cityId.toString(),
                    districtId = address.data.districtId.toString(),
                    lat = address.data.latitude,
                    long = address.data.longitude,
                    label = "${address.data.addressName} ${address.data.receiverName}",
                    postalCode = address.data.postalCode,
                    shopId = address.tokonow.shopId.toString(),
                    warehouseId = address.tokonow.warehouseId.toString(),
                    warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(address.tokonow.warehouses),
                    serviceType = address.tokonow.serviceType,
                    lastUpdate = address.tokonow.tokonowLastUpdate
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
}
