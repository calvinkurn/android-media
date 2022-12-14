package com.tokopedia.tkpd.flashsale.presentation.detail

import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProductData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import java.util.*
import kotlin.collections.List

object DummyDataHelper {
    fun generateDummyCampaignDetailData(tabName: FlashSaleListPageTab = FlashSaleListPageTab.UPCOMING): FlashSale {
        val flashSaleStartDate = GregorianCalendar(2022, 10, 10, 0, 0, 0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10, 20, 0, 0, 0).time
        val flashSaleReview = GregorianCalendar(2022, 10, 5, 0, 0, 0).time
        val flashSaleSubmission = GregorianCalendar(2022, 10, 1, 7, 0, 0).time

        return FlashSale(
            1,
            "",
            "",
            "",
            flashSaleEndDate,
            1,
            "Flash Sale 1",
            true,
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            1,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING,
            emptyList(),
            tabName
        )
    }

    fun generateDummySubmittedProductData(): SubmittedProductData {
        return SubmittedProductData(
            productList = listOf(
                SubmittedProduct(
                    0,
                    true,
                    true,
                    0,
                    0,
                    0,
                    "Sample Product",
                    "",
                    SubmittedProduct.ProductCriteria(0),
                    0,
                    "",
                    SubmittedProduct.Price(0.0, 0.0, 0.0),
                    SubmittedProduct.Discount(0, 0, 0),
                    SubmittedProduct.DiscountedPrice(0.0, 0.0, 0.0),
                    ProductStockStatus.MULTI_VARIANT_MULTI_LOCATION,
                    listOf(),
                    0,
                    "",
                    0
                )
            ),
            totalProduct = 0
        )
    }

    fun generateWaitingForSelectionProductData(): List<WaitingForSelectionItem> {
        return listOf(
            WaitingForSelectionItem(
                0,
                true,
                true,
                0,
                0,
                "Sample Product",
                "",
                SubmittedProduct.ProductCriteria(0),
                0,
                "",
                SubmittedProduct.Price(0.0, 0.0, 0.0),
                SubmittedProduct.Discount(0, 0, 0),
                SubmittedProduct.DiscountedPrice(0.0, 0.0, 0.0),
                ProductStockStatus.MULTI_VARIANT_MULTI_LOCATION,
                listOf(),
                0,
                false,
                false
            )
        )
    }

    fun generateDummyFlashSaleData(): FlashSale {
        val flashSaleStartDate = GregorianCalendar(2022, 10, 10, 0, 0, 0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10, 20, 0, 0, 0).time
        val flashSaleReview = GregorianCalendar(2022, 10, 5, 0, 0, 0).time
        val flashSaleSubmission = GregorianCalendar(2022, 10, 1, 7, 0, 0).time

        return FlashSale(
            1,
            "",
            "",
            "",
            flashSaleEndDate,
            1,
            "Flash Sale 1",
            true,
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            TabConstant.TAB_ID_UPCOMING,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING,
            emptyList(),
            FlashSaleListPageTab.UPCOMING
        )
    }

    fun generateDummyTimelineBottomSheetModel(): CampaignDetailBottomSheetModel {
        return CampaignDetailBottomSheetModel(
            listOf(
                TimelineStepModel(
                    title = "Periode Pendaftaran",
                    period = "01-01 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 98
                ),
                TimelineStepModel(
                    title = "Tambah Produk",
                    period = "01-01 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 104
                ),
                TimelineStepModel(
                    title = "Proses Seleksi",
                    "05-05 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 298
                ),
                TimelineStepModel(
                    title = "Promosi Aktif",
                    period = "10-20 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 144
                ),
                TimelineStepModel(
                    title = "",
                    period = "",
                    isEnded = true,
                    isActive = true,
                    icon = 208
                )
            ),
            productCriterias = emptyList(),
            showTimeline = true,
            showCriteria = false,
            showProductCriteria = false
        )
    }

    fun generateDummyProductCriteriaBottomSheetModel(): CampaignDetailBottomSheetModel {
        return CampaignDetailBottomSheetModel(
            emptyList(),
            productCriterias = emptyList(),
            showTimeline = false,
            showCriteria = true,
            showProductCriteria = true
        )
    }

    fun generateDummyGeneralBottomSheetModel(): CampaignDetailBottomSheetModel {
        return CampaignDetailBottomSheetModel(
            listOf(
                TimelineStepModel(
                    title = "Periode Pendaftaran",
                    period = "01-01 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 98
                ),
                TimelineStepModel(
                    title = "Tambah Produk",
                    period = "01-01 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 104
                ),
                TimelineStepModel(
                    title = "Proses Seleksi",
                    "05-05 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 298
                ),
                TimelineStepModel(
                    title = "Promosi Aktif",
                    period = "10-20 Nov 2022",
                    isEnded = true,
                    isActive = true,
                    icon = 144
                ),
                TimelineStepModel(
                    title = "",
                    period = "",
                    isEnded = true,
                    isActive = true,
                    icon = 208
                )
            ),
            productCriterias = emptyList(),
            showTimeline = true,
            showCriteria = true,
            showProductCriteria = true
        )
    }
}
