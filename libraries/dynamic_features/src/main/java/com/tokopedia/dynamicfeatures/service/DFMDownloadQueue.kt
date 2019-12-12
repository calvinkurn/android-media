package com.tokopedia.dynamicfeatures.service

import android.content.Context
import com.tokopedia.dynamicfeatures.service.DFJobService.Companion.KEY_SHARED_PREF_MODULE

object DFMDownloadQueue {
    /**
     * return the DF module in queue that need to download
     * example output: listOf("merchant_seller:2#merchant_buyer:5")
     * merchant_seller:2 means 2 failed attemps to download
     */
    fun getDFModuleList(context: Context):List<Pair<String, Int>>?{
        try {
            val sp = context.getSharedPreferences(
                DFJobService.SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
            val moduleList = sp.getString(KEY_SHARED_PREF_MODULE, "")
            return moduleList?.split(DFJobService.DELIMITER)?.map { it ->
                it.split(DFJobService.DELIMITER_2).let{ Pair(it[0], it[1].toInt()) }}
        } catch (e:Exception) {
            return null
        }
    }

    fun putDFModuleList(context: Context, moduleList: List<Pair<String, Int>>?){
        try {
            val sp = context.getSharedPreferences(
                DFJobService.SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
            if (moduleList == null || moduleList.isEmpty()) {
                sp.edit().clear().apply()
            } else {
                val moduleListString = moduleList.joinToString(DFJobService.DELIMITER) {
                    it.first + DFJobService.DELIMITER_2 + it.second
                }
                sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListString).apply()
            }
        } catch (ignored:Exception) {
        }
    }

}