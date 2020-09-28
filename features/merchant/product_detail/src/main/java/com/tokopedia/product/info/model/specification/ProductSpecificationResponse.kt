package com.tokopedia.product.info.model.specification


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductSpecificationResponse(
        @SerializedName("ProductCatalogQuery")
        val productCatalogQuery: ProductCatalogQuery = ProductCatalogQuery()
)

data class ProductCatalogQuery(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
)

data class Header(
        @SerializedName("process_time")
        val processTime: String = "",
        @SerializedName("status")
        val status: String = ""
)

data class Data(
        @SerializedName("catalog")
        val catalog: Catalog = Catalog()
)

data class Catalog(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("specification")
        val specification: ArrayList<Specification> = arrayListOf()
)

data class Specification(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("row")
        val row: List<Row> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createTypedArrayList(Row) ?: listOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(row)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Specification> {
        override fun createFromParcel(parcel: Parcel): Specification {
            return Specification(parcel)
        }

        override fun newArray(size: Int): Array<Specification?> {
            return arrayOfNulls(size)
        }
    }
}

data class Row(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: List<String> = listOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: listOf())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(key)
        dest.writeStringList(value)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Row> {
        override fun createFromParcel(parcel: Parcel): Row {
            return Row(parcel)
        }

        override fun newArray(size: Int): Array<Row?> {
            return arrayOfNulls(size)
        }
    }
}