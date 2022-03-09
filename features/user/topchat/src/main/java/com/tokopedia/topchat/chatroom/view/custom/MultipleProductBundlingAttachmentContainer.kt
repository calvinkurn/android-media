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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class MultipleProductBundlingAttachmentContainer : ConstraintLayout {

    private var regularPrice: Typography? = null
    private var textSave: Typography? = null
    private var savePrice: Typography? = null
    private var mainPrice: Typography? = null
    private var button: UnifyButton? = null

    private var listener: ProductBundlingListener? = null
    private var adapterListener: AdapterListener? = null
    private var adapterPosition: Int = RecyclerView.NO_POSITION

    private var recyclerView: RecyclerView? = null
    private val adapter = MultipleProductBundlingAdapter()

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
        initBundlingRecyclerView()
    }

    private fun initLayoutView() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBindView() {
        regularPrice = findViewById(R.id.tv_regular_price)
        textSave = findViewById(R.id.tv_save)
        savePrice = findViewById(R.id.tv_save_price)
        mainPrice = findViewById(R.id.tv_main_price)
        button = findViewById(R.id.button_open_package)
        recyclerView = findViewById(R.id.rv_product_bundle)
    }

    private fun initBundlingRecyclerView() {
        recyclerView?.let {
            it.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = adapter
            it.setHasFixedSize(true)
        }
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
        element: MultipleProductBundlingUiModel,
        adapterPosition: Int,
        listener: ProductBundlingListener,
        adapterListener: AdapterListener
    ) {
        this.adapterPosition = adapterPosition
        this.listener = listener
        this.adapterListener = adapterListener
        bindLayoutGravity(element)
//        if (element.isLoading && !element.isError) {
//            bindIsLoading(product)
//        } else {
        bindBackground(element)
        bindMargin(element)
        bindProductBundlingList(element)
        bindPrice(element)
        bindCtaClick(element)
    }

    private fun bindLayoutGravity(element: MultipleProductBundlingUiModel) {
        if (element.isSender) {
            gravityRight()
        } else {
            gravityLeft()
        }
    }

    private fun bindProductBundlingList(element: MultipleProductBundlingUiModel) {
        adapter.bundlingList = element.listBundling
    }

    private fun bindPrice(element: MultipleProductBundlingUiModel) {
        regularPrice?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            it.text = element.productPrice
        }
        savePrice?.text = element.discountAmount
        textSave?.text = element.discountText
        mainPrice?.text = element.discountPrice
    }

    private fun bindCtaClick(element: MultipleProductBundlingUiModel) {
        button?.setOnClickListener {
            listener?.onClickCtaMultipleProductBundling(element)
        }
    }

    //TODO: Implement search listener

    private fun bindMargin(product: MultipleProductBundlingUiModel) {
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

    private fun isNextItemOppositeFrom(product: MultipleProductBundlingUiModel): Boolean {
        return adapterListener?.isOpposite(adapterPosition, product.isSender) == true
    }

    private fun bindBackground(element: MultipleProductBundlingUiModel) {
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
        private val LAYOUT = R.layout.item_topchat_multiple_product_bundling_card
    }
}