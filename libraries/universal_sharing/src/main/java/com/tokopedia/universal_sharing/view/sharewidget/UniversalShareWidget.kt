package com.tokopedia.universal_sharing.view.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyResourceIdRef
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.universal_sharing.databinding.UniversalShareWidgetBinding
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.LinkShareWidgetProperties
import com.tokopedia.universal_sharing.view.sharewidget.LinkerResultWidget
import com.tokopedia.universal_sharing.view.sharewidget.UniversalShareWidgetViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.tokopedia.universal_sharing.R as universal_sharingR

class UniversalShareWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var channelShareIconId: Int = -1
    private var iconUnifyId: Int = IconUnify.WARNING
    private var callback: ShareWidgetLinkRequestCallback? = null

    private var linkProperties: LinkShareWidgetProperties? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let {
            ViewModelProvider(it, viewModelFactory)[UniversalShareWidgetViewModel::class.java]
        }
    }

    private var binding: UniversalShareWidgetBinding? = null

    init {
        binding = UniversalShareWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        inject()
        context.theme.obtainStyledAttributes(
            attrs,
            universal_sharingR.styleable.UniversalShareWidget,
            0, 0).apply {

            try {
                channelShareIconId = getInteger(universal_sharingR.styleable.UniversalShareWidget_channel_share, -1)
                if (channelShareIconId != -1) {
                    iconUnifyId = getIconUnifyId()
                    if (iconUnifyId != IconUnify.WARNING) {
                        val iconId = getIconUnifyResourceIdRef(channelShareIconId)
                        popuplateView(iconId)
                    }
                } else {
                    /* no-op */
                }
            } finally {
                recycle()
            }
        }
    }

    fun setLinkProperties(linkShareWidgetProperties: LinkShareWidgetProperties) {
        viewModel?.setData(linkShareWidgetProperties)
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

    private fun setShareWidgetCallback(shareWidgetLinkRequestCallback: ShareWidgetLinkRequestCallback) {
        callback = shareWidgetLinkRequestCallback
    }


    private fun popuplateView(icon: Int) {
        when (getVariant()) {
            UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_A -> {
                binding?.shareChannel?.setImage(IconUnify.SHARE)
            }

            UniversalShareConst.RemoteConfigKey.VALUE_VARIANT_B -> {
                if (SharingUtil.isAppInstalled(context, getPackageName())) {
                    binding?.shareChannel?.setImage(icon)
                } else {
                    binding?.shareChannel?.setImage(IconUnify.SHARE)
                }
            }

            UniversalShareConst.RemoteConfigKey.CONTROL_VARIANT -> {
//                this.gone()
            }
        }
        setOnClickChannel()
    }

    private fun setOnClickChannel() {
        this.rootView.setOnClickListener {
            linkProperties?.let {
                val linkerData = createLinkerData()
                viewModel?.executeLinkRequest(linkerData)

                when (channelShareIconId) {
                    CHANNEL_WHATSAPP -> {
//                    viewModel./executeLinkRequest(it)
                    }

                    else -> {
                        /* no-op */
                    }
                }
            }
        }
    }

    private fun createLinkerData(): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            channel = getLinkChannel()
            campaign = createUtmCampaign()
            feature = "share"
            type = linkProperties?.linkerType ?: ""
            isAffiliate = isAffiliate
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
            else -> ""
        }
    }

    private fun setObserver() {
        findViewTreeLifecycleOwner()?.let { treeLifecycleOwner ->
            viewModel?.linkProperties?.observe(treeLifecycleOwner) { data ->
                linkProperties = data
            }

            viewModel?.linkerResult?.observe(treeLifecycleOwner) { result ->
                when (result) {
                    is LinkerResultWidget.Success -> {
                        result.linkerShareResult?.let { callback?.onSuccessCreateUrl(it) }
                    }

                    is LinkerResultWidget.Failed -> {
                        result.error?.let { callback?.onErrorCreateUrl(it) }
                    }
                }
            }

            viewModel?.resultAffiliate?.observe(treeLifecycleOwner) { result ->
                when (result) {
                    is Success -> {
                        binding?.shareChannel?.setImage(IconUnify.SHARE_AFFILIATE)
                    }

                    is Fail -> {

                    }
                }
            }
        }
    }


    /**
     * method to enable affiliate if user eligible as affiliate
     * @param affiliateInput
     */
    fun enableAffiliate(affiliateInput: AffiliateInput) {
        viewModel?.checkIsAffiliate(affiliateInput)
    }

    /**
     * get icon unify id by share widget channel id
     * ex: [CHANNEL_SMS] -> [IconUnify.MESSAGE]
     */
    private fun getIconUnifyId(): Int {
        return when (channelShareIconId) {
            CHANNEL_WHATSAPP -> IconUnify.WHATSAPP
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
        val imageType = UniversalShareConst.ImageType.KEY_CONTEXTUAL_IMAGE
        return "${linkProperties?.page}-${linkProperties?.userId}-${linkProperties?.id}-$sharingDate-$imageType"
    }

    companion object {
        private const val CHANNEL_WHATSAPP = 0
        private const val CHANNEL_TELEGRAM = 1
        private const val CHANNEL_SMS = 2
    }
}

interface ShareWidgetLinkRequestCallback {
    fun onErrorCreateUrl(error: LinkerError)

    fun onSuccessCreateUrl(result: LinkerShareResult)
}

