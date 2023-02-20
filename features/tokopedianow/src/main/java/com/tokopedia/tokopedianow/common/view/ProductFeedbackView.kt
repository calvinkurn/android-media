package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.FeedbackTokonowWidgetBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.utils.image.ImageUtils

class ProductFeedbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
) : LinearLayout(context, attrs,defStyleAttr) {

    companion object{
        private const val FEEDBACK_WIDGET_IMAGE = "https://images.tokopedia.net/img/tokopedianow/feedback_loop_illustration.png"
    }

    var headerTitle:String?=""
    set(value) {
        binding?.feedbackWidgetTitle?.text = value
        field = value
    }

    var description:String?=""
    set(value) {
        binding?.feedbackWidgetDesc?.text = value
        field = value
    }

    private var feedbackCtaListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener?=null

    private var binding:FeedbackTokonowWidgetBinding? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = FeedbackTokonowWidgetBinding.inflate(inflater,this)
        initViews()
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        headerTitle = resources.getString(R.string.tokopedianow_feedback_widget_title)
        description = resources.getString(R.string.tokopedianow_feedback_widget_desc)
    }

    private fun initViews(){
        binding?.feebackWidgetCta?.setOnClickListener {
            feedbackCtaListener?.onFeedbackCtaClicked()
        }
        loadImage()
    }


    fun setupFeedbackListener(listener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener?){
        feedbackCtaListener = listener
    }

    fun loadImage(){
        binding?.feebackWidgetImg?.let {
            ImageUtils.loadImageWithoutPlaceholderAndError(it, FEEDBACK_WIDGET_IMAGE)
        }
    }
}
