package com.tokopedia.sellerhome.view.adapter

import com.tokopedia.sellerhome.view.model.*

/**
 * Created By @faisalramd on 2020-01-22
 */

interface SellerHomeTypeFactory {

    fun type(cardWidget: CardWidgetUiModel): Int

    fun type(lineGraphWidget: LineGraphWidgetUiModel): Int

    fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int

    fun type(descriptionWidget: DescriptionWidgetUiModel): Int

    fun type(sectionWidget: SectionWidgetUiModel): Int

    fun type(progressWidgetWidget: ProgressWidgetUiModel): Int

    fun type(postListWidget: PostListWidgetUiModel): Int
}
