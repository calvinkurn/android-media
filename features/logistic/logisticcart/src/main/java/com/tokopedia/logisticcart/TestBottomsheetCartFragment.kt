package com.tokopedia.logisticcart

import android.app.Service
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodDataPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PriceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RangePriceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.Texts
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.DividerModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.unifycomponents.UnifyButton

class TestBottomsheetCartFragment : Fragment() {

    private var button : UnifyButton? = null

    private val firstCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "kirimin"
            shipperId = 1
            shipperProductId = 1
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1000
            }
        }
        ratesId = "0"
    }

    private val secondCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 2
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1500
            }
        }
        ratesId = "0"
    }

    private val firstDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 1
            serviceName = "durasi (1 hari)"
            error = ErrorServiceData()
            texts = ServiceTextData().apply {
                textEtaSummarize = "Estimasi tiba 1 - 4 Dec"
                textEtd = "1-3 hari"
                textRangePrice = "Rp26.000 - Rp34.000"
                textServiceDesc = ""
            }
            rangePrice = RangePriceData().apply {
                minPrice = 26000
                maxPrice = 34000
            }
        }
        shippingCourierViewModelList = listOf(firstCourierFirstDuration, secondCourierFirstDuration)
    }

    private val firstCourierSecondDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 3
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 2000
            }
        }
        ratesId = "0"
    }

    private val secondDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 2
            serviceName = "durasi (2 hari)"
            error = ErrorServiceData()
            texts = ServiceTextData().apply {
                textEtaSummarize = "Estimasi tiba 1 - 4 Dec"
                textEtd = "1-3 hari"
                textRangePrice = "Rp26.000 - Rp34.000"
                textServiceDesc = ""
            }
            rangePrice = RangePriceData().apply {
                minPrice = 26000
                maxPrice = 34000
            }
        }
        shippingCourierViewModelList = listOf(firstCourierSecondDuration)
    }

    private val logisticPromoFirst = LogisticPromoUiModel("bbo", "bbo", "bbo", firstCourierSecondDuration.productData.shipperName,
        secondDuration.serviceData.serviceId, firstCourierSecondDuration.productData.shipperId, firstCourierSecondDuration.productData.shipperProductId,
        "", "", "", false, "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/BOE_Badge_10k_new.png",
        500, 2000, 1500, false, false, CodDataPromo(), EstimatedTimeArrivalPromo(), "Bebas Ongkir (Rp 0)", "Bebas Ongkir", "Tersedia bbo", false)

    private val logisticPromoEko = LogisticPromoUiModel("boeko", "boeko", "boeko", firstCourierSecondDuration.productData.shipperName,
        secondDuration.serviceData.serviceId, firstCourierSecondDuration.productData.shipperId, firstCourierSecondDuration.productData.shipperProductId,
        "", "", "", false, "",
        500, 2000, 1500, false, false, CodDataPromo(), EstimatedTimeArrivalPromo(), "Bebas Ongkir (Rp 0)", "Bebas Ongkir", "Tersedia bbo", false)

    val shippingRecommendationData = ShippingRecommendationData().apply {
        logisticPromo = logisticPromoFirst
        shippingDurationUiModels = listOf(firstDuration, secondDuration)
        listLogisticPromo = listOf(logisticPromoFirst, logisticPromoEko)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_bottomsheet_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById<UnifyButton>(R.id.btn_test)
        button?.setOnClickListener {
            openDurationBottomSheet()
        }
    }

    private fun openDurationBottomSheet() {
        val list = shippingRecommendationData.listLogisticPromo + listOf<RatesViewModelType>(
            DividerModel()
        ) + shippingRecommendationData.shippingDurationUiModels
                ShippingDurationOccBottomSheet().showBottomSheet(this, list, object :
            ShippingDurationOccBottomSheetListener {
            override fun onDurationChosen(serviceData: ServiceData, selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {

            }

            override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {

            }
        })
    }
}