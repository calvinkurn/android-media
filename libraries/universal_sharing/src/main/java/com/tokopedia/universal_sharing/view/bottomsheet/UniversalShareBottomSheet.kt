package com.tokopedia.universal_sharing.view.bottomsheet

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.linker.LinkerManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.constants.BroadcastChannelType
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.ImageGeneratorParamModel
import com.tokopedia.universal_sharing.di.DaggerUniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareModule
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.model.BroadcastChannelModel
import com.tokopedia.universal_sharing.model.TickerShareModel
import com.tokopedia.universal_sharing.model.generateImageGeneratorParam
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.usecase.ExtractBranchLinkUseCase
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.universal_sharing.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ImageListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ShareBottomSheetAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.TickerListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.Exception
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.tokopedia.iconunify.R as unifyIconR

/**
 * Created by Rafli Syam 20/07/2020
 */
open class UniversalShareBottomSheet : BottomSheetUnify() {

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

        //remote config Social media ordering keys
        private const val KEY_IG_FEED = "IG_Feed"
        private const val KEY_IG_STORY = "IG_Story"
        private const val KEY_IG_DM = "IG_DM"
        private const val KEY_FB_FEED = "FB_Feed"
        private const val KEY_FB_STORY = "FB_Story"
        private const val KEY_WHATSAPP = "WhatsApp"
        private const val KEY_LINE = "Line"
        private const val KEY_TWITTER = "Twitter"
        private const val KEY_TELEGRAM = "Telegram"

        //add remote config handling
        private const val GLOBAL_CUSTOM_SHARING_FEATURE_FLAG = "android_enable_custom_sharing"
        private const val GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG = "android_enable_screenshot_sharing"
        private const val GLOBAL_AFFILIATE_FEATURE_FLAG = "android_enable_affiliate_universal_sharing"
        private const val GLOBAL_ENABLE_OG_IMAGE_TRANSFORM = "android_enable_og_image_transformation"
        private var featureFlagRemoteConfigKey: String = ""
        private const val SOCIAL_MEDIA_ORDERING = "android_universal_sharing_order"

        //Optons Flag
        private var isImageOnlySharing: Boolean = false
        private var screenShotImagePath: String = ""

        private const val DELAY_TIME_MILLISECOND = 500L
        private const val DELAY_TIME_AFFILIATE_ELIGIBILITY_CHECK = 5000L
        private const val SCREENSHOT_TITLE = "Yay, screenshot & link tersimpan!"
        const val CUSTOM_SHARE_SHEET = 1
        const val SCREENSHOT_SHARE_SHEET = 2
        const val PREVIEW_IMG_SCREENSHOT_HEIGHT = 600
        const val PREVIEW_IMG_SCREENSHOT_WIDTH = 1080
        const val THUMBNAIL_IMG_SCREENSHOT_HEIGHT = 200
        const val THUMBNAIL_IMG_SCREENSHOT_WIDTH = 360

        //for affiliate and general user distinction
        private const val KEY_GENERAL_USER = "general"
        private const val KEY_AFFILIATE_USER = "affiliate"
        private var isAffiliateUser: String = KEY_GENERAL_USER

        //Image Type
        const val KEY_NO_IMAGE = "no_image"
        const val KEY_IMAGE_DEFAULT = "default"
        const val KEY_CONTEXTUAL_IMAGE = "contextual_image"
        const val KEY_PRODUCT_ID = "productId";


        fun createInstance(): UniversalShareBottomSheet = UniversalShareBottomSheet()

        fun isCustomSharingEnabled(context: Context?, remoteConfigKey: String = GLOBAL_CUSTOM_SHARING_FEATURE_FLAG): Boolean {
            val isEnabled: Boolean
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            isEnabled = remoteConfig.getBoolean(remoteConfigKey)
            featureFlagRemoteConfigKey = remoteConfigKey
            return isEnabled
        }

        fun setImageOnlySharingOption(imageOnly: Boolean) {
            isImageOnlySharing = imageOnly
        }

        fun setScreenShotImagePath(imgPath: String) {
            screenShotImagePath = imgPath
        }

        fun createAndStartScreenShotDetector(context: Context, screenShotListener: ScreenShotListener,
                                             fragment: Fragment,
                                             remoteConfigKey: String = GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG,
                                             addFragmentLifecycleObserver: Boolean = false,
                                             permissionListener: PermissionListener? = null): ScreenshotDetector? {
            val isEnabled: Boolean
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            isEnabled = remoteConfig.getBoolean(remoteConfigKey)
            var screenshotDetector: ScreenshotDetector? = null
            if (isEnabled) {
                screenshotDetector = ScreenshotDetector(context.applicationContext, screenShotListener, permissionListener)
                if (addFragmentLifecycleObserver) {
                    setFragmentLifecycleObserverForScreenShot(fragment, screenshotDetector)
                }
                screenshotDetector.detectScreenshots(fragment)
            }
            return screenshotDetector
        }

