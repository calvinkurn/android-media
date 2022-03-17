package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.SingleProductBundlingUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class SingleProductBundlingAttachmentContainer : ConstraintLayout {

    private var image: ImageUnify? = null
    private var label: Label? = null
    private var bundlingName: Typography? = null
    private var originalPrice: Typography? = null
    private var totalDiscount: Typography? = null
    private var bundlePrice: Typography? = null
    private var button: UnifyButton? = null

    private var listener: ProductBundlingListener? = null
    private var adapterListener: AdapterListener? = null

    private var adapterPosition: Int = RecyclerView.NO_POSITION

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

    private fun initLayoutView() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBindView() {
        image = findViewById(R.id.iv_single_product_thumbnail)
        label = findViewById(R.id.label_package)
        bundlingName = findViewById(R.id.tv_product_bundling_name)
        originalPrice = findViewById(R.id.tv_original_price)
        totalDiscount = findViewById(R.id.tv_total_discount)
        bundlePrice = findViewById(R.id.tv_bundle_price)
        button = findViewById(R.id.button_open_package)
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
        element: SingleProductBundlingUiModel,
        adapterPosition: Int,
        listener: ProductBundlingListener,
        adapterListener: AdapterListener
    ) {
        this.adapterPosition = adapterPosition
        bindListener(listener, adapterListener)
        bindLayoutGravity(element)
//        if (element.isLoading && !element.isError) {
//            bindIsLoading(product)
//        } else {
        bindBackground(element)
        bindMargin(element)
        bindImage(element.productBundling.bundleItem.first())
        bindLabel(element.productBundling.bundleItem.first())
        bindBundlingName(element.productBundling.bundleItem.first())
        bindPrice(element)
        bindCtaClick(element)
    }

    private fun bindListener(listener: ProductBundlingListener, adapterListener: AdapterListener) {
        this.listener = listener
        this.adapterListener = adapterListener
    }

    private fun bindLayoutGravity(element: SingleProductBundlingUiModel) {
        if (element.isSender) {
            gravityRight()
        } else {
            gravityLeft()
        }
    }

    private fun bindImage(item: BundleItem) {
        image?.setImageUrl(item.imageUrl)
    }

    private fun bindLabel(item: BundleItem) {
        label?.text = "Paket isi ${item.quantity}"
    }

    private fun bindBundlingName(item: BundleItem) {
        bundlingName?.text = item.name
    }

    private fun bindPrice(element: SingleProductBundlingUiModel) {
        originalPrice?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = element.productBundling.originalPrice
        }
        totalDiscount?.text = element.productBundling.totalDiscount
        bundlePrice?.text = element.productBundling.bundlePrice
    }

    private fun bindCtaClick(element: SingleProductBundlingUiModel) {
        button?.let {
            it.text = element.productBundling.buttonText
            it.setOnClickListener {
                listener?.onClickCtaProductBundling(element)
            }
        }
    }

    //TODO: Implement search listener

    private fun bindMargin(singleProduct: SingleProductBundlingUiModel) {
        val lp = layoutParams
        if (lp is LinearLayout.LayoutParams) {
            if (isNextItemOppositeFrom(singleProduct)) {
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

    private fun isNextItemOppositeFrom(singleProduct: SingleProductBundlingUiModel): Boolean {
        return adapterListener?.isOpposite(adapterPosition, singleProduct.isSender) == true
    }

    private fun bindBackground(element: SingleProductBundlingUiModel) {
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
        private const val DEFAULT_WIDTH_MULTIPLIER = 0.75f
        private val LAYOUT = R.layout.item_topchat_single_product_bundling_card
    }
}