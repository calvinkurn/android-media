package com.tokopedia.merchantvoucher.common.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherViewModel : Visitable<MerchantVoucherAdapterTypeFactory>, Parcelable {

    var voucherId: Int = 0
    var voucherName: String? = ""
    var voucherCode: String = ""

    @MerchantVoucherTypeDef
    var merchantVoucherType: Int? = MerchantVoucherTypeDef.TYPE_FREE_ONGKIR
    @MerchantVoucherAmountTypeDef
    var merchantVoucherAmountType: Int? = MerchantVoucherAmountTypeDef.TYPE_FIXED
    var merchantVoucherAmount: Float? = 0f
    var minimumSpend: Int = 0
    var validThru: Long? = 0
    var tnc: String? = ""
    var bannerUrl: String? = ""
    @MerchantVoucherStatusTypeDef
    var status: Int? = MerchantVoucherStatusTypeDef.TYPE_AVAILABLE
    @MerchantVoucherOwnerTypeDef
    var ownerId: Int? = MerchantVoucherOwnerTypeDef.TYPE_MERCHANT
    var enableButtonUse = false

    fun isAvailable() = status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE

    constructor(merchantVoucherModel: MerchantVoucherModel) {
        voucherId = merchantVoucherModel.voucherId
        voucherName = merchantVoucherModel.voucherName
        voucherCode = merchantVoucherModel.voucherCode ?: ""
        merchantVoucherType = merchantVoucherModel.merchantVoucherType?.type ?: MerchantVoucherTypeDef.TYPE_FREE_ONGKIR
        merchantVoucherAmountType = merchantVoucherModel.merchantVoucherAmount?.type ?: MerchantVoucherAmountTypeDef.TYPE_FIXED
        merchantVoucherAmount = merchantVoucherModel.merchantVoucherAmount?.amount ?: 0f
        minimumSpend = merchantVoucherModel.minimumSpend
        ownerId = merchantVoucherModel.merchantVoucherOwner.ownerId
        validThru = merchantVoucherModel.validThru.toLong()
        tnc = merchantVoucherModel.tnc
        bannerUrl = merchantVoucherModel.merchantVoucherBanner?.mobileUrl
        status = merchantVoucherModel.merchantVoucherStatus?.status ?: MerchantVoucherStatusTypeDef.TYPE_AVAILABLE
    }

    override fun type(typeFactory: MerchantVoucherAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(voucherId)
        parcel.writeString(voucherName)
        parcel.writeString(voucherCode)
        parcel.writeValue(merchantVoucherType)
        parcel.writeValue(merchantVoucherAmountType)
        parcel.writeValue(merchantVoucherAmount)
        parcel.writeInt(minimumSpend)
        parcel.writeValue(validThru)
        parcel.writeString(tnc)
        parcel.writeString(bannerUrl)
        parcel.writeValue(status)
        parcel.writeValue(ownerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) {
        voucherId = parcel.readInt()
        voucherName = parcel.readString()
        voucherCode = parcel.readString()
        merchantVoucherType = parcel.readValue(Int::class.java.classLoader) as? Int
        merchantVoucherAmountType = parcel.readValue(Int::class.java.classLoader) as? Int
        merchantVoucherAmount = parcel.readValue(Float::class.java.classLoader) as? Float
        minimumSpend = parcel.readInt()
        validThru = parcel.readValue(Long::class.java.classLoader) as? Long
        tnc = parcel.readString()
        bannerUrl = parcel.readString()
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        ownerId = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    companion object CREATOR : Parcelable.Creator<MerchantVoucherViewModel> {
        override fun createFromParcel(parcel: Parcel): MerchantVoucherViewModel {
            return MerchantVoucherViewModel(parcel)
        }

        override fun newArray(size: Int): Array<MerchantVoucherViewModel?> {
            return arrayOfNulls(size)
        }
    }

}

fun MerchantVoucherViewModel.getTypeString(context: Context): String {
    return when (this.merchantVoucherType) {
        MerchantVoucherTypeDef.TYPE_CASHBACK -> context.getString(R.string.title_cashback)
        MerchantVoucherTypeDef.TYPE_DISCOUNT -> context.getString(R.string.discount)
        MerchantVoucherTypeDef.TYPE_FREE_ONGKIR -> {
            context.getString(R.string.free_ongkir)
        }
        else -> ""
    }
}

fun MerchantVoucherViewModel.getAmountString(): String {
    if (merchantVoucherAmount == null) return ""
    return when (this.merchantVoucherAmountType) {
        MerchantVoucherAmountTypeDef.TYPE_FIXED -> KMNumbers.formatRupiahString(this.merchantVoucherAmount!!.toLong())
        MerchantVoucherAmountTypeDef.TYPE_PERCENTAGE -> KMNumbers.formatDouble2PCheckRound(this.merchantVoucherAmount!!.toDouble(), false) + "%"
        else -> ""
    }
}

fun MerchantVoucherViewModel.getAmountShortString(): String {
    if (merchantVoucherAmount == null) return ""
    return when (this.merchantVoucherAmountType) {
        MerchantVoucherAmountTypeDef.TYPE_FIXED -> KMNumbers.formatSuffixNumbers(this.merchantVoucherAmount!!.toLong())
        MerchantVoucherAmountTypeDef.TYPE_PERCENTAGE -> KMNumbers.formatDouble2PCheckRound(this.merchantVoucherAmount!!.toDouble(), false) + "%"
        else -> ""
    }
}

fun MerchantVoucherViewModel.getMinSpendLongString(context: Context): String {
    if (this.minimumSpend <= 0) {
        return context.getString(R.string.no_min_spend)
    }
    return context.getString(R.string.min_spend_x, getMinSpendAmountShortString())
}

fun MerchantVoucherViewModel.getMinSpendAmountString(): String {
    return KMNumbers.formatRupiahString(this.minimumSpend.toLong())
}

fun MerchantVoucherViewModel.getMinSpendAmountShortString(): String {
    return KMNumbers.formatSuffixNumbers(this.minimumSpend.toLong())
}

fun MerchantVoucherViewModel.getValidThruString(): String {
    return DateFormatUtils.getFormattedDate(this.validThru.toString(), DateFormatUtils.FORMAT_D_MMMM_YYYY)
}

fun MerchantVoucherViewModel.getStatusString(context: Context): String {
    return when (status) {
        MerchantVoucherStatusTypeDef.TYPE_AVAILABLE -> context.getString(R.string.available)
        MerchantVoucherStatusTypeDef.TYPE_IN_USE -> context.getString(R.string.in_use)
        MerchantVoucherStatusTypeDef.TYPE_RUN_OUT -> context.getString(R.string.run_out)
        else -> ""
    }
}