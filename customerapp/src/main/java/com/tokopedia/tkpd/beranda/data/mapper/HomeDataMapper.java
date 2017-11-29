package com.tokopedia.tkpd.beranda.data.mapper;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutSectionsModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksGroupsModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func5;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class HomeDataMapper implements Func5<HomeBannerResponseModel, Ticker,
        BrandsOfficialStoreResponseModel, TopPicksResponseModel,
        HomeCategoryResponseModel, List<Visitable>> {
    private Context context;

    public HomeDataMapper(Context context) {
        this.context = context;
    }

    @Override
    public List<Visitable> call(HomeBannerResponseModel homeBannerResponseModel, Ticker ticker,
                                BrandsOfficialStoreResponseModel brandsOfficialStoreResponseModel,
                                TopPicksResponseModel topPicksResponseModel,
                                HomeCategoryResponseModel homeCategoryResponseModel) {
        List<Visitable> list = new ArrayList<>();
        if (homeBannerResponseModel.isSuccess()) {
            list.add(mappingBanner(homeBannerResponseModel));
        }
        if (ticker.getData().getTickers().size() > 0) {
            list.add(mappingTicker(ticker.getData().getTickers()));
        }
        if (homeCategoryResponseModel.isSuccess()) {
            list.add(mappingCategorySection(homeCategoryResponseModel.getData().getLayoutSections()));
        }
        if (topPicksResponseModel.isSuccess()) {
            for (TopPicksGroupsModel topPicksGroups : topPicksResponseModel.getData().getGroups()) {
                for (TopPicksModel topPick : topPicksGroups.getToppicks()) {
                    list.add(mappingTopPicks(topPick));
                }
            }
        }
        if (brandsOfficialStoreResponseModel.isSuccess()) {
            list.add(mappingBrandsOs(brandsOfficialStoreResponseModel.getData()));
        }
        if (homeCategoryResponseModel.isSuccess()) {
            list.addAll(mappingCategoryItem(homeCategoryResponseModel.getData().getLayoutSections()));
        }
        return list;
    }

    private Visitable mappingTopPicks(TopPicksModel topPicks) {
        TopPicksViewModel viewModel = new TopPicksViewModel();
        viewModel.setTitle(topPicks.getName());
        viewModel.setTopPicksItems(topPicks.getItem());
        return viewModel;
    }

    private Visitable mappingCategorySection(List<CategoryLayoutSectionsModel> layoutSections) {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        for (CategoryLayoutSectionsModel sections : layoutSections) {
            viewModel.addSection(new LayoutSections(sections.getTitle(), R.drawable.ic_cat_clothing_big));
        }
        return viewModel;
    }

    private List<Visitable> mappingCategoryItem(List<CategoryLayoutSectionsModel> layoutSections) {
        List<Visitable> list = new ArrayList<>();
        for (CategoryLayoutSectionsModel sections : layoutSections) {
            if (sections.getId() == 22) { //Id 22 == Digitals
                list.add(new DigitalsViewModel(sections.getTitle()));
            } else {
                CategoryItemViewModel viewModel = new CategoryItemViewModel();
                viewModel.setTitle(sections.getTitle());
                viewModel.setItemList(sections.getLayoutRows());
                list.add(viewModel);
            }
        }
        return list;
    }

    private Visitable mappingBrandsOs(List<BrandDataModel> data) {
        BrandsViewModel viewModel = new BrandsViewModel();
        viewModel.setTitle(context.getString(R.string.title_home_official_store));
        viewModel.setData(data);
        return viewModel;
    }

    private Visitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private Visitable mappingBanner(HomeBannerResponseModel homeBannerResponseModel) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(homeBannerResponseModel.getData().getSlides());
        return viewModel;
    }
}
