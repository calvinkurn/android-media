package com.tokopedia.catalogcommon.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.listener.AccordionListener
import com.tokopedia.catalogcommon.listener.BannerListener
import com.tokopedia.catalogcommon.listener.CharacteristicListener
import com.tokopedia.catalogcommon.listener.ColumnedInfoListener
import com.tokopedia.catalogcommon.listener.DoubleBannerListener
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.listener.PanelImageListener
import com.tokopedia.catalogcommon.listener.SliderImageTextListener
import com.tokopedia.catalogcommon.listener.SupportFeatureListener
import com.tokopedia.catalogcommon.listener.TextDescriptionListener
import com.tokopedia.catalogcommon.listener.TopFeatureListener
import com.tokopedia.catalogcommon.listener.TrustMakerListener
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.listener.VideoListener
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
import com.tokopedia.catalogcommon.viewholder.AccordionInformationViewHolder
import com.tokopedia.catalogcommon.viewholder.BannerViewHolder
import com.tokopedia.catalogcommon.viewholder.BlankViewHolder
import com.tokopedia.catalogcommon.viewholder.CharacteristicViewHolder
import com.tokopedia.catalogcommon.viewholder.ColumnedInfoViewHolder
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.catalogcommon.viewholder.DoubleBannerViewHolder
import com.tokopedia.catalogcommon.viewholder.ExpertReviewViewHolder
import com.tokopedia.catalogcommon.viewholder.HeroBannerViewHolder
import com.tokopedia.catalogcommon.viewholder.PanelImageViewHolder
import com.tokopedia.catalogcommon.viewholder.SliderImageTextViewHolder
import com.tokopedia.catalogcommon.viewholder.StickyNavigationListener
import com.tokopedia.catalogcommon.viewholder.StickyTabNavigationViewHolder
import com.tokopedia.catalogcommon.viewholder.SupportFeatureViewHolder
import com.tokopedia.catalogcommon.viewholder.TextDescriptionViewHolder
import com.tokopedia.catalogcommon.viewholder.TopFeatureViewHolder
import com.tokopedia.catalogcommon.viewholder.TrustmakerViewHolder
import com.tokopedia.catalogcommon.viewholder.VideoViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.catalogcommon.viewholder.BuyerReviewViewHolder

