package com.tokopedia.sellerhomedrawer.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerTokoCashAction
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerWalletAction

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

        this.drawerTokoCashAction = parcel.readParcelable(DrawerTokoCashAction::class.java.classLoader)
        this.homeHeaderWalletAction = parcel.readParcelable(HomeHeaderWalletAction::class.java.classLoader)
        this.drawerWalletAction = parcel.readParcelable(DrawerWalletAction::class.java.classLoader)
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