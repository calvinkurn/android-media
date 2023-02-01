package com.tokopedia.hotel.hoteldetail.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import java.lang.ref.WeakReference

/**
 * @author by jessica on 20/10/20
 */

class HotelShare(
    val activity: WeakReference<Activity>,
    private val context: Context,
    private val view: View,
    private val trackerUtil: TrackingHotelUtil
) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity.get()) }
    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    fun showUniversalBottomSheet(fragmentManager: FragmentManager, propertyDetailData: PropertyDetailData, isPromo: Boolean, userId: String, imageList: ArrayList<String>) {
        fragmentManager.let {
            UniversalShareBottomSheet.createInstance().apply {
                init(object : ShareBottomsheetListener {
                    override fun onCloseOptionClicked() {
                        trackerUtil.closeShare(this@HotelShare.context, propertyDetailData.property.id)
                    }

                    override fun onShareOptionClicked(shareModel: ShareModel) {
                        trackerUtil.clickChannelShare(this@HotelShare.context, propertyDetailData.property.id, shareModel.channel ?: "", UniversalShareBottomSheet.KEY_IMAGE_DEFAULT)
                        if (this@HotelShare.activity.get() != null) {
                            onClickChannelWidget(
                                this@apply,
                                shareModel,
                                propertyDetailData,
                                isPromo,
                                this@HotelShare.context,
                                this@HotelShare.activity.get()!!,
                                this@HotelShare.view
                            )
                        }
                    }
                })
                imageSaved(imageList.firstOrNull() ?: "")
                setUtmCampaignData("Hotel", userId, propertyDetailData.property.id, "share")
                setMetaData(propertyDetailData.property.name, propertyDetailData.property.locationImageStatic, "", imageList)

                this.setShowListener {
                    trackerUtil.viewShare(this@HotelShare.context, propertyDetailData.property.id)
                }

                this.setSelectThumbnailImageListener {
                    trackerUtil.clickImageShare(this@HotelShare.context, propertyDetailData.property.id)
                }
            }.show(it, "")
        }
    }

    private fun openIntentShare(data: PropertyDetailData, url: String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.hotel_detail_share_cta_link, data.property.name, data.city.name, url))
        }
        activity.get()?.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.hotel_detail_share_bottomsheet_title)))
    }

    private fun onClickChannelWidget(
        universalShareBottomSheet: UniversalShareBottomSheet,
        shareModel: ShareModel,
        data: PropertyDetailData,
        isPromo: Boolean,
        context: Context,
        activity: Activity,
        view: View
    ) {
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    travelDataToLinkerDataMapper(shareModel, data, isPromo, context),
                    object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, context.getString(R.string.hotel_detail_share_cta_link, data.property.name, data.city.name, linkerShareData.url))
                        }

                        override fun onError(linkerError: LinkerError) {
                            val seoUrl = "${data.property.name.replace(" ", "-").trimEnd()}-${data.property.id}"
                            openIntentShare(data, TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl), context)
                        }
                    }
                )
            )
            universalShareBottomSheet.dismiss()
        } else {
            val seoUrl = "${data.property.name.replace(" ", "-").trimEnd()}-${data.property.id}"
            openIntentShare(data, TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl), context)
        }
    }

    private fun travelDataToLinkerDataMapper(shareModel: ShareModel, data: PropertyDetailData, isPromo: Boolean, context: Context): LinkerShareData {
        val seoUrl = "${data.property.name.replace(" ", "-").trimEnd()}-${data.property.id}"
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.property.id
                campaign = shareModel.campaign
                channel = shareModel.channel
                name = context.resources.getString(
                    R.string.hotel_detail_share_link_title,
                    "Hotel",
                    data.property.name
                )
                description = ""
                type = LinkerData.HOTEL_TYPE
                ogUrl = null
                imgUri = shareModel.ogImgUrl
                custmMsg = if (isPromo) "promo" else ""
                deepLink = context.resources.getString(R.string.hotel_detail_share_applink, data.property.id)
                uri = TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl)
                desktopUrl = TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.hotel_detail_share_weblink, data.city.countryName, seoUrl)
            }
        }
    }

    companion object {
        private const val TYPE = "text/plain"
    }
}
