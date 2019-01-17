package com.tokopedia.shop.settings.etalase.data

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 23/08/18.
 */
abstract class BaseShopEtalaseViewModel() : Parcelable, Visitable<BaseShopEtalaseFactory> {
    var id: String = ""
        protected set
    var name: String = ""
    var count: Int = 0
        protected set
    @ShopEtalaseTypeDef
    var type: Int = 0
        protected set
    var isPrimaryEtalase: Boolean = false
        protected set

    constructor(shopEtalaseModel: ShopEtalaseModel,
                isPrimaryEtalase: Boolean): this() {
        this.id = shopEtalaseModel.id
        this.name = shopEtalaseModel.name
        this.count = shopEtalaseModel.count
        this.type = shopEtalaseModel.type
        this.isPrimaryEtalase = isPrimaryEtalase
    }

    abstract override fun type(typeFactory: BaseShopEtalaseFactory): Int


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeString(this.name)
        dest.writeInt(this.count)
        dest.writeInt(this.type)
        dest.writeByte(if (this.isPrimaryEtalase) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) : this() {
        this.id = `in`.readString()
        this.name = `in`.readString()
        this.count = `in`.readInt()
        this.type = `in`.readInt()
        this.isPrimaryEtalase = `in`.readByte().toInt() != 0
    }
}
