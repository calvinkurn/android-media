package com.tokopedia.feedplus.view.viewmodel.onboarding

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory

/**
 * @author by yoasfs on 2019-08-30
 */
data class OnboardingDataViewModel (
         val id: Int = 0,
         val name: String = "",
         val image: String = "",
         var isSelected: Boolean = false,
         val isLihatSemuaItem: Boolean = false
) {
    companion object {
        val defaultLihatSemuaText: String = "Lihat Semua"
    }
}