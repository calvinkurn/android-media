package com.tokopedia.topchat.chatroom.view.custom.product_bundling

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
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.MultipleBundlingItemAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel.Companion.BUNDLE_TYPE_MULTIPLE
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductBundlingCardAttachmentContainer : ConstraintLayout {

    private var singleBundlingLayout: ConstraintLayout? = null
    private var image: ImageUnify? = null
    private var label: Label? = null
    private var singleItemBundlingName: Typography? = null
    private var multipleItemBundlingName: Typography? = null
    private var originalPrice: Typography? = null
    private var totalDiscount: Typography? = null
    private var bundlePrice: Typography? = null
    private var button: UnifyButton? = null
    private var loader: LoaderUnify? = null

    private var listener: ProductBundlingListener? = null
    private var adapterListener: AdapterListener? = null
    private var adapterPosition: Int = RecyclerView.NO_POSITION
    private var searchListener: SearchListener? = null
    private var commonListener: CommonViewHolderListener? = null
    private var deferredAttachment: DeferredViewHolderAttachment? = null

    private var recyclerView: RecyclerView? = null
    private var adapter: MultipleBundlingItemAdapter? = null
    private var itemDecorationProductAttachment: BundleSpaceItemDecoration? = null
    private var itemDecorationBroadcastAttachment: BroadcastBundleSpaceItemDecoration? = null
    private var source: BundlingSource? = null

    private val bgOpposite: Drawable? by lazy(LazyThreadSafetyMode.NONE) {
        ViewUtil.generateBackgroundWithShadow(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600,
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

    private val bottomMarginOpposite = getOppositeMargin(context).toInt()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initLayoutView()
        initBindView()
    }

    private fun initLayoutView() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBindView() {
        singleBundlingLayout = findViewById(R.id.single_bundling_layout)
        image = findViewById(R.id.iv_single_product_thumbnail)
        label = findViewById(R.id.label_package)
        singleItemBundlingName = findViewById(R.id.tv_product_bundling_name)
        multipleItemBundlingName = findViewById(R.id.tv_product_bundle_name)
        originalPrice = findViewById(R.id.tv_original_price)
        totalDiscount = findViewById(R.id.tv_total_discount)
        bundlePrice = findViewById(R.id.tv_bundle_price)
        button = findViewById(R.id.button_open_package)
        recyclerView = findViewById(R.id.rv_product_bundle)
        loader = findViewById(R.id.loader_bundle)
    }

    fun bindData(
        element: ProductBundlingUiModel,
        adapterPosition: Int,
        listener: ProductBundlingListener,
        adapterListener: AdapterListener,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
        deferredAttachment: DeferredViewHolderAttachment,
        source: BundlingSource?
    ) {
        this.source = source
        this.adapterPosition = adapterPosition
        bindListener(listener, adapterListener, searchListener, commonListener, deferredAttachment)
        bindBackground(element)
        bindSyncProductBundling(element)
        bindLoading(element)
        if (!element.isLoading || element.isError) {
            bindMargin(element)
            bindLayoutStyle(element)
            bindPrice(element)
            bindCtaClick(element)
            listener.onSeenProductBundling(element)
        }
    }

    private fun bindLayoutStyle(element: ProductBundlingUiModel) {
        if (isMultipleBundleItem(element)) {
            initRecyclerView(element)
            showRecyclerView()
        } else {
            bindImage(element.productBundling.bundleItem?.first(), element)
            bindLabel(element.productBundling.bundleItem?.first())
            bindBundlingName(element.productBundling.bundleItem?.first())
            hideRecyclerView()
        }
        bindItemBundlingName(element)
    }

    private fun isMultipleBundleItem(element: ProductBundlingUiModel) : Boolean {
        return element.productBundling.bundleType == BUNDLE_TYPE_MULTIPLE
    }

    private fun hideRecyclerView() {
        singleBundlingLayout?.show()
        recyclerView?.hide()
    }

    private fun showRecyclerView() {
        singleBundlingLayout?.hide()
        recyclerView?.show()
    }

    private fun bindItemBundlingName(element: ProductBundlingUiModel) {
        if (source == BundlingSource.BROADCAST_ATTACHMENT_SINGLE ||
                source == BundlingSource.BROADCAST_ATTACHMENT_MULTIPLE) {
            multipleItemBundlingName?.text = element.productBundling.bundleTitle
            multipleItemBundlingName?.show()
        } else {
            multipleItemBundlingName?.hide()
        }
    }

    private fun initRecyclerView(element: ProductBundlingUiModel) {
        recyclerView?.let {
            it.adapter = adapter
            bindBundleItemList(element)
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration()
        }
    }

    private fun bindBundleItemList(element: ProductBundlingUiModel) {
        element.productBundling.bundleItem?.let { list ->
            adapter?.bundlingList = list
        }
        adapter?.productBundling = element
    }

    private fun addItemDecoration() {
        val counter = recyclerView?.itemDecorationCount ?: 0
        for (i in 0 until counter) {
            recyclerView?.removeItemDecorationAt(i)
        }
        setSpaceItemDecoration()
    }

    private fun setSpaceItemDecoration() {
        when (source) {
            BundlingSource.PRODUCT_ATTACHMENT -> {
                if (itemDecorationProductAttachment == null) {
                    itemDecorationProductAttachment = BundleSpaceItemDecoration()
                }
                itemDecorationProductAttachment?.let {
                    recyclerView?.addItemDecoration(it)
                }
            }
            BundlingSource.BROADCAST_ATTACHMENT_SINGLE,
            BundlingSource.BROADCAST_ATTACHMENT_MULTIPLE -> {
                if (itemDecorationBroadcastAttachment == null) {
                    source?.let {
                        itemDecorationBroadcastAttachment = BroadcastBundleSpaceItemDecoration(it)
                    }
                }
                itemDecorationBroadcastAttachment?.let {
                    recyclerView?.addItemDecoration(it)
                }
            }
        }
    }

    private fun bindListener(
        listener: ProductBundlingListener,
        adapterListener: AdapterListener,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
        deferredAttachment: DeferredViewHolderAttachment
    ) {
        this.listener = listener
        this.adapterListener = adapterListener
        this.searchListener = searchListener
        this.commonListener = commonListener
        this.deferredAttachment = deferredAttachment
        adapter = MultipleBundlingItemAdapter(listener)
    }

    private fun bindLoading(element: ProductBundlingUiModel) {
        if (element.isLoading) {
            loader?.show()
        } else {
            loader?.hide()
        }
    }

    private fun bindImage(item: BundleItem?, element: ProductBundlingUiModel) {
        item?.let { bundleItem ->
            image?.apply {
                this.setImageUrl(bundleItem.imageUrl)
                this.setOnClickListener {
                    listener?.onClickProductBundlingImage(bundleItem, element)
                }
            }
        }
    }

    private fun bindLabel(item: BundleItem?) {
        item?.quantity?.let {
            label?.text = context.getString(R.string.topchat_product_bundling_label, it)
        }
    }

    private fun bindBundlingName(item: BundleItem?) {
        val query = searchListener?.getSearchQuery() ?: ""
        val spanText = SpannableString(item?.name?: "")
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
        singleItemBundlingName?.text = spanText
    }

    private fun bindPrice(element: ProductBundlingUiModel) {
        originalPrice?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = element.productBundling.originalPrice
        }
        totalDiscount?.text = element.productBundling.totalDiscount
        bundlePrice?.text = element.productBundling.bundlePrice
    }

    private fun bindCtaClick(element: ProductBundlingUiModel) {
        button?.let {
            if (commonListener?.isSeller() == true
                || element.productBundling.ctaBundling?.isButtonShown == false
            ) {
                it.hide()
            } else {
                bindButton(it, element)
                it.show()
            }
        }
    }

    private fun bindButton(button: UnifyButton, element: ProductBundlingUiModel) {
        button.text = getButtonText(element)
        button.buttonType = UnifyButton.Type.MAIN
        when(element.productBundling.ctaBundling?.isDisabled) {
            false -> bindActiveButton(button, element)
            true -> bindDisabledButton(button)
        }
    }

    private fun getButtonText(element: ProductBundlingUiModel): String? {
        return if (element.isBroadcast() &&
            element.productBundling.ctaBundling?.isDisabled == false
        ) {
            context?.getString(R.string.action_atc)
        } else {
            element.productBundling.ctaBundling?.ctaText
        }
    }

    private fun bindActiveButton(button: UnifyButton, element: ProductBundlingUiModel) {
        button.isEnabled = true
        button.setOnClickListener {
            listener?.onClickCtaProductBundling(element)
        }
    }

    private fun bindDisabledButton(button: UnifyButton) {
        button.isEnabled = false
    }

    private fun bindMargin(product: ProductBundlingUiModel) {
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

    private fun isNextItemOppositeFrom(product: ProductBundlingUiModel): Boolean {
        return adapterListener?.isOpposite(adapterPosition, product.isSender) == true
    }

    private fun bindBackground(element: ProductBundlingUiModel) {
        background = if (element.isSender) {
            setSellerBackground()
            bgSender
        } else {
            bgOpposite
        }
    }

    private fun setSellerBackground() {
        val stroke = if (source == BundlingSource.PRODUCT_ATTACHMENT) {
            com.tokopedia.unifyprinciples.R.color.Unify_GN50
        } else {
            null
        }
        bgSender = ViewUtil.generateBackgroundWithShadow(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            stroke,
            getStrokeWidthSenderDimenRes()
        )
    }

    /**
     * Update the element manually
     * When this view has not been rendered but the adapter has been updated
     */
    private fun bindSyncProductBundling(element: ProductBundlingUiModel) {
        if (!element.isLoading) return
        val chatAttachments = deferredAttachment?.getLoadedChatAttachments() ?: return
        val attachment = chatAttachments[element.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            element.syncError()
        } else {
            element.updateData(attachment.parsedAttributes)
        }
    }

    enum class BundlingSource {
        PRODUCT_ATTACHMENT,
        BROADCAST_ATTACHMENT_MULTIPLE,
        BROADCAST_ATTACHMENT_SINGLE
    }

    companion object {
        private val LAYOUT = R.layout.item_topchat_product_bundling_card
    }
}
