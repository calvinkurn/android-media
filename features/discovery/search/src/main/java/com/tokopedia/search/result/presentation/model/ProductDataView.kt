package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

class ProductDataView() : Parcelable {
    var productList = listOf<ProductItemDataView>()
    var additionalParams = ""
    var autocompleteApplink: String? = null
    var responseCode: String? = null
    var keywordProcess: String? = null
    var errorMessage: String? = null
    var tickerModel: TickerDataView? = null
    var suggestionModel: SuggestionDataView? = null
    var totalData = 0
    var isQuerySafe = false
    var adsModel: TopAdsModel? = null
    var cpmModel: CpmModel? = null
    var globalNavDataView: GlobalNavDataView? = null
    var inspirationCarouselDataView = listOf<InspirationCarouselDataView>()
    var inspirationCardDataView = listOf<InspirationCardDataView>()
    var defaultView = 0
    var relatedDataView: RelatedDataView? = null
    var totalDataText = ""
    var bannerDataView = BannerDataView()

    fun getTotalItem(): Int {
        return productList.size + (adsModel?.data?.size ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(productList)
        dest.writeString(additionalParams)
        dest.writeString(autocompleteApplink)
        dest.writeString(responseCode)
        dest.writeString(keywordProcess)
        dest.writeParcelable(tickerModel, flags)
        dest.writeParcelable(suggestionModel, flags)
        dest.writeInt(totalData)
        dest.writeByte(if (isQuerySafe) 1.toByte() else 0.toByte())
        dest.writeParcelable(adsModel, flags)
        dest.writeParcelable(cpmModel, flags)
        dest.writeParcelable(globalNavDataView, flags)
        dest.writeInt(defaultView)
    }

    private constructor(parcel: Parcel) : this() {
        productList = parcel.createTypedArrayList(ProductItemDataView.CREATOR) ?: listOf()
        additionalParams = parcel.readString() ?: ""
        autocompleteApplink = parcel.readString()
        responseCode = parcel.readString()
        keywordProcess = parcel.readString()
        tickerModel = parcel.readParcelable(TickerDataView::class.java.classLoader)
        suggestionModel = parcel.readParcelable(SuggestionDataView::class.java.classLoader)
        totalData = parcel.readInt()
        isQuerySafe = parcel.readByte().toInt() != 0
        adsModel = parcel.readParcelable(TopAdsModel::class.java.classLoader)
        cpmModel = parcel.readParcelable(CpmModel::class.java.classLoader)
        globalNavDataView = parcel.readParcelable(GlobalNavDataView::class.java.classLoader)
        defaultView = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductDataView> = object : Parcelable.Creator<ProductDataView> {
            override fun createFromParcel(source: Parcel): ProductDataView {
                return ProductDataView(source)
            }

            override fun newArray(size: Int): Array<ProductDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}