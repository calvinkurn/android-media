package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AFFILIATE_INSTAGRAM_REGEX
import com.tokopedia.affiliate.AFFILIATE_TIKTOK_REGEX
import com.tokopedia.affiliate.AFFILIATE_TWITTER_REGEX
import com.tokopedia.affiliate.AFFILIATE_YT_REGEX
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.FACEBOOK_DEFAULT
import com.tokopedia.affiliate.INSTAGRAM_DEFAULT
import com.tokopedia.affiliate.PAGE_TYPE_CAMPAIGN
import com.tokopedia.affiliate.PAGE_TYPE_PDP
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.PAGE_TYPE_WISHLIST
import com.tokopedia.affiliate.TIKTOK_DEFAULT
import com.tokopedia.affiliate.TWITTER_DEFAULT
import com.tokopedia.affiliate.WWW
import com.tokopedia.affiliate.YOUTUBE_DEFAULT
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliatePromotionBottomSheetBinding
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AffiliatePromotionBottomSheet : BottomSheetUnify(), ShareButtonInterface, AddSocialInterface {

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    private var binding by autoClearedNullable<AffiliatePromotionBottomSheetBinding>()

    private val adapter: AffiliateAdapter = AffiliateAdapter(
        AffiliateAdapterFactory(
            shareButtonInterface = this,
            addSocialInterface = this
        )
    )
    private var productId: String = ""
    private var currentName: String? = null
    private var currentServiceFormat = ""
    private var commission = ""
    private var status = ""
    private var type = ""
    private var originScreen = ORIGIN_PROMOSIKAN
    private var url: String? = null
    private var appUrl: String? = null
    private var identifier: String? = null
    private var isLinkGenerationEnabled = true

    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var sheetType = SheetType.LINK_GENERATION
    private var affiliatePromotionBottomSheetInterface: AffiliatePromotionBottomSheetInterface? =
        null
    private var selectedIds = arrayListOf<Int>()

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null
    private var affiliatePromotionBSViewModel: AffiliatePromotionBSViewModel? = null

    companion object {

        enum class SheetType {
            LINK_GENERATION,
            ADD_SOCIAL
        }

        private const val COPY_LABEL = "Tokopedia"
        private const val KEY_PRODUCT_ID = "KEY_PRODUCT_ID"
        private const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"
        private const val KEY_PRODUCT_IMAGE = "KEY_PRODUCT_IMAGE"
        private const val KEY_PRODUCT_URL = "KEY_PRODUCT_URL"
        private const val KEY_APP_URL = "KEY_APP_URL"
        private const val KEY_COMMISON_PRICE = "KEY_COMMISION_PRICE"
        private const val KEY_PRODUCT_IDENTIFIER = "KEY_PRODUCT_IDENTIFIER"
        private const val KEY_ORIGIN = "KEY_ORIGIN"
        private const val KEY_LINK_GEN_ENABLED = "KEY_LINK_GEN_ENABLED"
        private const val KEY_STATUS = "KEY_STATUS"
        private const val KEY_TYPE = "KEY_TYPE"

        private const val KEY_PARAMS = "KEY_PARAMS"

        private const val SHOP_ID_PARAM = "{shop_id}"

        const val ORIGIN_PROMOSIKAN = 1
        const val ORIGIN_HOME = 2
        const val ORIGIN_SSA_SHOP = 3
        const val ORIGIN_PERNAH_DIBELI_PROMOSIKA = 4
        const val ORIGIN_TERAKHIR_DILIHAT = 5
        const val ORIGIN_HOME_GENERATED = 6
        const val ORIGIN_PROMO_DISCO_BANNER = 7
        const val ORIGIN_PROMO_DISCO_BANNER_LIST = 8
        const val ORIGIN_PROMO_TOKO_NOW = 9

        const val OTHERS_ID = 0
        const val FACEBOOK_ID = 1
        const val INSTAGRAM_ID = 3
        const val LINE_ID = 4
        const val TIKTOK_ID = 9
        const val TWITTER_ID = 10
        const val WEBSITE_BLOG_ID = 11
        const val WHATSAPP_ID = 12
        const val YOUTUBE_ID = 13

        private const val TOTAL_IMG_2 = 2
        private const val TOTAL_IMG_3 = 3
        private const val TOTAL_IMG_4 = 4

        private const val SPEC_0 = 0
        private const val SPEC_2 = 2

        private val COLLECTION_MARGIN_1_5 = 1.5f.dp.value.toInt()
        private val COLLECTION_MARGIN_3 = 1.5f.dp.value.toInt()
        private const val COLLECTION_PARAM_SIZE = 4
        private const val INDEX_ONE = 1
        private const val INDEX_TWO = 2
        private const val INDEX_THREE = 3
        fun newInstance(
            params: AffiliatePromotionBottomSheetParams,
            bottomSheetType: SheetType,
            bottomSheetInterface: AffiliatePromotionBottomSheetInterface?
        ): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                sheetType = bottomSheetType
                affiliatePromotionBottomSheetInterface = bottomSheetInterface
                selectedIds = params.idArray ?: arrayListOf()
                arguments = Bundle().apply {
                    putParcelable(KEY_PARAMS, params)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        affiliatePromotionBSViewModel =
            viewModelProvider?.let { ViewModelProvider(this, it) }
                ?.get(AffiliatePromotionBSViewModel::class.java)
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        if (sheetType == SheetType.ADD_SOCIAL) {
            setTitle(getString(R.string.affiliate_add_social_media))
        } else {
            setTitle(getString(R.string.affiliate_where_to_promote))
        }
        binding = AffiliatePromotionBottomSheetBinding.inflate(LayoutInflater.from(context))

        afterViewSet()
        setChild(binding?.root)
        sendScreenEvent()
    }

    private fun afterViewSet() {
        binding?.run {
            arguments?.let { bundle ->
                val params: AffiliatePromotionBottomSheetParams? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(
                            KEY_PARAMS,
                            AffiliatePromotionBottomSheetParams::class.java
                        )
                    } else {
                        bundle.getParcelable(KEY_PARAMS) as? AffiliatePromotionBottomSheetParams
                    }
                type = params?.type ?: bundle.getString(KEY_TYPE, PAGE_TYPE_PDP)
                productName.text =
                    params?.itemName ?: bundle.getString(KEY_PRODUCT_NAME)
                if (type == PAGE_TYPE_WISHLIST) {
                    glCollectionItem.show()
                    productImage.hide()
                    setCollectionImages(params?.imageArray)
                } else {
                    glCollectionItem.hide()
                    productImage.show()
                    productImage.loadImage(params?.itemImage ?: bundle.getString(KEY_PRODUCT_IMAGE))
                }

                if (params?.ssaInfo?.ssaStatus == true) {
                    ssaGroup.isVisible = true
                    ssaMessage.text =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(params.ssaInfo.message, Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            Html.fromHtml(params.ssaInfo.message)
                        }
                    ssaLabel.apply {
                        isVisible = params.ssaInfo.label.labelText.isNotBlank()
                        text = params.ssaInfo.label.labelText
                    }
                    ssaExpiryDate.text = params.ssaInfo.ssaMessage
                }
                productId =
                    params?.itemId ?: bundle.getString(KEY_PRODUCT_ID, "")
                url = params?.itemUrl ?: bundle.getString(KEY_PRODUCT_URL, "")
                appUrl = params?.appUrl ?: bundle.getString(KEY_APP_URL, "")
                identifier = params?.productIdentifier ?: bundle.getString(KEY_PRODUCT_IDENTIFIER)
                originScreen = params?.origin ?: bundle.getInt(KEY_ORIGIN, ORIGIN_PROMOSIKAN)
                isLinkGenerationEnabled =
                    params?.isLinkGenerationEnabled ?: bundle.getBoolean(KEY_LINK_GEN_ENABLED)
                commission = params?.commission ?: bundle.getString(KEY_COMMISON_PRICE, "")
                status = params?.status ?: bundle.getString(KEY_STATUS, "")

                when (originScreen) {
                    ORIGIN_SSA_SHOP -> iconSsaMessage.setOnClickListener {
                        RouteManager.route(
                            context,
                            ApplinkConst.SHOP.replace(
                                SHOP_ID_PARAM,
                                productId
                            )
                        )
                    }

                    ORIGIN_PROMO_TOKO_NOW -> {
                        iconSsaMessage.setOnClickListener {
                            RouteManager.route(
                                context,
                                ApplinkConst.TokopediaNow.HOME
                            )
                        }
                    }

                    ORIGIN_PROMO_DISCO_BANNER, ORIGIN_PROMO_DISCO_BANNER_LIST -> {
                        iconSsaMessage.setOnClickListener {
                            RouteManager.route(
                                context,
                                appUrl
                            )
                        }
                    }
                }
            }

            if (sheetType == SheetType.ADD_SOCIAL) {
                productImage.hide()
                productName.hide()
            }

            setObservers()
        }
        binding?.shareRv?.let {
            addDataInRecyclerView()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    private fun setCollectionImages(
        images: List<String?>?
    ) {
        val params = getCollectionGridParams(images?.size.orZero())
        val imgCollections = arrayOf(
            binding?.imgCollection1,
            binding?.imgCollection2,
            binding?.imgCollection3,
            binding?.imgCollection4
        )

        if (images.isNullOrEmpty() || images.size <= 1) {
            binding?.glCollectionItem?.gone()
            binding?.productImage?.show()
            binding?.productImage?.apply {
                visible()
                setImageUrl(images?.getOrNull(0).orEmpty())
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        } else {
            repeat(images.size) {
                imgCollections[it]?.apply {
                    layout(0, 0, 0, 0)
                    visible()
                    layoutParams = params[it]
                    setImageUrl(images[it].orEmpty())
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }
    }

    private fun getCollectionGridParams(size: Int): Array<GridLayout.LayoutParams?> {
        val params = arrayOfNulls<GridLayout.LayoutParams>(COLLECTION_PARAM_SIZE)
        when (size) {
            TOTAL_IMG_2 -> {
                val param1: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection1?.layoutParams)
                param1.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                param1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param1.setMargins(Int.ZERO, Int.ZERO, COLLECTION_MARGIN_3, 0)

                val param2: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection2?.layoutParams)
                param2.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                param2.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                params[Int.ZERO] = param1
                params[INDEX_ONE] = param2
            }

            TOTAL_IMG_3 -> {
                val param1: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection1?.layoutParams)
                param1.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                param1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param1.setMargins(Int.ZERO, Int.ZERO, COLLECTION_MARGIN_3, COLLECTION_MARGIN_1_5)

                val param2: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection2?.layoutParams)
                param2.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param2.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param2.setMargins(Int.ZERO, Int.ZERO, Int.ZERO, COLLECTION_MARGIN_3)

                val param3: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection2?.layoutParams)
                param3.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param3.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)

                params[Int.ZERO] = param1
                params[INDEX_ONE] = param2
                params[INDEX_THREE] = param3
            }

            TOTAL_IMG_4 -> {
                val param1: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection1?.layoutParams)
                param1.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param1.setMargins(Int.ZERO, Int.ZERO, COLLECTION_MARGIN_1_5, COLLECTION_MARGIN_1_5)

                val param2: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection2?.layoutParams)
                param2.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param2.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param2.setMargins(COLLECTION_MARGIN_1_5, Int.ZERO, Int.ZERO, COLLECTION_MARGIN_1_5)

                val param3: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection3?.layoutParams)
                param3.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param3.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param3.setMargins(
                    COLLECTION_MARGIN_1_5,
                    COLLECTION_MARGIN_1_5,
                    COLLECTION_MARGIN_1_5,
                    Int.ZERO
                )

                val param4: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(binding?.imgCollection4?.layoutParams)
                param4.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                param4.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                param4.setMargins(
                    COLLECTION_MARGIN_1_5,
                    COLLECTION_MARGIN_1_5,
                    COLLECTION_MARGIN_1_5,
                    Int.ZERO
                )
                params[Int.ZERO] = param1
                params[INDEX_ONE] = param2
                params[INDEX_TWO] = param3
                params[INDEX_THREE] = param4
            }
        }
        return params
    }

    private fun addDataInRecyclerView() {
        listVisitable = arrayListOf<Visitable<AffiliateAdapterTypeFactory>>(
            AffiliateShareModel(
                "Instagram",
                IconUnify.INSTAGRAM,
                "instagram",
                INSTAGRAM_ID,
                sheetType,
                "Contoh: instagram.com/tokopedia",
                false,
                isChecked = false,
                isLinkGenerationEnabled,
                AFFILIATE_INSTAGRAM_REGEX,
                INSTAGRAM_DEFAULT
            ),
            AffiliateShareModel(
                "Tiktok", IconUnify.TIKTOK, "tiktok", TIKTOK_ID, sheetType,
                "Contoh: tiktok.com/tokopedia", false, isChecked = false, isLinkGenerationEnabled,
                AFFILIATE_TIKTOK_REGEX, TIKTOK_DEFAULT
            ),
            AffiliateShareModel(
                "YouTube",
                IconUnify.YOUTUBE,
                "youtube",
                YOUTUBE_ID,
                sheetType,
                "Contoh: youtube.com/tokopedia",
                false,
                isChecked = false,
                isLinkGenerationEnabled,
                AFFILIATE_YT_REGEX,
                YOUTUBE_DEFAULT
            ),
            AffiliateShareModel(
                "Facebook",
                IconUnify.FACEBOOK,
                "facebook",
                FACEBOOK_ID,
                sheetType,
                "Contoh: facebook.com/tokopedia",
                false,
                isChecked = false,
                isLinkGenerationEnabled,
                defaultText = FACEBOOK_DEFAULT
            ),
            AffiliateShareModel(
                "Twitter",
                IconUnify.TWITTER,
                "twitter",
                TWITTER_ID,
                sheetType,
                "Contoh: twitter.com/tokopedia",
                false,
                isChecked = false,
                isLinkGenerationEnabled,
                AFFILIATE_TWITTER_REGEX,
                TWITTER_DEFAULT
            ),
            AffiliateShareModel(
                "Website/Blog",
                IconUnify.GLOBE,
                "website",
                WEBSITE_BLOG_ID,
                sheetType,
                "Contoh: tokopedia.com/tokopedia",
                false,
                isChecked = false,
                isLinkGenerationEnabled,
                defaultText = WWW
            )
        )

        if (sheetType == SheetType.ADD_SOCIAL) {
            binding?.simpanBtn?.run {
                show()
                setOnClickListener {
                    onSaveSocialButtonClicked()
                }
            }
        } else {
            (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(
                AffiliateShareModel(
                    "WhatsApp",
                    IconUnify.WHATSAPP,
                    "whatsapp",
                    WHATSAPP_ID,
                    sheetType,
                    "",
                    false,
                    isChecked = false,
                    isLinkGenerationEnabled
                )
            )
            (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(
                AffiliateShareModel(
                    "Line", IconUnify.LINE, "line", LINE_ID, sheetType,
                    "", false, isChecked = false, isLinkGenerationEnabled
                )
            )
        }

        (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(
            AffiliateShareModel(
                "Lainnya", null, "others", OTHERS_ID, sheetType,
                "Contoh: yourwebsite.com", false, isChecked = false, isLinkGenerationEnabled
            )
        )
        setSelectedCheckBox()
    }

    private fun setSelectedCheckBox() {
        listVisitable.forEach {
            if (it is AffiliateShareModel && selectedIds.contains(it.id)) {
                it.isChecked = true
            }
        }
    }

    private fun initInject() {
        getComponent().injectPromotionBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    private fun setObservers() {
        affiliatePromotionBSViewModel?.generateLinkData()?.observe(this) {
            it?.let { data ->
                when (type) {
                    PAGE_TYPE_PDP -> sendClickEventProduct(
                        data.linkID,
                        AffiliateAnalytics.LabelKeys.SUCCESS
                    )

                    PAGE_TYPE_SHOP -> sendClickPGeventShop(
                        data.linkID,
                        AffiliateAnalytics.LabelKeys.SUCCESS,
                        status
                    )

                    PAGE_TYPE_CAMPAIGN -> sendClickEventCampaign(
                        data.linkID,
                        AffiliateAnalytics.LabelKeys.SUCCESS
                    )
                }
                val clipboardManager =
                    context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                        COPY_LABEL,
                        data.url?.shortURL
                    )
                )
                binding?.root?.let { root ->
                    Toaster.build(
                        root,
                        getString(R.string.affiliate_link_generated_succesfully, currentName),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL
                    ).show()
                }
            } ?: kotlin.run {
                when (type) {
                    PAGE_TYPE_PDP -> sendClickEventProduct("", AffiliateAnalytics.LabelKeys.FAIL)
                    PAGE_TYPE_SHOP ->
                        sendClickPGeventShop("", AffiliateAnalytics.LabelKeys.FAIL, status)

                    PAGE_TYPE_CAMPAIGN -> sendClickEventCampaign(
                        "",
                        AffiliateAnalytics.LabelKeys.FAIL
                    )
                }

                binding?.root?.let { root ->
                    Toaster.build(
                        root,
                        getString(R.string.affiliate_link_empty_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }

        affiliatePromotionBSViewModel?.loading()?.observe(this) { isLoad ->
            if (isLoad != null) {
                loading(isLoad)
            }
        }

        affiliatePromotionBSViewModel?.getErrorMessage()?.observe(this) { error ->
            if (error != null) {
                when (type) {
                    PAGE_TYPE_PDP -> sendClickEventProduct("", AffiliateAnalytics.LabelKeys.FAIL)
                    PAGE_TYPE_SHOP -> sendClickPGeventShop(
                        "",
                        AffiliateAnalytics.LabelKeys.FAIL,
                        status
                    )

                    PAGE_TYPE_CAMPAIGN -> sendClickEventCampaign(
                        "",
                        AffiliateAnalytics.LabelKeys.FAIL
                    )
                }
                binding?.root?.let { root ->
                    Toaster.build(
                        root,
                        error,
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }
    }

    private fun sendClickEventProduct(linkID: String?, state: String) {
        val params: AffiliatePromotionBottomSheetParams? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    KEY_PARAMS,
                    AffiliatePromotionBottomSheetParams::class.java
                )
            } else {
                arguments?.getParcelable(KEY_PARAMS) as? AffiliatePromotionBottomSheetParams
            }
        var eventAction = ""
        var eventCategory = ""
        var eventLabel =
            if (originScreen == ORIGIN_PROMOSIKAN) "$linkID - $status - $state" else "$linkID - $state"
        when (originScreen) {
            ORIGIN_HOME -> {
                eventAction =
                    AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_PRODUK_YANG_DIPROMOSIKAN
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_BOTTOM_SHEET
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }

            ORIGIN_HOME_GENERATED -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_DAFTAR_LINK_PRODUK
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_BOTTOM_SHEET
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }

            ORIGIN_PERNAH_DIBELI_PROMOSIKA -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_PERNAH_DIABEL
                eventCategory =
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }

            ORIGIN_TERAKHIR_DILIHAT -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_PERNAH_DILIHAT
                eventCategory =
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }

            ORIGIN_PROMOSIKAN -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_RESULT_PAGE
                eventCategory =
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }
        }
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            eventCategory,
            eventLabel,
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendClickPGeventShop(linkID: String?, status: String, entryFlag: String) {
        val params: AffiliatePromotionBottomSheetParams? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    KEY_PARAMS,
                    AffiliatePromotionBottomSheetParams::class.java
                )
            } else {
                arguments?.getParcelable(KEY_PARAMS) as? AffiliatePromotionBottomSheetParams
            }
        var eventAction = ""
        var eventCategory = ""
        var eventLabel = ""
        var event = AffiliateAnalytics.EventKeys.CLICK_PG
        when (originScreen) {
            ORIGIN_HOME -> {
                eventAction =
                    AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_SHOP_LINK_DENGAN_PERFORMA
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_BOTTOM_SHEET
                eventLabel = "$linkID - $status"
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += "komisi extra"
                }
            }

            ORIGIN_PROMOSIKAN -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_SHOP_SEARCH_RESULT
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                eventLabel = "$linkID - $entryFlag - $status"
                if (params?.ssaInfo?.ssaStatus == true) {
                    eventLabel += " - komisi extra"
                }
            }

            ORIGIN_SSA_SHOP -> {
                event = AffiliateAnalytics.EventKeys.CLICK_CONTENT
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_SSA_SHOP
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                eventLabel = "$linkID - active - $status - komisi extra"
            }
        }
        AffiliateAnalytics.sendEvent(
            event,
            eventAction,
            eventCategory,
            eventLabel,
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendClickEventCampaign(linkID: String?, status: String) {
        val params: AffiliatePromotionBottomSheetParams? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    KEY_PARAMS,
                    AffiliatePromotionBottomSheetParams::class.java
                )
            } else {
                arguments?.getParcelable(KEY_PARAMS) as? AffiliatePromotionBottomSheetParams
            }
        var eventAction = ""
        var eventCategory = ""
        var eventLabel = ""
        val event = AffiliateAnalytics.EventKeys.CLICK_CONTENT
        when (originScreen) {
            ORIGIN_HOME -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_DISCO_HOME
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_BOTTOM_SHEET
                eventLabel = "$linkID - $status"
            }

            ORIGIN_PROMO_DISCO_BANNER -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_DISCO_BANNER
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                eventLabel = "$linkID - $status"
            }

            ORIGIN_PROMO_DISCO_BANNER_LIST -> {
                eventAction = AffiliateAnalytics.ActionKeys.CLICK_SALIN_DISCO_BANNER_LIST
                eventCategory = AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_BOTTOM_SHEET
                eventLabel = "$linkID - $status"
            }
        }
        if (params?.ssaInfo?.ssaStatus == true) {
            eventLabel += " - komisi extra"
        }
        AffiliateAnalytics.sendEvent(
            event,
            eventAction,
            eventCategory,
            eventLabel,
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun loading(stop: Boolean) {
        for (visitable in listVisitable) {
            val updateVisitable = visitable as AffiliateShareModel
            if (currentName == updateVisitable.name) {
                updateVisitable.buttonLoad = stop
                adapter.setElement(adapter.list.indexOf(visitable), updateVisitable)
                break
            }
        }
    }

    private fun sendScreenEvent() {
        if (originScreen == ORIGIN_HOME) {
            AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                AffiliateAnalytics.ActionKeys.IMPRESSION_HOME_PORTAL_B_S,
                AffiliateAnalytics.CategoryKeys.HOME_PORTAL_B_S,
                "",
                userSessionInterface?.userId.orEmpty()
            )
        } else if (originScreen == ORIGIN_PROMOSIKAN) {
            AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                AffiliateAnalytics.ActionKeys.IMPRESSION_PROMOSIKAN_SRP_B_S,
                AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP_B_S,
                "",
                userSessionInterface?.userId.orEmpty()
            )
        }
    }

    override fun onShareButtonClick(name: String?, id: Int?, serviceFormat: String?) {
        currentName = name
        currentServiceFormat = serviceFormat ?: ""
        affiliatePromotionBSViewModel?.affiliateGenerateLink(
            id,
            type,
            productId,
            url,
            identifier,
            type
        )
    }

    private fun onSaveSocialButtonClicked() {
        val checkedSocialList = arrayListOf<AffiliateShareModel>()
        for (visitable in listVisitable) {
            if (visitable is AffiliateShareModel && visitable.isChecked) {
                checkedSocialList.add(visitable)
            }
        }
        affiliatePromotionBottomSheetInterface?.onButtonClick(checkedSocialList)
        dismiss()
    }

    override fun onSocialChecked(position: Int, isChecked: Boolean) {
        (listVisitable[position] as AffiliateShareModel).isChecked = isChecked
        checkForAtLeastOneSelected()
    }

    private fun checkForAtLeastOneSelected() {
        var count = 0
        for (visitable in listVisitable) {
            if ((visitable is AffiliateShareModel) && visitable.isChecked) {
                count += 1
            }
        }

        if (count == 0) {
            binding?.simpanBtn?.run {
                buttonVariant = UnifyButton.Variant.GHOST
                buttonType = UnifyButton.Type.ALTERNATE
                isEnabled = false
            }
            binding?.errorMessage?.show()
        } else {
            binding?.simpanBtn?.run {
                buttonVariant = UnifyButton.Variant.FILLED
                buttonType = UnifyButton.Type.MAIN
                isEnabled = true
            }
            binding?.errorMessage?.hide()
        }
    }
}

interface AffiliatePromotionBottomSheetInterface {
    fun onButtonClick(checkedSocialList: List<AffiliateShareModel>)
}
