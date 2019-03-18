package com.tokopedia.affiliate.feature.explore.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel
import com.tokopedia.affiliate.common.viewmodel.ExploreTitleViewModel
import com.tokopedia.affiliate.feature.explore.SECTION_ANNOUNCEMENT
import com.tokopedia.affiliate.feature.explore.SECTION_RECOMMENDATION
import com.tokopedia.affiliate.feature.explore.data.pojo.section.ExplorePageSection
import com.tokopedia.affiliate.feature.explore.data.pojo.section.ExploreSectionResponse
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerChildViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.RecommendationViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreSectionMapper @Inject constructor() : Func1<GraphqlResponse, List<Visitable<*>>> {
    override fun call(t: GraphqlResponse?): List<Visitable<*>> {
        val response: ExploreSectionResponse = t!!.getData(ExploreSectionResponse::class.java)

        val sections: MutableList<Visitable<*>> = arrayListOf()
        response.exploreSections.explorePageSection.forEach {
            when (it.type) {
                SECTION_ANNOUNCEMENT -> sections.add(mapAnnouncement(it))
                SECTION_RECOMMENDATION -> sections.add(mapRecommendation(it))
            }
        }
        return sections
    }

    private fun mapAnnouncement(section: ExplorePageSection): ExploreBannerViewModel {
        val banners: MutableList<ExploreBannerChildViewModel> = arrayListOf()
        section.items.forEach {
            banners.add(ExploreBannerChildViewModel(it.image, it.appLink))
        }
        return ExploreBannerViewModel(banners)
    }

    private fun mapRecommendation(section: ExplorePageSection): RecommendationViewModel {
        val recommendationList: MutableList<ExploreCardViewModel> = arrayListOf()
        section.items.forEach {
            recommendationList.add(ExploreCardViewModel(
                    it.title,
                    it.subtitle,
                    it.commissionValueDisplay,
                    it.image,
                    it.appLink,
                    it.adId.toString(),
                    it.productId.toString()
            ))
        }

        return RecommendationViewModel(
                recommendationList,
                ExploreTitleViewModel(section.title, section.subtitle)
        )
    }
}