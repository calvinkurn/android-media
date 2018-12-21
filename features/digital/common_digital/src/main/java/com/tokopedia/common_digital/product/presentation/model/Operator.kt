package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Operator : Parcelable, BaseWidgetItem {

    var operatorId: String? = null
        private set
    var operatorType: String? = null
        private set
    var name: String? = null
        private set
    var image: String? = null
        private set
    var lastorderUrl: String? = null
        private set
    var defaultProductId: Int = 0
        private set
    var rule: Rule? = null
        private set
    var prefixList: List<String>? = null
        private set
    var clientNumberList: List<ClientNumber>? = null
        private set
    var productList: List<Product>? = null
        private set
    var ussdCode: String? = null
        private set

    constructor(operatorId: String, operatorType: String, name: String, image: String, lastorderUrl: String,
                defaultProductId: Int, rule: Rule, prefixList: List<String>, clientNumberList: List<ClientNumber>,
                productList: List<Product>, ussdCode: String) {
        this.operatorId = operatorId
        this.operatorType = operatorType
        this.name = name
        this.image = image
        this.lastorderUrl = lastorderUrl
        this.defaultProductId = defaultProductId
        this.rule = rule
        this.prefixList = prefixList
        this.clientNumberList = clientNumberList
        this.productList = productList
        this.ussdCode = ussdCode
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.operatorId)
        dest.writeString(this.operatorType)
        dest.writeString(this.name)
        dest.writeString(this.image)
        dest.writeString(this.lastorderUrl)
        dest.writeInt(this.defaultProductId)
        dest.writeParcelable(this.rule, flags)
        dest.writeStringList(this.prefixList)
        dest.writeList(this.clientNumberList)
        dest.writeList(this.productList)
        dest.writeString(this.ussdCode)
    }

    protected constructor(`in`: Parcel) {
        this.operatorId = `in`.readString()
        this.operatorType = `in`.readString()
        this.name = `in`.readString()
        this.image = `in`.readString()
        this.lastorderUrl = `in`.readString()
        this.defaultProductId = `in`.readInt()
        this.rule = `in`.readParcelable(Rule::class.java.classLoader)
        this.prefixList = `in`.createStringArrayList()
        this.clientNumberList = ArrayList()
        `in`.readList(this.clientNumberList, ClientNumber::class.java.classLoader)
        this.productList = ArrayList()
        `in`.readList(this.productList, Product::class.java.classLoader)
        this.ussdCode = `in`.readString()
    }

    override fun toString(): String {
        return name.toString()
    }

    companion object CREATOR : Parcelable.Creator<Operator> {
        override fun createFromParcel(source: Parcel): Operator {
            return Operator(source)
        }

        override fun newArray(size: Int): Array<Operator?> {
            return arrayOfNulls(size)
        }
    }
}
