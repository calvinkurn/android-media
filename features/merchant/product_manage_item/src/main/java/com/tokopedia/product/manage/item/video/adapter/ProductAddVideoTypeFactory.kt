package com.tokopedia.product.manage.item.view.adapter

import com.tokopedia.product.manage.item.view.viewmodel.*

interface ProductAddVideoTypeFactory {

    fun type(sectionVideoRecommendationViewModel: SectionVideoRecommendationViewModel): Int

    fun type(videoViewModel: VideoViewModel): Int

    fun type(emptyVideoViewModel: EmptyVideoViewModel): Int

    fun type(titleVideoChoosenViewModel: TitleVideoChosenViewModel): Int

}