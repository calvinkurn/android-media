package com.tokopedia.purchase_platform.common.feature.insurance

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsuranceCartShopsViewModel(

        var shopId: Long = 0L,
        var shopItemsList: ArrayList<InsuranceCartShopItemsViewModel> = ArrayList()

) : Parcelable

@Parcelize
data class InsuranceCartShopItemsViewModel(

        var productId: Long = 0L,
        var digitalProductList: ArrayList<InsuranceCartDigitalProductViewModel> = ArrayList()

) : Parcelable

@Parcelize
data class InsuranceCartDigitalProductViewModel(
        var digitalProductId: Long,
        var cartItemId: Long,
        var typeId: Long,
        var pricePerProduct: Long,
        var totalPrice: Long,
        var optIn: Boolean,
        var isProductLevel: Boolean,
        var isPurchaseProtection: Boolean,
        var isSellerMoney: Boolean,
        var isApplicationNeeded: Boolean,
        var isNew: Boolean,
        var productInfo: InsuranceCartProductInfoViewModel = InsuranceCartProductInfoViewModel(),
        var applicationDetails: ArrayList<InsuranceProductApplicationDetailsViewModel> = ArrayList()

) : Parcelable

@Parcelize
data class InsuranceCartProductInfoViewModel(
        var title: String = "",
        var subTitle: String = "",
        var description: String = "",
        var iconUrl: String = "",
        var detailInfoTitle: String = "",
        var sectionTitle: String= "" ,
        var infoText: String = "",
        var appLinkUrl: String = "",
        var linkName: String = ""

) : Parcelable

@Parcelize
data class InsuranceProductApplicationDetailsViewModel(
        var id: Int = 0,
        var label: String = "",
        var placeHolder: String = "",
        var type: String = "",
        var isRequired: Boolean = false,
        var isError: Boolean = false,
        var value: String = "",
        var valuesList: ArrayList<InsuranceApplicationValueViewModel> = ArrayList(),
        var validationsList: ArrayList<InsuranceApplicationValidationViewModel> = ArrayList()
) : Parcelable

@Parcelize
data class InsuranceApplicationValueViewModel(
        var valuesId: Int = 0,
        var value: String = ""
) : Parcelable

@Parcelize
data class InsuranceApplicationValidationViewModel(
        var validationId: Int = 0,
        var type: String = "",
        var validationValue: String = "",
        var validationErrorMessage: String = ""
) : Parcelable