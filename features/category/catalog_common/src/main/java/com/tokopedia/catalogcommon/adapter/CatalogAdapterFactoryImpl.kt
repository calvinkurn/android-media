package com.tokopedia.catalogcommon.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.listener.AccordionListener
import com.tokopedia.catalogcommon.listener.BannerListener
import com.tokopedia.catalogcommon.listener.DoubleBannerListener
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.listener.TextDescriptionListener
import com.tokopedia.catalogcommon.listener.TopFeatureListener
import com.tokopedia.catalogcommon.listener.TrustMakerListener
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BlankUiModel
import com.tokopedia.catalogcommon.uimodel.CharacteristicUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.DoubleBannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.viewholder.AccordionInformationViewHolder
import com.tokopedia.catalogcommon.viewholder.BannerViewHolder
import com.tokopedia.catalogcommon.viewholder.BlankViewHolder
import com.tokopedia.catalogcommon.viewholder.CharacteristicViewHolder
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.catalogcommon.viewholder.DoubleBannerViewHolder
import com.tokopedia.catalogcommon.viewholder.DummyViewHolder
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
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder

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
) : BaseAdapterTypeFactory(), HomeComponentTypeFactory, CatalogAdapterFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopFeatureViewHolder.LAYOUT -> TopFeatureViewHolder(view,topFeatureListener)
            CharacteristicViewHolder.LAYOUT -> CharacteristicViewHolder(view)
            HeroBannerViewHolder.LAYOUT -> HeroBannerViewHolder(view, heroBannerListener)
            DummyViewHolder.LAYOUT -> DummyViewHolder(view)
            StickyTabNavigationViewHolder.LAYOUT -> StickyTabNavigationViewHolder(view, navListener)
            SliderImageTextViewHolder.LAYOUT -> SliderImageTextViewHolder(view)
            PanelImageViewHolder.LAYOUT -> PanelImageViewHolder(view)
            TrustmakerViewHolder.LAYOUT -> TrustmakerViewHolder(view,trustMakerListener)
            BannerRevampViewHolder.LAYOUT -> BannerRevampViewHolder(view, null)
            AccordionInformationViewHolder.LAYOUT -> AccordionInformationViewHolder(view, accordionListener)
            TextDescriptionViewHolder.LAYOUT -> TextDescriptionViewHolder(view, textDescriptionListener)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view,bannerListener)
            DoubleBannerViewHolder.LAYOUT -> DoubleBannerViewHolder(view,doubleBannerListener)
            ExpertReviewViewHolder.LAYOUT -> ExpertReviewViewHolder(view, videoExpertListener)
            SupportFeatureViewHolder.LAYOUT -> SupportFeatureViewHolder(view)
            ComparisonViewHolder.LAYOUT -> ComparisonViewHolder(view, comparisonItemListener)
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

    override fun type(uiModel: DummyUiModel): Int {
        return DummyViewHolder.LAYOUT
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
}
