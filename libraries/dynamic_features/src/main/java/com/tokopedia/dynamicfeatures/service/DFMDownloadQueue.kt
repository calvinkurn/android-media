package com.tokopedia.dynamicfeatures.service

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.dynamicfeatures.service.DFJobService.Companion.KEY_SHARED_PREF_MODULE

object DFMDownloadQueue {
    lateinit var sharedPreferences:SharedPreferences

    private fun getSharedPref(context: Context):SharedPreferences{
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.applicationContext.getSharedPreferences(
                DFJobService.SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        }
        return sharedPreferences
    }
    /**
     * return the DF module in queue that need to download
     * example output: listOf("merchant_seller:2#merchant_buyer:5")
     * merchant_seller:2 means 2 failed attemps to download
     */
    fun getDFModuleList(context: Context):List<Pair<String, Int>>{
        try {
            val sp = getSharedPref(context)
            val moduleList = sp.getString(KEY_SHARED_PREF_MODULE, "")
            return moduleList?.split(DFJobService.DELIMITER)?.map { it ->
                it.split(DFJobService.DELIMITER_2).let{ Pair(it[0], it[1].toInt()) }} ?: listOf()
        } catch (e:Exception) {
            return listOf()
        }
    }

    fun putDFModuleList(context: Context, moduleList: List<Pair<String, Int>>?){
        try {
            val sp = getSharedPref(context)
            if (moduleList == null || moduleList.isEmpty()) {
                sp.edit().clear().apply()
            } else {
                val moduleListString = moduleListToString(moduleList)
                sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListString).apply()
            }
        } catch (ignored:Exception) {
        }
    }

    private fun moduleListToString( moduleList: List<Pair<String, Int>>):String{
        return moduleList.joinToString(DFJobService.DELIMITER) {
            it.first + DFJobService.DELIMITER_2 + it.second
        }
    }

    fun clearDFModuleList(context: Context, moduleListToRemove: List<Pair<String, Int>>?){
        try {
            val sp = getSharedPref(context)
            if (moduleListToRemove == null || moduleListToRemove.isEmpty()) {
                return
            } else {
                val currentList = getDFModuleList(context).toMutableList()
                val iterator = currentList.iterator()
                val moduleToRemoveList = moduleListToRemove.map { it.first }.toMutableList()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    val indexFind = moduleToRemoveList.indexOf(item.first)
                    if (indexFind > -1) {
                        iterator.remove()
                        moduleToRemoveList.removeAt(indexFind)
                        if (moduleToRemoveList.isEmpty()) {
                            break
                        }
                    }
                }
                sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListToString(currentList)).apply()
            }
        } catch (ignored:Exception) {
        }
    }

    fun appendDFModuleList(context: Context, moduleListToAppend: List<Pair<String, Int>>?){
        try {
            val sp = getSharedPref(context)
            if (moduleListToAppend == null || moduleListToAppend.isEmpty()) {
                return
            } else {
                val currentList = getDFModuleList(context).toMutableList()
                val moduleListToAppendList = moduleListToAppend.map { it.first }
                val finalList = mutableListOf<Pair<String, Int>>()

                for ((index, item) in currentList.withIndex()) {
                    val indexFind = moduleListToAppendList.indexOf(item.first)
                    if (indexFind > -1) {
                        finalList.add(moduleListToAppend.get(indexFind))
                    } else {
                        finalList.add(currentList.get(index))
                    }
                }
                sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListToString(finalList)).apply()
            }
        } catch (ignored:Exception) {
        }
    }

}