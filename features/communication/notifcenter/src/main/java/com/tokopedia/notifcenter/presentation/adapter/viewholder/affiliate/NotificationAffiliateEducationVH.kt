package com.tokopedia.notifcenter.presentation.adapter.viewholder.affiliate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.postDelayed
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class NotificationAffiliateEducationVH(
    itemView: View?
) :
    AbstractViewHolder<NotificationAffiliateEducationUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.item_affiliate_education_carousel

        private const val SLIDE_TO_SHOW = 1.2f
        private const val THREE = 3
        private const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        private const val DD_MMM = "dd MMM"
        private const val DD_MMM_YY = "dd MMM yy"
    }

    private val carousel = itemView?.findViewById<CarouselUnify>(R.id.education_carousel)
    private val seeMore = itemView?.findViewById<Typography>(R.id.tv_education_see_more)

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
                                getFormattedDate(article.modifiedDate, YYYY_MM_DD_HH_MM_SS, DD_MMM),
                                getFormattedDate(
                                    article.publishTime,
                                    YYYY_MM_DD_HH_MM_SS,
                                    DD_MMM_YY
                                )
                            )
                        view.findViewById<View>(R.id.article_widget_container)?.setOnClickListener {
                            itemView.context?.let { ctx ->
                                RouteManager.route(ctx, article.youtubeUrl)
                            }
                        }
                    }
                }
            }
            shouldSmoothScroll = true
            slideToShow = SLIDE_TO_SHOW
            autoplay = false
            infinite = false
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            seeMore?.setOnClickListener {
                itemView.context?.let { ctx ->
                    RouteManager.route(ctx, ApplinkConst.AFFILIATE_TOKO_EDU_PAGE)
                }
            }
        }
        carousel?.postDelayed(TimeUnit.SECONDS.toMillis(THREE.toLong())) {
            carousel.activeIndex = THREE
        }
    }

    private fun getFormattedDate(
        input: String?,
        inputPattern: String,
        outputPattern: String
    ): String? {
        val parsedDate =
            LocalDate.parse(input, DateTimeFormatter.ofPattern(inputPattern))
        return parsedDate.format(DateTimeFormatter.ofPattern(outputPattern))
    }
}
