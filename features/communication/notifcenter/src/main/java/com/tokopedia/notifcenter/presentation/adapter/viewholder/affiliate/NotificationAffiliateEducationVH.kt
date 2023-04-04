package com.tokopedia.notifcenter.presentation.adapter.viewholder.affiliate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.updatePaddingRelative
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationAffiliateAnalytics
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.toDate
import timber.log.Timber
import java.text.ParseException
import java.util.*

class NotificationAffiliateEducationVH(
    itemView: View?
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
        private const val EDUCATION_ARTICLE_DETAIL_STAGING_URL =
            "https://affiliate-staging.tokopedia.com/edu/"
        private const val EDUCATION_ARTICLE_DETAIL_PROD_URL = "https://affiliate.tokopedia.com/edu/"
    }

    private val carousel = itemView?.findViewById<CarouselUnify>(R.id.education_carousel)
    private val seeMore = itemView?.findViewById<Typography>(R.id.tv_education_see_more)
    private val userSession = UserSession(itemView?.context)

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
                                NotificationAffiliateAnalytics.trackAffiliateEducationClick(
                                    userSession.userId
                                )
                                RouteManager.route(ctx, getArticleEventUrl(data.slug.toString()))
                            }
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
                        NotificationAffiliateAnalytics.trackAffiliateEducationImpression(
                            it,
                            current,
                            userSession.userId
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
                itemView.context?.let { ctx ->
                    NotificationAffiliateAnalytics.trackAffiliateEducationSeeMoreClick(userSession.userId)
                    RouteManager.route(ctx, ApplinkConst.AFFILIATE_TOKO_EDU_PAGE)
                }
            }
        }
    }

    private fun getArticleEventUrl(slug: String): String {
        return String.format(
            Locale.getDefault(),
            "%s?url=%s%s?navigation=hide",
            ApplinkConst.WEBVIEW,
            if (GraphqlUrl.BASE_URL.contains("staging")) {
                EDUCATION_ARTICLE_DETAIL_STAGING_URL
            } else {
                EDUCATION_ARTICLE_DETAIL_PROD_URL
            },
            slug
        )
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
