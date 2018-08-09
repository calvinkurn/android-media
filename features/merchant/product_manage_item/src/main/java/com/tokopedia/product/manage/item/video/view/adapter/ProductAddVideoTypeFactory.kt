package com.tokopedia.product.manage.item.video.view.adapter

import com.tokopedia.product.manage.item.video.view.model.EmptyVideoViewModel
import com.tokopedia.product.manage.item.video.view.model.SectionVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.TitleVideoChosenViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoViewModel

interface ProductAddVideoTypeFactory {

    fun type(sectionVideoRecommendationViewModel: SectionVideoRecommendationViewModel): Int

    fun type(videoViewModel: VideoViewModel): Int

    fun type(emptyVideoViewModel: EmptyVideoViewModel): Int

    fun type(titleVideoChoosenViewModel: TitleVideoChosenViewModel): Int

}