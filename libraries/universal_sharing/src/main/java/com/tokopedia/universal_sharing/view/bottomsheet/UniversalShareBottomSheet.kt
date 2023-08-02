package com.tokopedia.universal_sharing.view.bottomsheet

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ResolveInfo
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
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
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
import com.tokopedia.universal_sharing.di.DaggerUniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareModule
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.universal_sharing.model.BroadcastChannelModel
import com.tokopedia.universal_sharing.model.CampaignStatus
import com.tokopedia.universal_sharing.model.ImageGeneratorParamModel
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.model.PersonalizedCampaignModel
import com.tokopedia.universal_sharing.model.ShopPageParamModel
import com.tokopedia.universal_sharing.model.TickerShareModel
import com.tokopedia.universal_sharing.model.WishlistCollectionParamModel
import com.tokopedia.universal_sharing.model.generateImageGeneratorParam
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.usecase.ExtractBranchLinkUseCase
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.universal_sharing.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.util.DateUtil
import com.tokopedia.universal_sharing.util.MimeType
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ChipsAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ImageListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.ShareBottomSheetAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.TickerListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.ChipProperties
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.tokopedia.iconunify.R as unifyIconR
import com.tokopedia.universal_sharing.R as sharingR

/**
 * Created by Rafli Syam 20/07/2020
 *
 * please read the docs to implement [UniversalShareBottomSheet]
 * link: https://tokopedia.atlassian.net/l/cp/HNSff10B
 * @see com.tokopedia.shop.pageheader.presentation.fragment.NewShopFragment.kt
 */
open class UniversalShareBottomSheet : BottomSheetUnify(), HasComponent<UniversalShareComponent> {

    @Inject lateinit var userSession: UserSessionInterface

    @Inject lateinit var extractBranchLinkUseCase: ExtractBranchLinkUseCase

    @Inject lateinit var affiliateUsecase: AffiliateEligibilityCheckUseCase

    @Inject lateinit var shareTracker: UniversalSharebottomSheetTracker

    @Inject lateinit var imagePolicyUseCase: ImagePolicyUseCase

    @Inject lateinit var imageGeneratorUseCase: ImageGeneratorUseCase

    // View
    private var fragmentView: View? = null
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

    // View - Chip
    private var chipOptionHeader: Typography? = null
    private var lstChip: RecyclerView? = null

    // Options Flag
    private var featureFlagRemoteConfigKey: String = ""
    private var isImageOnlySharing: Boolean = false
    private var screenShotImagePath: String = ""

    // Fixed sharing options
    private var copyLinkImage: ImageView? = null
    private var smsImage: ImageView? = null
    private var smsTxtv: Typography? = null
    private var emailImage: ImageView? = null
    private var otherOptionsImage: ImageView? = null

    // loader view
    private var loaderUnify: LoaderUnify? = null

    // affiliate commission view
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
    private var shareText: String = ""
    private var subjectShare: String = ""
    private var linkProperties: LinkProperties? = null
    private var affiliateInput: AffiliateInput? = null
    private var affiliateInputTemp: AffiliateInput? = null
    private var userType: String = KEY_GENERAL_USER
    private var isAffiliateCommissionEnabled = false
    private var chipListData: List<ChipProperties>? = null

    private var showLoader: Boolean = false
    private var handler: Handler? = null
    private var gqlCallJob: Job? = null

    // observer flag
    private var preserveImage: Boolean = false

    // parent fragment
    private var parentFragmentContainer: Fragment? = null

    // Image generator page source ID
    private lateinit var sourceId: String

    // Array to contain image generator API data
    private var imageGeneratorDataArray: ArrayList<ImageGeneratorRequestData>? = null
    private var gqlJob: Job? = null

    // Flag to control Image generator option
    private var getImageFromMedia = false

    // Dynamic Social Media ordering from Remote Config
    private var socialMediaOrderHashMap: HashMap<String, Int>? = null

    private var affiliateListener: ((userType: String) -> Unit)? = null

    private var imageGeneratorParam: ImageGeneratorParamModel? = null

    private var imageThumbnailListener: ((imageUrl: String) -> Unit)? = null

    private var isInitialClickThumbnail = true

    /* flag to enable default share */
    private var isDefaultShareIntent = false

    // Variable to set personalized campaign
    private var personalizedMessage = ""
    private var personalizedImage = ""
    private var personalizedCampaignModel: PersonalizedCampaignModel? = null

