package com.tokopedia.tokofood.feature.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.CategoryWidgetV2ViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCommonTypeFactory
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeChooseAddressViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeIconsViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeLoadingViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeEmptyStateLocationViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeMerchantTitleViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeTickerViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeUSPViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodProgressBarViewHolder
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeMerchantTitleUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodView

class TokoFoodHomeAdapterTypeFactory (
    private val tokoFoodView: TokoFoodView? = null,
    private val dynamicLegoBannerCallback: TokoFoodHomeLegoComponentCallback? = null,
    private val bannerComponentCallback: TokoFoodHomeBannerComponentCallback? = null,
    private val categoryWidgetCallback: TokoFoodHomeCategoryWidgetV2ComponentCallback? = null,
    private val uspListener: TokoFoodHomeUSPViewHolder.TokoFoodUSPListener? = null,
    private val chooseAddressWidgetListener: TokoFoodHomeChooseAddressViewHolder.TokoFoodChooseAddressWidgetListener? = null,
    private val emptyStateLocationListener: TokoFoodHomeEmptyStateLocationViewHolder.TokoFoodHomeEmptyStateLocationListener? = null,
    private val errorStateListener: TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener? = null,
    private val homeIconListener: TokoFoodHomeIconsViewHolder.TokoFoodHomeIconsListener? = null,
    private val merchantListListener: TokoFoodMerchantListViewHolder.TokoFoodMerchantListListener? = null,
    private val tickerListener: TokoFoodHomeTickerViewHolder.TokoFoodHomeTickerListener? = null,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener? = null
):  BaseAdapterTypeFactory(),
    TokoFoodHomeTypeFactory,
    TokoFoodMerchantListTypeFactory,
    TokoFoodCommonTypeFactory,
    HomeComponentTypeFactory {

    // region TokoFood Home Component
    override fun type(uiModel: TokoFoodHomeUSPUiModel): Int = TokoFoodHomeUSPViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeEmptyStateLocationUiModel): Int = TokoFoodHomeEmptyStateLocationViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeLoadingStateUiModel): Int = TokoFoodHomeLoadingViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodMerchantListUiModel): Int = TokoFoodMerchantListViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeIconsUiModel): Int = TokoFoodHomeIconsViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeChooseAddressWidgetUiModel): Int = TokoFoodHomeChooseAddressViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodProgressBarUiModel): Int = TokoFoodProgressBarViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeMerchantTitleUiModel): Int = TokoFoodHomeMerchantTitleViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeTickerUiModel): Int = TokoFoodHomeTickerViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodErrorStateUiModel): Int = TokoFoodErrorStateViewHolder.LAYOUT
    // endregion


    // region Global Home Component
    override fun type(categoryWidgetV2DataModel: CategoryWidgetV2DataModel): Int = CategoryWidgetV2ViewHolder.LAYOUT
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){

            // region TokoFood Home Component
            TokoFoodHomeUSPViewHolder.LAYOUT -> TokoFoodHomeUSPViewHolder(view, uspListener)
            TokoFoodHomeEmptyStateLocationViewHolder.LAYOUT -> TokoFoodHomeEmptyStateLocationViewHolder(view, emptyStateLocationListener)
            TokoFoodHomeLoadingViewHolder.LAYOUT -> TokoFoodHomeLoadingViewHolder(view)
            TokoFoodMerchantListViewHolder.LAYOUT -> TokoFoodMerchantListViewHolder(view, merchantListListener, tokofoodScrollChangedListener)
            TokoFoodHomeIconsViewHolder.LAYOUT -> TokoFoodHomeIconsViewHolder(view, homeIconListener, tokofoodScrollChangedListener)
            TokoFoodHomeChooseAddressViewHolder.LAYOUT -> TokoFoodHomeChooseAddressViewHolder(view, tokoFoodView, chooseAddressWidgetListener)
            TokoFoodProgressBarViewHolder.LAYOUT -> TokoFoodProgressBarViewHolder(view)
            TokoFoodHomeMerchantTitleViewHolder.LAYOUT -> TokoFoodHomeMerchantTitleViewHolder(view)
            TokoFoodHomeTickerViewHolder.LAYOUT -> TokoFoodHomeTickerViewHolder(view, tickerListener)
            TokoFoodErrorStateViewHolder.LAYOUT -> TokoFoodErrorStateViewHolder(view, errorStateListener)
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }
            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentCallback, null)
            }
            CategoryWidgetV2ViewHolder.LAYOUT -> {
                CategoryWidgetV2ViewHolder(view, categoryWidgetCallback, cardInteraction = true)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }


}