        fun setFragmentLifecycleObserverForScreenShot(fragment: Fragment, screenshotDetector: ScreenshotDetector?) {
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
        fun getShareBottomSheetType(): Int {
            var shareSheetType = CUSTOM_SHARE_SHEET
            if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
                shareSheetType = SCREENSHOT_SHARE_SHEET
            }
            return shareSheetType
        }

        fun clearData() {
            isImageOnlySharing = false
            screenShotImagePath = ""
        }

        fun clearState(screenshotDetector: ScreenshotDetector?) {
            screenshotDetector?.stop()
            clearData()
        }

        fun removePreviousSavedImage(previousSavedImagePath: String, newSavedImagePath: String) {
            if (!TextUtils.isEmpty(previousSavedImagePath) &&
                !TextUtils.isEmpty(newSavedImagePath) &&
                previousSavedImagePath != newSavedImagePath
            ) {
                removeFile(previousSavedImagePath)
            }
        }

        private fun removeFile(filePath: String) {
            if (!TextUtils.isEmpty(filePath) &&
                !filePath.contains(ScreenshotDetector.screenShotRegex, true)) {
                File(filePath).apply {
                    if (exists()) {
                        delete()
                    }
                }
            }
        }

        fun getUserType(): String {
            return isAffiliateUser
        }
    }

    enum class MimeType(val type: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE),
        ALL(TYPE_ALL)
    }

    private var bottomSheetListener: ShareBottomsheetListener? = null
    private var rvSocialMediaList: RecyclerView? = null
    private var thumbNailTitleTxTv: Typography? = null
    private var thumbNailImage: ImageUnify? = null
    private var previewImage: ImageUnify? = null
    private var revImageOptionsContainer: RecyclerView? = null
    private var imageListViewGroup: Group? = null
    private var affiliateRegisterMsg: Typography? = null
    private var affiliateRegisterTitle: Typography? = null
    private var affiliateRegisterIcon: ImageView? = null
    private var affiliateRegisterContainer: CardUnify? = null
    private var rvTicker: RecyclerView? = null

    //Fixed sharing options
    private var copyLinkImage: ImageView? = null
    private var smsImage: ImageView? = null
    private var smsTxtv: Typography? = null
    private var emailImage: ImageView? = null
    private var otherOptionsImage: ImageView? = null

    //loader view
    private var loaderUnify: LoaderUnify? = null

    //affiliate commission view
    private var affiliateCommissionTextView: Typography? = null
    private var layoutCommisionExtra: View? = null

    private var thumbNailTitle = ""
    private var bottomSheetTitleRemoteConfKey = ""
    private var bottomSheetTitleStr = ""
    private var thumbNailImageUrl = ""
    private var thumbNailImageUrlFallback = ""
    private var previewImageUrl = ""
    private var imageOptionsList: ArrayList<String>? = ArrayList()
    private var takeViewSS: ((View, ((String) -> Unit)) -> Unit)? = null
    private var requestDataMap: Map<String, Any>? = null

    private var campaignStr: String = ""
    private var channelStr: String = ""
    private var ogImageUrl: String = ""
    private var savedImagePath: String = ""

    private var affiliateQueryData: AffiliatePDPInput? = null
    private var showLoader: Boolean = false
    private var handler: Handler? = null
    private var gqlCallJob: Job? = null

    //observer flag
    private var preserveImage: Boolean = false

    //parent fragment
    private var parentFragmentContainer: Fragment? = null

    //Image generator page source ID
    private lateinit var sourceId: String

    //Array to contain image generator API data
    private var imageGeneratorDataArray: ArrayList<ImageGeneratorRequestData>? = null
    private var gqlJob: Job? = null

    //Flag to control Image generator option
    private var getImageFromMedia = false
    private lateinit var imageGeneratorUseCase: ImageGeneratorUseCase

    //Dynamic Social Media ordering from Remote Config
    private var socialMediaOrderHashMap: HashMap<String, Int>? = null

    private val tracker: UniversalSharebottomSheetTracker by lazy {
        UniversalSharebottomSheetTracker(userSession)
    }

    private val userSession: UserSession by lazy {
        UserSession(LinkerManager.getInstance().context)
    }

    private var onViewReadyAction: (() -> Unit)? = null

    private var  imageGeneratorParam: ImageGeneratorParamModel? = null

    // parent fragment lifecycle observer
    private val parentFragmentLifecycleObserver by lazy {
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                removeFile(savedImagePath)
                super.onDestroy(owner)
            }
        }
    }

    @Inject lateinit var extractBranchLinkUseCase: ExtractBranchLinkUseCase

    /* boolean flag to show ticker list */
    private var isShowTickerList = false

    private val tickerListAdapter by lazy {
        TickerListAdapter(::onClickTicker)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }
    private var affiliateListener: ((userType: String) -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initImageOptionsRecyclerView()
        onViewReadyAction?.invoke()
    }

    private fun inject() {
        activity?.let {
            DaggerUniversalShareComponent.builder().baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                .universalShareModule(UniversalShareModule()).build().inject(this)
        }
    }

    fun init(bottomSheetListener: ShareBottomsheetListener) {
        clearContentPadding = true
        this.bottomSheetListener = bottomSheetListener
    }

    fun show(fragmentManager: FragmentManager?, fragment: Fragment, screenshotDetector: ScreenshotDetector? = null) {
        try {
            screenshotDetector?.detectScreenshots(fragment,
                {
                    fragmentManager?.let {
                        show(it, TAG)
                        setFragmentLifecycleObserverUniversalSharing(fragment)
                    }
                }, true, fragment.requireView())
                ?: fragmentManager?.let {
                    show(it, TAG)
                    setFragmentLifecycleObserverUniversalSharing(fragment)
                }
        } catch (ex: Exception) {
            logExceptionToRemote(ex)
        }
    }

    fun show(fragmentManager: FragmentManager?, fragment: Fragment, screenshotDetector: ScreenshotDetector? = null, safeViewAction: () -> Unit = {}) {
        onViewReadyAction = safeViewAction
        show(fragmentManager, fragment, screenshotDetector)
    }

    // call this method before show method if the request data is awaited
    fun affiliateRequestDataAwaited() {
        showLoader = true
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            affiliateRequestDataReceived(false)
        }, DELAY_TIME_AFFILIATE_ELIGIBILITY_CHECK)
    }

    /**
     * call this method if the request data is received
     * ideally, call this method after show Bottomsheet. there is a case hitting gql is finished, but view is not ready
     */
    fun affiliateRequestDataReceived(validRequest: Boolean) {
        val userSession = UserSession(LinkerManager.getInstance().context)
        if (userSession.isLoggedIn && validRequest && isAffiliateEnabled()) {
            executeAffiliateEligibilityUseCase()
            showLoader = true
            loaderUnify?.visibility = View.VISIBLE
        } else {
            clearLoader()
            affiliateQueryData = null
        }
    }

    private fun isAffiliateEnabled(): Boolean {
        if (LinkerManager.getInstance().context != null) {
            val remoteConfig = FirebaseRemoteConfigImpl(LinkerManager.getInstance().context)
            return remoteConfig.getBoolean(GLOBAL_AFFILIATE_FEATURE_FLAG, true)
        } else {
            return false
        }
    }

    private fun clearLoader() {
        showLoader = false
        loaderUnify?.visibility = View.GONE
    }

    private fun removeHandlerTimeout() {
        if (handler != null) {
            handler?.removeCallbacksAndMessages(null)
        }
    }

    private fun executeAffiliateEligibilityUseCase() {
        removeHandlerTimeout()
        gqlCallJob = CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val affiliateUseCase = AffiliateEligibilityCheckUseCase(GraphqlInteractor.getInstance().graphqlRepository)
                val generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility = affiliateUseCase.apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(affiliateQueryData!!)
                }.executeOnBackground()
                var deeplink = ""
                if (isExecuteExtractBranchLink(generateAffiliateLinkEligibility)) {
                    deeplink = executeExtractBranchLink(generateAffiliateLinkEligibility)
                }
                withContext(Dispatchers.Main) {
                    showAffiliateTicker(generateAffiliateLinkEligibility, deeplink)
                }
            }
        }, onError = {
            clearLoader()
            removeHandlerTimeout()
            it.printStackTrace()
        })
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            clearLoader()
            if (gqlCallJob?.isActive == true) {
                gqlCallJob?.cancel()
            }
            if (affiliateCommissionTextView?.visibility != View.VISIBLE) {
                affiliateQueryData = null
            }
        }, DELAY_TIME_AFFILIATE_ELIGIBILITY_CHECK)
    }

    private suspend fun executeExtractBranchLink(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): String {
        return try {
            extractBranchLinkUseCase(generateAffiliateLinkEligibility.banner?.ctaLink ?: "").android_deeplink
        } catch (e: Exception) {
            ""
        }
    }

    private fun isExecuteExtractBranchLink(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): Boolean {
        return generateAffiliateLinkEligibility.banner?.ctaLink?.isNotEmpty() == true && isShowAffiliateRegister(generateAffiliateLinkEligibility)
    }

    private fun showAffiliateTicker(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility, deeplink: String = "") {
        clearLoader()
        removeHandlerTimeout()

        if (isShowAffiliateComission(generateAffiliateLinkEligibility)) {
            showAffiliateCommission(generateAffiliateLinkEligibility)
        } else if (isShowAffiliateRegister(generateAffiliateLinkEligibility)) {
            showAffiliateRegister(generateAffiliateLinkEligibility, deeplink)
        }
        affiliateListener?.invoke(isAffiliateUser)

        if (generateAffiliateLinkEligibility.eligibleCommission?.ssaStatus == true) {
            showCommissionExtra(generateAffiliateLinkEligibility)
        }
    }

    private fun isShowAffiliateComission(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): Boolean {
        return generateAffiliateLinkEligibility.eligibleCommission?.isEligible == true
            && generateAffiliateLinkEligibility.affiliateEligibility?.isEligible == true
            && generateAffiliateLinkEligibility.affiliateEligibility?.isRegistered == true
    }

    private fun isShowAffiliateRegister(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): Boolean {
        return (generateAffiliateLinkEligibility.banner != null
            && generateAffiliateLinkEligibility.affiliateEligibility?.isRegistered == false) && userSession.isLoggedIn
            && userSession.shopId != affiliateQueryData?.shop?.shopID
    }

    private fun showAffiliateCommission(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility) {
        val commissionMessage = generateAffiliateLinkEligibility.eligibleCommission?.message ?: ""
        if (!TextUtils.isEmpty(commissionMessage)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                affiliateCommissionTextView?.text = Html.fromHtml(commissionMessage,
                    Html.FROM_HTML_MODE_LEGACY)
            } else {
                affiliateCommissionTextView?.text = Html.fromHtml(commissionMessage)
            }
            affiliateCommissionTextView?.visibility = View.VISIBLE
            tracker.viewOnAffiliateRegisterTicker(true, affiliateQueryData?.getIdFactory() ?: "", affiliateQueryData?.pageType ?: "")
            isAffiliateUser = KEY_AFFILIATE_USER
            return
        }
        affiliateQueryData = null
    }

    private fun showCommissionExtra(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility) {
        layoutCommisionExtra?.let { layoutCommisionExtra ->
            layoutCommisionExtra.visibility = View.VISIBLE
            val badgeView = layoutCommisionExtra.findViewById<Typography>(R.id.tg_commision_extra)
            val expiredDateView = layoutCommisionExtra.findViewById<Typography>(R.id.tg_expired_date)
            badgeView?.text = generateAffiliateLinkEligibility.eligibleCommission?.badge ?: ""
            expiredDateView?.text = generateAffiliateLinkEligibility.eligibleCommission?.expiredDateFormatted
                ?: ""
        }
    }

    private fun showAffiliateRegister(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility, deeplink: String) {
        generateAffiliateLinkEligibility.banner?.let { banner ->
            if (banner.title.isBlank() && banner.message.isBlank()) return

            affiliateRegisterContainer?.visible()
            tracker.viewOnAffiliateRegisterTicker(false, affiliateQueryData?.getIdFactory() ?: "", affiliateQueryData?.pageType ?: "")

            val id = affiliateQueryData?.getIdFactory() ?: ""
            val page = affiliateQueryData?.pageType ?: ""
            affiliateRegisterContainer?.setOnClickListener { _ ->
                tracker.onClickRegisterTicker(false, id, page)
                dismiss()
                RouteManager.route(context, Uri.parse(ApplinkConst.AFFILIATE_ONBOARDING).buildUpon().appendQueryParameter(KEY_PRODUCT_ID, "").build().toString())
            }
            affiliateRegisterIcon?.loadImage(banner.icon)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                affiliateRegisterTitle?.text = Html.fromHtml(banner.title, Html.FROM_HTML_MODE_LEGACY)
                affiliateRegisterMsg?.text = Html.fromHtml(banner.message, Html.FROM_HTML_MODE_LEGACY)

            } else {
                affiliateRegisterTitle?.text = Html.fromHtml(banner.title)
                affiliateRegisterMsg?.text = Html.fromHtml(banner.message)
            }

            isAffiliateUser = KEY_GENERAL_USER
        }
        affiliateQueryData = null
    }

    fun setOnGetAffiliateData(callback: (userType: String) -> Unit) {
        affiliateListener = callback
    }

    private fun setFragmentLifecycleObserverUniversalSharing(fragment: Fragment) {
        parentFragmentContainer = fragment
        parentFragmentContainer?.lifecycle?.addObserver(parentFragmentLifecycleObserver)
    }

    override fun onDestroy() {
        parentFragmentContainer?.lifecycle?.removeObserver(parentFragmentLifecycleObserver)
        parentFragmentContainer = null
        super.onDestroy()
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
            loaderUnify = findViewById(R.id.loader)
            affiliateCommissionTextView = findViewById(R.id.affilate_commision)
            affiliateRegisterMsg = findViewById(R.id.tv_description_affiliate)
            affiliateRegisterTitle = findViewById(R.id.tv_title_affiliate)
            affiliateRegisterIcon = findViewById(R.id.iv_affiliate)
            affiliateRegisterContainer = findViewById(R.id.card_register_affiliate)
            layoutCommisionExtra = findViewById(R.id.layout_commission_extra)
            rvTicker = findViewById(R.id.rv_ticker)
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
            generateRemoteConfigSocialMediaOrdering()
            adapter = ShareBottomSheetAdapter(context, ::executeShareOptionClick, generateSocialMediaList(context))
        }

        rvTicker?.shouldShowWithAction(isShowTickerList) {
            rvTicker?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = tickerListAdapter
            }
        }
    }

    private fun initImageOptionsRecyclerView() {
        if (imageOptionsList != null && imageOptionsList!!.size > 0) {
            revImageOptionsContainer?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = context?.let { ImageListAdapter(imageOptionsList!!, it, takeViewSS!!, ::imageSaved, ::updateThumbnailImage) }
            }
            revImageOptionsContainer?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    revImageOptionsContainer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    Handler(Looper.getMainLooper()).postDelayed({
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

    private fun getAppIntent(type: MimeType, packageName: String?, actionType: String = Intent.ACTION_SEND, uri: Uri? = null): Intent {
        val intentType = if (type == MimeType.IMAGE) {
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
        socialMediaList.add(ShareModel.Whatsapp().apply {
            packageName = PACKAGE_NAME_WHATSAPP
            socialMediaName = context?.resources?.getString(R.string.label_whatsapp)
            feature = channelStr
            campaign = campaignStr
            socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_WHATSAPP)
            channel = SharingUtil.labelWhatsapp
            platform = ImageGeneratorConstants.ImageGeneratorPlatforms.WHATSAPP
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_whatsapp) }
        })
        socialMediaList.add(ShareModel.Facebook().apply {
            packageName = PACKAGE_NAME_FACEBOOK
            socialMediaName = context?.resources?.getString(R.string.label_facebook)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelFbfeed
            platform = ImageGeneratorConstants.ImageGeneratorPlatforms.FACEBOOK_FEED
            socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_FB_FEED)
            shareOnlyLink = isImageOnlySharing
            if (isImageOnlySharing) {
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
            } else {
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }
            appIntent?.component = ComponentName(
                PACKAGE_NAME_FACEBOOK,
                FACEBOOK_FEED_ACTIVITY
            )
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_facebook) }
        })
        if (!TextUtils.isEmpty(savedImagePath)) {
            socialMediaList.add(ShareModel.Facebook().apply {
                packageName = PACKAGE_NAME_FACEBOOK
                //facebook story can share only the images
                socialMediaName = context?.resources?.getString(R.string.label_facebook_story)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelFbstory
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_FB_STORY)
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.FACEBOOK_STORY
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, actionType = FACEBOOK_STORY_INTENT_ACTION)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_facebook_story) }
            })
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_feed)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgfeed
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_FEED
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_IG_FEED)
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_FEED")
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram) }
            })
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_story)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgstory
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_IG_STORY)
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_STORY
                shareOnlyLink = true
                appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_STORY")
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram_story) }
            })
        } else {
            socialMediaList.add(ShareModel.Instagram().apply {
                packageName = PACKAGE_NAME_INSTAGRAM
                socialMediaName = context?.resources?.getString(R.string.label_instagram_dm)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelIgMessage
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_FEED
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_IG_DM)
                shareOnlyLink = false
                appIntent = getAppIntent(MimeType.TEXT, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram_dm) }
            })
        }
        socialMediaList.add(ShareModel.Line().apply {
            packageName = PACKAGE_NAME_LINE
            socialMediaName = context?.resources?.getString(R.string.label_line)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelLine
            platform = ImageGeneratorConstants.ImageGeneratorPlatforms.LINE
            socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_LINE)
            shareOnlyLink = isImageOnlySharing
            if (isImageOnlySharing) {
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
            } else {
                appIntent = getAppIntent(MimeType.TEXT, packageName)
            }
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_line) }
        })
        socialMediaList.add(ShareModel.Twitter().apply {
            packageName = PACKAGE_NAME_TWITTER
            socialMediaName = context?.resources?.getString(R.string.label_twitter)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelTwitter
            platform = ImageGeneratorConstants.ImageGeneratorPlatforms.TWITTER
            socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_TWITTER)
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_twitter) }
        })
        socialMediaList.add(ShareModel.Telegram().apply {
            packageName = PACKAGE_NAME_TELEGRAM
            socialMediaName = context?.resources?.getString(R.string.label_telegram)
            feature = channelStr
            campaign = campaignStr
            channel = SharingUtil.labelTelegram
            platform = ImageGeneratorConstants.ImageGeneratorPlatforms.TELEGRAM
            socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, KEY_TELEGRAM)
            shareOnlyLink = isImageOnlySharing
            appIntent = getAppIntent(MimeType.IMAGE, packageName)
            socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_telegram) }
        })
        socialMediaList.sortBy { it.socialMediaOrderingScore }
        return socialMediaList.filterNot {
            (it.packageName!!.isNotEmpty() && it.appIntent != null && getResolvedActivity(context, it.appIntent) == null)
        }
    }

    private fun setFixedOptionsClickListeners() {
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

        if (!isImageOnlySharing) {
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
                    takeSS: ((view: View, imageSaved: ((String) -> Unit)) -> Unit)? = null) {

        if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
            previewImageUrl = screenShotImagePath
            savedImagePath = screenShotImagePath
            thumbNailImageUrl = screenShotImagePath
            thumbNailImageUrlFallback = tnImage
            thumbNailTitle = SCREENSHOT_TITLE
            imageOptionsList = null
        } else {
            thumbNailTitle = tnTitle
            thumbNailImageUrl = tnImage
            previewImageUrl = previewImgUrl
            imageOptionsList = imageList
            imageOptionsList?.let {
                if (it.size > 0) {
                    imageSaved(it[0])
                }
            }
        }
        if (takeSS == null) {
            takeViewSS = (SharingUtil)::triggerSS
        } else {
            takeViewSS = takeSS
        }
    }

    fun setRequestData(requestPayLoad: Map<String, Any>?) {
        requestDataMap = requestPayLoad
    }

    fun setAffiliateRequestHolder(affiliatePDPInput: AffiliatePDPInput) {
        if (UserSession(LinkerManager.getInstance().context).isLoggedIn) {
            this.affiliateQueryData = affiliatePDPInput
        }
    }

    fun getAffiliateRequestHolder(): AffiliatePDPInput? {
        return affiliateQueryData
    }

    fun setBottomSheetTitle(title: String) {
        bottomSheetTitleStr = title
    }

    fun setBottomSheetTitleRemoteConfKey(key: String) {
        bottomSheetTitleRemoteConfKey = key
    }

    private fun setUserVisualData() {
        thumbNailTitleTxTv?.text = thumbNailTitle
        if (isImageOnlySharing) {
            try {
                context?.let {
                    thumbNailImage?.let { imgView ->
                        Glide.with(it).load(thumbNailImageUrl).override(THUMBNAIL_IMG_SCREENSHOT_WIDTH, THUMBNAIL_IMG_SCREENSHOT_HEIGHT).into(
                            imgView
                        )
                    }
                }
            } catch (ex: Exception) {
                thumbNailImage?.setImageUrl(thumbNailImageUrlFallback)
                logExceptionToRemote(ex)
            }
        } else {
            thumbNailImage?.setImageUrl(thumbNailImageUrl)
        }
        if (previewImageUrl.isNullOrEmpty()) {
            previewImage?.visibility = View.GONE
        } else {
            previewImage?.visibility = View.VISIBLE
            if (isImageOnlySharing) {
                try {
                    context?.let {
                        previewImage?.let { imgView ->
                            Glide.with(it).load(previewImageUrl).override(PREVIEW_IMG_SCREENSHOT_WIDTH, PREVIEW_IMG_SCREENSHOT_HEIGHT).into(
                                imgView
                            )
                        }
                    }
                } catch (ex: Exception) {
                    previewImage?.visibility = View.GONE
                    logExceptionToRemote(ex)
                }
            } else {
                previewImage?.setImageURI(Uri.parse(File(previewImageUrl).toString()))
            }
        }
        if (imageOptionsList != null) {
            imageListViewGroup?.visibility = View.VISIBLE
        } else {
            imageListViewGroup?.visibility = View.GONE
        }
//        previewImage?.setImageUrl(previewImageUrl)
        if (showLoader) {
            loaderUnify?.visibility = View.VISIBLE
        }
    }

    fun updateThumbnailImage(imgUrl: String) {
        thumbNailImage?.setImageUrl(imgUrl)
        ogImageUrl = imgUrl
    }

    fun setUtmCampaignData(pageName: String, userId: String, pageId: String, feature: String) {
        val sharingDate: String = SimpleDateFormat("ddMMyy", Locale.getDefault()).format(
            Date()
        )
        var tempUsr = userId
        if (TextUtils.isEmpty(tempUsr)) {
            tempUsr = "0"
        }
        val imageType = getImageTypeForUTM()
        campaignStr = "$pageName-$tempUsr-$pageId-$sharingDate-$imageType"
        if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
            channelStr = "screenshot-share"
        } else {
            channelStr = feature
        }
    }

    private fun getImageTypeForUTM(): String {
        return if (getImageFromMedia) {
            KEY_CONTEXTUAL_IMAGE
        } else if (!TextUtils.isEmpty(savedImagePath) && TextUtils.isEmpty(screenShotImagePath)) {
            KEY_IMAGE_DEFAULT
        } else {
            KEY_NO_IMAGE
        }
    }

    //  can be called like this  setUtmCampaignData(listOf("a", "b"), "c", "d", "e")
