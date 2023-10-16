package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.GetDefaultChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.usecase.GetDefaultChosenAddressUseCase
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
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

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

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
                val chosenAddress = getDefaultChosenAddressUseCase(
                    GetDefaultChosenAddressParam(
                        latLong = null,
                        source = SOURCE_LOGIN,
                        isTokonow = true
                    )
                ).response
                if (chosenAddress.error.detail.isEmpty()) {
                    val address = chosenAddress.data
                    val tokonow = chosenAddress.tokonow
                    if (address.cityId != 0) {
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
                    } else {
                        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                            applicationContext,
                            ChooseAddressConstant.defaultAddress
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val JOB_ID = 998877
        private const val SOURCE_LOGIN = "login"

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
