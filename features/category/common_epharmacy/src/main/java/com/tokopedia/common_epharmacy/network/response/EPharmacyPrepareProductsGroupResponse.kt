package com.tokopedia.common_epharmacy.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EPharmacyPrepareProductsGroupResponse(
    @SerializedName("prepareProductsGroup")
    val detailData: EPharmacyPrepareProductsGroupData?
) : Parcelable {

    @Parcelize
    data class EPharmacyPrepareProductsGroupData(
        @SerializedName("data")
        val groupsData: GroupData?
    ) : Parcelable {

        @Parcelize
        data class GroupData(
            @SerializedName("attachment_page_ticker_text")
            val attachmentPageTickerText: String?,
            @SerializedName("epharmacy_groups")
            val epharmacyGroups: List<EpharmacyGroup?>?
        ) : Parcelable {
            @Parcelize
            data class EpharmacyGroup(
                @SerializedName("consultation_data")
                val consultationData: ConsultationData?,
                @SerializedName("consultation_source")
                val consultationSource: ConsultationSource?,
                @SerializedName("epharmacy_group_id")
                val epharmacyGroupId: String?,
                @SerializedName("number_prescription_images")
                val numberPrescriptionImages: Int?,
                @SerializedName("prescription_images")
                val prescriptionImages: List<PrescriptionImage?>?,
                @SerializedName("prescription_source")
                val prescriptionSource: List<String?>?,
                @SerializedName("products_info")
                val shopInfo: List<ProductsInfo?>?
            ) : Parcelable {
                @Parcelize
                data class ConsultationData(
                    @SerializedName("consultation_status")
                    val consultationStatus: Int?,
                    @SerializedName("consultation_string")
                    val consultationString: String?,
                    @SerializedName("doctor_details")
                    val doctorDetails: DoctorDetails?,
                    @SerializedName("end_time")
                    val endTime: String?,
                    @SerializedName("medical_recommendation")
                    val medicalRecommendation: List<MedicalRecommendation?>?,
                    @SerializedName("partner_consultation_id")
                    val partnerConsultationId: String?,
                    @SerializedName("prescription")
                    val prescription: List<Prescription?>?,
                    @SerializedName("start_time")
                    val startTime: String?,
                    @SerializedName("toko_consultation_id")
                    val tokoConsultationId: String?
                ) : Parcelable {
                    @Parcelize
                    data class DoctorDetails(
                        @SerializedName("name")
                        val name: String?,
                        @SerializedName("specialties")
                        val specialties: String?
                    ) : Parcelable

                    @Parcelize
                    data class MedicalRecommendation(
                        @SerializedName("product_id")
                        val productId: String?,
                        @SerializedName("product_name")
                        val productName: String?,
                        @SerializedName("quantity")
                        val quantity: Int?
                    ) : Parcelable

                    @Parcelize
                    data class Prescription(
                        @SerializedName("document_url")
                        val documentUrl: String?,
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("type")
                        val type: String?
                    ) : Parcelable
                }

                @Parcelize
                data class ConsultationSource(
                    @SerializedName("enabler_name")
                    val enablerName: String?,
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("operating_schedule")
                    val operatingSchedule: OperatingSchedule?,
                    @SerializedName("enabler_logo_url")
                    val enablerLogoUrl: String?,
                    @SerializedName("pwa_link")
                    val pwaLink: String?,
                    @SerializedName("status")
                    val status: String?
                ) : Parcelable {
                    @Parcelize
                    data class OperatingSchedule(
                        @SerializedName("close_days")
                        val closeDays: List<String?>?,
                        @SerializedName("daily")
                        val daily: Daily?
                    ) : Parcelable {
                        @Parcelize
                        data class Daily(
                            @SerializedName("close_time")
                            val closeTime: String?,
                            @SerializedName("open_time")
                            val openTime: String?
                        ) : Parcelable
                    }
                }

                @Parcelize
                data class PrescriptionImage(
                    @SerializedName("expired_at")
                    val expiredAt: String?,
                    @SerializedName("prescription_id")
                    val prescriptionId: String?,
                    @SerializedName("reject_reason")
                    val rejectReason: String?,
                    @SerializedName("status")
                    val status: String?
                ) : Parcelable

                @Parcelize
                data class ProductsInfo(
                    @SerializedName("partner_logo_url")
                    val partnerLogoUrl: String?,
                    @SerializedName("products")
                    val products: List<Product?>?,
                    @SerializedName("shop_id")
                    val shopId: String?,
                    @SerializedName("shop_location")
                    val shopLocation: String?,
                    @SerializedName("shop_logo_url")
                    val shopLogoUrl: String?,
                    @SerializedName("shop_name")
                    val shopName: String?,
                    @SerializedName("shop_type")
                    val shopType: String?
                ) : Parcelable {
                    @Parcelize
                    data class Product(
                        @SerializedName("is_ethical_drug")
                        val isEthicalDrug: Boolean?,
                        @SerializedName("item_weight")
                        val itemWeight: Double?,
                        @SerializedName("name")
                        val name: String?,
                        @SerializedName("product_id")
                        val productId: Long?,
                        @SerializedName("product_image")
                        val productImage: String?,
                        @SerializedName("product_total_weight_fmt")
                        val productTotalWeightFmt: String?,
                        @SerializedName("quantity")
                        val quantity: Int?
                    ) : Parcelable
                }
            }
        }
    }
}

@Parcelize
data class EPharmacyItemButtonData(
    @SerializedName("iconUrl")
    var iconUrl: String? = "",
    @SerializedName("text")
    var text: String? = "",
    @SerializedName("subText")
    var subText: String? = "",
    @SerializedName("appLink")
    var appLink: String? = ""
) : Parcelable
