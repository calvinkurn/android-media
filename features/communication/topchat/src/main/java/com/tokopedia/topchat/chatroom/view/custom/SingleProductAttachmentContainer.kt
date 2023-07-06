package com.tokopedia.topchat.chatroom.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.topchat.common.util.ViewUtil.ellipsizeLongText
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class SingleProductAttachmentContainer : ConstraintLayout {

    private var contentContainer: ConstraintLayout? = null
    private var btnWishList: UnifyButton? = null
    private var btnBuy: UnifyButton? = null
    private var btnAtc: UnifyButton? = null
    private var label: Label? = null
    private var loadView: LoaderUnify? = null
    private var freeShippingImage: ImageView? = null
    private var statusContainer: LinearLayout? = null
    private var reviewStar: ImageView? = null
    private var reviewScore: Typography? = null
    private var reviewCount: Typography? = null
    private var productName: Typography? = null
    private var thumbnail: ImageView? = null
    private var sellerStockContainer: LinearLayout? = null
    private var sellerStockType: Typography? = null
    private var sellerStockCount: Typography? = null
    private var sellerFullfilment: LinearLayout? = null
    private var sellerFullfilmentImage: ImageView? = null
    private var sellerFulfillmentDesc: Typography? = null
    private var btnUpdateStockContainer: LinearLayout? = null
    private var btnUpdateStock: UnifyButton? = null
    private var footerContainer: LinearLayout? = null
    private var shippingLocation: Typography? = null
    private var variant: LinearLayout? = null
    private var productColorVariant: LinearLayout? = null
    private var productColorVariantValue: Typography? = null
    private var productSizeVariant: LinearLayout? = null
    private var productVariantSize: Typography? = null
    private var campaignDiscount: Label? = null
    private var campaignPrice: Typography? = null
    private var price: Typography? = null
    private var adapterPosition: Int = RecyclerView.NO_POSITION

    private var listener: TopchatProductAttachmentListener? = null
    private var deferredAttachment: DeferredViewHolderAttachment? = null
    private var searchListener: SearchListener? = null
    private var commonListener: CommonViewHolderListener? = null
    private var adapterListener: AdapterListener? = null
    private var parentMetaData: ParentViewHolderMetaData? = null
    private val bgOpposite: Drawable? by lazy(LazyThreadSafetyMode.NONE) {
        ViewUtil.generateBackgroundWithShadow(
          this,
          com.tokopedia.unifyprinciples.R.color.Unify_NN0,
          com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
          com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
          com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
          com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
          ViewUtil.getShadowColorViewHolder(context),
          R.dimen.dp_topchat_2,
          R.dimen.dp_topchat_1,
          Gravity.CENTER
        )
    }
    private var bgSender: Drawable? = null

    private val defaultMarginLeft: Int by lazy(LazyThreadSafetyMode.NONE) {
        (layoutParams as? LinearLayout.LayoutParams)?.leftMargin ?: 0
    }
    private val defaultMarginTop: Int by lazy(LazyThreadSafetyMode.NONE) {
        (layoutParams as? LinearLayout.LayoutParams)?.topMargin ?: 0
    }
    private val defaultMarginRight: Int by lazy(LazyThreadSafetyMode.NONE) {
        (layoutParams as? LinearLayout.LayoutParams)?.rightMargin ?: 0
    }
    private val defaultMarginBottom: Int by lazy(LazyThreadSafetyMode.NONE) {
        (layoutParams as? LinearLayout.LayoutParams)?.bottomMargin ?: 0
    }

    private var widthMultiplier = DEFAULT_WIDTH_MULTIPLIER
    private var isCarousel = false
    private val bottomMarginOpposite = getOppositeMargin(context).toInt()

    constructor(context: Context?) : super(context) {
        initAttr(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttr(context, attrs)
    }

    init {
        initLayoutView()
        initBindView()
    }

    private fun initBindView() {
        contentContainer = findViewById(R.id.cl_info)
        btnWishList = findViewById(R.id.tv_wishlist)
        btnBuy = findViewById(R.id.tv_buy)
        btnAtc = findViewById(R.id.tv_atc)
        label = findViewById(R.id.lb_product_label)
        loadView = findViewById(R.id.iv_attachment_shimmer)
        freeShippingImage = findViewById(R.id.iv_free_shipping)
        statusContainer = findViewById(R.id.ll_status_container)
        reviewStar = findViewById(R.id.iv_review_star)
        reviewScore = findViewById(R.id.tv_review_score)
        reviewCount = findViewById(R.id.tv_review_count)
        productName = findViewById(R.id.tv_product_name)
        thumbnail = findViewById(R.id.iv_thumbnail)
        sellerStockContainer = findViewById(R.id.ll_seller_stock_data)
        sellerStockType = findViewById(R.id.tp_seller_stock_category)
        sellerStockCount = findViewById(R.id.tp_seller_stock_count)
        sellerFullfilment = findViewById(R.id.ll_seller_fullfilment)
        sellerFullfilmentImage = findViewById(R.id.iv_seller_fullfilment)
        sellerFulfillmentDesc = findViewById(R.id.tp_seller_fullfilment)
        btnUpdateStockContainer = findViewById(R.id.ll_seller_update_stock)
        btnUpdateStock = findViewById(R.id.btn_update_stock)
        footerContainer = findViewById(R.id.ll_footer)
        shippingLocation = findViewById(R.id.tv_shipping_location)
        productColorVariant = findViewById(R.id.ll_variant_color)
        productColorVariantValue = findViewById(R.id.tv_variant_color)
        productSizeVariant = findViewById(R.id.ll_variant_size)
        productVariantSize = findViewById(R.id.tv_variant_size)
        campaignDiscount = findViewById(R.id.tv_campaign_discount)
        campaignPrice = findViewById(R.id.tv_campaign_price)
        price = findViewById(R.id.tv_price)
        variant = findViewById(R.id.ll_variant)
    }

    private fun initLayoutView() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        if (context == null || attrs == null) return
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SingleProductAttachmentContainer, 0, 0
        ).apply {
            try {
                widthMultiplier = getFloat(
                    R.styleable.SingleProductAttachmentContainer_widthMultiplier,
                    DEFAULT_WIDTH_MULTIPLIER
                )
                isCarousel = getBoolean(
                    R.styleable.SingleProductAttachmentContainer_isCarousel,
                    false
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = MeasureSpec.getSize(widthMeasureSpec) * widthMultiplier
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }

    fun bindData(
        product: ProductAttachmentUiModel,
        adapterPosition: Int,
        listener: TopchatProductAttachmentListener,
        deferredAttachment: DeferredViewHolderAttachment,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
        adapterListener: AdapterListener,
        useStrokeSender: Boolean = true,
        parentMetaData: ParentViewHolderMetaData?
    ) {
        initViewHolderData(adapterPosition, parentMetaData)
        initListener(listener, deferredAttachment, searchListener, commonListener, adapterListener)
        initBackgroundDrawable(useStrokeSender)
        bindSyncProduct(product)
        bindLayoutGravity(product)
        if (product.isLoading && !product.isError) {
            bindIsLoading(product)
        } else {
            bindIsLoading(product)
            bindProductClick(product)
            bindImage(product)
            bindImageSize(product)
            bindImageClick(product)
            bindProductName(product)
            bindVariant(product)
            bindCampaign(product)
            bindPrice(product)
            bindRating(product)
            bindFreeShipping(product)
            bindFooter(product)
            bindPreOrderLabel(product)
            bindEmptyStockLabel(product)
            bindBackground(product)
            bindSellerRemainingStock(product)
            bindSellerFullfilment(product)
            bindSellerUpdateStockBtn(product)
            bindSellerUpdateStockClick(product)
            bindMargin(product)
            bindContentPadding(product)
            bindShippingLocation(product)
            listener.trackSeenProduct(product)
        }
    }

    fun updateStockState(product: ProductAttachmentUiModel) {
        bindSellerStockCount(product)
        bindEmptyStockLabel(product)
    }

    private fun initBackgroundDrawable(useStrokeSender: Boolean) {
        if (bgSender == null) {
            val strokeColor = if (useStrokeSender) com.tokopedia.unifyprinciples.R.color.Unify_GN50 else null
            val strokeWidth = if (useStrokeSender) getStrokeWidthSenderDimenRes() else null
            bgSender = ViewUtil.generateBackgroundWithShadow(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                ViewUtil.getShadowColorViewHolder(context),
                R.dimen.dp_topchat_2,
                R.dimen.dp_topchat_1,
                Gravity.CENTER,
                strokeColor,
                strokeWidth
            )
        }
    }

    private fun initViewHolderData(
        adapterPosition: Int, parentMetaData: ParentViewHolderMetaData?
    ) {
        this.adapterPosition = adapterPosition
        this.parentMetaData = parentMetaData
    }

    private fun initListener(
        listener: TopchatProductAttachmentListener,
        deferredAttachment: DeferredViewHolderAttachment,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
        adapterListener: AdapterListener
    ) {
        this.listener = listener
        this.deferredAttachment = deferredAttachment
        this.searchListener = searchListener
        this.commonListener = commonListener
        this.adapterListener = adapterListener
    }

    private fun bindSyncProduct(product: ProductAttachmentUiModel) {
        if (!product.isLoading) return
        val chatAttachments = deferredAttachment?.getLoadedChatAttachments() ?: return
        val attachment = chatAttachments[product.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            product.syncError()
        } else {
            product.updateData(attachment.parsedAttributes)
        }
    }

    private fun bindLayoutGravity(product: ProductAttachmentUiModel) {
        if (product.isSender) {
            gravityRight()
        } else {
            gravityLeft()
        }
    }

    private fun bindIsLoading(product: ProductAttachmentUiModel) {
        if (product.isLoading) {
            loadView?.show()
        } else {
            loadView?.hide()
        }
    }

    private fun bindImage(product: ProductAttachmentUiModel) {
        thumbnail?.loadImageRounded(product.productImage, 8f.toPx())
    }

    private fun bindImageSize(product: ProductAttachmentUiModel) {
        val thumbnailSize = if (isCarousel) {
            CAROUSEL_THUMBNAIL_DIMENSION
        } else {
            SINGLE_THUMBNAIL_DIMENSION
        }
        val lp = thumbnail?.layoutParams
        lp?.apply {
            height = thumbnailSize.toInt()
            width = thumbnailSize.toInt()
        }
        thumbnail?.layoutParams = lp
    }

    private fun bindProductName(product: ProductAttachmentUiModel) {
        val query = searchListener?.getSearchQuery() ?: ""
        val spanText = SpannableString(product.productName)
        if (query.isNotEmpty()) {
            val color = Constant.searchTextBackgroundColor
            val index = spanText.indexOf(query, ignoreCase = true)
            if (index != -1) {
                var lastIndex = index + query.length
                if (lastIndex > spanText.lastIndex) {
                    lastIndex = spanText.lastIndex
                }
                if (index < spanText.length && spanText.length >= lastIndex) {
                    spanText.setSpan(BackgroundColorSpan(color), index, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        productName?.text = spanText
    }

    private fun bindVariant(product: ProductAttachmentUiModel) {
        if (product.doesNotHaveVariant()) {
            hideVariantLayout()
            return
        }

        showVariantLayout()
        if (product.hasColorVariant()) {
            productColorVariant?.show()
            productColorVariantValue?.text = ellipsizeLongText(
                product.colorVariant, MAX_VARIANT_LABEL_CHAR
            )
        } else {
            productColorVariant?.hide()
        }

        if (product.hasSizeVariant()) {
            productSizeVariant?.show()
            productVariantSize?.text = ellipsizeLongText(
                product.sizeVariant, MAX_VARIANT_LABEL_CHAR
            )
        } else {
            productSizeVariant?.hide()
        }
    }

    private fun bindCampaign(product: ProductAttachmentUiModel) {
        if (product.hasDiscount) {
            toggleCampaign(View.VISIBLE)
            bindDiscount(product)
            bindDropPrice(product)
        } else {
            toggleCampaign(View.GONE)
        }
    }

    private fun bindSellerRemainingStock(product: ProductAttachmentUiModel) {
        if (commonListener?.isSeller() == true && !product.isUpcomingCampaign) {
            sellerStockContainer?.show()
            bindSellerStockCount(product)
            bindSellerStockType(product)
        } else {
            sellerStockContainer?.hide()
        }
    }

    private fun bindSellerStockCount(product: ProductAttachmentUiModel) {
        sellerStockCount?.text = product.remainingStock.toString()
    }

    private fun bindSellerStockType(product: ProductAttachmentUiModel) {
        val stockCategoryRes = if (product.isProductCampaign()) {
            R.string.title_campaign_stock
        } else {
            R.string.title_regular_stock
        }
        sellerStockType?.setText(stockCategoryRes)
    }

    private fun bindSellerFullfilment(product: ProductAttachmentUiModel) {
        if (commonListener?.isSeller() == true && product.isFulfillment) {
            sellerFullfilment?.show()
            sellerFullfilmentImage?.loadImage(product.urlTokocabang) {
                fitCenter()
            }
            sellerFulfillmentDesc?.text = product.descTokoCabang
        } else {
            sellerFullfilment?.hide()
        }
    }

    private fun bindSellerUpdateStockBtn(product: ProductAttachmentUiModel) {
        if (product.canShowFooter || commonListener?.isSeller() == false ||
            product.isProductCampaign() || !enableUpdateStockSeller()
        ) {
            btnUpdateStockContainer?.hide()
        } else {
            btnUpdateStockContainer?.show()
        }
    }

    private fun bindSellerUpdateStockClick(product: ProductAttachmentUiModel) {
        btnUpdateStock?.setOnClickListener {
            listener?.updateProductStock(product, adapterPosition, parentMetaData)
            listener?.trackClickUpdateStock(product)
        }
    }

    private fun bindShippingLocation(product: ProductAttachmentUiModel) {
        if (commonListener?.isSeller() == true &&
            product.locationStock.districtFullName.isNotEmpty() &&
            !product.isFulfillment
        ) {
            shippingLocation?.show()
            shippingLocation?.text = product.locationStock.districtFullName
        } else {
            shippingLocation?.hide()
        }
    }

    private fun bindMargin(product: ProductAttachmentUiModel) {
        val lp = layoutParams
        if (lp is LinearLayout.LayoutParams) {
            if (isNextItemOppositeFrom(product)) {
                setMargin(
                    defaultMarginLeft,
                    bottomMarginOpposite,
                    defaultMarginRight,
                    defaultMarginBottom
                )
            } else {
                setMargin(
                    defaultMarginLeft,
                    defaultMarginTop,
                    defaultMarginRight,
                    defaultMarginBottom
                )
            }
        }
    }

    private fun bindContentPadding(product: ProductAttachmentUiModel) {
        val newPaddingBottom = if (btnUpdateStockContainer?.isVisible == true ||
            footerContainer?.isVisible == true
        ) {
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        } else {
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
        }
        contentContainer?.apply {
            setPadding(paddingLeft, paddingTop, paddingRight, newPaddingBottom)
        }
    }

    private fun isNextItemOppositeFrom(product: ProductAttachmentUiModel): Boolean {
        return adapterListener?.isOpposite(adapterPosition, product.isSender) == true
    }

    private fun bindBackground(product: ProductAttachmentUiModel) {
        if (product.isSender) {
            background = bgSender
        } else {
            background = bgOpposite
        }
    }

    private fun bindProductClick(product: ProductAttachmentUiModel) {
        setOnClickListener { listener?.onProductClicked(product) }
    }

    private fun bindImageClick(product: ProductAttachmentUiModel) {
        thumbnail?.setOnClickListener {
            listener?.trackClickProductThumbnail(product)
            it.context.startActivity(
                ImagePreviewActivity.getCallingIntent(
                    it.context,
                    ArrayList(product.images),
                    null,
                    0
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: ProductAttachmentUiModel) {
        campaignDiscount?.text = "${product.dropPercentage}%"
    }

    private fun bindDropPrice(product: ProductAttachmentUiModel) {
        campaignPrice?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = product.priceBefore
        }
    }

    private fun bindPrice(product: ProductAttachmentUiModel) {
        price?.text = product.productPrice
    }

    @SuppressLint("SetTextI18n")
    private fun bindRating(product: ProductAttachmentUiModel) {
        if (product.hasReview() && commonListener?.isSeller() == false) {
            reviewScore?.text = product.rating.score.toString()
            reviewCount?.text = "(${product.rating.count})"
            statusContainer?.show()
            reviewStar?.show()
            reviewScore?.show()
            reviewCount?.show()
        } else {
            statusContainer?.hide()
            reviewStar?.hide()
            reviewScore?.hide()
            reviewCount?.hide()
        }
    }

    private fun bindFreeShipping(product: ProductAttachmentUiModel) {
        if (product.hasFreeShipping() && commonListener?.isSeller() == false) {
            freeShippingImage?.show()
            freeShippingImage?.loadImageRounded(product.getFreeShippingImageUrl())
        } else {
            freeShippingImage?.hide()
        }
    }

    private fun bindFooter(product: ProductAttachmentUiModel) {
        if (product.canShowFooter && !GlobalConfig.isSellerApp()) {
            footerContainer?.show()
            bindBuy(product)
            bindAtc(product)
            bindWishList(product)
        } else {
            hideFooter()
        }
    }

    private fun bindPreOrderLabel(product: ProductAttachmentUiModel) {
        label?.apply {
            if (product.isPreOrder) {
                show()
                setText(R.string.title_topchat_pre_order)
                unlockFeature = true
                setLabelType(getEmptyStockLabelBg())
            } else {
                hide()
            }
        }
    }

    private fun bindEmptyStockLabel(product: ProductAttachmentUiModel) {
        label?.apply {
            if (product.hasEmptyStock() && !product.isUpcomingCampaign) {
                show()
                setText(R.string.title_topchat_empty_stock)
                unlockFeature = true
                setLabelType(getEmptyStockLabelBg())
            } else {
                if (!product.isPreOrder) {
                    hide()
                }
            }
        }
    }

    private fun getEmptyStockLabelBg(): String {
        return resources.getString(R.string.topchat_dms_hex_empty_stock_color_bg)
    }

    private fun hideFooter() {
        footerContainer?.hide()
        btnBuy?.hide()
        btnAtc?.hide()
        btnWishList?.hide()
    }

    private fun bindBuy(product: ProductAttachmentUiModel) {
        btnBuy?.let {
            if (product.hasEmptyStock() || product.isUpcomingCampaign) {
                it.hide()
            } else {
                it.show()
                it.isEnabled = true
                if (product.isPreOrder) {
                    it.setText(R.string.title_topchat_pre_order_camel)
                } else {
                    if (listener?.isOCCActive() == true && product.isEligibleOCC()) {
                        it.setText(com.tokopedia.chat_common.R.string.action_occ)
                    } else {
                        it.setText(com.tokopedia.chat_common.R.string.action_buy)
                    }
                }
                it.setOnClickListener {
                    listener?.onClickBuyFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindAtc(product: ProductAttachmentUiModel) {
        btnAtc?.let {
            if (product.hasEmptyStock() || product.isUpcomingCampaign) {
                it.hide()
            } else {
                it.show()
                it.setOnClickListener {
                    listener?.onClickATCFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindWishList(product: ProductAttachmentUiModel) {
        if (product.hasEmptyStock() || product.isUpcomingCampaign) {
            btnWishList?.show()
            setupWishlistButton(product)
        } else {
            btnWishList?.hide()
        }
    }

    private fun setupWishlistButton(product: ProductAttachmentUiModel) {
        if (product.wishList) {
            setupAddedToWishlistButton()
        } else {
            setupAddToWishlistButton()
        }
        btnWishList?.setOnClickListener {
            listener?.onClickAddToWishList(product) {
                product.wishList = true
                setupAddedToWishlistButton()
            }
        }
    }

    private fun setupAddedToWishlistButton() {
        btnWishList?.buttonType = UnifyButton.Type.ALTERNATE
        btnWishList?.text = context.getString(R.string.topchat_product_added_to_wishlist)
    }

    private fun setupAddToWishlistButton() {
        btnWishList?.buttonType = UnifyButton.Type.MAIN
        btnWishList?.text = context.getString(R.string.action_wishlist)
    }

    fun gravityRight() {
        setLayoutGravity(Gravity.END)
    }

    fun gravityLeft() {
        setLayoutGravity(Gravity.START)
    }

    private fun hideVariantLayout() {
        variant?.hide()
    }

    private fun showVariantLayout() {
        variant?.show()
    }

    private fun toggleCampaign(visibility: Int) {
        campaignDiscount?.visibility = visibility
        campaignPrice?.visibility = visibility
    }

    private fun setLayoutGravity(@Slide.GravityFlag gravity: Int) {
        (layoutParams as? LinearLayout.LayoutParams)?.apply {
            this.gravity = gravity
        }
    }

    /**
     * hansel function, remove if no longer used
     */
    private fun enableUpdateStockSeller(): Boolean {
        return true
    }

    /**
     * To refer product in carousel (broadcast or normal carousel)
     */
    class ParentViewHolderMetaData(
        val uiModel: Visitable<*>,
        val lastKnownPosition: Int
    )

    class PayloadUpdateStock(
        val productId: String
    )

    companion object {
        private const val DEFAULT_WIDTH_MULTIPLIER = 0.83f
        private const val MAX_VARIANT_LABEL_CHAR = 6
        private val SINGLE_THUMBNAIL_DIMENSION = 104f.toPx()
        private val CAROUSEL_THUMBNAIL_DIMENSION = 80f.toPx()
        private val LAYOUT = R.layout.item_topchat_product_card
    }
}
