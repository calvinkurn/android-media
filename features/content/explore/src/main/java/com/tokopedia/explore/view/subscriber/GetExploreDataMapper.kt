package com.tokopedia.explore.view.subscriber

import com.tokopedia.explore.domain.entity.Category
import com.tokopedia.explore.domain.entity.Content
import com.tokopedia.explore.domain.entity.PostKol
import com.tokopedia.explore.domain.entity.Tracking
import com.tokopedia.explore.view.type.ExploreCardType
import com.tokopedia.explore.view.type.ExploreCardType.Companion.getCardTypeByString
import com.tokopedia.explore.view.uimodel.ExploreCategoryViewModel
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import java.util.*

/**
 * @author by milhamj on 23/07/18.
 */
object GetExploreDataMapper {
    fun convertToKolPostViewModelList(postKolList: List<PostKol>): MutableList<ExploreImageViewModel> {
        val kolPostViewModelList: MutableList<ExploreImageViewModel> = ArrayList()
        var i = 0
        for (postKol in postKolList) {
            if (postKol.content.isNotEmpty()) {
                kolPostViewModelList.add(convertToKolPostViewModel(postKol, i))
                i++
            }
        }
        return kolPostViewModelList
    }

    private fun convertToKolPostViewModel(postKol: PostKol, pos: Int): ExploreImageViewModel {
        val content = getContent(postKol)
        return ExploreImageViewModel(
                postKol.id.toIntOrZero(),
                postKol.userName,
                getImageUrl(content),
                pos,
                checkType(postKol, content),
                convertToTrackingViewModel(postKol.tracking)
        )
    }

    private fun checkType(postKol: PostKol, content: Content?): ExploreCardType {
        return if (postKol.content.size > 1) {
            ExploreCardType.Multi
        } else {
            getCardTypeByString(content?.type ?: "")
        }
    }

    private fun getContent(postKol: PostKol): Content? {
        return postKol.content.firstOrNull()
    }

    private fun getImageUrl(content: Content?): String {
        return content?.imageurl ?: ""
    }

    fun convertToCategoryViewModelList(categoryList: List<Category>): MutableList<ExploreCategoryViewModel> {
        val categoryViewModelList: MutableList<ExploreCategoryViewModel> = ArrayList()
        for (category in categoryList) {
            categoryViewModelList.add(convertToCategoryViewModel(category))
        }
        return categoryViewModelList
    }

    private fun convertToCategoryViewModel(category: Category): ExploreCategoryViewModel {
        return ExploreCategoryViewModel(
                category.id.toIntOrZero(),
                category.name
        )
    }

    private fun convertToTrackingViewModel(trackingList: List<Tracking>): List<TrackingViewModel> {
        val trackingViewModelList: MutableList<TrackingViewModel> = ArrayList()
        for ((clickURL, viewURL, type, source, viewType, recomID) in trackingList) {
            trackingViewModelList.add(
                    TrackingViewModel(
                            clickURL,
                            viewURL,
                            type,
                            source,
                            viewType,
                            recomID
                    )
            )
        }
        return trackingViewModelList
    }

}