class CatalogAdapterFactoryImpl(
    private val heroBannerListener: HeroBannerListener? = null,
    private val navListener: StickyNavigationListener? = null,
    private val accordionListener: AccordionListener? = null,
    private val bannerListener: BannerListener? = null,
    private val textDescriptionListener: TextDescriptionListener? = null,
    private val trustMakerListener: TrustMakerListener? = null,
    private val videoExpertListener: VideoExpertListener? = null,
    private val topFeatureListener: TopFeatureListener? = null,
    private val doubleBannerListener: DoubleBannerListener? = null,
    private val comparisonItemListener: ComparisonViewHolder.ComparisonItemListener? = null,
    private val videoListener: VideoListener? = null,
    private val columnedInfoListener: ColumnedInfoListener? = null,
    private val isDisplayingTopSpec: Boolean = true,
    private val buyerReviewListener: BuyerReviewViewHolder.BuyerReviewListener? = null,
    private val supportFeatureListener: SupportFeatureListener? = null,
    private val imageTextListener: SliderImageTextListener? = null,
    private val characteristicListener: CharacteristicListener? = null,
    private val panelImageListener: PanelImageListener? = null
) : BaseAdapterTypeFactory(), HomeComponentTypeFactory, CatalogAdapterFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopFeatureViewHolder.LAYOUT -> TopFeatureViewHolder(view, topFeatureListener)
            CharacteristicViewHolder.LAYOUT -> CharacteristicViewHolder(view, characteristicListener)
            HeroBannerViewHolder.LAYOUT -> HeroBannerViewHolder(view, heroBannerListener)
            StickyTabNavigationViewHolder.LAYOUT -> StickyTabNavigationViewHolder(view, navListener)
            SliderImageTextViewHolder.LAYOUT -> SliderImageTextViewHolder(view,imageTextListener)
            PanelImageViewHolder.LAYOUT -> PanelImageViewHolder(view, panelImageListener)
            TrustmakerViewHolder.LAYOUT -> TrustmakerViewHolder(view, trustMakerListener)
            BannerRevampViewHolder.LAYOUT -> BannerRevampViewHolder(view, null)
            AccordionInformationViewHolder.LAYOUT -> AccordionInformationViewHolder(view, accordionListener)
            TextDescriptionViewHolder.LAYOUT -> TextDescriptionViewHolder(view, textDescriptionListener)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view, bannerListener)
            DoubleBannerViewHolder.LAYOUT -> DoubleBannerViewHolder(view, doubleBannerListener)
            ExpertReviewViewHolder.LAYOUT -> ExpertReviewViewHolder(view, videoExpertListener)
            SupportFeatureViewHolder.LAYOUT -> SupportFeatureViewHolder(view, supportFeatureListener)
            ComparisonViewHolder.LAYOUT -> ComparisonViewHolder(view, comparisonItemListener, isDisplayingTopSpec)
            VideoViewHolder.LAYOUT -> VideoViewHolder(view, videoListener)
            ColumnedInfoViewHolder.LAYOUT -> ColumnedInfoViewHolder(view, columnedInfoListener)
            BuyerReviewViewHolder.LAYOUT -> BuyerReviewViewHolder(view, buyerReviewListener)
            BlankViewHolder.LAYOUT -> BlankViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

    override fun type(uiModel: TopFeaturesUiModel): Int {
        return TopFeatureViewHolder.LAYOUT
    }

    override fun type(uiModel: HeroBannerUiModel): Int {
        return HeroBannerViewHolder.LAYOUT
    }

    override fun type(uiModel: BlankUiModel): Int {
        return BlankViewHolder.LAYOUT
    }

    override fun type(uiModel: SliderImageTextUiModel): Int {
        return SliderImageTextViewHolder.LAYOUT
    }

    override fun type(uiModel: PanelImageUiModel): Int {
        return PanelImageViewHolder.LAYOUT
    }

    override fun type(uiModel: StickyNavigationUiModel): Int {
        return StickyTabNavigationViewHolder.LAYOUT
    }

    override fun type(uiModel: AccordionInformationUiModel): Int {
        return AccordionInformationViewHolder.LAYOUT
    }

    override fun type(uiModel: TrustMakerUiModel): Int {
        return TrustmakerViewHolder.LAYOUT
    }

    override fun type(uiModel: TextDescriptionUiModel): Int {
        return TextDescriptionViewHolder.LAYOUT
    }

    override fun type(uiModel: ExpertReviewUiModel): Int {
        return ExpertReviewViewHolder.LAYOUT
    }

    override fun type(uiModel: BannerCatalogUiModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(uiModel: DoubleBannerCatalogUiModel): Int {
        return DoubleBannerViewHolder.LAYOUT
    }

    override fun type(uiModel: CharacteristicUiModel): Int {
        return CharacteristicViewHolder.LAYOUT
    }

    override fun type(uiModel: SupportFeaturesUiModel): Int {
        return SupportFeatureViewHolder.LAYOUT
    }

    override fun type(uiModel: ComparisonUiModel): Int {
        return ComparisonViewHolder.LAYOUT
    }

    override fun type(uiModel: VideoUiModel): Int {
        return VideoViewHolder.LAYOUT
    }

    override fun type(uiModel: ColumnedInfoUiModel): Int {
        return ColumnedInfoViewHolder.LAYOUT
    }

    override fun type(uiModel: BuyerReviewUiModel): Int {
        return BuyerReviewViewHolder.LAYOUT
    }
}
