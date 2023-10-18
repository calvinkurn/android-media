package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import android.os.Parcelable
import androidx.annotation.IntDef
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.ALL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.NO_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.PARTIAL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.VALUE_ERROR
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactory
import kotlinx.parcelize.Parcelize
import java.util.Date

data class ShopDiscountSetupProductUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listSetupProductData: List<SetupProductData> = listOf()
) {
    @Parcelize
    data class SetupProductData(
        val productId: String = "",
        val productName: String = "",
        val productImageUrl: String = "",
        val stock: String = "",
        val listProductWarehouse: List<ProductWarehouse> = listOf(),
        val slashPriceInfo: ProductSlashPriceInfo = ProductSlashPriceInfo(),
        val price: ProductPrice = ProductPrice(),
        val listProductVariant: List<SetupProductData> = listOf(),
        var productStatus: ProductStatus = ProductStatus(),
        var variantStatus: VariantStatus = VariantStatus(),
        var mappedResultData: MappedResultData = MappedResultData()
    ) : Parcelable, Visitable<ShopDiscountManageDiscountTypeFactory> {

        fun getListEnabledProductWarehouse(): List<ProductWarehouse> {
            return listProductWarehouse.filter {
                !it.disable
            }
        }

        @Parcelize
        data class MappedResultData(
            val minOriginalPrice: Int = 0,
            val maxOriginalPrice: Int = 0,
            val minDisplayedPrice: Int = 0,
            val maxDisplayedPrice: Int = 0,
            val minDiscountPercentage: Int = 0,
            val maxDiscountPercentage: Int = 0,
            val totalVariant: Int = 0,
            val totalDiscountedVariant: Int = 0,
            val totalLocation: Int = 0,
            val minStartDateUnix: Long? = null,
            val minEndDateUnix: Long? = null
        ) : Parcelable

        @Parcelize
        data class ProductStatus(
            val isProductDiscounted: Boolean = false,
            val isVariant: Boolean = false,
            val isMultiLoc: Boolean = false,
            var errorType: Int = 0,
            val selectedSlashPriceStatus: Int = 0
        ) : Parcelable

        @Parcelize
        data class VariantStatus(
            val isMultiLoc: Boolean = false,
            var isVariantEnabled: Boolean? = null
        ) : Parcelable

        @Parcelize
        data class ProductWarehouse(
            val warehouseId: String = "",
            val warehouseName: String = "",
            val warehouseLocation: String = "",
            val warehouseStock: String = "",
            var maxOrder: String = "",
            val abusiveRule: Boolean = false,
            val avgSoldPrice: Int = 0,
            val cheapestPrice: Int = 0,
            var discountedPrice: Int = 0,
            var discountedPercentage: Int = 0,
            val minRecommendationPrice: Int = 0,
            val minRecommendationPercentage: Int = 0,
            val maxRecommendationPrice: Int = 0,
            val maxRecommendationPercentage: Int = 0,
            val disable: Boolean = false,
            val disableRecommendation: Boolean = true,
            val warehouseType: Int = 0,
            val originalPrice: Int = 0
        ) : Parcelable

        @Parcelize
        data class ProductSlashPriceInfo(
            val slashPriceProductId: String = "",
            var discountedPrice: Int = 0,
            var discountPercentage: Int = 0,
            var startDate: Date = Date(),
            var endDate: Date = Date(),
            val slashPriceStatusId: String = ""
        ) : Parcelable

        @Parcelize
        data class ProductPrice(
            val min: Int = 0,
            val minFormatted: String = "",
            val max: Int = 0,
            val maxFormatted: String = ""
        ) : Parcelable

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(NO_ERROR, VALUE_ERROR, PARTIAL_ABUSIVE_ERROR, ALL_ABUSIVE_ERROR)
        annotation class ErrorType {
            companion object {
                const val NO_ERROR = 0
                const val VALUE_ERROR = 1
                const val PARTIAL_ABUSIVE_ERROR = 2
                const val ALL_ABUSIVE_ERROR = 3
                const val R2_ABUSIVE_ERROR = 4
                const val START_DATE_ERROR = 5
            }
        }

        override fun type(typeFactory: ShopDiscountManageDiscountTypeFactory): Int {
            return typeFactory.type(this)
        }
    }
}
