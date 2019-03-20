package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.listener.HomeInspirationListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.BannerViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DynamicIconSectionViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.UseCaseIconSectionViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DigitalsViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DummyViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DynamicChannelHeroViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DynamicChannelSprintViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.EmptyBlankViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.InspirationHeaderViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.RetryViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SellViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SprintSaleCarouselViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SpotlightViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SixGridChannelViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.ThreeGridChannelViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.TickerViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.TopAdsDynamicChannelViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.BusinessUnitViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.UseCaseIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BusinessUnitViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.InspirationHeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsDynamicChannelModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.DummyModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.RetryModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeAdapterFactory extends BaseAdapterTypeFactory implements HomeTypeFactory {

    private final HomeCategoryListener listener;
    private HomeFeedsListener homeFeedsListener;
    private final CountDownView.CountDownListener countDownListener;
    private HomeInspirationListener inspirationListener;
    private final FragmentManager fragmentManager;

    public HomeAdapterFactory(FragmentManager fragmentManager, HomeCategoryListener listener,
                              HomeInspirationListener inspirationListener,
                              HomeFeedsListener homeFeedsListener,
                              CountDownView.CountDownListener countDownListener) {
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        this.inspirationListener = inspirationListener;
        this.homeFeedsListener = homeFeedsListener;
        this.countDownListener = countDownListener;
    }

    @Override
    public int type(InspirationHeaderViewModel inspirationHeaderViewModel) {
        return InspirationHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerViewModel bannerViewModel) {
        return BannerViewHolder.LAYOUT;
    }

    @Override
    public int type(TickerViewModel tickerViewModel) {
        return TickerViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalsViewModel digitalsViewModel) {
        return DigitalsViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(BusinessUnitViewModel businessUnitViewModel) {
        return BusinessUnitViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(UseCaseIconSectionViewModel useCaseIconSectionViewModel) {
        return UseCaseIconSectionViewHolder.LAYOUT;
    }

    @Override
    public int type(DynamicIconSectionViewModel dynamicIconSectionViewModel) {
        return DynamicIconSectionViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsDynamicChannelModel topAdsDynamicChannelModel) {
        return TopAdsDynamicChannelViewHolder.LAYOUT;
    }

    @Override
    public int type(SellViewModel sellViewModel) {
        return SellViewHolder.LAYOUT;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationViewModel inspirationViewModel) {
        return InspirationViewHolder.LAYOUT;
    }

    public int type(RetryModel retryModel) {
        return RetryViewHolder.LAYOUT;
    }

    public int type(DummyModel dummyModel) {
        return DummyViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(DynamicChannelViewModel dynamicChannelViewModel) {
        if (DynamicHomeChannel.Channels.LAYOUT_3_IMAGE.equals(dynamicChannelViewModel.getChannel().getLayout())
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT.equals(dynamicChannelViewModel.getChannel().getLayout())
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO.equals(dynamicChannelViewModel.getChannel().getLayout())
                || DynamicHomeChannel.Channels.LAYOUT_ORGANIC.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return DynamicChannelSprintViewHolder.LAYOUT;
        } else if (DynamicHomeChannel.Channels.LAYOUT_HERO.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return DynamicChannelHeroViewHolder.LAYOUT;
        } else if (DynamicHomeChannel.Channels.LAYOUT_6_IMAGE.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return SixGridChannelViewHolder.LAYOUT;
        } else if (DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return ThreeGridChannelViewHolder.LAYOUT;
        } else if (DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return SprintSaleCarouselViewHolder.LAYOUT;
        } else {
            return EmptyBlankViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(SpotlightViewModel spotlightViewModel) {
        return SpotlightViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == BannerViewHolder.LAYOUT)
            viewHolder = new BannerViewHolder(view, listener);
        else if (type == TickerViewHolder.LAYOUT)
            viewHolder = new TickerViewHolder(view, listener);
        else if (type == DigitalsViewHolder.Companion.getLAYOUT())
            viewHolder = new DigitalsViewHolder(listener, fragmentManager, view);
        else if (type == BusinessUnitViewHolder.Companion.getLAYOUT())
            viewHolder = new BusinessUnitViewHolder(fragmentManager, view);
        else if (type == CategorySectionViewHolder.LAYOUT)
            viewHolder = new CategorySectionViewHolder(view, listener);
        else if (type == UseCaseIconSectionViewHolder.LAYOUT)
            viewHolder = new UseCaseIconSectionViewHolder(view, listener);
        else if (type == DynamicIconSectionViewHolder.LAYOUT)
            viewHolder = new DynamicIconSectionViewHolder(view, listener);
        else if (type == SellViewHolder.LAYOUT)
            viewHolder = new SellViewHolder(view, listener);
        else if(type == HeaderViewHolder.LAYOUT)
            viewHolder = new HeaderViewHolder(view, listener);
        else if (type == InspirationViewHolder.LAYOUT)
            viewHolder = new InspirationViewHolder(view, inspirationListener);
        else if (type == DynamicChannelHeroViewHolder.LAYOUT)
            viewHolder = new DynamicChannelHeroViewHolder(view, listener);
        else if (type == DynamicChannelSprintViewHolder.LAYOUT)
            viewHolder = new DynamicChannelSprintViewHolder(view, listener, countDownListener);
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = new RetryViewHolder(view, homeFeedsListener);
        else if (type == TopAdsViewHolder.LAYOUT)
            viewHolder = new TopAdsViewHolder(view);
        else if (type == TopAdsDynamicChannelViewHolder.LAYOUT)
            viewHolder = new TopAdsDynamicChannelViewHolder(view, inspirationListener);
        else if (type == SprintSaleCarouselViewHolder.LAYOUT)
            viewHolder = new SprintSaleCarouselViewHolder(view, listener, countDownListener);
        else if (type == SixGridChannelViewHolder.LAYOUT)
            viewHolder = new SixGridChannelViewHolder(view, listener, countDownListener);
        else if (type == ThreeGridChannelViewHolder.LAYOUT)
            viewHolder = new ThreeGridChannelViewHolder(view, listener, countDownListener);
        else if (type == SpotlightViewHolder.LAYOUT)
            viewHolder = new SpotlightViewHolder(view, listener);
        else if (type == EmptyBlankViewHolder.LAYOUT)
            viewHolder = new EmptyBlankViewHolder(view);
        else if (type == InspirationHeaderViewHolder.LAYOUT)
            viewHolder = new InspirationHeaderViewHolder(view);
        else if (type == DummyViewHolder.Companion.getLAYOUT()) {
            viewHolder = new DummyViewHolder(view,listener);
        }
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
