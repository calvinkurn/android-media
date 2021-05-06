package com.tkpd.atc_variant.data

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantComponentDataModel (
        val type: String = "",
        val name: String = "",
//        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, String> = mutableMapOf(),
        var isVariantError: Boolean = false,
        var isRefreshing: Boolean = false
)