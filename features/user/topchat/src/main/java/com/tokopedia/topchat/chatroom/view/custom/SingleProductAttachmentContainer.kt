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
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_topchat_product_card.view.*

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
    private var btnUpdateStockContainer: LinearLayout? = null
    private var btnUpdateStock: UnifyButton? = null
    private var footerContainer: LinearLayout? = null
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
                com.tokopedia.unifyprinciples.R.color.Unify_N0,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
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
        btnUpdateStockContainer = findViewById(R.id.ll_seller_update_stock)
        btnUpdateStock = findViewById(R.id.btn_update_stock)
        footerContainer = findViewById(R.id.ll_footer)
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
            product: ProductAttachmentViewModel,
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
            bindImageClick(product)
            bindProductName(product)
            bindVariant(product)
            bindCampaign(product)
            bindPrice(product)
            bindStatusContainer(product)
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
            listener.trackSeenProduct(product)
        }
    }

    fun updateStockState(product: ProductAttachmentViewModel) {
        bindSellerStockCount(product)
        bindEmptyStockLabel(product)
    }

    private fun initBackgroundDrawable(useStrokeSender: Boolean) {
        if (bgSender == null) {
            val strokeColor = if (useStrokeSender) com.tokopedia.unifyprinciples.R.color.Unify_G200 else null
            val strokeWidth = if (useStrokeSender) getStrokeWidthSenderDimenRes() else null
            bgSender = ViewUtil.generateBackgroundWithShadow(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_N0,
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
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

    private fun bindSyncProduct(product: ProductAttachmentViewModel) {
        if (!product.isLoading) return
        val chatAttachments = deferredAttachment?.getLoadedChatAttachments() ?: return
        val attachment = chatAttachments[product.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            product.syncError()
        } else {
            product.updateData(attachment.parsedAttributes)
        }
    }

    private fun bindLayoutGravity(product: ProductAttachmentViewModel) {
        if (product.isSender) {
            gravityRight()
        } else {
            gravityLeft()
        }
    }

    private fun bindIsLoading(product: ProductAttachmentViewModel) {
        if (product.isLoading) {
            loadView?.show()
        } else {
            loadView?.hide()
        }
    }

    private fun bindImage(product: ProductAttachmentViewModel) {
        ImageHandler.loadImageRounded2(
                context,
                thumbnail,
                product.productImage,
                8f.toPx()
        )
    }

    private fun bindProductName(product: ProductAttachmentViewModel) {
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
                spanText.setSpan(BackgroundColorSpan(color), index, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        productName?.text = spanText
    }

    private fun bindVariant(product: ProductAttachmentViewModel) {
        if (product.doesNotHaveVariant()) {
            hideVariantLayout()
            return
        }

        showVariantLayout()
        if (product.hasColorVariant()) {
            ll_variant_color?.show()
            val backgroundDrawable = ColorDrawableGenerator.generate(
                    context, product.colorHexVariant
            )
            iv_variant_color?.background = backgroundDrawable
            tv_variant_color?.text = product.colorVariant
        } else {
            ll_variant_color?.hide()
        }

        if (product.hasSizeVariant()) {
            ll_variant_size?.show()
            tv_variant_size?.text = product.sizeVariant
        } else {
            ll_variant_size?.hide()
        }
    }

    private fun bindCampaign(product: ProductAttachmentViewModel) {
        if (product.hasDiscount) {
            toggleCampaign(View.VISIBLE)
            bindDiscount(product)
            bindDropPrice(product)
        } else {
            toggleCampaign(View.GONE)
        }
    }

    private fun bindSellerRemainingStock(product: ProductAttachmentViewModel) {
        if (commonListener?.isSeller() == true) {
            sellerStockContainer?.show()
            bindSellerStockCount(product)
            bindSellerStockType(product)
        } else {
            sellerStockContainer?.hide()
        }
    }

    private fun bindSellerStockCount(product: ProductAttachmentViewModel) {
        sellerStockCount?.text = product.remainingStock.toString()
    }

    private fun bindSellerStockType(product: ProductAttachmentViewModel) {
        val stockCategoryRes = if (product.isProductCampaign()) {
            R.string.title_campaign_stock
        } else {
            R.string.title_regular_stock
        }
        sellerStockType?.setText(stockCategoryRes)
    }

    private fun bindSellerFullfilment(product: ProductAttachmentViewModel) {
        if (commonListener?.isSeller() == true && product.isFulfillment) {
            sellerFullfilment?.show()
            ImageHandler.LoadImage(sellerFullfilmentImage, product.urlTokocabang)
        } else {
            sellerFullfilment?.hide()
        }
    }

    private fun bindSellerUpdateStockBtn(product: ProductAttachmentViewModel) {
        if (product.canShowFooter || commonListener?.isSeller() == false ||
                product.isProductCampaign() || !enableUpdateStockSeller()) {
            btnUpdateStockContainer?.hide()
        } else {
            btnUpdateStockContainer?.show()
        }
    }

    private fun bindSellerUpdateStockClick(product: ProductAttachmentViewModel) {
        btnUpdateStock?.setOnClickListener {
            listener?.updateProductStock(product, adapterPosition, parentMetaData)
            listener?.trackClickUpdateStock(product)
        }
    }

    private fun bindMargin(product: ProductAttachmentViewModel) {
        val lp = layoutParams
        if (lp is LinearLayout.LayoutParams) {
            if (isNextItemOppositeFrom(product)) {
                setMargin(defaultMarginLeft, bottomMarginOpposite, defaultMarginRight, defaultMarginBottom)
            } else {
                setMargin(defaultMarginLeft, defaultMarginTop, defaultMarginRight, defaultMarginBottom)
            }
        }
    }

    private fun bindContentPadding(product: ProductAttachmentViewModel) {
        val newPaddingBottom = if (btnUpdateStockContainer?.isVisible == true ||
                footerContainer?.isVisible == true) {
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        } else {
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
        }
        contentContainer?.apply {
            setPadding(paddingLeft, paddingTop, paddingRight, newPaddingBottom)
        }
    }

    private fun isNextItemOppositeFrom(product: ProductAttachmentViewModel): Boolean {
        return adapterListener?.isOpposite(adapterPosition, product.isSender) == true
    }

    private fun bindBackground(product: ProductAttachmentViewModel) {
        if (product.isSender) {
            background = bgSender
        } else {
            background = bgOpposite
        }
    }

    private fun bindProductClick(product: ProductAttachmentViewModel) {
        setOnClickListener { listener?.onProductClicked(product) }
    }

    private fun bindImageClick(product: ProductAttachmentViewModel) {
        iv_thumbnail?.setOnClickListener {
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
    private fun bindDiscount(product: ProductAttachmentViewModel) {
        tv_campaign_discount?.text = "${product.dropPercentage}%"
    }

    private fun bindDropPrice(product: ProductAttachmentViewModel) {
        tv_campaign_price?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = product.priceBefore
        }
    }

    private fun bindPrice(product: ProductAttachmentViewModel) {
        tv_price?.text = product.productPrice
    }

    private fun bindStatusContainer(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping() || (product.hasReview() && product.fromBroadcast())) {
            statusContainer?.show()
        } else {
            statusContainer?.hide()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindRating(product: ProductAttachmentViewModel) {
        if (product.hasReview() && commonListener?.isSeller() == false) {
            reviewScore?.text = product.rating.score.toString()
            reviewCount?.text = "(${product.rating.count})"
            reviewStar?.show()
            reviewScore?.show()
            reviewCount?.show()
        } else {
            reviewStar?.hide()
            reviewScore?.hide()
            reviewCount?.hide()
        }
    }

    private fun bindFreeShipping(product: ProductAttachmentViewModel) {
        if (product.hasFreeShipping()) {
            freeShippingImage?.show()
            ImageHandler.loadImageRounded2(context, freeShippingImage, product.getFreeShippingImageUrl())
        } else {
            freeShippingImage?.hide()
        }
    }

    private fun bindFooter(product: ProductAttachmentViewModel) {
        if (product.canShowFooter && !GlobalConfig.isSellerApp()) {
            footerContainer?.show()
            bindBuy(product)
            bindAtc(product)
            bindWishList(product)
        } else {
            hideFooter()
        }
    }

    private fun bindPreOrderLabel(product: ProductAttachmentViewModel) {
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

    private fun bindEmptyStockLabel(product: ProductAttachmentViewModel) {
        label?.apply {
            if (product.hasEmptyStock()) {
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

    private fun bindBuy(product: ProductAttachmentViewModel) {
        btnBuy?.let {
            if (product.hasEmptyStock()) {
                it.hide()
            } else {
                it.show()
                it.isEnabled = true
                if (product.isPreOrder) {
                    it.setText(R.string.title_topchat_pre_order_camel)
                } else {
                    it.setText(com.tokopedia.chat_common.R.string.action_buy)
                }
                it.setOnClickListener {
                    listener?.onClickBuyFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindAtc(product: ProductAttachmentViewModel) {
        btnAtc?.let {
            if (product.hasEmptyStock()) {
                it.hide()
            } else {
                it.show()
                it.setOnClickListener {
                    listener?.onClickATCFromProductAttachment(product)
                }
            }
        }
    }

    private fun bindWishList(product: ProductAttachmentViewModel) {
        if (product.hasEmptyStock()) {
            btnWishList?.show()
            btnWishList?.setOnClickListener {
                listener?.onClickAddToWishList(product) {
                    product.wishList = true
                }
            }
        } else {
            btnWishList?.hide()
        }
    }

    fun gravityRight() {
        setLayoutGravity(Gravity.END)
    }

    fun gravityLeft() {
        setLayoutGravity(Gravity.START)
    }

    private fun hideVariantLayout() {
        ll_variant?.hide()
    }

    private fun showVariantLayout() {
        ll_variant?.show()
    }

    private fun toggleCampaign(visibility: Int) {
        tv_campaign_discount?.visibility = visibility
        tv_campaign_price?.visibility = visibility
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
        private val LAYOUT = R.layout.item_topchat_product_card
    }
}