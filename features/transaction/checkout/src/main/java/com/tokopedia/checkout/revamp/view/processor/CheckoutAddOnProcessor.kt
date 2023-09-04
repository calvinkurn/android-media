package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.revamp.view.epharmacy
import com.tokopedia.checkout.revamp.view.firstOrNullInstanceOf
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.ShipmentViewModel
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_APPROVED
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_REJECTED
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutAddOnProcessor @Inject constructor(
    private val prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine,
    private val epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase,
    private val saveAddOnProductUseCase: SaveAddOnStateUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun fetchPrescriptionIds(
        epharmacyData: EpharmacyData,
        listData: List<CheckoutItem>,
        uploadPrescriptionUiModel: UploadPrescriptionUiModel
    ) {
        withContext(dispatchers.io) {
            if (epharmacyData.checkoutId.isNotEmpty() && epharmacyData.showImageUpload && !epharmacyData.consultationFlow) {
                try {
                    val getPrescriptionIdsResponse = prescriptionIdsUseCase
                        .setParams(epharmacyData.checkoutId)
                        .executeOnBackground()
                    if (getPrescriptionIdsResponse.detailData != null && getPrescriptionIdsResponse.detailData!!.prescriptionData != null && getPrescriptionIdsResponse.detailData!!.prescriptionData!!.prescriptions != null) {
                        val prescriptions =
                            getPrescriptionIdsResponse.detailData!!.prescriptionData!!.prescriptions
                        val prescriptionIds = ArrayList<String>()
                        for (prescription in prescriptions!!) {
                            prescriptionIds.add(prescription!!.prescriptionId!!)
                        }
                        setPrescriptionIds(prescriptionIds, listData)
                        uploadPrescriptionUiModel.isError = false
                    }
                } catch (e: Throwable) {
                    Timber.d(e)
                }
            }
        }
    }

    fun fetchEpharmacyData(listData: List<CheckoutItem>, callback: (List<CheckoutItem>?) -> Unit) {
        epharmacyUseCase.getEPharmacyPrepareProductsGroup({ ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse ->
            callback.invoke(processEpharmacyData(ePharmacyPrepareProductsGroupResponse, listData))
        }) { throwable: Throwable? ->
            Timber.d(throwable)
        }
    }

    private fun processEpharmacyData(
        ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse,
        listData: List<CheckoutItem>
    ): List<CheckoutItem>? {
        val checkoutItems = listData.toMutableList()
        val uploadPrescriptionUiModel =
            listData.epharmacy()?.epharmacy?.copy() ?: return null
        if (ePharmacyPrepareProductsGroupResponse.detailData != null) {
            val groupsData = ePharmacyPrepareProductsGroupResponse.detailData!!.groupsData
            if (groupsData?.epharmacyGroups != null) {
                val epharmacyGroupIds = HashSet<String>()
                val mapPrescriptionCount = HashMap<String?, Int>()
                val enablerNames = HashSet<String>()
                val shopIds = ArrayList<String>()
                val cartIds = ArrayList<String>()
                var hasInvalidPrescription = false
                var tempProductMap: HashMap<Int, CheckoutProductModel> = hashMapOf()
                for ((index, item) in checkoutItems.withIndex()) {
                    if (item is CheckoutProductModel) {
                        tempProductMap[index] = item
                    }
                    if (item is CheckoutOrderModel) {
                        var shipmentCartItemModel = item.copy()
                        if (shipmentCartItemModel.hasEthicalProducts) {
                            shopIds.add(shipmentCartItemModel.shopId.toString())
                            enablerNames.add(shipmentCartItemModel.enablerLabel)
                            for (cartItemModel in shipmentCartItemModel.products) {
                                if (cartItemModel.ethicalDrugDataModel.needPrescription) {
                                    cartIds.add(cartItemModel.cartId.toString())
                                }
                            }
                        }
                        if (!shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                            var updated = false
                            var shouldResetCourier = false
                            var productErrorCount = 0
                            var firstProductErrorIndex = -1
                            for (epharmacyGroup in groupsData.epharmacyGroups!!) {
                                if (updated) {
                                    break
                                }
                                if (epharmacyGroup?.shopInfo != null) {
                                    epharmacyGroupIds.add(epharmacyGroup.epharmacyGroupId!!)
                                    for (productsInfo in epharmacyGroup.shopInfo!!) {
                                        if (updated) {
                                            break
                                        }
                                        if (productsInfo?.shopId != null &&
                                            productsInfo.shopId!!.isNotBlankOrZero() &&
                                            shipmentCartItemModel.shopId == productsInfo.shopId!!.toLong()
                                        ) {
                                            if (productsInfo.products != null) {
                                                for (product in productsInfo.products!!) {
                                                    if (updated) {
                                                        break
                                                    }
                                                    if (product?.productId != null) {
                                                        for ((i, cartItemModel) in tempProductMap) {
                                                            if (product.productId == cartItemModel.productId && !cartItemModel.isError) {
                                                                if (epharmacyGroup.consultationData != null && epharmacyGroup.consultationData!!.consultationStatus != null && epharmacyGroup.consultationData!!.consultationStatus == EPHARMACY_CONSULTATION_STATUS_REJECTED) {
                                                                    shipmentCartItemModel.tokoConsultationId =
                                                                        ""
                                                                    shipmentCartItemModel.partnerConsultationId =
                                                                        ""
                                                                    shipmentCartItemModel.consultationDataString =
                                                                        ""
                                                                    hasInvalidPrescription = true
                                                                    if (shipmentCartItemModel.hasNonEthicalProducts) {
                                                                        checkoutItems[i] =
                                                                            cartItemModel.copy(
                                                                                isError = true,
                                                                                errorMessage = uploadPrescriptionUiModel.rejectedWording
                                                                            )
                                                                        shouldResetCourier = true
                                                                    } else {
                                                                        shipmentCartItemModel.firstProductErrorIndex =
                                                                            0
                                                                        shipmentCartItemModel.isError =
                                                                            true
                                                                        shipmentCartItemModel.isAllItemError =
                                                                            true
                                                                        for ((i1, cartItemModel1) in tempProductMap) {
                                                                            checkoutItems[i1] =
                                                                                cartItemModel1.copy(
                                                                                    isError = true,
                                                                                    isShopError = true
                                                                                )
                                                                        }
                                                                        shipmentCartItemModel.errorTitle =
                                                                            "Yaah, ada ${shipmentCartItemModel.products.size} barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain."
                                                                        shipmentCartItemModel.isCustomEpharmacyError =
                                                                            true
                                                                        shipmentCartItemModel.spId =
                                                                            0
                                                                        shipmentCartItemModel =
                                                                            shipmentCartItemModel.copy(
                                                                                shipment = shipmentCartItemModel.shipment.copy(
                                                                                    courierItemData = null
                                                                                )
                                                                            )
                                                                        updated = true
                                                                        break
                                                                    }
                                                                } else if (epharmacyGroup.consultationData != null && epharmacyGroup.consultationData!!.consultationStatus != null && epharmacyGroup.consultationData!!.consultationStatus == EPHARMACY_CONSULTATION_STATUS_APPROVED) {
                                                                    shipmentCartItemModel.tokoConsultationId =
                                                                        epharmacyGroup.consultationData!!.tokoConsultationId!!
                                                                    shipmentCartItemModel.partnerConsultationId =
                                                                        epharmacyGroup.consultationData!!.partnerConsultationId!!
                                                                    shipmentCartItemModel.consultationDataString =
                                                                        epharmacyGroup.consultationData!!.consultationString!!
                                                                    mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                        1
                                                                    updated = true
                                                                    break
                                                                } else if (epharmacyGroup.prescriptionImages != null && epharmacyGroup.prescriptionImages!!.isNotEmpty()) {
                                                                    val prescriptionIds =
                                                                        ArrayList<String>()
                                                                    for (prescriptionImage in epharmacyGroup.prescriptionImages!!) {
                                                                        if (prescriptionImage != null && !prescriptionImage.prescriptionId.isNullOrEmpty()
                                                                        ) {
                                                                            prescriptionIds.add(
                                                                                prescriptionImage.prescriptionId!!
                                                                            )
                                                                        }
                                                                    }
                                                                    shipmentCartItemModel.prescriptionIds =
                                                                        prescriptionIds
                                                                    mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                        prescriptionIds.size
                                                                    updated = true
                                                                    break
                                                                }
                                                            }
                                                            if (cartItemModel.isError) {
                                                                productErrorCount += 1
                                                                firstProductErrorIndex =
                                                                    if (firstProductErrorIndex < 0) {
                                                                        i
                                                                    } else {
                                                                        minOf(
                                                                            i,
                                                                            firstProductErrorIndex
                                                                        )
                                                                    }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (shouldResetCourier) {
                                shipmentCartItemModel.isHasUnblockingError = true
                                shipmentCartItemModel.firstProductErrorIndex =
                                    firstProductErrorIndex
                                shipmentCartItemModel.unblockingErrorMessage =
                                    "Yaah, ada $productErrorCount barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain."
                                shipmentCartItemModel.spId = 0
                                shipmentCartItemModel.shouldResetCourier = true
                                shipmentCartItemModel = shipmentCartItemModel.copy(
                                    shipment = shipmentCartItemModel.shipment.copy(courierItemData = null)
                                )
                            }
                        }
                        checkoutItems[index] = shipmentCartItemModel
                        tempProductMap = hashMapOf()
                    }
                    if (item is CheckoutEpharmacyModel) {
                        break
                    }
                }
                var totalPrescription = 0
                for (value in mapPrescriptionCount.values) {
                    totalPrescription += value
                }
                uploadPrescriptionUiModel.epharmacyGroupIds = ArrayList(epharmacyGroupIds)
                uploadPrescriptionUiModel.isError = false
                uploadPrescriptionUiModel.uploadedImageCount = totalPrescription
                uploadPrescriptionUiModel.hasInvalidPrescription = hasInvalidPrescription
                uploadPrescriptionUiModel.enablerNames = ArrayList(enablerNames)
                uploadPrescriptionUiModel.shopIds = shopIds
                uploadPrescriptionUiModel.cartIds = cartIds
                checkoutItems[checkoutItems.size - 5] =
                    CheckoutEpharmacyModel(epharmacy = uploadPrescriptionUiModel)
                return checkoutItems
            }
        }
        return null
    }

    fun setPrescriptionIds(prescriptionIds: ArrayList<String>, listData: List<CheckoutItem>) {
        val uploadPrescriptionUiModel =
            listData.firstOrNullInstanceOf(CheckoutEpharmacyModel::class.java)?.epharmacy ?: return
        for (shipmentCartItemModel in listData) {
            if (shipmentCartItemModel is CheckoutOrderModel && !shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                shipmentCartItemModel.prescriptionIds = prescriptionIds
            }
        }
        uploadPrescriptionUiModel.uploadedImageCount = prescriptionIds.size
    }

    fun setMiniConsultationResult(
        results: ArrayList<EPharmacyMiniConsultationResult>,
        listData: List<CheckoutItem>
    ): List<CheckoutItem>? {
        val checkoutItems = listData.toMutableList()
        val uploadPrescriptionUiModel =
            listData.epharmacy()?.epharmacy?.copy() ?: return null
        val epharmacyGroupIds = HashSet<String>()
        val mapPrescriptionCount = HashMap<String?, Int>()
        val enablerNames = HashSet<String>()
        val shopIds = ArrayList<String>()
        val cartIds = ArrayList<String>()
        var hasInvalidPrescription = false
        var tempProductMap: HashMap<Int, CheckoutProductModel> = hashMapOf()
        for ((index, item) in checkoutItems.withIndex()) {
            if (item is CheckoutProductModel) {
                tempProductMap[index] = item
            }
            if (item is CheckoutOrderModel) {
                var shipmentCartItemModel = item.copy()
                if (shipmentCartItemModel.hasEthicalProducts) {
                    shopIds.add(shipmentCartItemModel.shopId.toString())
                    enablerNames.add(shipmentCartItemModel.enablerLabel)
                    for (cartItemModel in shipmentCartItemModel.products) {
                        if (cartItemModel.ethicalDrugDataModel.needPrescription) {
                            cartIds.add(cartItemModel.cartId.toString())
                        }
                    }
                }
                if (!shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                    var updated = false
                    var shouldResetCourier = false
                    var productErrorCount = 0
                    var firstProductErrorIndex = -1
                    for (epharmacyGroup in results) {
                        if (updated) {
                            break
                        }
                        if (epharmacyGroup.shopInfo != null) {
                            epharmacyGroupIds.add(epharmacyGroup.epharmacyGroupId!!)
                            for (productsInfo in epharmacyGroup.shopInfo!!) {
                                if (updated) {
                                    break
                                }
                                if (productsInfo?.shopId != null &&
                                    productsInfo.shopId!!.isNotBlankOrZero() &&
                                    shipmentCartItemModel.shopId == productsInfo.shopId!!.toLong()
                                ) {
                                    if (productsInfo.products != null) {
                                        for (product in productsInfo.products!!) {
                                            if (updated) {
                                                break
                                            }
                                            if (product?.productId != null) {
                                                for ((i, cartItemModel) in tempProductMap) {
                                                    if (product.productId == cartItemModel.productId && !cartItemModel.isError) {
                                                        if (epharmacyGroup.consultationStatus != null && epharmacyGroup.consultationStatus == EPHARMACY_CONSULTATION_STATUS_REJECTED) {
                                                            shipmentCartItemModel.tokoConsultationId =
                                                                ""
                                                            shipmentCartItemModel.partnerConsultationId =
                                                                ""
                                                            shipmentCartItemModel.consultationDataString =
                                                                ""
                                                            hasInvalidPrescription = true
                                                            if (shipmentCartItemModel.hasNonEthicalProducts) {
                                                                checkoutItems[i] =
                                                                    cartItemModel.copy(
                                                                        isError = true,
                                                                        errorMessage =
                                                                        uploadPrescriptionUiModel.rejectedWording
                                                                    )
                                                                shouldResetCourier = true
                                                            } else {
                                                                shipmentCartItemModel.firstProductErrorIndex =
                                                                    0
                                                                shipmentCartItemModel.isError =
                                                                    true
                                                                shipmentCartItemModel.isAllItemError =
                                                                    true
                                                                for ((i1, cartItemModel1) in tempProductMap) {
                                                                    checkoutItems[i1] =
                                                                        cartItemModel1.copy(
                                                                            isError = true,
                                                                            isShopError = true
                                                                        )
                                                                }
                                                                shipmentCartItemModel.errorTitle =
                                                                    "Yaah, ada ${shipmentCartItemModel.products.size} barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain."
                                                                shipmentCartItemModel.isCustomEpharmacyError =
                                                                    true
                                                                shipmentCartItemModel.spId =
                                                                    0
                                                                shipmentCartItemModel =
                                                                    shipmentCartItemModel.copy(
                                                                        shipment = shipmentCartItemModel.shipment.copy(
                                                                            courierItemData = null
                                                                        )
                                                                    )
                                                                updated = true
                                                                break
                                                            }
                                                        } else if (epharmacyGroup.consultationStatus != null && epharmacyGroup.consultationStatus == EPHARMACY_CONSULTATION_STATUS_APPROVED) {
                                                            shipmentCartItemModel.tokoConsultationId =
                                                                epharmacyGroup.tokoConsultationId!!
                                                            shipmentCartItemModel.partnerConsultationId =
                                                                epharmacyGroup.partnerConsultationId!!
                                                            shipmentCartItemModel.consultationDataString =
                                                                epharmacyGroup.consultationString!!
                                                            mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                1
                                                            updated = true
                                                            break
                                                        } else if (epharmacyGroup.prescriptionImages != null && epharmacyGroup.prescriptionImages!!.isNotEmpty()) {
                                                            val prescriptionIds =
                                                                ArrayList<String>()
                                                            for (prescriptionImage in epharmacyGroup.prescriptionImages!!) {
                                                                if (prescriptionImage != null && !prescriptionImage.prescriptionId.isNullOrEmpty()
                                                                ) {
                                                                    prescriptionIds.add(
                                                                        prescriptionImage.prescriptionId!!
                                                                    )
                                                                }
                                                            }
                                                            shipmentCartItemModel.prescriptionIds =
                                                                prescriptionIds
                                                            mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                prescriptionIds.size
                                                            updated = true
                                                            break
                                                        }
                                                    }
                                                    if (cartItemModel.isError) {
                                                        productErrorCount += 1
                                                        firstProductErrorIndex =
                                                            if (firstProductErrorIndex < 0) {
                                                                i
                                                            } else {
                                                                minOf(
                                                                    i,
                                                                    firstProductErrorIndex
                                                                )
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (shouldResetCourier) {
                        shipmentCartItemModel.isHasUnblockingError = true
                        shipmentCartItemModel.firstProductErrorIndex =
                            firstProductErrorIndex
                        shipmentCartItemModel.unblockingErrorMessage =
                            "Yaah, ada $productErrorCount barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain."
                        shipmentCartItemModel.spId = 0
                        shipmentCartItemModel.shouldResetCourier = true
                        shipmentCartItemModel = shipmentCartItemModel.copy(
                            shipment = shipmentCartItemModel.shipment.copy(courierItemData = null)
                        )
                    }
                }
                checkoutItems[index] = shipmentCartItemModel
                tempProductMap = hashMapOf()
            }
            if (item is CheckoutEpharmacyModel) {
                break
            }
        }
        var totalPrescription = 0
        for (value in mapPrescriptionCount.values) {
            totalPrescription += value
        }
        uploadPrescriptionUiModel.epharmacyGroupIds = ArrayList(epharmacyGroupIds)
        uploadPrescriptionUiModel.isError = false
        uploadPrescriptionUiModel.uploadedImageCount = totalPrescription
        uploadPrescriptionUiModel.hasInvalidPrescription = hasInvalidPrescription
        uploadPrescriptionUiModel.enablerNames = ArrayList(enablerNames)
        uploadPrescriptionUiModel.shopIds = shopIds
        uploadPrescriptionUiModel.cartIds = cartIds
        checkoutItems[checkoutItems.size - 5] =
            CheckoutEpharmacyModel(epharmacy = uploadPrescriptionUiModel)
        return checkoutItems
    }

    fun validatePrescriptionOnBackPressed(epharmacy: CheckoutEpharmacyModel?): CheckoutEpharmacyModel? {
        if (epharmacy != null && epharmacy.epharmacy.showImageUpload) {
            if (epharmacy.epharmacy.uploadedImageCount > 0 || epharmacy.epharmacy.hasInvalidPrescription) {
                return epharmacy
            }
        }
        return null
    }

    suspend fun saveAddonsProduct(product: CheckoutProductModel, isOneClickShipment: Boolean) {
        withContext(dispatchers.io) {
            try {
                val params =
                    ShipmentAddOnProductServiceMapper.generateSaveAddOnProductRequestParamsNew(
                        product,
                        isOneClickShipment
                    )
                saveAddOnProductUseCase.setParams(params, true)
                saveAddOnProductUseCase.executeOnBackground()
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    suspend fun saveAddOnsProductBeforeCheckout(
        listData: List<CheckoutItem>,
        isOneClickShipment: Boolean
    ): CheckoutPageToaster? {
        return withContext(dispatchers.io) {
            val listCartItemModel = listData.filterIsInstance(CheckoutProductModel::class.java)
            if (listCartItemModel.indexOfFirst { it.addOnProduct.listAddOnProductData.isNotEmpty() } != -1) {
                val params =
                    ShipmentAddOnProductServiceMapper.generateSaveAddOnProductRequestParamsNew(
                        listCartItemModel,
                        isOneClickShipment
                    )
                saveAddOnProductUseCase.setParams(params, false)
                try {
                    val response = saveAddOnProductUseCase.executeOnBackground()
                    if (response.saveAddOns.status.equals(ShipmentViewModel.statusOK, true)) {
                        return@withContext null
                    } else {
                        return@withContext CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            toasterMessage = response.saveAddOns.errorMessage.firstOrNull()
                                ?: "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
                        )
                    }
                } catch (t: Throwable) {
                    Timber.d(t)
                    return@withContext CheckoutPageToaster(Toaster.TYPE_ERROR, throwable = t)
                }
            }
            return@withContext null
        }
    }
}
