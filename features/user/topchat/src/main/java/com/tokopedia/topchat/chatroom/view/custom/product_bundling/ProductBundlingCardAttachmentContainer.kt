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
import androidx.transition.Slide
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
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel.Companion.STATUS_ACTIVE
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel.Companion.STATUS_UPCOMING
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
    private var bundlingName: Typography? = null
    private var originalPrice: Typography? = null
    private var totalDiscount: Typography? = null
    private var bundlePrice: Typography? = null
    private var button: UnifyButton? = null
    private var loader: LoaderUnify? = null

    private var listener: ProductBundlingListener? = null
    private var adapterListener: AdapterListener? = null
    private var adapterPosition: Int = RecyclerView.NO_POSITION
    private var deferredAttachment: DeferredViewHolderAttachment? = null
    private var searchListener: SearchListener? = null
    private var commonListener: CommonViewHolderListener? = null

    private var recyclerView: RecyclerView? = null
    private val adapter = MultipleBundlingItemAdapter()
    private val itemDecoration = BundleSpaceItemDecoration(SPACE)

    private val bgOpposite: Drawable? by lazy(LazyThreadSafetyMode.NONE) {
        ViewUtil.generateBackgroundWithShadow(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
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

    private val bgSender: Drawable? by lazy(LazyThreadSafetyMode.NONE) {
        ViewUtil.generateBackgroundWithShadow(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Background,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_topchat_2,
                R.dimen.dp_topchat_1,
                Gravity.CENTER,
                com.tokopedia.unifyprinciples.R.color.Unify_G200,
                getStrokeWidthSenderDimenRes()
            )
    }

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
        bundlingName = findViewById(R.id.tv_product_bundling_name)
        originalPrice = findViewById(R.id.tv_original_price)
        totalDiscount = findViewById(R.id.tv_total_discount)
        bundlePrice = findViewById(R.id.tv_bundle_price)
        button = findViewById(R.id.button_open_package)
        recyclerView = findViewById(R.id.rv_product_bundle)
        loader = findViewById(R.id.loader_product_bundling)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = MeasureSpec.getSize(widthMeasureSpec) * widthMultiplier
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }

    fun bindData(
        element: ProductBundlingUiModel,
        adapterPosition: Int,
        listener: ProductBundlingListener,
        adapterListener: AdapterListener,
        deferredAttachment: DeferredViewHolderAttachment,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
    ) {
        this.adapterPosition = adapterPosition
        bindListener(listener, adapterListener, deferredAttachment, searchListener, commonListener)
        bindLayoutGravity(element)
        bindBackground(element)
        bindMargin(element)
        bindSyncProductBundling(element)
        bindIsLoading(element)
        if (!element.isLoading && !element.isError) {
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
            bindImage(element.productBundling.bundleItem.first())
            bindLabel(element.productBundling.bundleItem.first())
            bindBundlingName(element.productBundling.bundleItem.first())
            hideRecyclerView()
        }
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

    private fun initRecyclerView(element: ProductBundlingUiModel) {
        recyclerView?.let {
            it.adapter = adapter
            bindBundleItemList(element.productBundling.bundleItem)
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration()
        }
    }

    private fun bindBundleItemList(list : List<BundleItem>) {
        adapter.bundlingList = list
    }

    private fun addItemDecoration() {
        val counter = recyclerView?.itemDecorationCount ?: 0
        for (i in 0 until counter) {
            recyclerView?.removeItemDecorationAt(i)
        }
        recyclerView?.addItemDecoration(itemDecoration)
    }

    private fun bindListener(
        listener: ProductBundlingListener,
        adapterListener: AdapterListener,
        deferredAttachment: DeferredViewHolderAttachment,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
    ) {
        this.listener = listener
        this.adapterListener = adapterListener
        this.deferredAttachment = deferredAttachment
        this.searchListener = searchListener
        this.commonListener = commonListener
    }

    private fun bindLayoutGravity(element: ProductBundlingUiModel) {
        if (element.isSender) {
            gravityRight()
        } else {
            gravityLeft()
        }
    }

    private fun bindIsLoading(element: ProductBundlingUiModel) {
        if (element.isLoading) {
            loader?.show()
        } else {
            loader?.hide()
        }
    }

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

    private fun bindImage(item: BundleItem) {
        image?.setImageUrl(item.imageUrl)
    }

    private fun bindLabel(item: BundleItem) {
        label?.text = "Paket isi ${item.quantity}"
    }

    private fun bindBundlingName(item: BundleItem) {
        val query = searchListener?.getSearchQuery() ?: ""
        val spanText = SpannableString(item.name)
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
        bundlingName?.text = spanText
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
            if (commonListener?.isSeller() == true) {
                it.hide()
            } else {
                bindButton(it, element)
                it.show()
            }
        }
    }

    private fun bindButton(button: UnifyButton, element: ProductBundlingUiModel) {
        button.text = element.productBundling.buttonText
        button.buttonType = UnifyButton.Type.MAIN
        when(element.productBundling.bundleStatus) {
            STATUS_ACTIVE -> bindActiveButton(button, element)
            STATUS_UPCOMING -> bindActiveButton(button, element)
            else -> bindDisabledButton(button)
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
            bgSender
        } else {
            bgOpposite
        }
    }

    private fun gravityRight() {
        setLayoutGravity(Gravity.END)
    }

    private fun gravityLeft() {
        setLayoutGravity(Gravity.START)
    }

    private fun setLayoutGravity(@Slide.GravityFlag gravity: Int) {
        (layoutParams as? LinearLayout.LayoutParams)?.apply {
            this.gravity = gravity
        }
    }

    companion object {
        private const val SPACE = 15
        private const val DEFAULT_WIDTH_MULTIPLIER = 0.75f
        private val LAYOUT = R.layout.item_topchat_product_bundling_card
    }
}