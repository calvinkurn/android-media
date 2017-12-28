package com.tokopedia.tkpd.beranda.presentation.view.adapter.factory;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.BannerViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.BrandsViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.CategoryItemViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.CategorySectionViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.DigitalsViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.SellViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.SaldoViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.TickerViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.TopPicksViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeAdapterFactory extends BaseAdapterTypeFactory implements HomeTypeFactory {

    private final HomeCategoryListener listener;
    private final FragmentManager fragmentManager;

    public HomeAdapterFactory(FragmentManager fragmentManager, HomeCategoryListener listener) {
        this.fragmentManager = fragmentManager;
        this.listener = listener;
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
    public int type(TopPicksViewModel topPicksViewModel) {
        return TopPicksViewHolder.LAYOUT;
    }

    @Override
    public int type(BrandsViewModel brandsViewModel) {
        return BrandsViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalsViewModel digitalsViewModel) {
        return DigitalsViewHolder.LAYOUT;
    }

    @Override
    public int type(CategorySectionViewModel categorySectionViewModel) {
        return CategorySectionViewHolder.LAYOUT;
    }

    @Override
    public int type(SellViewModel sellViewModel) {
        return SellViewHolder.LAYOUT;
    }

    @Override
    public int type(CategoryItemViewModel categoryItemViewModel) {
        return CategoryItemViewHolder.LAYOUT;
    }

    @Override
    public int type(SaldoViewModel saldoViewModel) {
        return SaldoViewHolder.LAYOUT;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == BannerViewHolder.LAYOUT)
            viewHolder = new BannerViewHolder(view, listener);
        else if (type == TickerViewHolder.LAYOUT)
            viewHolder = new TickerViewHolder(view, listener);
        else if (type == TopPicksViewHolder.LAYOUT)
            viewHolder = new TopPicksViewHolder(view, listener);
        else if (type == BrandsViewHolder.LAYOUT)
            viewHolder = new BrandsViewHolder(view, listener);
        else if (type == DigitalsViewHolder.LAYOUT)
            viewHolder = new DigitalsViewHolder(fragmentManager, view, listener);
        else if (type == CategorySectionViewHolder.LAYOUT)
            viewHolder = new CategorySectionViewHolder(view, listener);
        else if (type == CategoryItemViewHolder.LAYOUT)
            viewHolder = new CategoryItemViewHolder(view, listener);
        else if (type == SellViewHolder.LAYOUT)
            viewHolder = new SellViewHolder(view, listener);
        else if (type == SaldoViewHolder.LAYOUT)
            viewHolder = new SaldoViewHolder(view, listener);
        else if(type == HeaderViewHolder.LAYOUT)
            viewHolder = new HeaderViewHolder(view, listener);
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
