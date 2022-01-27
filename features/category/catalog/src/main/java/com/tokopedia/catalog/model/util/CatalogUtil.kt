package com.tokopedia.catalog.model.util

import android.content.Context
import android.content.Intent
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.util.CatalogConstant.CATALOG_URL

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

    fun getShareURI(catalogUrl: String): String {
        return  if (!catalogUrl.contains("www."))
            catalogUrl.replace("https://", "https://www.")
        else
            catalogUrl
    }

    fun getShareUrl(catalogId : String) : String{
        return "${CatalogConstant.CATALOG_DEEPLINK_PREFIX}$catalogId"
    }

    fun getImagesFromCatalogImages(catalogImages: ArrayList<CatalogImage>?): ArrayList<String>? {
        if(catalogImages == null || catalogImages.isEmpty())
            return null
        val imagesArray = arrayListOf<String>()
        catalogImages.forEach {
            if(!it.imageURL.isNullOrBlank())
                imagesArray.add(it.imageURL)
        }
        return imagesArray
    }
}