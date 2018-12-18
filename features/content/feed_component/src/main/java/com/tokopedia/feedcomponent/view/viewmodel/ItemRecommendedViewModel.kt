package com.tokopedia.feedcomponent.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by yfsx on 04/12/18.
 */
data class ItemRecommendedViewModel(
        val title: String = "",
        val image1Url: String = "",
        val image2Url: String = "",
        val image3Url: String = "",
        val profileImageUrl: String = "",
        val badgeUrl: String = "",
        val profileName: String = "",
        val description: String = "",
        val btnText: String = "",
        val isFollowing:Boolean = false
) : Visitable<DynamicPostTypeFactory>{
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return 0
    }
}