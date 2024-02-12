package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.experiments.ReimagineListViewStrategy
import com.tokopedia.video_widget.VideoPlayerController

class ProductCardListView: ConstraintLayout {

    private val strategy = ReimagineListViewStrategy(this)

    val video: VideoPlayerController
        get() = strategy.getVideoPlayerController()

    val additionalMarginStart: Int
        get() = strategy.additionalMarginStart()

    constructor(context: Context) : super(context) {
        strategy.init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        strategy.init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        strategy.init(context, attrs, defStyleAttr)
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        strategy.setProductModel(productCardModel)
    }

    fun addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
        strategy.setImageProductViewHintListener(holder, object: ViewHintListener {
            override fun onViewHint() { onView() }
        })
    }

    fun setThreeDotsClickListener(onClickListener: OnClickListener) {
        strategy.setThreeDotsOnClickListener(onClickListener)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        strategy.setOnClickListener(l)
    }

    fun setAddToCartOnClickListener(onClickListener: OnClickListener) {
        strategy.setAddToCartOnClickListener(onClickListener)
    }

    fun recycle() {
        strategy.recycle()
    }
}
