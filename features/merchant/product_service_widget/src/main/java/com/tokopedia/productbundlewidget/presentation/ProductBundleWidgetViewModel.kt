package com.tokopedia.productbundlewidget.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.ProductServiceWidgetConstant
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_bundle.common.data.model.request.*
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.productbundlewidget.model.ProductBundleWidgetUiMapper
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
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

    fun getBundleInfo(productId: String, warehouseId: String, bundleIdList: List<Bundle>) {
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
                        productID = productId,
                        warehouseIDs = listOf(warehouseId)
                    ),
                    bundleIdList = bundleIdList
                )
                getBundleInfoUseCase.executeOnBackground()
            }
            _bundleUiModels.value = productBundleWidgetUiMapper.groupAndMap(result.getBundleInfo?.bundleInfo.orEmpty())
        }, onError = {

        })
    }
}
