package com.tokopedia.hotel.hoteldetail.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult

/**
 * @author by jessica on 20/10/20
 */

class HotelShare(val activity: Activity) {

    fun shareEvent(data: PropertyDetailData, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        generateBranchLink(data, loadShare, doneLoadShare, context)
    }

    private fun openIntentShare(title: String, url: String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, "Hotel")
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.startActivity(Intent.createChooser(shareIntent, "Bagikan Produk Ini"))
    }

    private fun generateBranchLink(data: PropertyDetailData, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        loadShare()
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        travelDataToLinkerDataMapper(data, context), object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        openIntentShare(data.city.countryName, linkerShareData.shareContents, context)
                        doneLoadShare()
                    }

                    override fun onError(linkerError: LinkerError) {
                        doneLoadShare()
                    }
                }))
    }

    private fun travelDataToLinkerDataMapper(data: PropertyDetailData, context: Context): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.property.id.toString()
                name = data.property.name
                description = "${data.property.name} di ${data.city.name} - Tokopedia"
                type = LinkerData.HOTEL_TYPE
                ogUrl = null
                imgUri = data.property.images.firstOrNull()?.urlMax300 ?: ""
                custmMsg = "promo"
                uri = "tokopedia://hotel/detail/${id}"
                deepLink = "https://www.tokopedia.com/hotel/detail/$id"
            }
        }
    }


    companion object {
        private const val TYPE = "text/plain"
    }
}