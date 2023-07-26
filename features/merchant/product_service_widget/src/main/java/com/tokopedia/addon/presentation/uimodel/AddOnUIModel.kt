package com.tokopedia.addon.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class AddOnUIModel(
    var id: String = "",
    var name: String = "",
    var price: Long = 0L,
    var discountedPrice: Long = 0L,
    var isSelected: Boolean = false,
    var isPreselected: Boolean = false,
    var isMandatory: Boolean = false,
    var addOnType: Int = 0,
    var eduLink: String = "",
    var uniqueId: String = "",
    var description: String = "",
    var shopId: String = ""
) : Serializable {
    fun getSelectedStatus(): AddOnSelectedStatus {
        return when {
            isMandatory -> AddOnSelectedStatus.MANDATORY
            isPreselected && !isSelected -> AddOnSelectedStatus.UNCHECKED
            !isPreselected && isSelected -> AddOnSelectedStatus.CHECKED
            else -> AddOnSelectedStatus.DEFAULT
        }
    }

    fun isDiscounted(): Boolean = price != discountedPrice
}

data class AddOnGroupUIModel(
    val title: String = "",
    val iconUrl: String = "",
    val iconDarkmodeUrl: String = "",
    val productId: Long = 0L,
    val warehouseId: Long = 0L,
    val addOnLevel: String = "",
    var addon: List<AddOnUIModel> = emptyList()
)

@Parcelize
data class AddOnPageResult(
    val changedAddons: List<AddOnUIModel> = emptyList(),
    val aggregatedData: AggregatedData = AggregatedData(),
    var cartId: Long = 0
) : Parcelable {
    @Parcelize
    data class AggregatedData(
        val title: String = "",
        val price: Long = 0,
        val selectedAddons: List<AddOnUIModel> = emptyList(),
        var isGetDataSuccess: Boolean = false,
        val getDataErrorMessage: String = ""
    ) : Parcelable
}
