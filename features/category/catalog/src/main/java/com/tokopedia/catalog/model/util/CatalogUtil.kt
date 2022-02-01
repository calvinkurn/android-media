package com.tokopedia.catalog.model.util

import android.content.Context
import android.content.Intent

object CatalogUtil {

    fun shareData(context: Context?, shareTxt: String?, productUri: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
        context?.startActivity(Intent.createChooser(share, shareTxt))
    }

    fun getSortFilterAnalytics(searchFilterMap : HashMap<String,String>?) : String{
        var label = ""
        searchFilterMap?.forEach { map ->
            label = "$label&${map.key}=${map.value}"
        }
        return label.removePrefix("&")
    }

    fun getRatingString(rating : String?) : String {
        if(rating.isNullOrBlank()){
            return ""
        }
        return if(rating.length >= 3){
            rating.substring(0,3)
        }else if(rating.length < 2 && rating.isNotBlank()) {
            rating.substring(0,1)
        }else {
            ""
        }
    }
}