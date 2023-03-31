package com.tokopedia.notifcenter.presentation.adapter.viewholder.affiliate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.postDelayed
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit

class NotificationAffiliateEducationVH(itemView: View?) :
    AbstractViewHolder<NotificationAffiliateEducationUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.item_affiliate_education_carousel

        private const val SLIDE_TO_SHOW = 1.2f
        private const val THREE = 3
    }

    private val carousel = itemView?.findViewById<CarouselUnify>(R.id.education_carousel)

    override fun bind(element: NotificationAffiliateEducationUiModel?) {
        setData(element)
    }

    private fun setData(element: NotificationAffiliateEducationUiModel?) {
        carousel?.apply {
            element?.data?.articles?.let {
                addItems(
                    R.layout.item_affiliate_education_carousel_item,
                    ArrayList(it.filterNotNull())
                ) { view, data ->
                    (data as? AffiliateEducationArticleResponse.CardsArticle.Data.CardsItem.Article)?.let { article ->
                        view.findViewById<ImageUnify>(R.id.image_article_widget)
                            ?.setImageUrl(article.thumbnail?.android.toString())
                        view.findViewById<Typography>(R.id.article_widget_item_title)?.text =
                            article.title
                        view.findViewById<Typography>(R.id.article_widget_item_detail)?.text =
                            view.context?.getString(
                                R.string.article_widget_detail,
                                article.categories?.get(0)?.title,
                                article.description,
                                article.modifiedDate
                            )
                    }
                }
            }
            shouldSmoothScroll = true
            slideToShow = SLIDE_TO_SHOW
            autoplay = false
            infinite = false
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            onItemClick = {
            }
        }
        carousel?.postDelayed(TimeUnit.SECONDS.toMillis(THREE.toLong())) {
            carousel.activeIndex = THREE
        }
    }
}
