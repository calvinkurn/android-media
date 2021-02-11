package com.tokopedia.tokopatch.model

import java.io.File

/**
 * Author errysuprayogi on 11,June,2020
 */
data class Patch(
    var patchesInfoImplClassFullName: String = "",
    var name: String = "",
    var version: String = "",
    var url: String = "",
    var tempPath: String = "",
    var md5: String = "",
    var appHash: String = "",
    var isAppliedSuccess: Boolean = false
) : Cloneable {

    fun delete() {
        val f = File(tempPath)
        f.delete()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        try {
            return super.clone()
        } catch (e: CloneNotSupportedException) {
            throw e
        }
    }

}