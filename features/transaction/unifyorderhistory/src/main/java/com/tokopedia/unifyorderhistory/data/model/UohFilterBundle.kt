package com.tokopedia.unifyorderhistory.data.model

/**
 * Created by fwidjaja on 19/11/20.
 */
data class UohFilterBundle (
        val key: String = "",
        val value: String = "",
        val desc: String = "",
        val type: Int = -1)

        /*type = 0 --> label
        type = 1 --> sublabel*/
