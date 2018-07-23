package com.tokopedia.product.edit.view.adapter

import com.tokopedia.product.edit.view.viewmodel.*

interface ProductAddVideoTypeFactory {

    fun type(sectionVideoRecommendationViewModel: SectionVideoRecommendationViewModel): Int

    fun type(videoViewModel: VideoViewModel): Int

    fun type(emptyVideoViewModel: EmptyVideoViewModel): Int

    fun type(titleVideoChoosenViewModel: TitleVideoChosenViewModel): Int

}