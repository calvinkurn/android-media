package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils

class ProductFeedbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
) : LinearLayout(context, attrs,defStyleAttr) {

    var headerTitle:String?=""
    set(value) {
        headerTv?.text = value
        field = value
    }

    var description:String?=""
    set(value) {
        descTv?.text = value
        field = value
    }

    private var feedbackCtaListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener?=null

    private var cta:UnifyButton?=null

    private var headerTv:Typography?=null
    private var descTv:Typography?=null
    private var imageView:ImageUnify?=null

    init {
        inflate(context,layout,this)
        initViews()
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        headerTitle = resources.getString(R.string.tokopedianow_feedback_widget_title)
        description = resources.getString(R.string.tokopedianow_feedback_widget_desc)
    }

    private fun initViews(){
        headerTv = findViewById(R.id.feedback_widget_title)
        descTv = findViewById(R.id.feedback_widget_desc)
        cta = findViewById(R.id.feeback_widget_cta)
        imageView = findViewById(R.id.feeback_widget_img)
        cta?.setOnClickListener {
            feedbackCtaListener?.onFeedbackCtaClicked(it)
        }
        loadImage()
    }


    fun setupFeedbackListener(listener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener?){
        feedbackCtaListener = listener
    }

    fun loadImage(){
        imageView?.let {
            ImageUtils.loadImageWithoutPlaceholderAndError(it, FEEDBACK_WIDGET_IMAGE)
        }
    }

    private fun setupPadding(){
        val topPadding = dpToPx(12).toInt()
        val commonPadding = dpToPx(16).toInt()
        setPadding(commonPadding,topPadding,commonPadding,commonPadding)
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    companion object{
        @LayoutRes
        private val layout = R.layout.feedback_tokonow_widget
        private const val FEEDBACK_WIDGET_IMAGE = "https://images.tokopedia.net/img/tokopedianow/feedback_loop_illustration.png"
    }
}
