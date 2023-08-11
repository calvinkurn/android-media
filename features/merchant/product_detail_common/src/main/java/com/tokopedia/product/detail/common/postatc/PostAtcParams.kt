package com.tokopedia.product.detail.common.postatc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostAtcParams(
    val cartId: String = "",
    val layoutId: String = "",
    val pageSource: Source = Source.Default,
    val session: String = "",
    val addons: Addons? = null
) : Parcelable {

    @Parcelize
    data class Addons(
        val deselectedAddonsIds: List<String>,
        val isFulfillment: Boolean,
        val selectedAddonsIds: List<String>,
        val warehouseId: String,
        val quantity: Int
    ) : Parcelable

    @Parcelize
    sealed class Source(
        open val name: String
    ) : Parcelable {
        object PDP : Source("product detail page")
        object Default : Source("")

        class Custom(override val name: String) : Source(name)

        companion object {
            fun parse(name: String): Source? {
                val matchingSubclass = Source::class.sealedSubclasses.find {
                    it.objectInstance?.name == name
                }
                return matchingSubclass?.objectInstance
            }
        }
    }
}
