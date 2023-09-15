package com.tokopedia.epharmacy.utils

import android.content.Context
import android.text.Html
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailInfoDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailPaymentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.params.CheckoutCartGeneralParams
import com.tokopedia.epharmacy.network.params.EPharmacyCheckoutParams
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.usecase.BuildConfig
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object EPharmacyUtils {

    fun logException(e: Exception) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(e)
        } else {
            e.printStackTrace()
        }
    }

    fun checkIsError(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel?): Boolean {
        return (
            (
                ePharmacyAttachmentDataModel != null &&
                    checkIsErrorForConsultationData(ePharmacyAttachmentDataModel) &&
                    checkIsErrorForPrescriptionImages(ePharmacyAttachmentDataModel)
                )
            )
    }

    private fun checkIsErrorForConsultationData(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel): Boolean {
        return (
            ePharmacyAttachmentDataModel.consultationData == null ||
                !arrayListOf(
                    EPharmacyConsultationStatus.APPROVED.status,
                    EPharmacyConsultationStatus.REJECTED.status
                )
                    .contains(ePharmacyAttachmentDataModel.consultationStatus)
            )
    }

    private fun checkIsErrorForPrescriptionImages(ePharmacyAttachmentDataModel: EPharmacyAttachmentDataModel): Boolean {
        return ePharmacyAttachmentDataModel.prescriptionImages?.isEmpty() == true
    }

    fun formatDateToLocal(currentFormat: String = YYYY_MM_DD_T_HH_MM_SS_Z, newFormat: String = NEW_DATE_FORMAT, dateString: String): Date? {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
            fromFormat.isLenient = false
            fromFormat.timeZone = TimeZone.getTimeZone(UTC)
            val toFormat: DateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
            toFormat.isLenient = false
            toFormat.timeZone = TimeZone.getDefault()

            fromFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun getTimeFromDate(date: Date?): String {
        date?.let {
            val fromFormat: DateFormat = SimpleDateFormat(HH_MM, Locale.ENGLISH)
            return fromFormat.format(date).toString()
        } ?: kotlin.run {
            return ""
        }
    }

    fun getConsultationIds(response: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        response?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            ids.add(gp?.consultationSource?.id.toString())
        }
        return ids
    }

    fun getPrescriptionIds(response: EPharmacyPrepareProductsGroupResponse?): ArrayList<String?> {
        val ids = arrayListOf<String?>()
        response?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            gp?.prescriptionImages?.forEach { pres ->
                if (!pres?.prescriptionId.isNullOrBlank()) {
                    ids.add(pres?.prescriptionId)
                }
            }
        }
        return ids
    }

    fun getShopIds(ePharmacyGroupId: String?, ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): List<String> {
        val ids = mutableListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return emptyList()
        for (gp in groups) {
            if (gp?.epharmacyGroupId == ePharmacyGroupId) {
                gp?.shopInfo?.forEach { si ->
                    si?.shopId?.takeIf { it.isNotBlank() }?.let { ids.add(it) }
                }
            }
        }
        return ids
    }

    fun getShopIds(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): List<String> {
        val ids = arrayListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return arrayListOf()
        groups.forEach { gp ->
            gp?.shopInfo?.forEach { si ->
                if (!si?.shopId.isNullOrBlank()) {
                    ids.add(si?.shopId ?: "")
                }
            }
        }
        return ids
    }

    fun getEnablers(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups?.forEach { gp ->
            if (!gp?.consultationSource?.enablerName.isNullOrBlank()) {
                ids.add(gp?.consultationSource?.enablerName ?: "")
            }
        }
        return ids
    }

    fun getGroupIds(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): ArrayList<String> {
        val ids = arrayListOf<String>()
        val groups = ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups ?: return arrayListOf()
        groups.forEach { gp ->
            if (!gp?.epharmacyGroupId.isNullOrBlank()) {
                ids.add(gp?.epharmacyGroupId ?: "")
            }
        }
        return ids
    }

    fun findGroup(ePharmacyGroupId: String?, ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup? {
        return ePharmacyPrepareProductsGroupResponse?.detailData?.groupsData?.epharmacyGroups?.find { group -> group?.epharmacyGroupId == ePharmacyGroupId }
    }

    fun getMiniConsultationModel(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup?): EPharmacyMiniConsultationResult {
        return EPharmacyMiniConsultationResult(
            group?.epharmacyGroupId,
            group?.shopInfo,
            group?.consultationData?.consultationStatus,
            group?.consultationData?.consultationString,
            group?.consultationData?.prescription,
            group?.consultationData?.partnerConsultationId,
            group?.consultationData?.tokoConsultationId.toString(),
            group?.prescriptionImages
        )
    }

    fun mapGroupsDataIntoDataModel(data: EPharmacyPrepareProductsGroupResponse): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        if (data.detailData?.groupsData?.attachmentPageTickerText?.isNotBlank() == true) {
            listOfComponents.add(
                EPharmacyTickerDataModel(
                    TICKER_COMPONENT,
                    TICKER_COMPONENT,
                    data.detailData?.groupsData?.attachmentPageTickerText,
                    data.detailData?.groupsData?.attachmentPageTickerLogoUrl,
                    EPHARMACY_TICKER_BACKGROUND
                )
            )
        }

        var orderNumber = 0
        data.detailData?.groupsData?.epharmacyGroups?.forEachIndexed { indexGroup, group ->
            if (!group?.shopInfo.isNullOrEmpty()) {
                group?.shopInfo?.forEachIndexed { shopIndex, info ->
                    if (info?.products?.isEmpty() != true) {
                        orderNumber++
                        listOfComponents.add(
                            getGroupComponent(
                                group,
                                orderNumber,
                                info,
                                shopIndex,
                                EPharmacyMapper.isLastIndex(data.detailData?.groupsData?.epharmacyGroups, indexGroup)
                            )
                        )
                    }
                }
            }
        }
        return EPharmacyDataModel(listOfComponents)
    }

    private fun getGroupComponent(
        group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
        orderNumber: Int,
        info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        shopIndex: Int,
        isLastGroup: Boolean
    ): BaseEPharmacyDataModel {
        return EPharmacyMapper.mapGroupsToAttachmentComponents(group, orderNumber, info, shopIndex, isLastGroup)
    }

    fun createCheckoutGeneralParams(ePharmacyCheckoutParams: EPharmacyCheckoutParams): CheckoutCartGeneralParams {
        return CheckoutCartGeneralParams(
            CheckoutCartGeneralParams.Transaction(
                arrayListOf(
                    CheckoutCartGeneralParams.Transaction.BusinessData(
                    ePharmacyCheckoutParams.businessId,
                    arrayListOf(
                        CheckoutCartGeneralParams.Transaction.BusinessData.CartGroup(
                            ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.cartGroupId, arrayListOf(
                                CheckoutCartGeneralParams.Transaction.BusinessData.CartGroup.Cart(
                                    ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.cartId,
                                    CheckoutCartGeneralParams.Transaction.BusinessData.CartGroup.Cart.Metadata(
                                        ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.metadata?.enablerId,
                                        ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.metadata?.epharmacyGroupId,
                                        ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.metadata?.tokoConsultationId
                                    ),
                                    ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.price,
                                    ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.productId,
                                    ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.quantity,
                                    ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup?.carts?.firstOrNull()?.shopId
                                )
                            )
                        )
                    ),
                    ePharmacyCheckoutParams.checkoutBusinessType,
                    ePharmacyCheckoutParams.checkoutDataType,
                )), ePharmacyCheckoutParams.flowType
            )
        )
    }

    fun createAtcParams(groupId: String, enablerId: String, tConsultationId: String, ePharmacyCheckoutParams: EPharmacyCheckoutParams): CartGeneralAddToCartInstantParams {
        return CartGeneralAddToCartInstantParams(
            CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData(
                ePharmacyCheckoutParams.businessId,
                arrayListOf(
                    CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData.CartGeneralAddToCartInstantRequestProductData(
                        ePharmacyCheckoutParams.productId,
                        ePharmacyCheckoutParams.productQuantity,
                        CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData.CartGeneralAddToCartInstantRequestProductData.CartGeneralCustomStruct(
                            groupId,
                            enablerId,
                            tConsultationId
                        ),
                        ePharmacyCheckoutParams.shopId,
                        ePharmacyCheckoutParams.note
                    )
                )
            ),
            EPHARMACY_ANDROID_SOURCE
        )
    }

    fun getTextFromHtml(htmlText: String): CharSequence =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlText)
        }

    fun getTotalAmount(quantity: Int?, price: Double?): Double {
        return ((quantity?.toDouble().orZero()) * ((price.orZero())))
    }

    fun getTotalAmountFmt(subTotal: Double?): String {
        return "Rp$subTotal"
    }

    fun getChatDokterNote(context: Context?, operatingSchedule: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource.OperatingSchedule?, note: String?): String {
        if(operatingSchedule?.isClosingHour == true){
            val openTimeLocal: Date? = formatDateToLocal(dateString = operatingSchedule.daily?.openTime.orEmpty())
            val closeTimeLocal: Date? = formatDateToLocal(dateString = operatingSchedule.daily?.closeTime.orEmpty())
            return context?.resources?.getString(
                com.tokopedia.epharmacy.R.string.epharmacy_chooser_outside,
                getTimeFromDate(openTimeLocal),
                getTimeFromDate(closeTimeLocal)
            ).orEmpty()
        }
        return note.orEmpty()
    }

    internal fun mapResponseToOrderDetail(orderData: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderData?): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        listOfComponents.add(EPharmacyOrderDetailHeaderDataModel(
            ORDER_HEADER_COMPONENT,ORDER_HEADER_COMPONENT,
            orderData?.orderStatusDesc,
            orderData?.ticker?.type,
            orderData?.ticker?.message,
            orderData?.invoiceNumber,
            "",
            "",
            ""
        ))
        listOfComponents.add(EPharmacyOrderDetailInfoDataModel(
            ORDER_INFO_COMPONENT, ORDER_INFO_COMPONENT,
            "",
            orderData?.consultationSource?.enablerName,
            orderData?.consultationSource?.operatingSchedule?.duration,
            orderData?.consultationSource?.priceStr,
            ""
        ))
        listOfComponents.add(EPharmacyOrderDetailPaymentDataModel(
            ORDER_PAYMENT_COMPONENT, ORDER_PAYMENT_COMPONENT,
            orderData?.paymentMethod,
            orderData?.paymentAmountStr,
            ""
        ))
        return EPharmacyDataModel(listOfComponents)
    }
}

enum class PrescriptionActionType(val type: String) {
    REDIRECT_PWA("REDIRECT_CONSULTATION_PWA"),
    REDIRECT_OPTION("SHOW_PRESCRIPTION_ATTACHMENT_OPTION"),
    REDIRECT_UPLOAD("REDIRECT_UPLOAD_PRESC_PAGE"),
    REDIRECT_PRESCRIPTION("REDIRECT_CONSULTATION_PRESCRIPTION"),
    REDIRECT_CHECK_PRESCRIPTION("REDIRECT_CEK_PRESCRIPTION_PAGE")
}
