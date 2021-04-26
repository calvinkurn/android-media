package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class GlobalNavDataView(
        val source: String = "",
        val title: String = "",
        val keyword: String = "",
        val navTemplate: String = "",
        val background: String = "",
        val seeAllApplink: String = "",
        val seeAllUrl: String = "",
        val isShowTopAds: Boolean = false,
        val itemList: List<Item> = listOf(),
) : Parcelable, Visitable<ProductListTypeFactory?> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    class Item(
        val categoryName: String = "",
        val name: String = "",
        val info: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        val url: String = "",
        val subtitle: String = "",
        val strikethrough: String = "",
        val backgroundUrl: String = "",
        val logoUrl: String = "",
        val position: Int = 0,
    ) : Parcelable {

        fun getGlobalNavItemAsObjectDataLayer(creativeName: String?): Any {
            return DataLayer.mapOf(
                    "id", name,
                    "name", "/search result - widget",
                    "creative", creativeName,
                    "position", position.toString()
            )
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(categoryName)
            dest.writeString(name)
            dest.writeString(info)
            dest.writeString(imageUrl)
            dest.writeString(applink)
            dest.writeString(url)
            dest.writeString(subtitle)
            dest.writeString(strikethrough)
            dest.writeString(backgroundUrl)
            dest.writeString(logoUrl)
            dest.writeInt(position)
        }

        constructor(`in`: Parcel): this (
            categoryName = `in`.readString() ?: "",
            name = `in`.readString() ?: "",
            info = `in`.readString() ?: "",
            imageUrl = `in`.readString() ?: "",
            applink = `in`.readString() ?: "",
            url = `in`.readString() ?: "",
            subtitle = `in`.readString() ?: "",
            strikethrough = `in`.readString() ?: "",
            backgroundUrl = `in`.readString() ?: "",
            logoUrl = `in`.readString() ?: "",
            position = `in`.readInt(),
        )

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
                override fun createFromParcel(source: Parcel): Item {
                    return Item(source)
                }

                override fun newArray(size: Int): Array<Item?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(source)
        dest.writeString(title)
        dest.writeString(keyword)
        dest.writeString(navTemplate)
        dest.writeString(background)
        dest.writeString(seeAllApplink)
        dest.writeString(seeAllUrl)
        dest.writeTypedList(itemList)
    }

    constructor(parcel: Parcel): this(
        source = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        keyword = parcel.readString() ?: "",
        navTemplate = parcel.readString() ?: "",
        background = parcel.readString() ?: "",
        seeAllApplink = parcel.readString() ?: "",
        seeAllUrl = parcel.readString() ?: "",
        itemList = parcel.createTypedArrayList(Item.CREATOR) ?: listOf(),
    )

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GlobalNavDataView> = object : Parcelable.Creator<GlobalNavDataView> {
            override fun createFromParcel(source: Parcel): GlobalNavDataView {
                return GlobalNavDataView(source)
            }

            override fun newArray(size: Int): Array<GlobalNavDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}