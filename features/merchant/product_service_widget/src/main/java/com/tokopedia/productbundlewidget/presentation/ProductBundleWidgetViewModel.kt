package com.tokopedia.productbundlewidget.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.ProductServiceWidgetConstant
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_bundle.common.data.model.request.*
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParam
import com.tokopedia.productbundlewidget.model.ProductBundleWidgetUiMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductBundleWidgetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getBundleInfoUseCase: GetBundleInfoUseCase,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val productBundleWidgetUiMapper: ProductBundleWidgetUiMapper
) : BaseViewModel(dispatchers.main) {

    private val _bundleUiModels: MutableLiveData<List<BundleUiModel>> = MutableLiveData()
    val bundleUiModels: LiveData<List<BundleUiModel>> get() = _bundleUiModels
    val isBundleEmpty = Transformations.map(bundleUiModels) { it.isEmpty() }

    private val _error: MutableLiveData<Throwable> = MutableLiveData()
    val error: LiveData<Throwable> get() = _error

    fun getBundleInfo(param: GetBundleParam) {
        val chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getBundleInfoUseCase.setParams(
                    type = param.widgetType.typeCode,
                    squad = ProductServiceWidgetConstant.SQUAD_VALUE,
                    usecase = ProductServiceWidgetConstant.USECASE_BUNDLE_VALUE,
                    requestData = RequestData(
                        inventoryDetail = InventoryDetail(
                            required = true,
                            userLocation = UserLocation(
                                addressId = chosenAddress.addressId,
                                districtID = chosenAddress.districtId,
                                postalCode = chosenAddress.postalCode,
                                latlon = chosenAddress.geolocation
                            )
                        )
                    ),
                    productData = ProductData(
                        productID = param.productId,
                        warehouseIDs = listOf(param.warehouseId)
                    ),
                    bundleIdList = param.bundleList
                )
                getBundleInfoUseCase.executeOnBackground()
            }
            _bundleUiModels.value = productBundleWidgetUiMapper.groupAndMap(result.getBundleInfo?.bundleInfo.orEmpty())
        }, onError = {
            _error.value = it
        })
    }
}
