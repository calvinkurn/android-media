//package com.tokopedia.expresscheckout.view.variant.viewmodel
//
//import android.os.Parcel
//import android.os.Parcelable
//
//data class InsuranceCartResponseViewModel(
//        var cartShopsList: ArrayList<InsuranceCartShopsViewModel> = ArrayList()
//
//) : Parcelable {
//
//    companion object {
//
//        @JvmField
//        val CREATOR = object : Parcelable.Creator<InsuranceCartResponseViewModel> {
//
//            override fun createFromParcel(parcel: Parcel): InsuranceCartResponseViewModel {
//                return InsuranceCartResponseViewModel(parcel)
//            }
//
//            override fun newArray(size: Int): Array<InsuranceCartResponseViewModel?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
//
//    constructor(parcel: Parcel? = null) : this(
//            arrayListOf<InsuranceCartShopsViewModel>().apply {
//                parcel?.readList(this, InsuranceCartShopsViewModel::class.java.classLoader)
//            }
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeList(cartShopsList)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//}