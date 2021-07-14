package com.tokopedia.universal_sharing.view.bottomsheet

import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ImageListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ShareBottomSheetAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import java.io.File
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

        fun createInstance(): UniversalShareBottomSheet = UniversalShareBottomSheet()
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

    //Optons Flag
    private var isImageOnlySharing: Boolean = false
    private var campaignStr: String = ""
    private var channelStr: String = ""
    private var ogImageUrl: String = ""
    private var savedImagePath: String = ""
    //add remote config handling
    private var featureFlagRemoteConfigKey: String = ""

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

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
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
                bottomSheetListener?.onCloseBottomSheet()
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
                    }, 500)
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
        return mutableListOf(
            ShareModel.Whatsapp().apply {
                packageName = PACKAGE_NAME_WHATSAPP
                socialMediaName = context?.resources?.getString(R.string.label_whatsapp)
                feature = socialMediaName
                campaign = campaignStr
                channel = channelStr
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_whatsapp) }
            },
            ShareModel.Facebook().apply {
                packageName = PACKAGE_NAME_FACEBOOK
                socialMediaName = context?.resources?.getString(R.string.label_facebook)
                feature = socialMediaName
                campaign = campaignStr
                channel = channelStr
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
            },
            ShareModel.Facebook().apply {
                packageName = PACKAGE_NAME_FACEBOOK
                //facebook story can share only the images
                socialMediaName = context?.resources?.getString(R.string.label_facebook_story)
                feature = socialMediaName
                campaign = campaignStr
                channel = channelStr
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, actionType = FACEBOOK_STORY_INTENT_ACTION)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_icon_fbstories3) }
            },
                ShareModel.Instagram().apply {
                    packageName = PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_instagram)
                    feature = socialMediaName
                    campaign = campaignStr
                    channel = channelStr
                    shareOnlyLink = true
                    appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_FEED")
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_instagram) }
                },
                ShareModel.Instagram().apply {
                    packageName = PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_instagram_story)
                    feature = socialMediaName
                    campaign = campaignStr
                    channel = channelStr
                    shareOnlyLink = true
                    appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_STORY")
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_icon_igstory) }
                },
                ShareModel.Line().apply {
                    packageName = PACKAGE_NAME_LINE
                    socialMediaName = context?.resources?.getString(R.string.label_line)
                    feature = socialMediaName
                    campaign = campaignStr
                    channel = channelStr
                    shareOnlyLink = isImageOnlySharing
                    if(isImageOnlySharing){
                        appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    }
                    else{
                        appIntent = getAppIntent(MimeType.TEXT, packageName)
                    }
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_line) }
                },
                ShareModel.Twitter().apply {
                    packageName = PACKAGE_NAME_TWITTER
                    socialMediaName = context?.resources?.getString(R.string.label_twitter)
                    feature = socialMediaName
                    campaign = campaignStr
                    channel = channelStr
                    shareOnlyLink = isImageOnlySharing
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_twitter) }
                },
                ShareModel.Telegram().apply {
                    packageName = PACKAGE_NAME_TELEGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_telegram)
                    feature = socialMediaName
                    campaign = campaignStr
                    channel = channelStr
                    shareOnlyLink = isImageOnlySharing
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, R.drawable.universal_sharing_ic_icon_telegram) }
                }
        ).filterNot {
            (it.packageName!!.isNotEmpty() && it.appIntent != null && getResolvedActivity(context, it.appIntent) == null)
        }
    }

    private fun setFixedOptionsClickListeners(){
        val copyLinkShareModel = ShareModel.CopyLink().apply {
            socialMediaName = context?.resources?.getString(R.string.label_copy_link)
            feature = context?.resources?.getString(R.string.label_bhasha_copy_link)
            campaign = campaignStr
            channel = channelStr
            shareOnlyLink = isImageOnlySharing
        }
        copyLinkImage?.setOnClickListener {
            copyLinkShareModel.ogImgUrl = ogImageUrl
            bottomSheetListener?.onItemBottomsheetShareClicked(copyLinkShareModel)
        }

        val otherOptionsShareModel = ShareModel.Others().apply {
            socialMediaName = context?.resources?.getString(R.string.label_action_more)
            feature = socialMediaName
            campaign = campaignStr
            channel = channelStr
            shareOnlyLink = isImageOnlySharing
        }
        otherOptionsImage?.setOnClickListener {
            otherOptionsShareModel.ogImgUrl = ogImageUrl
            bottomSheetListener?.onItemBottomsheetShareClicked(otherOptionsShareModel)
        }

        if(!isImageOnlySharing) {
            smsImage?.visibility = View.VISIBLE
            smsTxtv?.visibility = View.VISIBLE
            val smsShareModel = ShareModel.SMS().apply {
                packageName = Telephony.Sms.getDefaultSmsPackage(context);
                socialMediaName = context?.resources?.getString(R.string.label_chat)
                feature = socialMediaName
                campaign = campaignStr
                channel = channelStr
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }

            smsImage?.setOnClickListener {
                smsShareModel.ogImgUrl = ogImageUrl
                bottomSheetListener?.onItemBottomsheetShareClicked(smsShareModel)
            }
        }

        val emailShareModel = ShareModel.Email().apply {
            packageName = PACKAGE_NAME_GMAIL
            socialMediaName = context?.resources?.getString(R.string.share_email)
            feature = socialMediaName
            campaign = campaignStr
            channel = channelStr
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
        }

        emailImage?.setOnClickListener {
            emailShareModel.ogImgUrl = ogImageUrl
            bottomSheetListener?.onItemBottomsheetShareClicked(emailShareModel)
        }
    }

    fun setMetaData(tnTitle: String, tnImage: String,
                    previewImgUrl: String = "",
                    imageList: ArrayList<String>? = null,
                    takeSS : ((view: View, imageSaved: ((String)->Unit)) -> Unit)? = null){
        thumbNailTitle = tnTitle
        thumbNailImageUrl = tnImage
        previewImageUrl = previewImgUrl
        imageOptionsList = imageList
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

    fun setImageOnlySharingOption(imageOnly:Boolean){
        isImageOnlySharing = imageOnly
    }

    fun updateThumbnailImage(imgUrl:String){
        thumbNailImage?.setImageUrl(imgUrl)
        ogImageUrl = imgUrl
    }

    fun setUtmCampaignData(pageName: String, userId: String, pageId: String, feature: String){
        val sharingDate: String = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(
            Date()
        )
        campaignStr = pageName+"-"+userId+"-"+pageId+"-"+sharingDate
        channelStr = feature
    }

    fun setOgImageUrl(imgUrl: String){
        ogImageUrl = imgUrl
    }

    fun imageSaved(imgPath: String){
        savedImagePath = imgPath
    }

    fun executeShareOptionClick(shareModel: ShareModel){
        shareModel.ogImgUrl = ogImageUrl
        shareModel.savedImageFilePath = savedImagePath
        bottomSheetListener?.onItemBottomsheetShareClicked(shareModel)
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
            bottomSheetListener?.onItemBottomsheetShareClicked(getNaviteShareIntent())
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

}