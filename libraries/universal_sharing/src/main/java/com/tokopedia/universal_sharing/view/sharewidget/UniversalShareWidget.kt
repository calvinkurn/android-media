package com.tokopedia.universal_sharing.view.customview

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.universal_sharing.databinding.UniversalShareWidgetBinding
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.model.ImageGeneratorParamModel
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.LinkShareWidgetProperties
import com.tokopedia.universal_sharing.view.model.ShareWidgetParam
import com.tokopedia.universal_sharing.view.sharewidget.LinkerResultWidget
import com.tokopedia.universal_sharing.view.sharewidget.UniversalShareWidgetViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.universal_sharing.R as universal_sharingR

class UniversalShareWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var channelShareIconId: Int = CHANNEL_SHARE
    private var iconUnifyId: Int = IconUnify.SOCIAL_SHARE
    private var callback: ShareWidgetCallback? = null
    private var isDirectChannel = false
    private var isAffiliate = false

    // properties for generation link
    private var linkProperties: LinkShareWidgetProperties? = null

    // properties for image generator
    private var sourceId: String? = null
    private var imageGeneratorModel: ImageGeneratorParamModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tracker: UniversalSharebottomSheetTracker

    private val loading: LoaderDialog by lazy {
        LoaderDialog(context)
    }

    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let {
            ViewModelProvider(it, viewModelFactory)[UniversalShareWidgetViewModel::class.java]
        }
    }

    private var binding: UniversalShareWidgetBinding? = null

    init {
        binding = UniversalShareWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        this.gone()
        inject()
        context.theme.obtainStyledAttributes(
            attrs,
            universal_sharingR.styleable.UniversalShareWidget,
            0,
            0
        ).apply {

            try {
                channelShareIconId = getInteger(universal_sharingR.styleable.UniversalShareWidget_channel_share, -1)
                if (channelShareIconId != -1) {
                    iconUnifyId = getIconUnifyId()
                    if (iconUnifyId != IconUnify.WARNING) {
                        populateView()
                    }
                } else {
                    /* no-op */
                }
            } finally {
                recycle()
            }
        }
    }

    fun setData(shareWidgetParam: ShareWidgetParam) {
        viewModel?.setData(shareWidgetParam.linkProperties)
        shareWidgetParam.affiliateInput?.let {
            enableAffiliate(it)
        }

        shareWidgetParam.imageGenerator?.let {
            setImageGenerator(it.sourceId, it.imageGeneratorParamModel)
        }
    }

    /**
     * change color for icon with the type IconUnify.SHARE or IconUnify.SHARE_AFFILIATE
     */
    fun setColorShareIcon(color: Int) {
        if (isDirectChannel.not()) {
            runCatching {
                val colorRes = ContextCompat.getColor(context, color)
                binding?.shareChannel?.setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setObserver()
    }

    private fun inject() {
        return ActivityComponentFactory.instance.createActivityComponent(
            LinkerManager.getInstance().context.applicationContext
        ).inject(this)
    }

    fun setShareWidgetCallback(shareWidgetCallback: ShareWidgetCallback) {
        callback = shareWidgetCallback
    }

    private fun populateView() {
        when (getVariant()) {
            UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_A -> {
                binding?.shareChannel?.setImage(IconUnify.SOCIAL_SHARE)
                channelShareIconId = CHANNEL_SHARE
            }

            UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_B -> {
                if (SharingUtil.isAppInstalled(context, getPackageName())) {
                    binding?.shareChannel?.setImage(iconUnifyId)
                    mapColorIcon()
                    isDirectChannel = true
                } else {
                    binding?.shareChannel?.setImage(IconUnify.SOCIAL_SHARE)
                    channelShareIconId = CHANNEL_SHARE
                    iconUnifyId = IconUnify.SOCIAL_SHARE
                }
            }

            UniversalShareConst.RemoteConfigKey.CONTROL_VARIANT, "" -> {
                this.gone()
            }

            else -> {
                /* no-op */
                this.gone()
            }
        }
        setOnClickChannel()
    }

    private fun mapColorIcon() {
        when (iconUnifyId) {
            IconUnify.WHATSAPP_SHARE -> {
                val colorRes = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
                binding?.shareChannel?.setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun setOnClickChannel() {
        this.rootView.setOnClickListener {
            if (isDirectChannel.not()) {
                callback?.onShowNormalBottomSheet()
            } else {
                linkProperties?.let {
                    val linkerData = createLinkerData()
                    viewModel?.executeLinkRequest(linkerData, sourceId, imageGeneratorModel)
                    loading.show()
                }
            }
            callback?.onClickShareWidget(linkProperties?.id.orEmpty(), getLinkChannel(), isAffiliate, isDirectChannel)
        }
    }

    fun setImageGenerator(sourceId: String, param: ImageGeneratorParamModel) {
        this.sourceId = sourceId
        this.imageGeneratorModel = param
    }

    private fun createLinkerData(): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            channel = getLinkChannel()
            campaign = createUtmCampaign()
            feature = FEATURE_SHARE
            type = linkProperties?.linkerType ?: ""
            isAffiliate = this@UniversalShareWidget.isAffiliate
            ogImageUrl = linkProperties?.ogImageUrl
            ogTitle = linkProperties?.ogTitle
            ogDescription = linkProperties?.ogDescription
            uri = linkProperties?.desktopUrl
            deepLink = linkProperties?.deeplink
            id = linkProperties?.id
            linkAffiliateType = AffiliateLinkType.PDP.value
        }
        return LinkerShareData().apply {
            this.linkerData = linkerData
        }
    }

    private fun getLinkChannel(): String {
        return when (channelShareIconId) {
            CHANNEL_WHATSAPP -> SharingUtil.labelWhatsapp
            CHANNEL_SMS -> SharingUtil.labelSms
            CHANNEL_TELEGRAM -> SharingUtil.labelTelegram
            CHANNEL_SHARE -> SHARING_CHANNEL
            else -> ""
        }
    }

    private fun setObserver() {
        findViewTreeLifecycleOwner()?.let { treeLifecycleOwner ->
            viewModel?.linkProperties?.observe(treeLifecycleOwner) { data ->
                linkProperties = data
            }

            viewModel?.linkerResult?.observe(treeLifecycleOwner) { result ->
                loading.dismiss()
                when (result) {
                    is LinkerResultWidget.Success -> {
                        result.linkerShareResult?.let {
                            callback?.onSuccessCreateUrl(it)
                            val shareMessage = if (linkProperties?.message?.isEmpty().orTrue()) {
                                it.url
                            } else {
                                String.format(
                                    linkProperties!!.message,
                                    it.url
                                )
                            }

                            when (channelShareIconId) {
                                CHANNEL_WHATSAPP -> {
                                    SharingUtil.shareWhatsapp(context, shareMessage)
                                }

                                CHANNEL_SMS -> {
                                    SharingUtil.shareSMS(context, shareMessage)
                                }

                                else -> {
                                    /* no-op */
                                }
                            }
                        }
                    }

                    is LinkerResultWidget.Failed -> {
                        result.error?.let { callback?.onErrorCreateUrl(it) }
                    }
                }
            }

            viewModel?.resultAffiliate?.observe(treeLifecycleOwner) { result ->
                when (result) {
                    is Success -> {
                        if (result.data.eligibleCommission?.isEligible.orFalse()) {
                            if (isShowShareIcon()) {
                                binding?.shareChannel?.setImage(IconUnify.SOCIAL_SHARE_AFFILIATE)
                            }
                        }

                        if (eligibleGenerateAffiliateLink(result.data)) {
                            isAffiliate = true
                        }
                    }

                    is Fail -> {
                        /* no-op */
                    }
                }
            }
        }
    }

    private fun eligibleGenerateAffiliateLink(data: GenerateAffiliateLinkEligibility): Boolean {
        return data.affiliateEligibility?.isEligible.orFalse() &&
            data.affiliateEligibility?.isRegistered.orFalse()
    }

    /**
     * method to enable affiliate if user eligible as affiliate
     * @param affiliateInput
     */
    private fun enableAffiliate(affiliateInput: AffiliateInput) {
        viewModel?.checkIsAffiliate(affiliateInput)
    }

    fun show() {
        if (getVariant() == UniversalShareConst.RemoteConfigKey.CONTROL_VARIANT ||
            getVariant() == ""
        ) {
            return
        }

        this.visible()
        tracker.viewShareWidget(
            shareIconId = getLinkChannel(),
            productId = linkProperties?.id ?: ""
        )
    }

    /**
     * get icon unify id by share widget channel id
     * ex: [CHANNEL_SMS] -> [IconUnify.MESSAGE]
     */
    private fun getIconUnifyId(): Int {
        return when (channelShareIconId) {
            CHANNEL_WHATSAPP -> IconUnify.WHATSAPP_SHARE
            CHANNEL_TELEGRAM -> IconUnify.TELEGRAM
            CHANNEL_SMS -> IconUnify.MESSAGE
            else -> IconUnify.WARNING
        }
    }

    private fun getPackageName(): String {
        return when (channelShareIconId) {
            CHANNEL_WHATSAPP -> UniversalShareConst.PackageChannel.PACKAGE_NAME_WHATSAPP
            CHANNEL_TELEGRAM -> UniversalShareConst.PackageChannel.PACKAGE_NAME_TELEGRAM
            CHANNEL_SMS -> UniversalShareConst.PackageChannel.NO_PACKAGE_CHANNEL
            else -> ""
        }
    }

    private fun getVariant(): String =
        RemoteConfigInstance.getInstance().abTestPlatform.getString(
            UniversalShareConst.RemoteConfigKey.KEY_AB_TESTING,
            ""
        )

    private fun createUtmCampaign(): String {
        val sharingDate: String = SimpleDateFormat("ddMMyy", Locale.getDefault()).format(
            Date()
        )
        val imageType = if (sourceId != null && imageGeneratorModel != null) {
            UniversalShareConst.ImageType.KEY_CONTEXTUAL_IMAGE
        } else if (linkProperties?.ogImageUrl?.isEmpty().orFalse()) {
            UniversalShareConst.ImageType.KEY_IMAGE_DEFAULT
        } else {
            UniversalShareConst.ImageType.KEY_NO_IMAGE
        }
        return "${linkProperties?.page}-${linkProperties?.userId}-${linkProperties?.id}-$sharingDate-$imageType"
    }

    /**
     * show share when meets these conditions
     * - user get variant A
     * - user get variant B but Whatsapp is not installed
     */
    private fun isShowShareIcon(): Boolean {
        return (getVariant() == UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_A) ||
            (
                (getVariant() == UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_B) &&
                    SharingUtil.isAppInstalled(context, getPackageName()).not()
                )
    }

    companion object {
        private const val CHANNEL_WHATSAPP = 0
        private const val CHANNEL_TELEGRAM = 1
        private const val CHANNEL_SMS = 2
        private const val CHANNEL_SHARE = -1
        private const val FEATURE_SHARE = "share"
        private const val SHARING_CHANNEL = "share button"
        const val EMPTY_ATTRS = "-1"
    }
}

interface ShareWidgetCallback {
    /**
     * this method will be invoked when failed create share link url
     */
    fun onErrorCreateUrl(error: LinkerError) {}

    /**
     * this method will be invoked when successfully create share link url
     */
    fun onSuccessCreateUrl(result: LinkerShareResult) {}

    /**
     * this method will be invoked when show normal share icon instead of channel icon (WA, Telegram, and etc)
     * and should show [com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet]
     */
    fun onShowNormalBottomSheet() {}

    /**
     * @param isAffiliate will return true if eligible for affiliate
     * @param isDirectChannel will return true if showing channel icon (WA, Telegram, and etc)
     */
    fun onClickShareWidget(id: String, channel: String, isAffiliate: Boolean, isDirectChannel: Boolean) {}
}
