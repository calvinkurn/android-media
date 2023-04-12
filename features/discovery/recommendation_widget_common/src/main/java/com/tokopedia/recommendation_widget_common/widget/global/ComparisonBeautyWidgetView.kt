package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.databinding.ViewBeautyComparisonWidgetBinding

/**
 * Created by frenzel on 27/03/23
 */
class ComparisonBeautyWidgetView : BaseRecomWidget {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        val LAYOUT = com.tokopedia.recommendation_widget_common.R.layout.view_beauty_comparison_widget
    }

    private var binding: ViewBeautyComparisonWidgetBinding? = null

    private var specOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null

    init {
        binding = ViewBeautyComparisonWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun bind(model: RecomVisitable) {
        binding?.run {

        }
    }
}
