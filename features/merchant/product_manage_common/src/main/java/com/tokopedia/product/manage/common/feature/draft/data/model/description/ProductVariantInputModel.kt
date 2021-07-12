package com.tokopedia.product.manage.common.feature.draft.data.model.description

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ProductVariantInputModel (
        @SerializedName("variant")
        @Expose
        var variantOptionParent: ArrayList<ProductVariantOptionParent> = ArrayList(),
        @SerializedName("product_variant")
        @Expose
        var productVariant: ArrayList<ProductVariantCombinationViewModel> = ArrayList(),
        var productSizeChart: PictureViewModel? = null
): Parcelable

@Parcelize
data class ProductVariantOptionParent (
    @SerializedName("v") //variant id, ex: 1: color
    @Expose
    var v: Int = 0,
    @SerializedName("vu") //variant unit, ex: 0 for no unit; 7 for ukuran
    @Expose
    var vu: Int = 0,
    @SerializedName(value = "pos", alternate = ["position"])
    @Expose
    var position: Int = 0,
    @SerializedName(value = "opt", alternate = ["option"])
    @Expose
    var productVariantOptionChild: List<ProductVariantOptionChild>? = ArrayList(),
    @SerializedName("name") // ex; warna
    @Expose
    var name: String? = null,
    @SerializedName("identifier") // ex: color
    @Expose
    var identifier: String? = null,
    @SerializedName("unit_name")
    @Expose
    var unitName: String? = null // ex: "" (for no unit),  "International"
): Parcelable

@Parcelize
data class ProductVariantCombinationViewModel (
    @SerializedName("st")
    @Expose
    var st: Int = 0,
    @SerializedName("price_var")
    @Expose
    var priceVar: Double = 0.toDouble(),
    @SerializedName("stock")
    @Expose
    var stock: Long = 0,
    @SerializedName("sku")
    @Expose
    var sku: String = "",
    @SerializedName("opt")// option combination of t_id of selected variants
    @Expose
    var opt: List<Int> = emptyList(),
    @SerializedName("level1String")
    @Expose
    var level1String: String? = null,
    @SerializedName("level2String")
    @Expose
    var level2String: String? = null
): Parcelable

@Parcelize
data class ProductVariantOptionChild (
    @SerializedName(value = "pvo", alternate = ["id"]) // id for this variant option
    @Expose
    var pvo: Int = 0,
    @SerializedName("vuv") //variant option id, ex: 19: Ungu
    @Expose
    var vuv: Int = 0,
    @SerializedName("t_id") // temporary ID for submit
    @Expose
    var tId: Int = 0,
    @SerializedName("cstm") // custom name for variant Option. ex; merah delima, also for original value, if vuv is 0
    @Expose
    var value: String = "",
    @SerializedName("image")
    @Expose
    var productPictureViewModelList: List<PictureViewModel>? = ArrayList(),
    @SerializedName("hex")
    @Expose
    var hex: String = ""
): Parcelable

@Parcelize
data class PictureViewModel (
    @SerializedName(value = "v_pic_id")
    @Expose
    var id: Long = 0,
    @SerializedName("status")
    @Expose
    var status: Int= 1,
    @SerializedName("file_name")
    @Expose
    var fileName: String = "",
    @SerializedName("file_path")
    @Expose
    var filePath: String = "",
    @SerializedName("url_original")
    @Expose
    var urlOriginal: String = "",
    @SerializedName("url_thumbnail")
    @Expose
    var urlThumbnail: String = "",
    @SerializedName("x")
    @Expose
    var x: Long = 0,
    @SerializedName("y")
    @Expose
    var y: Long = 0,
    @SerializedName("from_ig")
    @Expose
    var fromIg: Int = 0
): Parcelable
