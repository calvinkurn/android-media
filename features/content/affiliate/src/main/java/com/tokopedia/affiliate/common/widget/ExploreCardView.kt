package com.tokopedia.affiliate.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreCardViewModel
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.widget_af_explore_card.view.*

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreCardView : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_af_explore_card, this)
    }

    fun bind(element: ExploreCardViewModel) {
        image.loadImage(element.imageUrl)
        title.shouldShowWithAction(!TextUtils.isEmpty(element.title)) {
            title.text = element.title
        }
        subtitle.shouldShowWithAction(!TextUtils.isEmpty(element.subtitle)) {
            subtitle.text = element.subtitle
        }
        commission.shouldShowWithAction(!TextUtils.isEmpty(element.commission)) {
            commission.text = element.commission
        }
    }

    fun clearImage() {
        image.clearImage()
    }
}