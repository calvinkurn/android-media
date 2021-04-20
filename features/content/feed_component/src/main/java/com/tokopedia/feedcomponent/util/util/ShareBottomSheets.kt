package com.tokopedia.feedcomponent.util.util

import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.feedcomponent.R
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
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
        private const val CLASS_NAME_INSTAGRAM_DM = "com.instagram.direct.share.handler.DirectShareHandlerActivity"
        private const val CLASS_NAME_INSTAGRAM_STORY = "com.instagram.share.handleractivity.StoryShareHandlerActivity"
        private const val CLASS_NAME_INSTAGRAM_FEED = "com.instagram.share.handleractivity.ShareHandlerActivity"

        /**
         * Custom Action
         */
        private const val ACTION_INSTAGRAM_STORY = "com.instagram.share.ADD_TO_STORY"

        /**
         * Extra
         */
        private const val IG_STORY_EXTRA_STICKER_URI = "interactive_asset_uri"
        private const val IG_STORY_EXTRA_TOP_BG = "top_background_color"
        private const val IG_STORY_EXTRA_BOTTOM_BG = "bottom_background_color"

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
        private const val KEY_INSTAGRAM_FEED = "instagram_feed"
        private const val KEY_INSTAGRAM_STORY = "instagram_story"
        private const val KEY_OTHER = "lainnya"
        private const val KEY_COPY = "salinlink"

        val KEY_INSTAGRAM_DIRECT = "Direct"
        val NAME_INSTAGRAM = "Instagram"
        val KEY_YOUTUBE = "youtube"

        /**
         * Content Type
         */
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
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
                @DrawableRes val imageResource: Int,
                val handler: () -> Unit
        ) : ShareType()
    }

    enum class MimeType(val typeString: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE)
    }

    var data: LinkerData? = null
        private set

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

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TITLE_EN)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_feed_share
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun title(): String {
        return arguments?.getString(EXTRA_TITLE) ?: data?.ogTitle ?: ""
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBarUnify
    private lateinit var mLayoutError: LinearLayout
    private lateinit var mTextViewError: Typography

    override fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_bottomsheet)
        mProgressBar = view.findViewById(R.id.progressbar)
        mLayoutError = view.findViewById(R.id.layout_error)
        mTextViewError = view.findViewById(R.id.message_error)

        val mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager

        broadcastAddProduct()
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val btnClose = getDialog()?.findViewById<ImageView>(com.tokopedia.design.R.id.btn_close)
        btnClose?.setOnClickListener { dismiss() }
    }

    private fun init() {
        val adapter = ShareAdapter(
                generateAvailableShareTypes(
                        mutableListOf<MimeType>().apply {
                            add(MimeType.TEXT)
                            if (arguments?.get(EXTRA_MEDIA_URL) != null) add(MimeType.IMAGE)
                        }
                )
        )
        mRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun onItemClick(type: ShareType) {
        when (type) {
            is ShareType.ActivityShare -> doActivityShare(type)
            is ShareType.ActionShare -> doActionShare(type)
        }
    }

    private fun actionCopy() {
        data?.source = COPY
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        DataMapper().getLinkerShareData(data),
                        object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult) {
                                activity?.let {
                                    ClipboardHandler().copyToClipboard(it, data?.uri
                                            ?: "")
                                }
                            }

                            override fun onError(linkerError: LinkerError) {

                            }
                        })
        )

        showToast(getString(R.string.msg_copy))
    }

    private fun doActivityShare(type: ShareType.ActivityShare) {
        try {
            startActivity(type.intent)
            sendTracker(type.key)
        } catch (ex: PackageManager.NameNotFoundException) {
            showToast(getString(R.string.error_apps_not_installed))
            ex.printStackTrace()
        } catch (ex: Exception) {
            showToast(getString(R.string.error_occurred))
            ex.printStackTrace()
        }
    }

    private fun doActionShare(type: ShareType.ActionShare) {
        type.handler()
    }

    private fun actionMore() {
        val intent = getIntent(data?.uri ?: "", TYPE_TEXT)
        startActivity(Intent.createChooser(intent, getString(R.string.other)))
        sendTracker(KEY_OTHER)
    }

    private fun getIntent(textToShare: String, type: String): Intent {
        return Intent(Intent.ACTION_SEND)
                .setType(type)
                .putExtra(Intent.EXTRA_TITLE, data?.name ?: "")
                .putExtra(Intent.EXTRA_SUBJECT, data?.name ?: "")
                .putExtra(Intent.EXTRA_TEXT, textToShare)
    }

    private fun getTextIntent(packageName: String, className: String): Intent {
        return Intent(Intent.ACTION_SEND)
                .setType(MimeType.TEXT.typeString)
                .setComponent(ComponentName(packageName, className))
                .putExtra(Intent.EXTRA_TITLE, data?.name ?: "")
                .putExtra(Intent.EXTRA_SUBJECT, data?.name ?: "")
                .putExtra(Intent.EXTRA_TEXT, arguments?.getString(EXTRA_SHARE_FORMAT).orEmpty())
    }

    @SuppressWarnings("ResourceType")
    private fun getInstagramStoryIntent(mediaUri: Uri): Intent {
        activity?.grantUriPermission(
                PACKAGE_NAME_INSTAGRAM, mediaUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        return Intent(ACTION_INSTAGRAM_STORY)
                .setType(MimeType.IMAGE.typeString)
                .putExtra(IG_STORY_EXTRA_STICKER_URI, mediaUri)
                .putExtra(IG_STORY_EXTRA_TOP_BG, getString(com.tokopedia.unifyprinciples.R.color.Unify_N75))
                .putExtra(IG_STORY_EXTRA_BOTTOM_BG, getString(com.tokopedia.unifyprinciples.R.color.Unify_T400))
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    private fun getInstagramFeedIntent(mediaUri: Uri): Intent {
        return Intent(Intent.ACTION_SEND)
                .setType(MimeType.IMAGE.typeString)
                .putExtra(Intent.EXTRA_STREAM, mediaUri)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setClassName(PACKAGE_NAME_INSTAGRAM, CLASS_NAME_INSTAGRAM_FEED)
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

    private fun generateAvailableShareTypes(typeList: List<MimeType>): List<ShareType> {
        return mutableListOf<ShareType>().apply {
            add(ShareType.ActivityShare(KEY_WHATSAPP, getString(R.string.share_whatsapp), MimeType.TEXT, getWhatsAppIntent(PACKAGE_NAME_WHATSAPP)))
            add(ShareType.ActivityShare(KEY_FACEBOOK, getString(R.string.share_facebook), MimeType.TEXT, getTextIntent(PACKAGE_NAME_FACEBOOK, CLASS_NAME_FACEBOOK)))
            add(ShareType.ActivityShare(KEY_LINE, getString(R.string.share_line).toUpperCase(), MimeType.TEXT, getTextIntent(PACKAGE_NAME_LINE, CLASS_NAME_LINE)))
            add(ShareType.ActivityShare(KEY_TWITTER, getString(R.string.share_twitter), MimeType.TEXT, getTextIntent(PACKAGE_NAME_TWITTER, CLASS_NAME_TWITTER)))

            val mediaUrl: String? = arguments?.getString(EXTRA_MEDIA_URL)
            if (typeList.contains(MimeType.IMAGE) && mediaUrl?.isNotEmpty() == true) {
                add(ShareType.ActivityShare(KEY_INSTAGRAM_FEED, getString(R.string.share_instagram_feed), MimeType.IMAGE, getInstagramFeedIntent(Uri.parse(mediaUrl))))
                add(ShareType.ActivityShare(KEY_INSTAGRAM_STORY, getString(R.string.share_instagram_story), MimeType.IMAGE, getInstagramStoryIntent(Uri.parse(mediaUrl))))
            }

            add(ShareType.ActionShare(KEY_COPY, getString(R.string.copy), MimeType.TEXT, R.drawable.ic_copy_clipboard, ::actionCopy))
            add(ShareType.ActionShare(KEY_OTHER, getString(R.string.other), MimeType.TEXT, R.drawable.ic_btn_more, ::actionMore))
        }
                .filterNot { shareType -> shareType is ShareType.ActivityShare && shareType.getResolveActivity(context as Context) == null }
                .distinctBy(ShareType::key)
    }

    private fun getWhatsAppIntent(packageName: String): Intent {
        return Intent(Intent.ACTION_SEND)
                .setType(MimeType.TEXT.typeString)
                .setPackage(packageName)
                .putExtra(Intent.EXTRA_TITLE, data?.name ?: "")
                .putExtra(Intent.EXTRA_SUBJECT, data?.name ?: "")
                .putExtra(Intent.EXTRA_TEXT, arguments?.getString(EXTRA_SHARE_FORMAT).orEmpty())
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