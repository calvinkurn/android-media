package com.tokopedia.tkpd.beranda.listener;

import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;

/**
 * @author by errysuprayogi on 11/29/17.
 */

public interface HomeCategoryListener {

    void onSectionItemClicked(LayoutSections sections, int parentPosition, int childPosition);

    void onCategoryItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onTopPicksItemClicked(TopPicksItemModel data, int parentPosition, int childPosition);

    void onTopPicksMoreClicked(String url, int pos);

    void onBrandsItemClicked(BrandDataModel data, int parentPosition, int childPosition);

    void onBrandsMoreClicked(int pos);

    void onDigitalMoreClicked(int pos);

    void onCloseTicker(int pos);
}
