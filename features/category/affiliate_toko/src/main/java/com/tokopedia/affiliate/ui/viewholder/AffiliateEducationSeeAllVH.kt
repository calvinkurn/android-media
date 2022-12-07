package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.AffiliateAnalytics.ActionKeys
import com.tokopedia.affiliate.AffiliateAnalytics.CategoryKeys
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE_TOPIC
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.PATTERN
import com.tokopedia.affiliate.YYYY_MM_DD_HH_MM_SS
import com.tokopedia.affiliate.interfaces.AffiliateEducationSeeAllCardClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate_toko.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationSeeAllVH(
    itemView: View,
    private val seeAllCardClickInterface: AffiliateEducationSeeAllCardClickInterface?
) : AbstractViewHolder<AffiliateEducationSeeAllUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_see_all_item
    }

    private val imageSeeAll = itemView.findViewById<ImageUnify>(R.id.image_see_all_result)
    private val seeAllTitle = itemView.findViewById<Typography>(R.id.see_all_item_title)
    private val seeAllDetail = itemView.findViewById<Typography>(R.id.see_all_item_detail)
    private val seeAllContainer = itemView.findViewById<View>(R.id.see_all_container)

    override fun bind(element: AffiliateEducationSeeAllUiModel?) {
        imageSeeAll.loadImage(element?.article?.thumbnail?.android)
        seeAllTitle.text = element?.article?.title
        seeAllDetail.text =
            when (element?.pageType) {
                PAGE_EDUCATION_EVENT -> {
                    itemView.context.getString(
                        R.string.see_all_event_widget_detail,
                        element.article?.categories?.get(0)?.title,
                        element.article?.description.orEmpty()
                    )
                }
                else -> {
                    val readMinute = itemView.context.getString(
                        R.string.article_widget_detail_read,
                        element?.article?.attributes?.readTime
                    )
                    itemView.context.getString(
                        R.string.article_widget_detail,
                        element?.article?.categories?.get(0)?.title,
                        DateUtils().formatDate(
                            currentFormat = YYYY_MM_DD_HH_MM_SS,
                            newFormat = PATTERN,
                            dateString = element?.article?.modifiedDate.orEmpty()
                        ),
                        readMinute
                    )
                }
            }
        seeAllContainer?.setOnClickListener {
            seeAllCardClickInterface?.onCardClick(
                element?.pageType.orEmpty(),
                element?.article?.slug.orEmpty()
            )
            when (element?.pageType) {
                PAGE_EDUCATION_EVENT -> sendEducationClickEvent(
                    element.article?.title,
                    element.article?.articleId.toString(),
                    ActionKeys.CLICK_EVENT_CARD,
                    CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_EVENT
                )
                PAGE_EDUCATION_ARTICLE, PAGE_EDUCATION_ARTICLE_TOPIC -> sendEducationClickEvent(
                    element.article?.title,
                    element.article?.articleId.toString(),
                    ActionKeys.CLICK_ARTICLE_CARD,
                    CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_ARTICLE
                )
                PAGE_EDUCATION_TUTORIAL -> sendEducationClickEvent(
                    element.article?.title,
                    element.article?.articleId.toString(),
                    ActionKeys.CLICK_TUTORIAL_CARD,
                    CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_TUTORIAL
                )
            }
        }
    }

    private fun sendEducationClickEvent(
        creativeName: String?,
        eventId: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            UserSession(itemView.context).userId,
            creativeName
        )
    }
}
