package com.tokopedia.feedcomponent.util.util

import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.support.annotation.DrawableRes
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.feedcomponent.R
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.videoplayer.utils.showToast

/**
 * @author by yfsx on 17/05/19.
 */
class ShareBottomSheets : BottomSheets(), ShareAdapter.OnItemClickListener {

    companion object {

        private const val TITLE_EN = "Share"

        /**
         * Available Share Package Name
         */
        private const val PACKAGE_NAME_WHATSAPP = "com.whatsapp"
        private const val PACKAGE_NAME_FACEBOOK = "com.facebook.katana"
        private const val PACKAGE_NAME_LINE = "jp.naver.line.android"
        private const val PACKAGE_NAME_TWITTER = "com.twitter.android"
        private const val PACKAGE_NAME_INSTAGRAM = "com.instagram.android"

        /**
         * Available Share Activity Class Name
         */
        private const val CLASS_NAME_WHATSAPP = "com.whatsapp.ContactPicker"
        private const val CLASS_NAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"
        private const val CLASS_NAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity"
        private const val CLASS_NAME_TWITTER = "com.twitter.composer.ComposerShareActivity"
        private const val CLASS_NAME_INSTAGRAM = "com.instagram.direct.share.handler.DirectShareHandlerActivity"
        private const val CLASS_NAME_INSTAGRAM_STORY = "com.instagram.share.ADD_TO_STORY"

        /**
         * Arguments
         */
        private const val EXTRA_NAME = "name"
        private const val EXTRA_AVATAR = "avatar"
        private const val EXTRA_LINK = "link"
        private const val EXTRA_SHARE_FORMAT = "share_format"
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_MEDIA_URL = "media_url"

        @Deprecated(
                level = DeprecationLevel.WARNING,
                message = "Use newInstance instead",
                replaceWith = ReplaceWith("fun ShareBottomSheets.newInstance(name, avatar, link, shareFormat, shareTitle, listener)")
        )
        fun constructShareData(name: String, avatar: String, link: String, shareFormat: String, shareTitle: String): LinkerData {
            val linkerData = LinkerData()
            linkerData.name = name
            linkerData.imgUri = avatar
            linkerData.uri = link
            linkerData.textContent = shareFormat
            linkerData.ogTitle = shareTitle
            return linkerData
        }

        fun newInstance(listener: OnShareItemClickListener, name: String, avatar: String, link: String, shareFormat: String, shareTitle: String, mediaUrl: String? = null): ShareBottomSheets {
            val bottomSheet = ShareBottomSheets()
            val args = Bundle().apply {
                putString(EXTRA_NAME, name)
                putString(EXTRA_AVATAR, avatar)
                putString(EXTRA_LINK, link)
                putString(EXTRA_SHARE_FORMAT, shareFormat)
                putString(EXTRA_TITLE, shareTitle)
                putString(EXTRA_MEDIA_URL, mediaUrl)
            }
            bottomSheet.apply {
                arguments = args
                data = constructShareData(name, avatar, link, shareFormat, shareTitle)
                this.listener = listener
            }
            return bottomSheet
        }

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
        private const val KEY_WHATSAPP = "whatsapp"
        private const val KEY_LINE = "line"
        private const val KEY_TWITTER = "twitter"
        private const val KEY_FACEBOOK = "facebook"
        private const val KEY_GOOGLE = "google"
        private const val KEY_INSTAGRAM = "instagram"
        private const val KEY_INSTAGRAM_STORY = "instagram_story"
        val KEY_INSTAGRAM_DIRECT = "Direct"
        val NAME_INSTAGRAM = "Instagram"
        val KEY_YOUTUBE = "youtube"
        val KEY_OTHER = "lainnya"
        val KEY_COPY = "salinlink"

        /**
         * Content Type
         */
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
        private const val TYPE_VIDEO = "video/*"
    }

    sealed class ShareType {

        abstract val key: String
        abstract val displayName: String
        abstract val mimeType: MimeType

        data class ActivityShare(
                override val key: String,
                override val displayName: String,
                override val mimeType: MimeType,
                val intent: Intent
        ) : ShareType() {

            fun getResolveActivity(context: Context): ResolveInfo? = context.packageManager.resolveActivity(intent, 0)

            fun getActivityIcon(context: Context): Drawable? = getResolveActivity(context)?.loadIcon(context.packageManager)
        }

        data class ActionShare(
                override val key: String,
                override val displayName: String,
                override val mimeType: MimeType,
                @DrawableRes val imageResource: Int
        ) : ShareType()
    }