//  seller specific example  setUtmCampaignData(listOf("ShopRS", "$[User ID]", "$[Shop ID]", "$[Campaign Type ID]"), "$userId", "$pageId", "$feature")
    fun setUtmCampaignData(pageName: String, userId: String, pageIdConstituents: List<String>, feature: String) {
        val pageIdCombined = TextUtils.join("-", pageIdConstituents)
        setUtmCampaignData(pageName, userId, pageIdCombined, feature)
    }


    fun setOgImageUrl(imgUrl: String) {
        ogImageUrl = imgUrl
    }

    fun imageSaved(imgPath: String) {
        if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
            savedImagePath = screenShotImagePath
        } else {
            removePreviousSavedImage(savedImagePath, imgPath)
            savedImagePath = imgPath
        }
    }

    fun executeShareOptionClick(shareModel: ShareModel) {
        setIfAffiliate(shareModel)
        if (getImageFromMedia) {
            when (sourceId) {
                ImageGeneratorConstants.ImageGeneratorSourceId.AB_TEST_PDP -> {
                    executePdpContextualImage(shareModel)
                }
                ImageGeneratorConstants.ImageGeneratorSourceId.WISHLIST_COLLECTION -> {
                    executeWishlistCollectionContextualImage(shareModel)
                }
                else -> {
                    addImageGeneratorData(ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM, shareModel.platform)
                    addImageGeneratorData(ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL, ogImageUrl)
                    imageGeneratorDataArray?.let { executeImageGeneratorUseCase(it, shareModel) }
                }
            }
        } else {
            executeSharingFlow(shareModel)
        }
    }

    private fun executePdpContextualImage(shareModel: ShareModel) {
        if (imageGeneratorParam == null || !(imageGeneratorParam is PdpParamModel)) return
        (imageGeneratorParam as? PdpParamModel)?.apply {
            this.platform = shareModel.platform
            this.productImageUrl = transformOgImageURL(ogImageUrl)
        }

        lifecycleScope.launchCatchError(block = {
            val result = ImagePolicyUseCase(GraphqlInteractor.getInstance().graphqlRepository)(sourceId)
            val listOfParams = result.generateImageGeneratorParam(imageGeneratorParam!!)
            executeImageGeneratorUseCase(listOfParams, shareModel)
        }, onError =  {
            executeSharingFlow(shareModel)
        })
    }

    private fun executeWishlistCollectionContextualImage(shareModel: ShareModel) {
        if (imageGeneratorParam == null) return
        imageGeneratorParam?.apply {
            this.platform = shareModel.platform
        }
        lifecycleScope.launchCatchError(block = {
            val result = ImagePolicyUseCase(GraphqlInteractor.getInstance().graphqlRepository)(sourceId)
            val listOfParams = result.generateImageGeneratorParam(imageGeneratorParam!!)
            executeImageGeneratorUseCase(listOfParams, shareModel)
        }, onError = {
            executeSharingFlow(shareModel)
        })
    }

    private fun executeMediaImageSharingFlow(shareModel: ShareModel, mediaImageUrl: String) {
        activity?.runOnUiThread {
            loaderUnify?.visibility = View.GONE
        }
        preserveImage = true
        shareModel.ogImgUrl = mediaImageUrl
        shareModel.savedImageFilePath = savedImagePath
        bottomSheetListener?.onShareOptionClicked(shareModel)
    }

    private fun executeSharingFlow(shareModel: ShareModel) {
        activity?.runOnUiThread {
            loaderUnify?.visibility = View.GONE
        }
        preserveImage = true
        shareModel.ogImgUrl = transformOgImageURL(ogImageUrl)
        shareModel.savedImageFilePath = savedImagePath
        bottomSheetListener?.onShareOptionClicked(shareModel)
    }

    private fun setIfAffiliate(shareModel: ShareModel) {
        if (affiliateQueryData != null &&
            affiliateCommissionTextView?.visibility == View.VISIBLE) {
            shareModel.isAffiliate = true
        }
    }

    private fun transformOgImageURL(imageURL: String): String {
        if (context != null) {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val ogImageTransformationEnabled = remoteConfig.getBoolean(GLOBAL_ENABLE_OG_IMAGE_TRANSFORM)
            if (ogImageTransformationEnabled && !TextUtils.isEmpty(imageURL) && imageURL.endsWith(".webp")) {
                if (imageURL.endsWith(".png.webp") || imageURL.endsWith(".jpg.webp")
                    || imageURL.endsWith(".jpeg.webp")
                ) {
                    return imageURL.replace(".webp", "")
                }
            }
        }
        return imageURL
    }

    fun setFeatureFlagRemoteConfigKey(key: String) {
        featureFlagRemoteConfigKey = key
    }

    override fun show(manager: FragmentManager, tag: String?) {
        var customBottomSheetEnabled = true
        if (!TextUtils.isEmpty(featureFlagRemoteConfigKey)) {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            customBottomSheetEnabled = remoteConfig.getBoolean(featureFlagRemoteConfigKey)
        }
        if (customBottomSheetEnabled) {//enabled
            super.show(manager, tag)
        } else {
            //call the native bottom sheet share
            bottomSheetListener?.onShareOptionClicked(getNaviteShareIntent())
        }
    }

    fun getNaviteShareIntent(): ShareModel {
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

    private fun removeLifecycleObserverAndSavedImage() {
        if (!preserveImage) {
            removeFile(savedImagePath)
            parentFragmentContainer?.lifecycle?.removeObserver(parentFragmentLifecycleObserver)
        }
    }

    override fun dismiss() {
        try {
            onViewReadyAction = null
            affiliateListener = null
            clearData()
            removeLifecycleObserverAndSavedImage()
            if (gqlCallJob?.isActive == true) {
                gqlCallJob?.cancel()
            }
            if (gqlJob?.isActive == true) {
                gqlJob?.cancel()
            }
            super.dismiss()
        } catch (ex: Exception) {
            logExceptionToRemote(ex)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        try {
            onViewReadyAction = null
            affiliateListener = null
            clearData()
            removeLifecycleObserverAndSavedImage()
            if (gqlCallJob?.isActive == true) {
                gqlCallJob?.cancel()
            }
            if (gqlJob?.isActive == true) {
                gqlJob?.cancel()
            }
            super.onDismiss(dialog)
        } catch (ex: Exception) {
            logExceptionToRemote(ex)
        }
    }

    private fun logExceptionToRemote(ex: Exception) {
        if (ex.localizedMessage != null) {
            val errorMap = mapOf("type" to "crashLog", "reason" to (ex.localizedMessage))
            SharingUtil.logError(errorMap)
        }
    }

    fun addImageGeneratorData(key: String, value: String) {
        if (imageGeneratorDataArray == null) {
            imageGeneratorDataArray = ArrayList()
        }
        imageGeneratorDataArray?.add(ImageGeneratorRequestData(key, value))
    }

    private fun executeImageGeneratorUseCase(args: ArrayList<ImageGeneratorRequestData>,
                                             shareModel: ShareModel) {
        loaderUnify?.visibility = View.VISIBLE
        gqlJob = CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                imageGeneratorUseCase = ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
                val response = imageGeneratorUseCase.apply {
                    params = ImageGeneratorUseCase.createParam(sourceId, args)
                }.executeOnBackground()
                val mediaImageUrl = response.imageUrl

                /* for A/B Testing on PDP Page */
                if (sourceId == ImageGeneratorConstants.ImageGeneratorSourceId.AB_TEST_PDP) {
                    setAbTestContextual(shareModel, response.sourceId)
                }
                SharingUtil.saveImageFromURLToStorage(context, mediaImageUrl) {
                    imageSaved(it)
                    executeMediaImageSharingFlow(shareModel, mediaImageUrl)
                }
            }
        }, onError = {
            it.printStackTrace()
            executeSharingFlow(shareModel)
        })
    }

    /***
     * @param sourceId is from result of [ImageGeneratorUseCase]
     */
    private fun setAbTestContextual(shareModel: ShareModel, sourceId: String) {
        if (getImageFromMedia) {
            shareModel.campaign = shareModel.campaign?.replace(KEY_CONTEXTUAL_IMAGE, sourceId)
        }
    }

    fun getImageFromMedia(getImageFromMediaFlag: Boolean) {
        getImageFromMedia = getImageFromMediaFlag
        savedImagePath = "{media_image}"
    }

    fun setImageGeneratorParam(param: ImageGeneratorParamModel) {
        imageGeneratorParam = param
    }

    /* set page source id */
    fun setMediaPageSourceId(pageSourceId: String) {
        sourceId = pageSourceId
    }

    fun setBroadcastChannel(context: Context, type: BroadcastChannelType, id: String) {
        isShowTickerList = true
        tickerListAdapter.addItem(
            BroadcastChannelModel(
            id = id, type = type,
            title = context.getString(com.tokopedia.universal_sharing.R.string.title_broadcast),
            description = context.getString(com.tokopedia.universal_sharing.R.string.description_broadcast),
            imageResDrawable = com.tokopedia.universal_sharing.R.drawable.ic_broadcast
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun generateRemoteConfigSocialMediaOrdering() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val socialMediaOrderingArray: String? = remoteConfig.getString(SOCIAL_MEDIA_ORDERING)
        if (!TextUtils.isEmpty(socialMediaOrderingArray)) {
            val socialMediaJsonArray: JSONArray? = JSONArray(socialMediaOrderingArray)
            if (socialMediaJsonArray != null) {
                socialMediaOrderHashMap = HashMap()
                for (i in 0 until socialMediaJsonArray.length()) {
                    socialMediaOrderHashMap?.put(socialMediaJsonArray.getString(i), i)
                }
            }
        }
    }

    private fun getSocialMediaOrderingScore(originalScore: Int, socialMediaKey: String): Int {
        return if (socialMediaOrderHashMap != null && socialMediaOrderHashMap?.containsKey(socialMediaKey) == true) {
            socialMediaOrderHashMap?.get(socialMediaKey)!!
        } else {
            originalScore
        }
    }

    private fun onClickTicker(ticker: TickerShareModel) {
        when (ticker) {
            is BroadcastChannelModel -> {
                openBroadcastPage(ticker.id, ticker.type.value)
            }
        }
    }

    private fun openBroadcastPage(id: String, type: Int) {
        RouteManager.route(
            context,
            String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, "https://www.tokopedia.com/broadcast-chat/create/content?id=$id&type=$type")
        )
    }
}
