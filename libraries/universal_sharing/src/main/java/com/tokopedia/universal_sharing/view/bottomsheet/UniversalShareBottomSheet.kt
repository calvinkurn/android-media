package com.tokopedia.universal_sharing.view.bottomsheet

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Telephony
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ImageListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ShareBottomSheetAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Rafli Syam 20/07/2020
 */
class UniversalShareBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.universal_share_bottomsheet
        private val TAG = UniversalShareBottomSheet::class.java.simpleName
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
        private const val TYPE_ALL = "*/*"
        private const val PACKAGE_NAME_INSTAGRAM = "com.instagram.android"
        private const val PACKAGE_NAME_FACEBOOK = "com.facebook.katana"
        private const val FACEBOOK_FEED_ACTIVITY = "com.facebook.composer.shareintent.AddToStoryAlias"
        private const val FACEBOOK_STORY_INTENT_ACTION = "com.facebook.stories.ADD_TO_STORY"
        private const val PACKAGE_NAME_WHATSAPP = "com.whatsapp"
        private const val PACKAGE_NAME_LINE = "jp.naver.line.android"
        private const val PACKAGE_NAME_TWITTER = "com.twitter.android"
        private const val PACKAGE_NAME_TELEGRAM = "org.telegram.messenger"
        private const val PACKAGE_NAME_GMAIL = "com.google.android.gm"
        //add remote config handling
        private const val GLOBAL_CUSTOM_SHARING_FEATURE_FLAG = "android_enable_custom_sharing"
        private const val GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG = "android_enable_screenshot_sharing"
        private var featureFlagRemoteConfigKey: String = ""
        //Optons Flag
        private var isImageOnlySharing: Boolean = false
        private var screenShotImagePath: String = ""

        private const val DELAY_TIME_MILLISECOND = 500L
        private const val SCREENSHOT_TITLE = "Yay, screenshot & link tersimpan!"
        const val CUSTOM_SHARE_SHEET = 1
        const val SCREENSHOT_SHARE_SHEET = 2

        fun createInstance(): UniversalShareBottomSheet = UniversalShareBottomSheet()

        fun isCustomSharingEnabled(context: Context?, remoteConfigKey: String = GLOBAL_CUSTOM_SHARING_FEATURE_FLAG): Boolean{
            val isEnabled: Boolean
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            isEnabled = remoteConfig.getBoolean(remoteConfigKey)
            featureFlagRemoteConfigKey = remoteConfigKey
            return isEnabled
        }

        fun setImageOnlySharingOption(imageOnly:Boolean){
            isImageOnlySharing = imageOnly
        }

        fun setScreenShotImagePath(imgPath: String){
            screenShotImagePath = imgPath
        }

        fun createAndStartScreenShotDetector(context: Context, screenShotListener: ScreenShotListener,
                                             fragment: Fragment,
                                             remoteConfigKey: String = GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG,
                                             addFragmentLifecycleObserver: Boolean = false,
                                             permissionListener: PermissionListener? = null) : ScreenshotDetector?{
            val isEnabled: Boolean
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            isEnabled = remoteConfig.getBoolean(remoteConfigKey)
            var screenshotDetector : ScreenshotDetector? = null
            if(isEnabled) {
                screenshotDetector = ScreenshotDetector(context.applicationContext, screenShotListener, permissionListener)
                if(addFragmentLifecycleObserver){
                    setFragmentLifecycleObserverForScreenShot(fragment, screenshotDetector)
                }
                screenshotDetector.detectScreenshots(fragment)
            }
            return screenshotDetector
        }

        fun setFragmentLifecycleObserverForScreenShot(fragment: Fragment, screenshotDetector: ScreenshotDetector?){
            fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    super.onResume(owner)
                    screenshotDetector?.start()
                }
                override fun onStop(owner: LifecycleOwner) {
                    super.onStop(owner)
                    clearState(screenshotDetector)
                }
                override fun onDestroy(owner: LifecycleOwner) {
                    fragment.lifecycle.removeObserver(this)
                    clearState(screenshotDetector)
                    super.onDestroy(owner)
                }
            })
        }

        //Use this method to get type of the Share Bottom Sheet inside the onShareOptionClicked and onCloseOptionClicked methods
        //This method can be used to get the bottomsheet type after show() method is called to send required GTM events based on bottomsheet type
        fun getShareBottomSheetType() : Int{
            var shareSheetType = CUSTOM_SHARE_SHEET
            if(isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
                shareSheetType = SCREENSHOT_SHARE_SHEET
            }
            return shareSheetType
        }

        fun clearData(){
            isImageOnlySharing = false
            screenShotImagePath = ""
        }

        fun clearState(screenshotDetector: ScreenshotDetector?){
            screenshotDetector?.stop()
            clearData()
        }
    }

    enum class MimeType(val type: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE),
        ALL(TYPE_ALL)
    }

    private var bottomSheetListener: ShareBottomsheetListener? = null
    private var rvSocialMediaList: RecyclerView? = null
    private var thumbNailTitleTxTv : Typography? = null
    private var thumbNailImage: ImageUnify? = null
    private var previewImage: ImageUnify? = null
    private var revImageOptionsContainer: RecyclerView? = null
    private var imageListViewGroup : Group? = null

    //Fixed sharing options
    private var copyLinkImage: ImageView? = null
    private var smsImage: ImageView? = null
    private var smsTxtv: Typography? = null
    private var emailImage: ImageView? = null
    private var otherOptionsImage: ImageView? = null

    private var thumbNailTitle = ""
    private var bottomSheetTitleRemoteConfKey = ""
    private var bottomSheetTitleStr = ""
    private var thumbNailImageUrl = ""
    private var previewImageUrl = ""
    private var imageOptionsList: ArrayList<String>? = ArrayList()
    private var takeViewSS : ((View, ((String)->Unit)) -> Unit)? = null
    private var requestDataMap : Map<String, Any>? = null

    private var campaignStr: String = ""
    private var channelStr: String = ""
    private var ogImageUrl: String = ""
    private var savedImagePath: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initImageOptionsRecyclerView()
    }

    fun init(bottomSheetListener: ShareBottomsheetListener) {
        clearContentPadding = true
        this.bottomSheetListener = bottomSheetListener
    }

    fun show(fragmentManager: FragmentManager?, fragment: Fragment, screenshotDetector: ScreenshotDetector? = null) {
        screenshotDetector?.detectScreenshots(fragment, {fragmentManager?.let { show(it, TAG) }}, true, fragment.requireView())
            ?: fragmentManager?.let { show(it, TAG) }
    }

    private fun setupBottomSheetChildView(inflater: LayoutInflater, container: ViewGroup?) {
        inflater.inflate(LAYOUT, container).apply {
            rvSocialMediaList = findViewById(R.id.rv_social_media_list)
            thumbNailTitleTxTv = findViewById(R.id.thumb_nail_title)
            thumbNailImage = findViewById(R.id.thumb_nail_image)
            previewImage = findViewById(R.id.preview_image)
            revImageOptionsContainer = findViewById(R.id.image_list_container)
            imageListViewGroup = findViewById(R.id.image_selection_view_group)

            //setting click listeners for fixed options
            copyLinkImage = findViewById(R.id.copy_link_img)
            copyLinkImage?.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
            otherOptionsImage = findViewById(R.id.others_img)
            otherOptionsImage?.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
            smsImage = findViewById(R.id.sms_img)
            smsImage?.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
            smsTxtv = findViewById(R.id.sms_link_txtv)
            emailImage = findViewById(R.id.email_img)
            emailImage?.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
            setFixedOptionsClickListeners()

            setUserVisualData()
            setTitle(context.getString(R.string.label_to_social_media_text))
            setChild(this)
            setCloseClickListener {
                bottomSheetListener?.onCloseOptionClicked()
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvSocialMediaList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ShareBottomSheetAdapter(context, ::executeShareOptionClick, generateSocialMediaList(context))
        }
    }

    private fun initImageOptionsRecyclerView() {
        if(imageOptionsList != null && imageOptionsList!!.size > 0) {
            revImageOptionsContainer?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = context?.let { ImageListAdapter(imageOptionsList!!, it, takeViewSS!!,::imageSaved ,::updateThumbnailImage) }
            }
            revImageOptionsContainer?.viewTreeObserver?.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    revImageOptionsContainer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    Handler().postDelayed({
                        revImageOptionsContainer?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
                    }, DELAY_TIME_MILLISECOND)
                }
            })
        }
    }

    private fun getResolvedActivity(context: Context?, intent: Intent?): ResolveInfo? = context?.let { ctx ->
        intent?.let {
            return ctx.packageManager.resolveActivity(it, 0)
        }
    }

    private fun getActivityIcon(context: Context?, intent: Intent?): Drawable? {
        getResolvedActivity(context, intent).let {
            return it?.loadIcon(context?.packageManager)
        }
    }

    private fun getAppIntent(type: MimeType, packageName: String?, actionType: String = Intent.ACTION_SEND, uri:Uri? = null): Intent {
        val intentType = if(type == MimeType.IMAGE) {
            MimeType.IMAGE.type
        } else {
            MimeType.TEXT.type
        }
        return Intent(actionType).apply {
            setType(intentType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(packageName)
        }
    }

    private fun generateSocialMediaList(context: Context?): List<ShareModel> {
        val socialMediaList: MutableList<ShareModel> = mutableListOf()
        socialMediaList.add( ShareModel.Whatsapp().apply {
            packageName = PACKAGE_NAME_WHATSAPP
            socialMediaName = context?.resources?.getString(R.string.label_whatsapp)
            feature = channelStr
            campaign = campaignStr
            channel =  SharingUtil.labelWhatsapp
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_whatsapp) }
        })
        socialMediaList.add(ShareModel.Facebook().apply {
            packageName = PACKAGE_NAME_FACEBOOK
            socialMediaName = context?.resources?.getString(R.string.label_facebook)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelFbfeed
            shareOnlyLink = isImageOnlySharing
            if(isImageOnlySharing){
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
            }
            else{
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }
            appIntent?.component = ComponentName(
                PACKAGE_NAME_FACEBOOK,
                FACEBOOK_FEED_ACTIVITY
            )
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_facebook) }
        })
        if(!TextUtils.isEmpty(savedImagePath)){
            socialMediaList.add( ShareModel.Facebook().apply {
                packageName = PACKAGE_NAME_FACEBOOK
                //facebook story can share only the images
                socialMediaName = context?.resources?.getString(R.string.label_facebook_story)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelFbstory
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, actionType = FACEBOOK_STORY_INTENT_ACTION)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_icon_fbstories3) }
            })
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_feed)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgfeed
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_FEED")
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_instagram) }
            })
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_story)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgstory
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_STORY")
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_icon_igstory) }
            })
        }else{
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_msg)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgMessage
                shareOnlyLink = false
                appIntent = getAppIntent(MimeType.TEXT, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_instagram) }
            })
        }
        socialMediaList.add(ShareModel.Line().apply {
            packageName = PACKAGE_NAME_LINE
            socialMediaName = context?.resources?.getString(R.string.label_line)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelLine
            shareOnlyLink = isImageOnlySharing
            if(isImageOnlySharing){
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
            }
            else{
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_line) }
        })
        socialMediaList.add(ShareModel.Twitter().apply {
            packageName = PACKAGE_NAME_TWITTER
            socialMediaName = context?.resources?.getString(R.string.label_twitter)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelTwitter
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_twitter) }
        })
        socialMediaList.add(ShareModel.Telegram().apply {
            packageName = PACKAGE_NAME_TELEGRAM
            socialMediaName = context?.resources?.getString(R.string.label_telegram)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelTelegram
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_icon_telegram) }
        })
        return socialMediaList.filterNot {
            (it.packageName!!.isNotEmpty() && it.appIntent != null && getResolvedActivity(context, it.appIntent) == null)
        }
    }

    private fun setFixedOptionsClickListeners(){
        val copyLinkShareModel = ShareModel.CopyLink().apply {
            socialMediaName = context?.resources?.getString(R.string.label_copy_link)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelSalinLink
            shareOnlyLink = isImageOnlySharing
        }
        copyLinkImage?.setOnClickListener {
            executeShareOptionClick(copyLinkShareModel)
        }

        val otherOptionsShareModel = ShareModel.Others().apply {
            socialMediaName = context?.resources?.getString(R.string.label_action_more)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelOthers
            shareOnlyLink = isImageOnlySharing
        }
        otherOptionsImage?.setOnClickListener {
            executeShareOptionClick(otherOptionsShareModel)
        }

        if(!isImageOnlySharing) {
            smsImage?.visibility = View.VISIBLE
            smsTxtv?.visibility = View.VISIBLE
            val smsShareModel = ShareModel.SMS().apply {
                packageName = Telephony.Sms.getDefaultSmsPackage(context);
                socialMediaName = context?.resources?.getString(R.string.label_chat)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelSms
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }

            smsImage?.setOnClickListener {
                executeShareOptionClick(smsShareModel)
            }
        }

        val emailShareModel = ShareModel.Email().apply {
            packageName = PACKAGE_NAME_GMAIL
            socialMediaName = context?.resources?.getString(R.string.share_email)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelEmail
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
        }

        emailImage?.setOnClickListener {
            executeShareOptionClick(emailShareModel)
        }
    }

    fun setMetaData(tnTitle: String, tnImage: String,
                    previewImgUrl: String = "",
                    imageList: ArrayList<String>? = null,
                    takeSS : ((view: View, imageSaved: ((String)->Unit)) -> Unit)? = null){

        if(isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)){
            previewImageUrl = screenShotImagePath
            savedImagePath = screenShotImagePath
            thumbNailImageUrl = screenShotImagePath
            thumbNailTitle = SCREENSHOT_TITLE
            imageOptionsList = null
        }
        else {
            thumbNailTitle = tnTitle
            thumbNailImageUrl = tnImage
            previewImageUrl = previewImgUrl
            imageOptionsList = imageList
            imageOptionsList?.let {
                if (it.size > 0){
                    imageSaved(it[0])
                }
            }
        }
        if(takeSS == null){
            takeViewSS = (SharingUtil)::triggerSS
        }else{
            takeViewSS = takeSS
        }
    }

    fun setRequestData(requestPayLoad: Map<String, Any>?){
        requestDataMap = requestPayLoad
    }

    fun setBottomSheetTitle(title: String){
        bottomSheetTitleStr = title
    }

    fun setBottomSheetTitleRemoteConfKey(key: String){
        bottomSheetTitleRemoteConfKey = key
    }

    private fun setUserVisualData(){
        thumbNailTitleTxTv?.text = thumbNailTitle
        if(isImageOnlySharing){
            thumbNailImage?.setImageURI(Uri.parse(File(thumbNailImageUrl).toString()))
        }
        else{
            thumbNailImage?.setImageUrl(thumbNailImageUrl)
        }
        if(previewImageUrl.isNullOrEmpty()){
            previewImage?.visibility = View.GONE
        }
        else {
            previewImage?.visibility = View.VISIBLE
            previewImage?.setImageURI(Uri.parse(File(previewImageUrl).toString()))
        }
        if(imageOptionsList != null){
            imageListViewGroup?.visibility = View.VISIBLE
        }
        else{
            imageListViewGroup?.visibility = View.GONE
        }
//        previewImage?.setImageUrl(previewImageUrl)
    }

    fun updateThumbnailImage(imgUrl:String){
        thumbNailImage?.setImageUrl(imgUrl)
        ogImageUrl = imgUrl
    }

    fun setUtmCampaignData(pageName: String, userId: String, pageId: String, feature: String){
        val sharingDate: String = SimpleDateFormat("ddMMyy", Locale.getDefault()).format(
            Date()
        )
        var tempUsr = userId
        if(TextUtils.isEmpty(tempUsr)){
            tempUsr = "0"
        }
        campaignStr = "$pageName-$tempUsr-$pageId-$sharingDate"
        if(isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)){
            channelStr = "screenshot-share"
        }
        else {
            channelStr = feature
        }
    }

    fun setOgImageUrl(imgUrl: String){
        ogImageUrl = imgUrl
    }

    fun imageSaved(imgPath: String){
        if(isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)){
            savedImagePath = screenShotImagePath
        }
        else {
            savedImagePath = imgPath
        }
    }

    fun executeShareOptionClick(shareModel: ShareModel){
        shareModel.ogImgUrl = ogImageUrl
        shareModel.savedImageFilePath = savedImagePath
        bottomSheetListener?.onShareOptionClicked(shareModel)
    }

    fun setFeatureFlagRemoteConfigKey(key: String){
        featureFlagRemoteConfigKey = key
    }

    override fun show(manager: FragmentManager, tag: String?) {
        var customBottomSheetEnabled = true
        if(!TextUtils.isEmpty(featureFlagRemoteConfigKey)){
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            customBottomSheetEnabled = remoteConfig.getBoolean(featureFlagRemoteConfigKey)
        }
        if(customBottomSheetEnabled) {//enabled
            super.show(manager, tag)
        }else{
            //call the native bottom sheet share
            bottomSheetListener?.onShareOptionClicked(getNaviteShareIntent())
        }
    }

    fun getNaviteShareIntent(): ShareModel{
        val otherOptionsShareModel = ShareModel.Others().apply {
            socialMediaName = context?.resources?.getString(R.string.label_action_more)
            feature = socialMediaName
            campaign = campaignStr
            channel = channelStr
            shareOnlyLink = isImageOnlySharing
            ogImgUrl = ogImageUrl
        }
        return otherOptionsShareModel
    }

    override fun dismiss() {
        clearData()
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        clearData()
        super.onDismiss(dialog)
    }
}