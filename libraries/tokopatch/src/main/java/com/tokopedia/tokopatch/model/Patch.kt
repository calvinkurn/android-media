package com.tokopedia.tokopatch.model

import android.util.Log
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
        Log.d("robust", "delete temp/cache file $tempPath")
        val f = File(tempPath)
        f.delete()
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        var clone: Patch? = null
        clone = try {
            return super.clone()
        } catch (e: CloneNotSupportedException) {
            throw e
        }
        return clone
    }

}