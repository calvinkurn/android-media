package com.tokopedia.dynamicfeatures.service

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.dynamicfeatures.service.DFDownloader.DELIMITER
import com.tokopedia.dynamicfeatures.service.DFDownloader.DELIMITER_2
import com.tokopedia.dynamicfeatures.service.DFDownloader.KEY_SHARED_PREF_MODULE
import com.tokopedia.dynamicfeatures.service.DFDownloader.MAX_ATTEMPT_DOWNLOAD
import com.tokopedia.dynamicfeatures.service.DFDownloader.SHARED_PREF_NAME

object DFQueue {
    lateinit var sharedPreferences: SharedPreferences

    private fun getSharedPref(context: Context): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.applicationContext.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        }
        return sharedPreferences
    }

    /**
     * return the DF module in queue that need to download
     * example output:
     * merchant_seller:2
     * merchant_buyer:2
     * 2 is the number of failed attempt to download
     */
    fun getDFModuleList(context: Context): List<Pair<String, Int>> {
        try {
            val sp = getSharedPref(context)
            val moduleList = sp.getString(KEY_SHARED_PREF_MODULE, "")
            return moduleList?.split(DELIMITER)?.map { it ->
                it.split(DELIMITER_2).let { Pair(it[0], it[1].toInt()) }
            } ?: listOf()
        } catch (e: Exception) {
            return listOf()
        }
    }

    /**
     * replace the current queue
     * Basically this is put the sharedPreference value from the given input.
     * @param moduleList moduleList to replace the current queue
     */
    fun putDFModuleList(context: Context, moduleList: List<Pair<String, Int>>?) {
        try {
            val sp = getSharedPref(context)
            if (moduleList == null || moduleList.isEmpty()) {
                sp.edit().clear().apply()
            } else {
                val moduleListString = moduleListToString(moduleList)
                sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListString).apply()
            }
        } catch (ignored: Exception) {
        }
    }

    fun clear(context: Context) {
        putDFModuleList(context, null)
    }

    /**
     * convert the queue to String
     * input: seller,2 ; buyer:3
     * output: seller:2#buyer:3
     */
    private fun moduleListToString(moduleList: List<Pair<String, Int>>): String {
        return moduleList.joinToString(DELIMITER) {
            it.first + DELIMITER_2 + it.second
        }
    }

    fun removeModuleFromQueue(
        context: Context,
        moduleListToRemove: List<Pair<String, Int>>?
    ) {
        updateQueue(context, null, moduleListToRemove)
    }

    /***
     * change current DF download queue
     * @param moduleListToAppend module to update in queue
     * @param moduleListToRemove module to remove in queue
     *
     * example current list: seller,1 : buyer:2, travel:1
     * input:
     * append: seller:2, buyer:2
     * remove: travel
     *
     * Output:
     * seller,2 : buyer:2
     */
    fun updateQueue(
        context: Context,
        moduleListToAppend: List<Pair<String, Int>>?,
        moduleListToRemove: List<Pair<String, Int>>?
    ) {
        try {
            val sp = getSharedPref(context)
            val currentList = getDFModuleList(context).toMutableList()
            val moduleListToAppendList = moduleListToAppend?.map { it.first } ?: listOf()
            val moduleListToRemoveList = moduleListToRemove?.map { it.first } ?: listOf()
            val finalList = mutableListOf<Pair<String, Int>>()

            for ((index, item) in currentList.withIndex()) {
                val indexRemoveFind = moduleListToRemoveList.indexOf(item.first)
                if (indexRemoveFind > -1) {
                    // not adding to final list, so just continue
                    continue
                }
                val indexAppendFind = moduleListToAppendList.indexOf(item.first)
                if (indexAppendFind > -1) {
                    moduleListToAppend?.get(indexAppendFind)?.let {
                        if (it.second < MAX_ATTEMPT_DOWNLOAD) {
                            finalList.add(it)
                        }
                    }
                } else {
                    finalList.add(currentList[index])
                }
            }
            sp.edit().putString(KEY_SHARED_PREF_MODULE, moduleListToString(finalList)).apply()
        } catch (ignored: Exception) {
        }
    }

    /***
     * @param moduleList module To download, requested directly
     * @return combinedList
     *
     * moduleList = module_seller, module_travel
     * output: module_seller, 1; module_travel, 1; module_digital,2
     */
    fun combineListAndPut(context: Context, moduleListToDownload: List<String>?) {
        val queueList = getDFModuleList(context)
        val list = if (queueList.isEmpty()) {
            moduleListToDownload?.map { Pair(it, 1) } ?: emptyList()
        } else {
            combineList(moduleListToDownload, queueList)
        }
        putDFModuleList(context, list)
    }

    private fun combineList(
        moduleListToDownload: List<String>?,
        queueList: List<Pair<String, Int>>
    ): List<Pair<String, Int>> {
        val list = moduleListToDownload?.map { Pair(it, 1) }?.toMutableList() ?: mutableListOf()
        queueList.forEach {
            if (it.second < MAX_ATTEMPT_DOWNLOAD) {
                if (moduleListToDownload?.contains(it.first) != true) {
                    list.add(Pair(it.first, it.second))
                }
            }
        }
        return list
    }

}