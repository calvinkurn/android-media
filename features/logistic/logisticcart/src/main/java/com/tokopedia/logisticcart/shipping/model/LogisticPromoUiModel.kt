package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodDataPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import kotlinx.android.parcel.Parcelize

/**
 * Created by fajarnuha on 29/03/19.
 */
@Parcelize
data class LogisticPromoUiModel(val promoCode: String,
                                val title: String,
                                val description: String,
                                val shipperName: String,
                                val serviceId: Int,
                                val shipperId: Int,
                                val shipperProductId: Int,
                                val shipperDesc: String,
                                val disableText: String,
                                val dialogMsg: String,
                                val isApplied: Boolean,
                                val imageUrl: String,
                                val discountedRate: Int,
                                val shippingRate: Int,
                                val benefitAmount: Int,
                                val disabled: Boolean,
                                val hideShipperName: Boolean,
                                val codData: CodDataPromo,
                                val etaData: EstimatedTimeArrivalPromo,
                                val isBebasOngkirExtra: Boolean) : RatesViewModelType, Parcelable