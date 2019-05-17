package com.tokopedia.profile.view.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.profile.R
import com.tokopedia.track.TrackApp
import java.util.*

/**
 * @author by yfsx on 17/05/19.
 */
class ShareBottomSheets: BottomSheets(), ShareAdapter.OnItemClickListener {
    val TITLE_EN = "Share"

    val KEY_ADDING = ".isAddingProduct"

    private val PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker"
    private val PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"
    private val PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity"
    private val PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity"
    private val PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias"

    private val ClassNameApplications = arrayOf(PACKAGENAME_WHATSAPP, PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_TWITTER, PACKAGENAME_GPLUS)

    companion object {
        val COPY = "Copy"
        val STATUS_FLAG = "STATUS_FLAG"
        val STATUS_RUNNING = 1
        val STATUS_DONE = 2
        val STATUS_ERROR = 3
        val MESSAGE_ERROR_FLAG = "MESSAGE_ERROR_FLAG"
        val BROADCAST_ADD_PRODUCT = "BROADCAST_ADD_PRODUCT";
        val PRODUCT_ID = "PRODUCT_ID"
        val NO_PRODUCT_ID = -1
        val INVALID_MESSAGE_ERROR = "default"
        val PRODUCT_NAME = "PRODUCT_NAME"
        val IMAGE_URI = "IMAGE_URI"
        val PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION"
        val PRODUCT_URI = "PRODUCT_URI"
        private val KEY_WHATSAPP = "whatsapp"
        private val KEY_LINE = "line"
        private val KEY_TWITTER = "twitter"
        private val KEY_FACEBOOK = "facebook"
        private val KEY_GOOGLE = "google"
        val KEY_OTHER = "lainnya"
        val KEY_COPY = "salinlink"

        private val TYPE = "text/plain"
    }

    private lateinit var data: LinkerData
    private var isAdding: Boolean = false

    fun newInstance(data: LinkerData, isAddingProduct: Boolean): ShareBottomSheets {
        val fragment = ShareBottomSheets()
        val bundle = Bundle()
        bundle.putParcelable(ShareBottomSheets::class.java.getName(), data)
        bundle.putBoolean(ShareBottomSheets::class.java.getName() + KEY_ADDING, isAddingProduct)
        fragment.setArguments(bundle)
        return fragment
    }

    fun show(fragmentManager: FragmentManager, data: LinkerData,
             isAddingProduct: Boolean) {
        newInstance(data, isAddingProduct).show(fragmentManager, TITLE_EN)
    }

    fun show(fragmentManager: FragmentManager, data: LinkerData) {
        newInstance(data, false).show(fragmentManager, TITLE_EN)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_share
    }

    protected override fun state(): BottomSheets.BottomSheetsState {
        return BottomSheets.BottomSheetsState.FULL
    }

    protected override fun title(): String {
        return getString(R.string.title_share)
    }

    protected override fun configView(parentView: View) {
        arguments?.let{
            data = it.getParcelable(ShareBottomSheets::class.java.getName())
            isAdding = it.getBoolean(ShareBottomSheets::class.java.getName() + KEY_ADDING, false)
        }
        super.configView(parentView)
    }

    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null
    private var mLayoutError: LinearLayout? = null
    private var mTextViewError: TextView? = null

    override fun initView(view: View) {

        mRecyclerView = view.findViewById(R.id.recyclerview)
        mProgressBar = view.findViewById(R.id.progressbar)
        mLayoutError = view.findViewById(R.id.layout_error)
        mTextViewError = view.findViewById(R.id.message_error)

        val mLayoutManager = LinearLayoutManager(getActivity())
        mRecyclerView!!.layoutManager = mLayoutManager

        broadcastAddProduct()
    }

    private fun init() {
        val intent = getIntent("")

        val resolvedActivities = getActivity()!!.getPackageManager()
                .queryIntentActivities(intent, 0)
        if (!resolvedActivities.isEmpty()) {
            val showApplications = validate(resolvedActivities)

            val adapter = ShareAdapter(showApplications, getActivity()!!
                    .getPackageManager())
            mRecyclerView!!.adapter = adapter

            adapter.setOnItemClickListener(this)
        }
    }

    private fun validate(resolvedActivities: List<ResolveInfo>): List<ResolveInfo> {
        val showApplications = ArrayList<ResolveInfo>()
        for (resolveInfo in resolvedActivities) {
            if (Arrays.asList(*ClassNameApplications)
                            .contains(resolveInfo.activityInfo.name)) {
                showApplications.add(resolveInfo)
            }
        }
        return showApplications
    }

    override fun onItemClick(packageName: String) {
        if (packageName.equals(KEY_OTHER, ignoreCase = true)) {
            actionMore(packageName)
        } else if (packageName.equals(KEY_COPY, ignoreCase = true)) {
            actionCopy()
        } else {
            actionShare(packageName)
        }
    }

