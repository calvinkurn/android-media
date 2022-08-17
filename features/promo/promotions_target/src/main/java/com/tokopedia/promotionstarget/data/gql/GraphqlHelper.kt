package com.tokopedia.promotionstarget.data.gql

import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object GraphqlHelper {
    val QUERY = "query"
    val VARIABLES = "variables"
    val OPERATION_NAME = "operationName"

    fun loadRawString(resources:Resources,resId:Int) : String {
        val rawResource = resources.openRawResource(resId)
        val content = streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
        }
        return content
    }

    fun streamToString(`in`: InputStream?): String {
        var temp:String
        val bufferedReader = BufferedReader(InputStreamReader(`in`))
        val stringBuilder = StringBuilder()
        try {
            while (bufferedReader.readLine().also { temp = it } != null) {
                stringBuilder.append("$temp\n")
            }
        } catch (e: IOException) {
        }
        return stringBuilder.toString()
    }

}