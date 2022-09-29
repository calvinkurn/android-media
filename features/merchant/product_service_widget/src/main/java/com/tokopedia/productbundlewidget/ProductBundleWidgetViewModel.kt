package com.tokopedia.productbundlewidget

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.ProductServiceWidgetConstant
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_bundle.common.data.model.request.InventoryDetail
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.request.UserLocation
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductBundleWidgetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getBundleInfoUseCase: GetBundleInfoUseCase,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : BaseViewModel(dispatchers.main) {

    val testdata: MutableLiveData<List<String>> = MutableLiveData()

    fun getBundleInfo(productId: Long, warehouseId: String) {
        val chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getBundleInfoUseCase.setParams(
                    squad = ProductServiceWidgetConstant.SQUAD_VALUE,
                    usecase = ProductServiceWidgetConstant.USECASE_BUNDLE_VALUE,
                    requestData = RequestData(
                        variantDetail = true,
                        CheckCampaign = true,
                        BundleGroup = true,
                        Preorder = true,
                        BundleStats = true,
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
                        productID = productId.toString(),
                        warehouseIDs = listOf(warehouseId)
                    ),
                    bundleIdList = emptyList()
                )
                getBundleInfoUseCase.executeOnBackground()
            }
            testdata.value = result.getBundleInfo?.bundleInfo?.map {
                it.name
            }
        }, onError = {

        })
    }
}
