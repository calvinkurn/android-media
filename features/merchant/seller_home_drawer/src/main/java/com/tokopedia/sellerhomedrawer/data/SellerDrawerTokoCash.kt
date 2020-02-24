package com.tokopedia.sellerhomedrawer.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerTokoCashAction
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerWalletAction

class SellerDrawerTokoCash(): Parcelable {
    
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SellerDrawerTokoCash> = object : Parcelable.Creator<SellerDrawerTokoCash> {
            override fun createFromParcel(source: Parcel): SellerDrawerTokoCash {
                return SellerDrawerTokoCash(source)
            }

            override fun newArray(size: Int): Array<SellerDrawerTokoCash?> {
                return arrayOfNulls(size)
            }
        }
    }
    
    protected constructor(parcel: Parcel): this() {

        this.drawerTokoCashAction = parcel.readParcelable(SellerDrawerTokoCashAction::class.java.classLoader)
        this.homeHeaderWalletAction = parcel.readParcelable(SellerHomeHeaderWalletAction::class.java.classLoader)
        this.drawerWalletAction = parcel.readParcelable(SellerDrawerWalletAction::class.java.classLoader)
    }
    
    
    @Deprecated("")
    var drawerTokoCashAction: SellerDrawerTokoCashAction? = null

    var homeHeaderWalletAction: SellerHomeHeaderWalletAction? = null
    var drawerWalletAction: SellerDrawerWalletAction? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.drawerTokoCashAction, flags)
        dest.writeParcelable(this.homeHeaderWalletAction, flags)
        dest.writeParcelable(this.drawerWalletAction, flags)
    }

}