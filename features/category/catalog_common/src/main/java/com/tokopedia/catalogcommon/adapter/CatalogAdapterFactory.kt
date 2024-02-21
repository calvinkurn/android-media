package com.tokopedia.catalogcommon.adapter

import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BlankUiModel
import com.tokopedia.catalogcommon.uimodel.CharacteristicUiModel
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.DoubleBannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.uimodel.VideoUiModel
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel

interface CatalogAdapterFactory {
    fun type(uiModel: TopFeaturesUiModel): Int
    fun type(uiModel: HeroBannerUiModel): Int
    fun type(uiModel: BlankUiModel): Int
    fun type(uiModel: SliderImageTextUiModel): Int
    fun type(uiModel: StickyNavigationUiModel): Int

    fun type(uiModel: PanelImageUiModel): Int
    fun type(uiModel: AccordionInformationUiModel): Int
    fun type(uiModel: TrustMakerUiModel): Int
    fun type(uiModel: TextDescriptionUiModel): Int
    fun type(uiModel: ExpertReviewUiModel): Int
    fun type(uiModel: BannerCatalogUiModel): Int
    fun type(uiModel: DoubleBannerCatalogUiModel): Int
    fun type(uiModel: CharacteristicUiModel): Int
    fun type(uiModel: SupportFeaturesUiModel): Int

    fun type(uiModel: ComparisonUiModel): Int
    fun type(uiModel: VideoUiModel): Int
    fun type(uiModel: ColumnedInfoUiModel): Int

    fun type(uiModel: BuyerReviewUiModel): Int

}
