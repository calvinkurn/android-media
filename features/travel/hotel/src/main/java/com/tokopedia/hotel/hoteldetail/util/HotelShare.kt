package com.tokopedia.hotel.hoteldetail.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * @author by jessica on 20/10/20
 */

class HotelShare(val activity: Activity) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }
    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    fun shareEvent(data: PropertyDetailData, isPromo: Boolean, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        generateBranchLink(data, isPromo, loadShare, doneLoadShare, context)
    }

    private fun openIntentShare(data: PropertyDetailData, url: String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.hotel_detail_share_cta_link, data.property.name, data.city.name, url))
        }
        activity.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.hotel_detail_share_bottomsheet_title)))
    }

    private fun generateBranchLink(data: PropertyDetailData, isPromo: Boolean, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        loadShare()
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            travelDataToLinkerDataMapper(data, isPromo, context), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data, linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))
        } else {
            val seoUrl = "${data.property.name.replace(" ", "-").trimEnd()}-${data.property.id}"
            openIntentShare(data, context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl) , context)
            doneLoadShare()
        }
    }

    private fun travelDataToLinkerDataMapper(data: PropertyDetailData, isPromo: Boolean, context: Context): LinkerShareData {
        val seoUrl = "${data.property.name.replace(" ", "-").trimEnd()}-${data.property.id}"
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.property.id.toString()
                name = context.resources.getString(R.string.hotel_detail_share_link_title,
                        data.property.name, data.city.name)
                description = context.resources.getString(R.string.hotel_detail_share_link_content,
                        data.property.name, data.city.name)
                type = LinkerData.HOTEL_TYPE
                ogUrl = null
                imgUri = data.property.images.firstOrNull()?.urlMax300 ?: ""
                custmMsg = if (isPromo) "promo" else ""
                deepLink = context.resources.getString(R.string.hotel_detail_share_applink, data.property.id.toString())
                uri = context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl)
                desktopUrl = context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl)
            }
        }
    }


    companion object {
        private const val TYPE = "text/plain"
    }
}