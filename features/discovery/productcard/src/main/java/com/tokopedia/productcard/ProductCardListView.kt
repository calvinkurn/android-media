package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Space
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.unifyprinciples.Typography
import kotlin.LazyThreadSafetyMode.NONE

class ProductCardListView: BaseProductCardListView {
    override val buttonAddVariant: UnifyButton? by lazy {
        findViewById(R.id.buttonAddVariant)
    }

    override val buttonNotify: UnifyButton? by lazy {
        findViewById(R.id.buttonNotify)
    }

    override val buttonAddToCartWishlist: UnifyButton? by lazy {
        findViewById(R.id.buttonAddToCartWishlist)
    }

    override val buttonSeeSimilarProductWishlist: UnifyButton? by lazy {
        findViewById(R.id.buttonSeeSimilarProductWishlist)
    }

    override val buttonAddToCart: UnifyButton? by lazy {
        findViewById(R.id.buttonAddToCart)
    }

    override val buttonDeleteProduct: UnifyButton? by lazy {
        findViewById(R.id.buttonDeleteProduct)
    }

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_list_layout, this)
    }
}
