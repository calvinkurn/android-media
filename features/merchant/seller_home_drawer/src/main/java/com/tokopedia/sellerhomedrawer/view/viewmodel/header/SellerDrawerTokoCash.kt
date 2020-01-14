package com.tokopedia.sellerhomedrawer.view.viewmodel.header

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction

class SellerDrawerTokoCash : Parcelable {

    @Deprecated("")
    @get:Deprecated("")
    @set:Deprecated("")
    var sellerDrawerTokoCashAction: SellerDrawerTokoCashAction? = null

    var sellerHomeHeaderWalletAction: SellerHomeHeaderWalletAction? = null
    var sellerDrawerWalletAction: SellerDrawerWalletAction? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.sellerDrawerTokoCashAction, flags)
        dest.writeParcelable(this.sellerHomeHeaderWalletAction, flags)
        dest.writeParcelable(this.sellerDrawerWalletAction, flags)
    }

    protected constructor(parcel: Parcel) {
        this.sellerDrawerTokoCashAction = parcel.readParcelable(DrawerTokoCashAction::class.java.classLoader)
        this.sellerHomeHeaderWalletAction = parcel.readParcelable(HomeHeaderWalletAction::class.java.classLoader)
        this.sellerDrawerWalletAction = parcel.readParcelable(DrawerWalletAction::class.java.classLoader)
    }

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
}
