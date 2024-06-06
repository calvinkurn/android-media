package com.tokopedia.recommendation_widget_common.infinite.foryou.content

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouContentCardBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class ContentCardViewHolder constructor(
    view: View,
    private val listener: Listener
) : BaseRecommendationViewHolder<ContentCardModel>(
    view,
    ContentCardModel::class.java
), AppLogRecTriggerInterface {

    private val binding: WidgetForYouContentCardBinding? by viewBinding()

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(element: ContentCardModel) {
        setRecTriggerObject(element)
        binding?.view?.setListener(object : ContentCardView.Listener {
            override fun onContentCardClicked(item: ContentCardModel) {
                listener.onContentCardClicked(item, bindingAdapterPosition)
            }

            override fun onContentCardImpressed(item: ContentCardModel) {
                listener.onContentCardImpressed(item, bindingAdapterPosition)
            }
        })

        binding?.view?.setupView(element)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.view?.removeListener()
    }

    private fun setRecTriggerObject(model: ContentCardModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
            listName = model.tabName,
            listNum = model.tabIndex,
        )
    }

    interface Listener {
        fun onContentCardImpressed(item: ContentCardModel, position: Int)
        fun onContentCardClicked(item: ContentCardModel, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_content_card
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
