package com.tokopedia.shop.settings.notes.data

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.view.adapter.factory.BaseShopNoteFactory

class ShopNoteViewModel() : Parcelable, Visitable<BaseShopNoteFactory> {
    var id: String? = null
        private set
    var title: String? = null
    var content: String? = null
    var terms: Boolean = false
    var updateTime: String? = null
        private set
    var updateTimeUTC: Long = 0

    constructor(shopNoteModel: ShopNoteModel) : this() {
        this.id = shopNoteModel.id
        this.title = shopNoteModel.title
        this.content = shopNoteModel.content
        this.terms = shopNoteModel.terms
        this.updateTime = shopNoteModel.updateTime
        if (!StringUtils.isEmptyNumber(updateTime)){
            // update time in server is in GMT, convert to UTC, by minus 7 * 3600
            this.updateTimeUTC = updateTime!!.toLong() - 25200
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeString(this.title)
        dest.writeString(this.content)
        dest.writeValue(this.terms)
        dest.writeString(this.updateTime)
        dest.writeLong(this.updateTimeUTC)
    }

    protected constructor(`in`: Parcel) : this() {
        this.id = `in`.readString()
        this.title = `in`.readString()
        this.content = `in`.readString()
        this.terms = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.updateTime = `in`.readString()
        this.updateTimeUTC = `in`.readLong()
    }

    override fun type(typeFactory: BaseShopNoteFactory): Int {
        return typeFactory.type(this)
    }

    companion object CREATOR : Parcelable.Creator<ShopNoteViewModel> {
        override fun createFromParcel(source: Parcel): ShopNoteViewModel {
            return ShopNoteViewModel(source)
        }

        override fun newArray(size: Int): Array<ShopNoteViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
