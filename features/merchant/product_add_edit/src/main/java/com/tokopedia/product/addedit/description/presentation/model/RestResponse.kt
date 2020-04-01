package com.tokopedia.product.addedit.description.presentation.model

import java.lang.reflect.Type

class RestResponse(private val result: Any, val code: Int, val isCached: Boolean) {
    var errorBody: String? = null
    var isError = false
    var type: Type? = null

    /**
     * @param <T> Class type of T ( e.g. object of Xyx class)
     * @return Return the object of T
    </T> */
    fun <T> getData(): T {
        return try {
            result as T
        } catch (cce: ClassCastException) {
            cce.printStackTrace()
            throw RuntimeException(if (("Class type mismatch, please use same class object whose type token was provided in Request object."
                            + "Current object type is :"
                            + result) == null) "null" else result.javaClass.name)
        }
    }

}