    private fun actionCopy() {
        data.setSource(COPY)
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                ClipboardHandler().CopyToClipboard(activity!!, linkerShareData.getShareUri())
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        })
        )

        Toast.makeText(getActivity(), getString(R.string.msg_copy), Toast.LENGTH_SHORT).show()
        sendAnalyticsToGtm(data.getType(), COPY)
    }

    private fun actionShare(packageName: String) {
        val media = constantMedia(packageName)
        data.setSource(media)

        ShareSocmedHandler(activity!!).ShareSpecific(data, activity!!, packageName,
                TYPE, null, "")

        sendTracker(packageName)
    }

    private fun actionMore(packageName: String) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                        0, DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                val intent = getIntent(linkerShareData.getShareContents())
                                startActivity(Intent.createChooser(intent, getString(R.string.other)))
                                sendTracker(packageName)
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        }
                )
        )
    }

    private fun getIntent(contains: String): Intent {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = TYPE

        mIntent.putExtra(Intent.EXTRA_TITLE, data.getName())
        mIntent.putExtra(Intent.EXTRA_SUBJECT, data.getName())
        mIntent.putExtra(Intent.EXTRA_TEXT, contains)
        return mIntent
    }

    private var addProductReceiver: BroadcastReceiver? = null

    private fun stateProgress(progress: Boolean) {
        mLayoutError!!.visibility = View.GONE
        if (progress) {
            mProgressBar!!.visibility = View.VISIBLE
            mRecyclerView!!.visibility = View.GONE
        } else {
            mProgressBar!!.visibility = View.GONE
            mRecyclerView!!.visibility = View.VISIBLE
            init()
        }
    }

    private fun broadcastAddProduct() {
        stateProgress(isAdding)
        addProductReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    val status = bundle.getInt(STATUS_FLAG, STATUS_ERROR)
                    when (status) {
                        STATUS_DONE -> {
                            setData(bundle)
                            stateProgress(false)
                        }
                        STATUS_ERROR -> {
                            stateProgress(false)
                            onError(bundle)
                        }
                        else -> {
                            stateProgress(false)
                            onError(bundle)
                        }
                    }
                }
            }
        }
    }

    fun onError(resultData: Bundle) {
        val messageError = resultData.getString(MESSAGE_ERROR_FLAG)
        mProgressBar!!.visibility = View.GONE
        mLayoutError!!.visibility = View.VISIBLE
        mRecyclerView!!.visibility = View.GONE
        mTextViewError!!.text = messageError + "\n" + getString(R.string.error_failed_add_product)
    }

    fun setData(data: Bundle) {
        this.data = LinkerData()
        this.data.setType(LinkerData.PRODUCT_TYPE)
        this.data.setName(data.getString(PRODUCT_NAME))
        val imageUri = data.getString(IMAGE_URI)
        this.data.setImgUri(data.getString(IMAGE_URI))
        this.data.setDescription(data.getString(PRODUCT_DESCRIPTION))
        this.data.setUri(data.getString(PRODUCT_URI))
        this.data.setId(data.getString(PRODUCT_ID))
    }

    override fun onResume() {
        super.onResume()
        getActivity()!!.registerReceiver(addProductReceiver,
                IntentFilter(BROADCAST_ADD_PRODUCT))
    }

    override fun onPause() {
        super.onPause()
        getActivity()!!.unregisterReceiver(addProductReceiver)
    }

    /**
     * Tracking
     * @param packageName
     * @return String media tracking
     */
    private fun constantMedia(packageName: String): String {
//        if (packageName.contains(KEY_WHATSAPP)) {
//            return AppEventTracking.SOCIAL_MEDIA.WHATSHAPP
//        } else if (packageName.contains(KEY_LINE)) {
//            return AppEventTracking.SOCIAL_MEDIA.LINE
//        } else if (packageName.contains(KEY_TWITTER)) {
//            return AppEventTracking.SOCIAL_MEDIA.TWITTER
//        } else if (packageName.contains(KEY_FACEBOOK)) {
//            return AppEventTracking.SOCIAL_MEDIA.FACEBOOK
//        } else if (packageName.contains(KEY_GOOGLE)) {
//            return AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS
//        } else if (packageName.contains(KEY_OTHER)) {
//            return AppEventTracking.SOCIAL_MEDIA.OTHER
//        }
        return ""
    }

    private fun sendTracker(packageName: String) {
        val media = constantMedia(packageName)
        if (!media.isEmpty()) {
            if (data.getType() == LinkerData.CATEGORY_TYPE) {
                shareCategory(data, media)
            } else {
                sendAnalyticsToGtm(data.getType(), media)
            }
        }
    }

    private fun shareCategory(data: LinkerData, media: String) {
        val shareParam = data.getSplittedDescription(",")
        if (shareParam.size == 2) {
            eventShareCategory(shareParam[0], shareParam[1] + "-" + media)
        }
    }

    fun eventShareCategory(parentCat: String, label: String) {
//        TrackApp.getInstance().gtm.sendGeneralEvent(
//                AppEventTracking.Event.CATEGORY_PAGE,
//                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
//                AppEventTracking.Action.CATEGORY_SHARE,
//                label)
    }

    private fun sendAnalyticsToGtm(type: String, channel: String) {
//        when (type) {
//            LinkerData.REFERRAL_TYPE -> {
//                UnifyTracking.eventReferralAndShare(getContext(), AppEventTracking.Action.SELECT_CHANNEL, channel)
//                TrackingUtils.sendMoEngageReferralShareEvent(getContext(), channel)
//            }
//            LinkerData.APP_SHARE_TYPE -> UnifyTracking.eventAppShareWhenReferralOff(getContext(), AppEventTracking.Action.SELECT_CHANNEL,
//                    channel)
//            LinkerData.HOTLIST_TYPE -> HotlistPageTracking.eventShareHotlist(getContext(), channel)
//            else -> UnifyTracking.eventShare(getContext(), channel)
//        }
    }
}