package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("bid")
    val bid: BidBase? = BidBase(),
    @SerializedName("btn_action")
    val btnAction: BtnAction? = BtnAction(),
    @SerializedName("keyword")
    val keyword: KeywordBase? = KeywordBase(),
    @SerializedName("negative")
    val negative: NegativeBase?= NegativeBase()
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(BidBase::class.java.classLoader),
            parcel.readParcelable(BtnAction::class.java.classLoader),
            parcel.readParcelable(KeywordBase::class.java.classLoader),
            parcel.readParcelable(NegativeBase::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(bid, flags)
        parcel.writeParcelable(btnAction, flags)
        parcel.writeParcelable(keyword, flags)
        parcel.writeParcelable(negative, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Header> {
        override fun createFromParcel(parcel: Parcel): Header {
            return Header(parcel)
        }

        override fun newArray(size: Int): Array<Header?> {
            return arrayOfNulls(size)
        }
    }
}

data class KeywordBase(
        @SerializedName("box")
        val box: Box? = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table>? = listOf()
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Box::class.java.classLoader),
            parcel.readInt(),
            parcel.createTypedArrayList(Table)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(box, flags)
        parcel.writeInt(id)
        parcel.writeTypedList(table)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KeywordBase> {
        override fun createFromParcel(parcel: Parcel): KeywordBase {
            return KeywordBase(parcel)
        }

        override fun newArray(size: Int): Array<KeywordBase?> {
            return arrayOfNulls(size)
        }
    }
}

data class NegativeBase(
        @SerializedName("box")
        val box: Box? = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table>? = listOf()
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Box::class.java.classLoader),
            parcel.readInt(),
            parcel.createTypedArrayList(Table)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(box, flags)
        parcel.writeInt(id)
        parcel.writeTypedList(table)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NegativeBase> {
        override fun createFromParcel(parcel: Parcel): NegativeBase {
            return NegativeBase(parcel)
        }

        override fun newArray(size: Int): Array<NegativeBase?> {
            return arrayOfNulls(size)
        }
    }
}

data class BidBase(
        @SerializedName("box")
        val box: Box? = Box(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("table")
        val table: List<Table>? = listOf()
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Box::class.java.classLoader),
            parcel.readInt(),
            parcel.createTypedArrayList(Table)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(box, flags)
        parcel.writeInt(id)
        parcel.writeTypedList(table)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BidBase> {
        override fun createFromParcel(parcel: Parcel): BidBase {
            return BidBase(parcel)
        }

        override fun newArray(size: Int): Array<BidBase?> {
            return arrayOfNulls(size)
        }
    }
}