    // parent fragment lifecycle observer
    private val parentFragmentLifecycleObserver by lazy {
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                SharingUtil.removeFile(savedImagePath)
                super.onDestroy(owner)
            }
        }
    }

    /* boolean flag to show ticker list */
    private var isShowTickerList = false

    private val tickerListAdapter by lazy {
        TickerListAdapter(::onClickTicker)
    }

    private var chipSelectedListener: ChipsAdapter.Listener? = null

    /* flag to show chip list */
    private var isShowChipList = false

    private val chipListAdapter by lazy {
        ChipsAdapter(object : ChipsAdapter.Listener {
            override fun onChipChanged(chip: ChipProperties) {
                chipSelectedListener?.onChipChanged(chip)
                setLinkProperties(chip.properties)
            }
        })
    }

    override fun getComponent(): UniversalShareComponent? {
        return DaggerUniversalShareComponent.builder().baseAppComponent((LinkerManager.getInstance().context.applicationContext as BaseMainApplication).baseAppComponent)
            .universalShareModule(UniversalShareModule()).universalShareUseCaseModule(UniversalShareUseCaseModule()).build()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initImageOptionsRecyclerView()
        initAffiliate()
    }

    override fun onDestroy() {
        parentFragmentContainer?.lifecycle?.removeObserver(parentFragmentLifecycleObserver)
        parentFragmentContainer = null
        super.onDestroy()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        var customBottomSheetEnabled = true
        if (!TextUtils.isEmpty(featureFlagRemoteConfigKey)) {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            customBottomSheetEnabled = remoteConfig.getBoolean(featureFlagRemoteConfigKey)
        }
        if (customBottomSheetEnabled) { // enabled
            super.show(manager, tag)
        } else {
            // call the native bottom sheet share
            val shareModel = getNativeShareIntent()
            if (isDefaultShareIntent) {
                shareChannelClicked(shareModel)
            }
            bottomSheetListener?.onShareOptionClicked(shareModel)
        }
    }

    override fun dismiss() {
        try {
            affiliateListener = null
            imageThumbnailListener = null
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
            affiliateListener = null
            chipSelectedListener = null
            imageThumbnailListener = null
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

    init {
        inject()
    }

    fun init(bottomSheetListener: ShareBottomsheetListener) {
        clearContentPadding = true
        this.bottomSheetListener = bottomSheetListener
    }

    fun show(fragmentManager: FragmentManager?, fragment: Fragment, screenshotDetector: ScreenshotDetector? = null) {
        try {
            screenshotDetector?.detectScreenshots(
                fragment,
                {
                    fragmentManager?.let {
                        show(it, TAG)
                        setFragmentLifecycleObserverUniversalSharing(fragment)
                    }
                },
                true,
                fragment.requireView()
            )
                ?: fragmentManager?.let {
                    show(it, TAG)
                    setFragmentLifecycleObserverUniversalSharing(fragment)
                }
        } catch (ex: Exception) {
            logExceptionToRemote(ex)
        }
    }

    // call this method before show method if the request data is awaited
    @Deprecated("this function is deprecated. Please use enableAffiliateCommission")
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
    @Deprecated("this function is deprecated. Please use enableAffiliateCommission")
    fun affiliateRequestDataReceived(validRequest: Boolean) {
        if (userSession.isLoggedIn && validRequest && isAffiliateEnabled()) {
            executeAffiliateEligibilityUseCase()
            showLoader = true
            loaderUnify?.visibility = View.VISIBLE
        } else {
            clearLoader()
            affiliateInput = null
        }
    }

    fun enableDefaultShareIntent() {
        isDefaultShareIntent = true
    }

    fun setOnGetAffiliateData(callback: (userType: String) -> Unit) {
        affiliateListener = callback
    }

    fun setMetaData(
        tnTitle: String,
        tnImage: String,
        previewImgUrl: String = "",
        imageList: ArrayList<String>? = null,
        takeSS: ((view: View, imageSaved: ((String) -> Unit)) -> Unit)? = null
    ) {
        if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
            previewImageUrl = screenShotImagePath
            savedImagePath = screenShotImagePath
            thumbNailImageUrl = screenShotImagePath
            thumbNailImageUrlFallback = tnImage
            thumbNailTitle = getString(sharingR.string.screenshoot_success_title)
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

    /**
     * to enable affiliate commission
     * if this flag is enabled, the bottomsheet will show
     * affiliate commission ticker if user registered
     * otherwise, affiliate register ticker will be showed
     * @see [https://tokopedia.atlassian.net/wiki/spaces/AF/pages/1693717743/Validate+Affiliate+Link+Generation+Eligibility]
     */
    fun enableAffiliateCommission(affiliateInput: AffiliateInput) {
        isAffiliateCommissionEnabled = true
        affiliateInputTemp = affiliateInput
    }

    fun isAffiliateCommissionEnabled() = isAffiliateCommissionEnabled

    @Deprecated("this function is deprecated. Please use enableAffiliateCommission")
    fun setAffiliateRequestHolder(affiliateInput: AffiliateInput) {
        if (userSession.isLoggedIn) {
            this.affiliateInput = affiliateInput
        }
    }

    fun getAffiliateRequestHolder(): AffiliateInput? {
        return affiliateInput
    }

    fun setBottomSheetTitle(title: String) {
        bottomSheetTitleStr = title
    }

    fun setBottomSheetTitleRemoteConfKey(key: String) {
        bottomSheetTitleRemoteConfKey = key
    }

    fun setFeatureFlagRemoteConfigKey(remoteConfigKey: String = UniversalShareConst.RemoteConfigKey.GLOBAL_CUSTOM_SHARING_FEATURE_FLAG) {
        featureFlagRemoteConfigKey = remoteConfigKey
    }

    private fun updateThumbnailImage(imgUrl: String) {
        if (!isInitialClickThumbnail) {
            imageThumbnailListener?.invoke(imgUrl)
        }
        thumbNailImage?.setImageUrl(imgUrl)
        ogImageUrl = imgUrl
    }

    /* this func is used to set subject on email */
    fun setSubject(subject: String) {
        subjectShare = subject
    }

    /**
     *  this func is used to set text on textfield channel,
     *  @param text please put string as String.format and insert `%s` to insert link into the text
     *  @param text e.g: Hai kamu! Belanja produk dari Oreo,Kraft & Cadbury Official Store jadi makin mudah di Tokopedia! Cek barang-barang yang kamu suka, yuk. %s
     *  the [text] will be transformed to `Hai kamu! Belanja produk dari Oreo,Kraft & Cadbury Official Store jadi makin mudah di Tokopedia! Cek barang-barang yang kamu suka, yuk. https://tokopedia.link/owQLAVeA3wb`
     */
    fun setShareText(text: String) {
        shareText = text
    }

    fun setChipList(chips: List<ChipProperties>) {
        chipListData = chips
    }

    fun onChipChangedListener(invoke: (ChipProperties) -> Unit) {
        chipSelectedListener = object : ChipsAdapter.Listener {
            override fun onChipChanged(chip: ChipProperties) {
                invoke(chip)
            }
        }
    }

    fun setSelectThumbnailImageListener(listener: (imgUrl: String) -> Unit) {
        imageThumbnailListener = listener
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

    /**
     * can be called like this  setUtmCampaignData(listOf("a", "b"), "c", "d", "e")
     * seller specific example  setUtmCampaignData(listOf("ShopRS", "$[User ID]", "$[Shop ID]", "$[Campaign Type ID]"), "$userId", "$pageId", "$feature")
     */
    fun setUtmCampaignData(pageName: String, userId: String, pageIdConstituents: List<String>, feature: String) {
        val pageIdCombined = TextUtils.join("-", pageIdConstituents)
        setUtmCampaignData(pageName, userId, pageIdCombined, feature)
    }

    /**
     * this function is deprecated and replaced with [setLinkProperties]
     */
    @Deprecated("if you are enable enableDefaultShareIntent, please use setLinkProperties to set ogImage")
    fun setOgImageUrl(imgUrl: String) {
        ogImageUrl = imgUrl
    }

    /**
     * this function to set properties that commonly used in [LinkerData]
     * @see LinkerData is object that is used to create Branch link.
     * @param linkProperties
     */
    fun setLinkProperties(linkProperties: LinkProperties) {
        this.linkProperties = linkProperties
        ogImageUrl = linkProperties.ogImageUrl
    }

    fun imageSaved(imgPath: String) {
        if (isImageOnlySharing && !TextUtils.isEmpty(screenShotImagePath)) {
            savedImagePath = screenShotImagePath
        } else {
            SharingUtil.removePreviousSavedImage(savedImagePath, imgPath)
            savedImagePath = imgPath
        }
    }

    /**
     * this function is used to set personalized campaign message and output
     */
    fun setPersonalizedCampaign(model: PersonalizedCampaignModel) {
        personalizedCampaignModel = model
        val context = LinkerManager.getInstance().context
        when (model.getCampaignStatus()) {
            CampaignStatus.UPCOMING -> {
                if (model.discountPercentage != 0F) {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_upcoming_discount,
                        model.getStartDateCampaign(),
                        model.getDiscountString(),
                        model.price
                    )
                } else {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_upcoming_without_discount,
                        model.getStartDateCampaign()
                    )
                }
                personalizedImage = context.getString(
                    com.tokopedia.universal_sharing.R.string.start_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(model.startTime)
                )
            }
            CampaignStatus.ON_GOING -> {
                if (model.discountPercentage != 0F) {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_ongoing_discount,
                        model.getDiscountString(),
                        model.price
                    )
                } else {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_ongoing_without_disc,
                        model.price
                    )
                }
                personalizedImage = context.getString(
                    com.tokopedia.universal_sharing.R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(model.endTime)
                )
            }
            CampaignStatus.END_SOON -> {
                if (model.discountPercentage != 0F) {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_endsoon_discount,
                        model.getMinuteLeft().toString(),
                        model.getDiscountString(),
                        model.price
                    )
                } else {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_endsoon_without_disc,
                        model.getMinuteLeft().toString(),
                        model.getDiscountString()
                    )
                }
                personalizedImage = context.getString(
                    com.tokopedia.universal_sharing.R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(model.endTime)
                )
            }
            CampaignStatus.END_BY_A_WEEK -> {
                if (model.discountPercentage != 0F) {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_endweek_discount,
                        model.getDiscountString(),
                        model.price
                    )
                } else {
                    personalizedMessage = context.getString(
                        R.string.personalized_campaign_message_ongoing_without_disc,
                        model.price
                    )
                }
                personalizedImage = context.getString(
                    com.tokopedia.universal_sharing.R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(model.endTime)
                )
            }
            CampaignStatus.NO_CAMPAIGN -> {
                /* no-op */
            }
        }
    }

    private fun isPersonalizedCampaignActive(): Boolean = personalizedCampaignModel != null

    fun addImageGeneratorData(key: String, value: String) {
        if (imageGeneratorDataArray == null) {
            imageGeneratorDataArray = ArrayList()
        }
        imageGeneratorDataArray?.add(ImageGeneratorRequestData(key, value))
    }

    fun getImageFromMedia(getImageFromMediaFlag: Boolean) {
        getImageFromMedia = getImageFromMediaFlag
        savedImagePath = UniversalShareConst.ImageType.MEDIA_VALUE_PLACEHOLDER
    }

    fun setImageGeneratorParam(param: ImageGeneratorParamModel) {
        imageGeneratorParam = param
    }

    /* set page source id */
    fun setMediaPageSourceId(pageSourceId: String) {
        sourceId = pageSourceId
    }

    fun setBroadcastChannel(context: Context, type: BroadcastChannelType, id: String, callback: () -> Unit = {}) {
        isShowTickerList = true
        tickerListAdapter.addItem(
            BroadcastChannelModel(
                id = id,
                type = type,
                title = context.getString(com.tokopedia.universal_sharing.R.string.title_broadcast),
                description = context.getString(com.tokopedia.universal_sharing.R.string.description_broadcast),
                imageResDrawable = com.tokopedia.universal_sharing.R.drawable.ic_broadcast,
                callback = callback
            )
        )
    }

    /**
     * check whether user is affiliate or general
     */
    fun getUserType(): String {
        return userType
    }

    fun setImageOnlySharingOption(imageOnly: Boolean) {
        isImageOnlySharing = imageOnly
    }

    fun setScreenShotImagePath(imgPath: String) {
        screenShotImagePath = imgPath
    }

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

    private fun setFragmentLifecycleObserverUniversalSharing(fragment: Fragment) {
        parentFragmentContainer = fragment
        parentFragmentContainer?.lifecycle?.addObserver(parentFragmentLifecycleObserver)
    }

    private fun setupBottomSheetChildView(inflater: LayoutInflater, container: ViewGroup?) {
        inflater.inflate(LAYOUT, container).apply {
            rvSocialMediaList = findViewById(R.id.rv_social_media_list)
            thumbNailTitleTxTv = findViewById(R.id.thumb_nail_title)
            thumbNailImage = findViewById(R.id.thumb_nail_image)
            previewImage = findViewById(R.id.preview_image)
            revImageOptionsContainer = findViewById(R.id.image_list_container)
            imageListViewGroup = findViewById(R.id.image_selection_view_group)

            // chip
            lstChip = findViewById(R.id.lst_chip)
            chipOptionHeader = findViewById(R.id.chip_options_heading)

            // setting click listeners for fixed options
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
            setChipContentList()
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

        lstChip?.shouldShowWithAction(isShowChipList) {
            chipOptionHeader?.show()

            lstChip?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = chipListAdapter
            }
        }
    }

    private fun initImageOptionsRecyclerView() {
        if (imageOptionsList != null && imageOptionsList!!.size > 0) {
            revImageOptionsContainer?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = context?.let { ImageListAdapter(imageOptionsList!!, it, ::imageSaved, ::updateThumbnailImage) }
            }
            revImageOptionsContainer?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    revImageOptionsContainer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    Handler(Looper.getMainLooper()).postDelayed({
                        revImageOptionsContainer?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
                        isInitialClickThumbnail = false
                    }, DELAY_TIME_MILLISECOND)
                }
            })
        }
    }

    private fun initAffiliate() {
        if (isAffiliateCommissionEnabled) {
            affiliateInputTemp?.let { affiliateInput ->
                setAffiliateRequestHolder(affiliateInput)
            }
            affiliateRequestDataReceived(true)
            affiliateInputTemp = null
        }
    }

    private fun getResolvedActivity(context: Context?, intent: Intent?): ResolveInfo? = context?.let { ctx ->
        intent?.let {
            return ctx.packageManager.resolveActivity(it, 0)
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
        socialMediaList.add(
            ShareModel.Whatsapp().apply {
                packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_WHATSAPP
                socialMediaName = context?.resources?.getString(R.string.label_whatsapp)
                feature = channelStr
                campaign = campaignStr
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_WHATSAPP)
                channel = SharingUtil.labelWhatsapp
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.WHATSAPP
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_whatsapp) }
            }
        )
        socialMediaList.add(
            ShareModel.Facebook().apply {
                packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_FACEBOOK
                socialMediaName = context?.resources?.getString(R.string.label_facebook)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelFbfeed
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.FACEBOOK_FEED
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_FB_FEED)
                shareOnlyLink = isImageOnlySharing
                if (isImageOnlySharing) {
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                } else {
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                }
                appIntent?.component = ComponentName(
                    UniversalShareConst.PackageChannel.PACKAGE_NAME_FACEBOOK,
                    UniversalShareConst.PackageChannel.FACEBOOK_FEED_ACTIVITY
                )
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_facebook) }
            }
        )
        if (!TextUtils.isEmpty(savedImagePath)) {
            socialMediaList.add(
                ShareModel.Facebook().apply {
                    packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_FACEBOOK
                    // facebook story can share only the images
                    socialMediaName = context?.resources?.getString(R.string.label_facebook_story)
                    feature = channelStr
                    campaign = campaignStr
                    channel = SharingUtil.labelFbstory
                    socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_FB_STORY)
                    platform = ImageGeneratorConstants.ImageGeneratorPlatforms.FACEBOOK_STORY
                    shareOnlyLink = true
                    appIntent = getAppIntent(MimeType.IMAGE, packageName, actionType = UniversalShareConst.PackageChannel.FACEBOOK_STORY_INTENT_ACTION)
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_facebook_story) }
                }
            )
            socialMediaList.add(
                ShareModel.Instagram().apply {
                    packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_instagram_feed)
                    feature = channelStr
                    campaign = campaignStr
                    channel = SharingUtil.labelIgfeed
                    platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_FEED
                    socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_IG_FEED)
                    shareOnlyLink = true
                    appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_FEED")
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram) }
                }
            )
            socialMediaList.add(
                ShareModel.Instagram().apply {
                    packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_instagram_story)
                    feature = channelStr
                    campaign = campaignStr
                    channel = SharingUtil.labelIgstory
                    socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_IG_STORY)
                    platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_STORY
                    shareOnlyLink = true
                    appIntent = getAppIntent(MimeType.IMAGE, packageName, "com.instagram.share.ADD_TO_STORY")
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram_story) }
                }
            )
        } else {
            socialMediaList.add(
                ShareModel.Instagram().apply {
                    packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_INSTAGRAM
                    socialMediaName = context?.resources?.getString(R.string.label_instagram_dm)
                    feature = channelStr
                    campaign = campaignStr
                    channel = SharingUtil.labelIgMessage
                    platform = ImageGeneratorConstants.ImageGeneratorPlatforms.INSTAGRAM_FEED
                    socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_IG_DM)
                    shareOnlyLink = false
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                    socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_instagram_dm) }
                }
            )
        }
        socialMediaList.add(
            ShareModel.Line().apply {
                packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_LINE
                socialMediaName = context?.resources?.getString(R.string.label_line)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelLine
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.LINE
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_LINE)
                shareOnlyLink = isImageOnlySharing
                if (isImageOnlySharing) {
                    appIntent = getAppIntent(MimeType.IMAGE, packageName)
                } else {
                    appIntent = getAppIntent(MimeType.TEXT, packageName)
                }
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_line) }
            }
        )
        socialMediaList.add(
            ShareModel.Twitter().apply {
                packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_TWITTER
                socialMediaName = context?.resources?.getString(R.string.label_twitter)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelTwitter
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.TWITTER
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_TWITTER)
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_twitter) }
            }
        )
        socialMediaList.add(
            ShareModel.Telegram().apply {
                packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_TELEGRAM
                socialMediaName = context?.resources?.getString(R.string.label_telegram)
                feature = channelStr
                campaign = campaignStr
                channel = SharingUtil.labelTelegram
                platform = ImageGeneratorConstants.ImageGeneratorPlatforms.TELEGRAM
                socialMediaOrderingScore = getSocialMediaOrderingScore(socialMediaOrderingScore, UniversalShareConst.OrderingKey.KEY_TELEGRAM)
                shareOnlyLink = isImageOnlySharing
                appIntent = getAppIntent(MimeType.IMAGE, packageName)
                socialMediaIcon = context?.let { AppCompatResources.getDrawable(it, unifyIconR.drawable.iconunify_telegram) }
            }
        )
        socialMediaList.sortBy { it.socialMediaOrderingScore }
        return socialMediaList.filterNot {
            (it.packageName!!.isNotEmpty() && it.appIntent != null && getResolvedActivity(context, it.appIntent) == null)
        }
    }

    private fun createLinkerData(shareModel: ShareModel): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            channel = shareModel.channel
            campaign = shareModel.campaign
            feature = shareModel.feature
            type = linkProperties?.linkerType ?: ""
            isAffiliate = shareModel.isAffiliate
            ogImageUrl = shareModel.ogImgUrl
            ogTitle = linkProperties?.ogTitle
            ogDescription = linkProperties?.ogDescription
            uri = linkProperties?.desktopUrl
            deepLink = linkProperties?.deeplink
            id = linkProperties?.id
            linkAffiliateType = affiliateInput?.affiliateLinkType?.value
        }
        return LinkerShareData().apply {
            this.linkerData = linkerData
        }
    }

    private fun isAffiliateEnabled(): Boolean {
        if (LinkerManager.getInstance().context != null) {
            val remoteConfig = FirebaseRemoteConfigImpl(LinkerManager.getInstance().context)
            return remoteConfig.getBoolean(UniversalShareConst.RemoteConfigKey.GLOBAL_AFFILIATE_FEATURE_FLAG, true)
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
                val generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility = affiliateUsecase.apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(affiliateInput!!)
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
                affiliateInput = null
            }
        }, DELAY_TIME_AFFILIATE_ELIGIBILITY_CHECK)
    }

    private suspend fun executeExtractBranchLink(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): String {
        return try {
            extractBranchLinkUseCase(generateAffiliateLinkEligibility.banner?.ctaLink ?: "").android_deeplink
        } catch (ignore: Exception) {
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
        affiliateListener?.invoke(userType)

        if (generateAffiliateLinkEligibility.eligibleCommission?.ssaStatus == true) {
            showCommissionExtra(generateAffiliateLinkEligibility)
        }
    }

    private fun isShowAffiliateComission(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): Boolean {
        return generateAffiliateLinkEligibility.eligibleCommission?.isEligible == true &&
            generateAffiliateLinkEligibility.affiliateEligibility?.isEligible == true &&
            generateAffiliateLinkEligibility.affiliateEligibility?.isRegistered == true
    }

    private fun isShowAffiliateRegister(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility): Boolean {
        return (
            generateAffiliateLinkEligibility.banner != null &&
                generateAffiliateLinkEligibility.affiliateEligibility?.isRegistered == false
            ) && userSession.isLoggedIn &&
            userSession.shopId != affiliateInput?.shop?.shopID
    }

    private fun showAffiliateCommission(generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility) {
        val commissionMessage = generateAffiliateLinkEligibility.eligibleCommission?.message ?: ""
        if (!TextUtils.isEmpty(commissionMessage)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                affiliateCommissionTextView?.text = Html.fromHtml(
                    commissionMessage,
                    Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                affiliateCommissionTextView?.text = Html.fromHtml(commissionMessage)
            }
            affiliateCommissionTextView?.visibility = View.VISIBLE
            shareTracker.viewOnAffiliateRegisterTicker(true, affiliateInput?.getIdFactory() ?: "", affiliateInput?.pageType ?: "")
            userType = KEY_AFFILIATE_USER
            return
        }
        affiliateInput = null
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
            shareTracker.viewOnAffiliateRegisterTicker(false, affiliateInput?.getIdFactory() ?: "", affiliateInput?.pageType ?: "")

            val id = affiliateInput?.getIdFactory() ?: ""
            val page = affiliateInput?.pageType ?: ""
            affiliateRegisterContainer?.setOnClickListener { _ ->
                shareTracker.onClickRegisterTicker(false, id, page)
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

            userType = KEY_GENERAL_USER
        }
        affiliateInput = null
    }

    private fun executeShareOptionClick(shareModel: ShareModel) {
        setIfAffiliate(shareModel)
        if (getImageFromMedia) {
            when (sourceId) {
                ImageGeneratorConstants.ImageGeneratorSourceId.AB_TEST_PDP -> {
                    executePdpContextualImage(shareModel)
                }
                ImageGeneratorConstants.ImageGeneratorSourceId.SHOP_PAGE -> {
                    executeContextualImage<ShopPageParamModel>(shareModel)
                }
                ImageGeneratorConstants.ImageGeneratorSourceId.WISHLIST_COLLECTION -> {
                    executeContextualImage<WishlistCollectionParamModel>(shareModel)
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

    private inline fun <reified T : ImageGeneratorParamModel> executeContextualImage(shareModel: ShareModel) {
        if (imageGeneratorParam == null || !(imageGeneratorParam is T)) return
        (imageGeneratorParam as? PdpParamModel)?.apply {
            this.platform = shareModel.platform
        }

        lifecycleScope.launchCatchError(block = {
            val result = imagePolicyUseCase(sourceId)
            val listOfParams = result.generateImageGeneratorParam(imageGeneratorParam!!)
            executeImageGeneratorUseCase(listOfParams, shareModel)
        }, onError = {
                executeSharingFlow(shareModel)
            })
    }

    private fun executePdpContextualImage(shareModel: ShareModel) {
        if (imageGeneratorParam == null || !(imageGeneratorParam is PdpParamModel)) return
        (imageGeneratorParam as? PdpParamModel)?.apply {
            this.platform = shareModel.platform
            this.productImageUrl = SharingUtil.transformOgImageURL(context, ogImageUrl)

            if (isPersonalizedCampaignActive()) {
                val campaignName = personalizedCampaignModel?.getCampaignName() ?: ""
                imageGeneratorParam = this.copy(campaignInfo = personalizedImage, campaignName = campaignName, hasRibbon = true)
            }
        }

        lifecycleScope.launchCatchError(block = {
            val result = imagePolicyUseCase(sourceId)
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
        if (isDefaultShareIntent) {
            shareChannelClicked(shareModel)
        }
        if (personalizedMessage.isNotEmpty()) {
            shareModel.personalizedMessageFormat = "$personalizedMessage\n%s"
        }
        bottomSheetListener?.onShareOptionClicked(shareModel)
    }

    private fun executeSharingFlow(shareModel: ShareModel) {
        activity?.runOnUiThread {
            loaderUnify?.visibility = View.GONE
        }
        preserveImage = true
        shareModel.ogImgUrl = SharingUtil.transformOgImageURL(context, ogImageUrl)
        shareModel.savedImageFilePath = savedImagePath
        if (isDefaultShareIntent) {
            shareChannelClicked(shareModel)
        }
        if (personalizedMessage.isNotEmpty()) {
            shareModel.personalizedMessageFormat = "$personalizedMessage\n%s"
        }
        bottomSheetListener?.onShareOptionClicked(shareModel)
    }

    private fun setIfAffiliate(shareModel: ShareModel) {
        if (affiliateInput != null &&
            affiliateCommissionTextView?.visibility == View.VISIBLE
        ) {
            shareModel.isAffiliate = true
        }
    }

    private fun executeImageGeneratorUseCase(
        args: ArrayList<ImageGeneratorRequestData>,
        shareModel: ShareModel
    ) {
        loaderUnify?.visibility = View.VISIBLE
        gqlJob = CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
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

    private fun generateRemoteConfigSocialMediaOrdering() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val socialMediaOrderingArray: String? = remoteConfig.getString(UniversalShareConst.RemoteConfigKey.SOCIAL_MEDIA_ORDERING)
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

    /***
     * @param sourceId is from result of [ImageGeneratorUseCase]
     */
    private fun setAbTestContextual(shareModel: ShareModel, sourceId: String) {
        if (getImageFromMedia) {
            shareModel.campaign = shareModel.campaign?.replace(UniversalShareConst.ImageType.KEY_CONTEXTUAL_IMAGE, sourceId)
        }
    }

    private fun setChipContentList() {
        if (chipListData == null) return

        // validation the chip list visibility
        isShowChipList = chipListData?.isNotEmpty() == true

        chipListAdapter.setData(chipListData)
    }

    private fun setUserVisualData() {
        thumbNailTitleTxTv?.text = thumbNailTitle
        if (isImageOnlySharing) {
            try {
                context?.let {
                    thumbNailImage?.let { imgView ->
                        Glide.with(it)
                            .load(thumbNailImageUrl)
                            .override(
                                UniversalShareConst
                                    .SizeScreenShoot
                                    .THUMBNAIL_IMG_SCREENSHOT_WIDTH,
                                UniversalShareConst.SizeScreenShoot.THUMBNAIL_IMG_SCREENSHOT_HEIGHT
                            )
                            .into(
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
                            Glide.with(it).load(previewImageUrl).override(UniversalShareConst.SizeScreenShoot.PREVIEW_IMG_SCREENSHOT_WIDTH, UniversalShareConst.SizeScreenShoot.PREVIEW_IMG_SCREENSHOT_HEIGHT).into(
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
        if (showLoader) {
            loaderUnify?.visibility = View.VISIBLE
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
                packageName = Telephony.Sms.getDefaultSmsPackage(context)
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
            packageName = UniversalShareConst.PackageChannel.PACKAGE_NAME_GMAIL
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

    private fun shareChannelClicked(shareModel: ShareModel) {
        if (linkProperties == null) throw Exception("Please set link properties")
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                createLinkerData(shareModel),
                object : ShareCallback {
                    override fun urlCreated(linkerShareResult: LinkerShareResult) {
                        shareModel.subjectName = subjectShare
                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareResult,
                            activity,
                            fragmentView,
                            String.format(
                                shareText,
                                linkerShareResult.url
                            )
                        )

                        dismiss()
                    }

                    override fun onError(linkerError: LinkerError) {
                        dismiss()
                    }
                }
            )
        )
    }

    private fun removeLifecycleObserverAndSavedImage() {
        if (!preserveImage) {
            SharingUtil.removeFile(savedImagePath)
            parentFragmentContainer?.lifecycle?.removeObserver(parentFragmentLifecycleObserver)
        }
    }

    private fun getImageTypeForUTM(): String {
        return if (getImageFromMedia) {
            UniversalShareConst.ImageType.KEY_CONTEXTUAL_IMAGE
        } else if (!TextUtils.isEmpty(savedImagePath) && TextUtils.isEmpty(screenShotImagePath)) {
            UniversalShareConst.ImageType.KEY_IMAGE_DEFAULT
        } else {
            UniversalShareConst.ImageType.KEY_NO_IMAGE
        }
    }

    private fun getNativeShareIntent(): ShareModel {
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

    private fun inject() {
        component?.inject(this)
    }

    private fun logExceptionToRemote(ex: Exception) {
        if (ex.localizedMessage != null) {
            val errorMap = mapOf("type" to "crashLog", "reason" to (ex.localizedMessage))
            SharingUtil.logError(errorMap)
        }
    }

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.universal_share_bottomsheet
        val TAG = UniversalShareBottomSheet::class.java.simpleName

        // Delay time for timeout
        private const val DELAY_TIME_MILLISECOND = 500L
        private const val DELAY_TIME_AFFILIATE_ELIGIBILITY_CHECK = 5000L

        const val CUSTOM_SHARE_SHEET = 1
        const val SCREENSHOT_SHARE_SHEET = 2

        // for affiliate and general user distinction
        private const val KEY_GENERAL_USER = "general"
        private const val KEY_AFFILIATE_USER = "affiliate"
        const val KEY_PRODUCT_ID = "productId"

        fun createInstance(): UniversalShareBottomSheet = UniversalShareBottomSheet()

        /**
         * if you're using [enableDefaultShareIntent] please create the instance using this function,
         * otherwise the toaster after clicking `salin link` won't show
         */
        fun createInstance(fragmentView: View?) = UniversalShareBottomSheet().apply {
            this.fragmentView = fragmentView
        }
    }
}
