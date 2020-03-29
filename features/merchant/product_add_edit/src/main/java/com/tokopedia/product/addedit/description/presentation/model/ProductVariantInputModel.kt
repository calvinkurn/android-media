package com.tokopedia.product.addedit.description.presentation.model

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
data class ProductVariantInputModel (
        @SerializedName("variant")
        @Expose
        var variantOptionParent: List<ProductVariantOptionParent> = ArrayList(),
        @SerializedName("product_variant")
        @Expose
        var productVariant: ArrayList<ProductVariantCombinationViewModel> = ArrayList(),
        var productSizeChart: PictureViewModel? = null
): Parcelable

@Parcelize
class ProductVariantOptionParent: Parcelable{

    @SerializedName("v")
    @Expose
    private var v: Int = 0 //variant id, ex: 1: color

    @SerializedName("vu")
    @Expose
    private var vu: Int = 0 //variant unit, ex: 0 for no unit; 7 for ukuran

    @SerializedName(value = "pos", alternate = ["position"])
    @Expose
    private var position: Int = 0

    @SerializedName(value = "opt", alternate = ["option"])
    @Expose
    private var productVariantOptionChild: List<ProductVariantOptionChild>? = ArrayList()

    @SerializedName("name")
    @Expose
    private var name: String? = null // ex; warna

    @SerializedName("identifier")
    @Expose
    private var identifier: String? = null // ex: color

    @SerializedName("unit_name")
    @Expose
    private var unitName: String? = null // ex: "" (for no unit),  "International"
}

@Parcelize
class ProductVariantCombinationViewModel: Parcelable{

    val ACTIVE_STATUS = 1 // from API
    val NOT_ACTIVE_STATUS = 0 // from API

    @SerializedName("st")
    @Expose
    private var st: Int = 0

    @SerializedName("price_var")
    @Expose
    private var priceVar: Double = 0.toDouble()

    @SerializedName("stock")
    @Expose
    private var stock: Long = 0

    @SerializedName("sku")
    @Expose
    private var sku: String? = null

    @SerializedName("opt")
    @Expose
    private var opt: List<Int>? = null // option combination of t_id of selected variants
}

@Parcelize
class ProductVariantOptionChild: Parcelable{
    @SerializedName(value = "pvo", alternate = ["id"])
    @Expose
    private var pvo: Int = 0 // id for this variant option

    @SerializedName("vuv")
    @Expose
    private var vuv: Int = 0 //variant option id, ex: 19: Ungu

    @SerializedName("t_id")
    @Expose
    private var tId: Int = 0 // temporary ID for submit

    @SerializedName("cstm")
    @Expose
    private var value: String? = null // custom name for variant Option. ex; merah delima, also for original value, if vuv is 0

    @SerializedName("image")
    @Expose
    private var productPictureViewModelList: List<PictureViewModel>? = ArrayList()

    @SerializedName("hex")
    @Expose
    private var hex: String? = null // ex; "#bf00ff"
}

@Parcelize
class PictureViewModel: Parcelable {

    @SerializedName(value = "v_pic_id")
    @Expose
    private var id: Long = 0

    @SerializedName("status")
    @Expose
    private var status = 1

    @SerializedName("file_name")
    @Expose
    private var fileName: String = ""

    @SerializedName("file_path")
    @Expose
    private var filePath: String = ""

    @SerializedName("url_original")
    @Expose
    private var urlOriginal: String = ""

    @SerializedName("url_thumbnail")
    @Expose
    private var urlThumbnail: String = ""

    @SerializedName("x")
    @Expose
    private var x: Long = 0

    @SerializedName("y")
    @Expose
    private var y: Long = 0

    @SerializedName("from_ig")
    @Expose
    private var fromIg: Int = 0
}
