package com.tokopedia.digital.product.view.model

import android.os.Parcelable
import com.tokopedia.common_digital.product.presentation.model.ClientNumber
import com.tokopedia.common_digital.product.presentation.model.Operator
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author anggaprasetiyo on 4/25/17.
 */
@Parcelize
class CategoryData(
        @JvmField
        var categoryId: String? = null,
        @JvmField
        private var categoryType: String? = null,
        @JvmField
        var titleText: String? = null,
        @JvmField
        var name: String? = null,
        @JvmField
        var icon: String? = null,
        @JvmField
        var iconUrl: String? = null,
        @JvmField
        var isNew: Boolean = false,
        @JvmField
        var isInstantCheckout: Boolean = false,
        @JvmField
        var slug: String? = null,
        @JvmField
        var defaultOperatorId: String? = null,
        @JvmField
        var operatorStyle: String? = null,
        @JvmField
        var operatorLabel: String? = null,
        @JvmField
        var additionalFeature: AdditionalFeature? = null,
        @JvmField
        var clientNumberList: List<ClientNumber>? = ArrayList(),
        @JvmField
        var operatorList: List<Operator>? = ArrayList(),
        @JvmField
        var bannerDataListIncluded: List<BannerData>? = ArrayList(),
        @JvmField
        var otherBannerDataListIncluded: List<BannerData>? = ArrayList(),
        @JvmField
        var guideDataList: List<GuideData>? = ArrayList()) : Parcelable {

    constructor(builder: Builder) : this() {
        categoryId = builder.categoryId
        titleText = builder.titleText
        name = builder.name
        icon = builder.icon
        iconUrl = builder.iconUrl
        isNew = builder.isNew
        isInstantCheckout = builder.instantCheckout
        slug = builder.slug
        defaultOperatorId = builder.defaultOperatorId
        operatorStyle = builder.operatorStyle
        operatorLabel = builder.operatorLabel
        additionalFeature = builder.additionalFeature
        clientNumberList = builder.clientNumberList
        operatorList = builder.operatorList
        bannerDataListIncluded = builder.bannerDataListIncluded
        otherBannerDataListIncluded = builder.otherBannerDataListIncluded
        guideDataList = builder.guideDataList
    }

    val isSupportedStyle: Boolean
        get() = Arrays.asList(*STYLE_COLLECTION_SUPPORTED).contains(operatorStyle)

    class Builder {
        var categoryId: String? = null
        var categoryType: String? = null
        var titleText: String? = null
        var name: String? = null
        var icon: String? = null
        var iconUrl: String? = null
        var teaser: Teaser? = null
        var isNew = false
        var instantCheckout = false
        var slug: String? = null
        var defaultOperatorId: String? = null
        var operatorStyle: String? = null
        var operatorLabel: String? = null
        var additionalFeature: AdditionalFeature? = null
        var clientNumberList: List<ClientNumber>? = null
        var operatorList: List<Operator>? = null
        var bannerDataListIncluded: List<BannerData>? = null
        var otherBannerDataListIncluded: List<BannerData>? = null
        var guideDataList: List<GuideData>? = null
        fun categoryId(`val`: String?): Builder {
            categoryId = `val`
            return this
        }

        fun categoryType(`val`: String?): Builder {
            categoryType = `val`
            return this
        }

        fun titleText(`val`: String?): Builder {
            titleText = `val`
            return this
        }

        fun name(`val`: String?): Builder {
            name = `val`
            return this
        }

        fun icon(`val`: String?): Builder {
            icon = `val`
            return this
        }

        fun iconUrl(`val`: String?): Builder {
            iconUrl = `val`
            return this
        }

        fun teaser(`val`: Teaser?): Builder {
            teaser = `val`
            return this
        }

        fun isNew(`val`: Boolean): Builder {
            isNew = `val`
            return this
        }

        fun instantCheckout(`val`: Boolean): Builder {
            instantCheckout = `val`
            return this
        }

        fun slug(`val`: String?): Builder {
            slug = `val`
            return this
        }

        fun defaultOperatorId(`val`: String?): Builder {
            defaultOperatorId = `val`
            return this
        }

        fun operatorStyle(`val`: String?): Builder {
            operatorStyle = `val`
            return this
        }

        fun operatorLabel(`val`: String?): Builder {
            operatorLabel = `val`
            return this
        }

        fun additionalFeature(`val`: AdditionalFeature?): Builder {
            additionalFeature = `val`
            return this
        }

        fun clientNumberList(`val`: List<ClientNumber>?): Builder {
            clientNumberList = `val`
            return this
        }

        fun operatorList(`val`: List<Operator>?): Builder {
            operatorList = `val`
            return this
        }

        fun bannerDataListIncluded(`val`: List<BannerData>?): Builder {
            bannerDataListIncluded = `val`
            return this
        }

        fun otherBannerDataListIncluded(`val`: List<BannerData>?): Builder {
            otherBannerDataListIncluded = `val`
            return this
        }

        fun guideDataList(`val`: List<GuideData>?): Builder {
            guideDataList = `val`
            return this
        }

        fun build(): CategoryData {
            return CategoryData(this)
        }
    }

    companion object {
        const val STYLE_PRODUCT_CATEGORY_1 = "style_1"
        const val STYLE_PRODUCT_CATEGORY_2 = "style_2"
        const val STYLE_PRODUCT_CATEGORY_3 = "style_3"
        const val STYLE_PRODUCT_CATEGORY_4 = "style_4"
        const val STYLE_PRODUCT_CATEGORY_5 = "style_5"
        const val STYLE_PRODUCT_CATEGORY_99 = "style_99"
        const val SLUG_PRODUCT_CATEGORY_PULSA = "pulsa"
        const val SLUG_PRODUCT_CATEGORY_EMONEY = "emoney"
        private val STYLE_COLLECTION_SUPPORTED = arrayOf(
                STYLE_PRODUCT_CATEGORY_1, STYLE_PRODUCT_CATEGORY_2, STYLE_PRODUCT_CATEGORY_2,
                STYLE_PRODUCT_CATEGORY_3, STYLE_PRODUCT_CATEGORY_4, STYLE_PRODUCT_CATEGORY_5,
                STYLE_PRODUCT_CATEGORY_99
        )
    }
}