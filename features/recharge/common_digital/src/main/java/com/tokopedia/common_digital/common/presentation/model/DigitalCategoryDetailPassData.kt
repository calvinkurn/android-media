package com.tokopedia.common_digital.common.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 5/10/17.
 */
@Parcelize
class DigitalCategoryDetailPassData : Parcelable {

    var categoryId: String? = null
    var operatorId: String? = null
    var productId: String? = null
    var clientNumber: String? = null
    var menuId: String? = null
    var isFromWidget = false
    var isCouponApplied = false
    var url: String? = null
    var appLinks: String? = null
    var categoryName: String? = null
    var additionalETollBalance: String? = null
    var additionalETollLastUpdatedDate: String? = null
    var additionalETollOperatorName: String? = null

    private constructor(builder: Builder) {
        categoryId = builder.categoryId
        operatorId = builder.operatorId
        menuId = builder.menuId
        productId = builder.productId
        clientNumber = builder.clientNumber
        isFromWidget = builder.isFromWidget
        isCouponApplied = builder.isCouponApplied
        url = builder.url
        appLinks = builder.appLinks
        categoryName = builder.categoryName
        additionalETollBalance = builder.additionalETollLastBalance
        additionalETollLastUpdatedDate = builder.additionalETollLastUpdatedDate
        additionalETollOperatorName = builder.additionalETollOperatorName
    }

    class Builder {
        var categoryId: String? = null
        var operatorId: String? = null
        var productId: String? = null
        var menuId: String? = null
        var clientNumber: String? = null
        var isFromWidget = false
        var isCouponApplied = false
        var url: String? = null
        var appLinks: String? = null
        var categoryName: String? = null
        var additionalETollLastBalance: String? = null
        var additionalETollLastUpdatedDate: String? = null
        var additionalETollOperatorName: String? = null

        fun menuId(`val`: String?): Builder {
            menuId = `val`
            return this
        }

        fun categoryId(`val`: String?): Builder {
            categoryId = `val`
            return this
        }

        fun operatorId(`val`: String?): Builder {
            operatorId = `val`
            return this
        }

        fun productId(`val`: String?): Builder {
            productId = `val`
            return this
        }

        fun clientNumber(`val`: String?): Builder {
            clientNumber = `val`
            return this
        }

        fun isFromWidget(`val`: Boolean): Builder {
            isFromWidget = `val`
            return this
        }

        fun isCouponApplied(`val`: Boolean): Builder {
            isCouponApplied = `val`
            return this
        }

        fun url(`val`: String?): Builder {
            url = `val`
            return this
        }

        fun appLinks(`val`: String?): Builder {
            appLinks = `val`
            return this
        }

        fun categoryName(`val`: String?): Builder {
            categoryName = `val`
            return this
        }

        fun additionalETollLastBalance(`val`: String?): Builder {
            additionalETollLastBalance = `val`
            return this
        }

        fun additionalETollLastUpdatedDate(`val`: String?): Builder {
            additionalETollLastUpdatedDate = `val`
            return this
        }

        fun additionalETollOperatorName(`val`: String?): Builder {
            additionalETollOperatorName = `val`
            return this
        }

        fun build(): DigitalCategoryDetailPassData {
            return DigitalCategoryDetailPassData(this)
        }
    }

    companion object {
        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_IS_FROM_WIDGET = "is_from_widget"
    }
}