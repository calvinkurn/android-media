package com.tokopedia.tkpd.beranda.data.mapper;

import android.content.Context;
import android.content.res.TypedArray;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutSectionsModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksGroupsModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Func5;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class HomeDataMapper implements Func5<HomeBannerResponseModel, Ticker,
        BrandsOfficialStoreResponseModel, TopPicksResponseModel,
        HomeCategoryResponseModel, List<Visitable>> {
    public static final int DIGITAL_ID = 57;
    private final Context context;
    public HomeDataMapper(Context context) {
        this.context = context;
    }

    @Override
    public List<Visitable> call(HomeBannerResponseModel homeBannerResponseModel, Ticker ticker,
                                BrandsOfficialStoreResponseModel brandsOfficialStoreResponseModel,
                                TopPicksResponseModel topPicksResponseModel,
                                HomeCategoryResponseModel homeCategoryResponseModel) {
        List<Visitable> list = new ArrayList<>();

        if (homeBannerResponseModel.isSuccess() && homeBannerResponseModel.getData().getSlides().size() > 0) {
            list.add(mappingBanner(homeBannerResponseModel));
        }

        if (ticker != null && ticker.getData().getTickers().size() > 0) {
            list.add(mappingTicker(ticker.getData().getTickers()));
        }

        if (homeCategoryResponseModel.isSuccess() && homeCategoryResponseModel.getData().getLayoutSections().size() > 0) {
            list.add(mappingCategorySection());
        }
        if (topPicksResponseModel.isSuccess() && topPicksResponseModel.getData().getGroups().size() > 0) {
            for (TopPicksGroupsModel topPicksGroups : topPicksResponseModel.getData().getGroups()) {
                for (TopPicksModel topPick : topPicksGroups.getToppicks()) {
                    list.add(mappingTopPicks(topPick));
                }
            }
        }
        if (brandsOfficialStoreResponseModel.isSuccess() && brandsOfficialStoreResponseModel.getData().size() > 0) {
            list.add(mappingBrandsOs(brandsOfficialStoreResponseModel.getData()));
        }
        if (homeCategoryResponseModel.isSuccess() && homeCategoryResponseModel.getData().getLayoutSections().size() > 0) {
            list.addAll(mappingCategoryItem(homeCategoryResponseModel.getData().getLayoutSections()));
        }
        if (SessionHandler.isUserHasShop(context)) {
            list.add(mappingManageShop());
        } else {
            list.add(mappingOpenShop());
        }
        return swapList(list);
    }

    private Visitable mappingOpenShop() {
        SellViewModel model = new SellViewModel();
        model.setTitle(context.getString(R.string.empty_shop_wording_title));
        model.setSubtitle(context.getString(R.string.empty_shop_wording_subtitle));
        model.setBtn_title(context.getString(R.string.buka_toko));
        return model;
    }

    private Visitable mappingManageShop() {
        SellViewModel model = new SellViewModel();
        model.setTitle(context.getString(R.string.open_shop_wording_title));
        model.setSubtitle(context.getString(R.string.manage_shop_wording_subtitle));
        model.setBtn_title(context.getString(R.string.manage_toko));
        return model;
    }

    private List<Visitable> swapList(List<Visitable> list) {
        int brandIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof BrandsViewModel) {
                brandIndex = i;
            }
        }
        if (brandIndex > 0)
            Collections.swap(list, brandIndex, (brandIndex + 1));
        return list;
    }

    private Visitable mappingTopPicks(TopPicksModel topPicks) {
        TopPicksViewModel viewModel = new TopPicksViewModel();
        viewModel.setTitle(topPicks.getName());
        viewModel.setTopPickUrl(topPicks.getUrl());
        viewModel.setTopPicksItems(topPicks.getItem());
        return viewModel;
    }

    private Visitable mappingCategorySection() {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        String[] title = context.getResources().getStringArray(R.array.section_title);
        TypedArray icons = context.getResources().obtainTypedArray(R.array.section_icon);
        for (int i = 0; i < title.length; i++) {
            viewModel.addSection(new LayoutSections(title[i], icons.getResourceId(i, R.drawable.ic_beli)));
        }
        return viewModel;
    }

    private List<Visitable> mappingCategoryItem(List<CategoryLayoutSectionsModel> layoutSections) {
        List<Visitable> list = new ArrayList<>();
        for (int i = 0; i < layoutSections.size(); i++) {
            CategoryLayoutSectionsModel sections = layoutSections.get(i);
            if (sections.getId() == DIGITAL_ID) { //Id 22 == Digitals
                list.add(new DigitalsViewModel(sections.getTitle(), i));
            } else {
                CategoryItemViewModel viewModel = new CategoryItemViewModel();
                viewModel.setSectionId(i);
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
