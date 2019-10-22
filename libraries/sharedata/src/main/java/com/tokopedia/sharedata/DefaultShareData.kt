package com.tokopedia.sharedata

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.TrackingUtils
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.sharedata.service.ShareBroadcastReceiver
import com.tokopedia.track.TrackApp

class DefaultShareData(
        private val activity: Activity?,
        private val shareData: LinkerData
): ShareCallback {

    companion object {
        private const val TYPE = "text/plain"
        private const val KEY_OTHER = "lainnya"
        private const val TITLE_OTHER = "Lainnya"
        private const val PLACEHOLDER_LINK = "{{branchlink}}"
    }

    fun show() {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest<LinkerShareData>(
                        0,
                        DataMapper.getLinkerShareData(shareData),
                        this
                ))
    }

    private fun getIntent(contains: String, url: String): Intent {
        var containsTemp = contains
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = TYPE

        val title = shareData.name?: ""

        if (!TextUtils.isEmpty(shareData.custmMsg) && shareData.custmMsg.contains(PLACEHOLDER_LINK)) {
            containsTemp = FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.custmMsg, PLACEHOLDER_LINK, url)
        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        mIntent.putExtra(Intent.EXTRA_TEXT, containsTemp)
        return mIntent
    }

    private fun sendTracker() {
        if (shareData.type == LinkerData.CATEGORY_TYPE) {
            shareCategory(shareData)
        } else {
            sendAnalyticsToGtm(shareData.type)
        }
    }

    private fun shareCategory(data: LinkerData) {
        val shareParam = data.getSplittedDescription(",")
        if (shareParam.size == 2) {
            eventShareCategory(shareParam[0], shareParam[1] + "-" + KEY_OTHER)
        }
    }

    private fun eventShareCategory(parentCat: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_SHARE,
                label)
    }

    private fun sendAnalyticsToGtm(type: String) {
        when (type) {
            LinkerData.REFERRAL_TYPE -> {
                UnifyTracking.eventReferralAndShare(activity, AppEventTracking.Action.SELECT_CHANNEL, KEY_OTHER)
                TrackingUtils.sendMoEngageReferralShareEvent(activity, KEY_OTHER)
            }
            LinkerData.APP_SHARE_TYPE -> UnifyTracking.eventAppShareWhenReferralOff(activity, AppEventTracking.Action.SELECT_CHANNEL,
                    KEY_OTHER)
            else -> UnifyTracking.eventShare(activity, KEY_OTHER)
        }
    }

    override fun urlCreated(linkerShareData: LinkerShareResult) {
        if(linkerShareData.url.isNullOrBlank()) {
            return
        }
        val intent = getIntent(linkerShareData.shareContents, linkerShareData.url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val receiver = Intent(activity, ShareBroadcastReceiver::class.java)
            receiver.putExtra(ShareBroadcastReceiver.KEY_TYPE, shareData.type)
            val pendingIntent = PendingIntent.getBroadcast(activity, 0,
                    receiver, PendingIntent.FLAG_UPDATE_CURRENT)
            activity?.startActivity(Intent.createChooser(intent, TITLE_OTHER,
                    pendingIntent.intentSender))

        } else {
            activity?.startActivity(Intent.createChooser(intent, TITLE_OTHER))
        }
        sendTracker()
    }

    override fun onError(linkerError: LinkerError) {}

}