    enum class MimeType(val typeString: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE),
        VIDEO(TYPE_VIDEO)
    }

    private val textShareApps = arrayOf(CLASS_NAME_WHATSAPP,
            CLASS_NAME_FACEBOOK,
            CLASS_NAME_LINE,
            CLASS_NAME_TWITTER,
            CLASS_NAME_INSTAGRAM)

    private lateinit var data: LinkerData
    private lateinit var adapter: ShareAdapter
    private var isAdding: Boolean = false
    private lateinit var listener: OnShareItemClickListener

    @Deprecated(
            level = DeprecationLevel.WARNING,
            message = "Use show(FragmentManager) instead"
    )
    fun show(fragmentManager: FragmentManager, data: LinkerData, listener: OnShareItemClickListener) {
        this.data = data
        this.listener = listener
        show(fragmentManager, TITLE_EN)
    }

    fun show(fragmentManager: FragmentManager?) {
        show(fragmentManager, TITLE_EN)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_feed_share
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun title(): String {
        return arguments?.getString(EXTRA_TITLE) ?: getString(R.string.title_share)
    }

    override fun configView(parentView: View) {
        super.configView(parentView)
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mLayoutError: LinearLayout
    private lateinit var mTextViewError: TextView

    override fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_bottomsheet)
        mProgressBar = view.findViewById(R.id.progressbar)
        mLayoutError = view.findViewById(R.id.layout_error)
        mTextViewError = view.findViewById(R.id.message_error)

        val mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager

        broadcastAddProduct()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val btnClose = getDialog().findViewById<ImageView>(com.tokopedia.design.R.id.btn_close)
        btnClose.setOnClickListener { dismiss() }
    }

    private fun init() {
        /*activity?.let {
            val textIntent = getIntent("", TYPE_TEXT)
            val textResolvedActivities = it.packageManager
                    .queryIntentActivities(textIntent, 0)

            val mediaIntent = getIntent("", TYPE_IMAGE)
            val mediaResolvedActivities = it.packageManager
                    .queryIntentActivities(mediaIntent, 0)

            val media: List<ResolveInfo> = (validate(textResolvedActivities) + validate(mediaResolvedActivities)).distinctBy { resolveInfo -> resolveInfo.activityInfo.packageName }
            adapter = ShareAdapter(media, it.packageManager)
            mRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener(this)
        }*/

        adapter = ShareAdapter(generateAvailableShareTypes(MimeType.IMAGE))
        mRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    private fun validate(resolvedActivities: List<ResolveInfo>): List<ResolveInfo> {
        return resolvedActivities
                .filter {
                    textShareApps.contains(it.activityInfo.name)
                }
    }

    override fun onItemClick(type: ShareType) {
//        when (packageName) {
//            KEY_OTHER -> {
//                actionMore(packageName)
//            }
//            KEY_COPY -> {
//                actionCopy()
//            }
//            KEY_YOUTUBE -> {
//
//            }
//            else -> {
//                actionShare(packageName)
//            }
//        }
        when (type) {
            is ShareType.ActivityShare -> activity?.startActivityForResult(type.intent, 0)
        }
//        showToast("${type.displayName} - ${type.mimeType}")
    }

    private fun actionCopy() {
        data.source = COPY
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                activity?.let {
                                    ClipboardHandler().CopyToClipboard(it, data.originalTextContent)
                                }
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        })
        )

        showToast(getString(R.string.msg_copy))
    }

    private fun actionShare(packageName: String) {
        val media = constantMedia(packageName)
        data.source = media
        activity?.let {
            ShareSocmedHandler(it).shareData(it, packageName,
                    TYPE_TEXT, data.originalTextContent, "", null, "")
            sendTracker(packageName)
        }
    }

    private fun actionMore(packageName: String) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                        0, DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                val intent = getIntent(data.originalTextContent, TYPE_TEXT)
                                startActivity(Intent.createChooser(intent, getString(R.string.other)))
                                sendTracker(packageName)
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        }
                )
        )
    }

    private fun getIntent(contains: String, type: String): Intent {
        return Intent(Intent.ACTION_SEND)
                .setType(type)
                .putExtra(Intent.EXTRA_TITLE, data.name)
                .putExtra(Intent.EXTRA_SUBJECT, data.name)
                .putExtra(Intent.EXTRA_TEXT, contains)
    }

    private fun getTextIntent(packageName: String, className: String): Intent {
        return Intent()
                .setType(MimeType.TEXT.typeString)
                .setComponent(ComponentName(packageName, className))
                .putExtra(Intent.EXTRA_TITLE, data.name)
                .putExtra(Intent.EXTRA_SUBJECT, data.name)
    }

    private fun getInstagramStoryIntent(mimeType: MimeType, mediaUri: Uri, destinationUrl: String): Intent {
        context?.grantUriPermission(
                PACKAGE_NAME_INSTAGRAM, mediaUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return Intent(CLASS_NAME_INSTAGRAM_STORY)
                .setDataAndType(mediaUri, mimeType.typeString)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra("content_url", destinationUrl)
    }

    private var addProductReceiver: BroadcastReceiver? = null

    private fun stateProgress(progress: Boolean) {
        mLayoutError.visibility = View.GONE
        if (progress) {
            mProgressBar.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        } else {
            mProgressBar.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
            init()
        }
    }

    private fun broadcastAddProduct() {
        stateProgress(isAdding)
        addProductReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    when (bundle.getInt(STATUS_FLAG, STATUS_ERROR)) {
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
        mProgressBar.visibility = View.GONE
        mLayoutError.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        mTextViewError.text = messageError + "\n" + getString(R.string.error_failed_add_product)
    }

    fun setData(data: Bundle) {
        this.data = LinkerData().apply {
            type = LinkerData.PRODUCT_TYPE
            name = data.getString(PRODUCT_NAME)
            imgUri = data.getString(IMAGE_URI)
            description = data.getString(PRODUCT_DESCRIPTION)
            uri = data.getString(PRODUCT_URI)
            id = data.getString(PRODUCT_ID)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(addProductReceiver,
                IntentFilter(BROADCAST_ADD_PRODUCT))
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(addProductReceiver)
    }

    private fun generateAvailableShareTypes(type: MimeType): List<ShareType> {
        return mutableListOf<ShareType>().apply {
            add(ShareType.ActivityShare(KEY_WHATSAPP, "WhatsApp", MimeType.TEXT, getTextIntent(PACKAGE_NAME_WHATSAPP, CLASS_NAME_WHATSAPP)))
            add(ShareType.ActivityShare(KEY_FACEBOOK, "Facebook", MimeType.TEXT, getTextIntent(PACKAGE_NAME_FACEBOOK, CLASS_NAME_FACEBOOK)))
            add(ShareType.ActivityShare(KEY_LINE, "LINE", MimeType.TEXT, getTextIntent(PACKAGE_NAME_LINE, CLASS_NAME_LINE)))
            add(ShareType.ActivityShare(KEY_TWITTER, "Twitter", MimeType.TEXT, getTextIntent(PACKAGE_NAME_TWITTER, CLASS_NAME_TWITTER)))
            add(ShareType.ActivityShare(KEY_INSTAGRAM, "Instagram", MimeType.TEXT, getTextIntent(PACKAGE_NAME_INSTAGRAM, CLASS_NAME_INSTAGRAM)))

            if (type != MimeType.TEXT && arguments?.getString(EXTRA_MEDIA_URL) != null)
                add(ShareType.ActivityShare(KEY_INSTAGRAM_STORY, "Instagram Story", type, getInstagramStoryIntent(type, Uri.parse(arguments!!.getString(EXTRA_MEDIA_URL)), "https://www.tokopedia.com/")))

            add(ShareType.ActionShare(KEY_COPY, "Salin Link", MimeType.TEXT, R.drawable.ic_copy_clipboard))
            add(ShareType.ActionShare(KEY_OTHER, "Lainnya", MimeType.TEXT, R.drawable.ic_btn_more))
        }.filterNot { shareType -> shareType is ShareType.ActivityShare && shareType.getResolveActivity(context as Context) == null }
    }

    /**
     * Tracking
     * @param packageName
     * @return String media tracking
     */
    private fun constantMedia(packageName: String): String {
        return when {
            packageName.contains(KEY_WHATSAPP) -> KEY_WHATSAPP
            packageName.contains(KEY_LINE) -> KEY_LINE
            packageName.contains(KEY_TWITTER) -> KEY_TWITTER
            packageName.contains(KEY_FACEBOOK) -> KEY_FACEBOOK
            packageName.contains(KEY_GOOGLE) -> KEY_GOOGLE
            packageName.contains(KEY_OTHER) -> KEY_OTHER
            else -> ""
        }
    }

    private fun sendTracker(packageName: String) {
        listener.onShareItemClicked(constantMedia(packageName))
    }

    interface OnShareItemClickListener {
        fun onShareItemClicked(packageName: String)
    }
}