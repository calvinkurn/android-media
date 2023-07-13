package com.tokopedia.notifcenter.view.adapter.viewholder.affiliate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.updatePaddingRelative
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.notifcenter.view.listener.NotificationAffiliateEduEventListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toDate
import timber.log.Timber
import java.text.ParseException

class NotificationAffiliateEducationViewHolder(
    itemView: View?,
    private val notificationAffiliateEduEventListener: NotificationAffiliateEduEventListener?
) :
    AbstractViewHolder<NotificationAffiliateEducationUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.item_affiliate_education_carousel

        private const val SLIDE_TO_SHOW = 1.1f
        private const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        private const val DD_MMM = "dd MMM"
        private const val DD_MMM_YY = "dd MMM yy"
        private const val ITEM_HORIZONTAL_PADDING = 12
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
                            notificationAffiliateEduEventListener?.onEducationItemClick(data)
                        }
                        view.updatePaddingRelative(
                            start = ITEM_HORIZONTAL_PADDING,
                            end = ITEM_HORIZONTAL_PADDING
                        )
                    }
                }
            }
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    element?.let {
                        notificationAffiliateEduEventListener?.onEducationActiveIndexChanged(
                            current,
                            it
                        )
                    }
                }
            }
            shouldSmoothScroll = true
            slideToShow = SLIDE_TO_SHOW
            autoplay = false
            infinite = false
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            seeMore?.setOnClickListener {
                notificationAffiliateEduEventListener?.onEducationLihatSemuaClick()
            }
        }
    }

    private fun getFormattedDate(
        input: String?,
        inputPattern: String,
        outputPattern: String
    ): String? {
        if (!input.isNullOrEmpty()) {
            return try {
                input.toDate(inputPattern).formatTo(outputPattern)
            } catch (e: ParseException) {
                Timber.e(e)
                ""
            }
        }
        return ""
    }
}
