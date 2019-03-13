package com.tokopedia.expresscheckout.domain.model;

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class HeaderModel(
        var processTime: Double = 0.0,
        var errors: ArrayList<String>? = null,
        var messages: ArrayList<String>? = null,
        var reason: String? = null,
        var errorCode: String? = null
)