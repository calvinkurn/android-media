package com.tokopedia.digital.categorylist.data.mapper;

import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.entity.homeMenu.LayoutRow;
import com.tokopedia.core.network.entity.homeMenu.LayoutSection;
import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class CategoryDigitalListDataMapper implements ICategoryDigitalListDataMapper {
    @Override
    public List<DigitalCategoryItemData>
    transformDigitalCategoryItemDataList(HomeCategoryMenuItem homeCategoryMenuItem)
            throws MapperDataException {
        List<DigitalCategoryItemData> digitalCategoryItemDataList = new ArrayList<>();
        for (LayoutSection layoutSection : homeCategoryMenuItem.getData().getLayoutSections()) {
            if (layoutSection.getId() == 4)
                for (LayoutRow layoutRow : layoutSection.getLayoutRows()) {
                    if (layoutRow.getType().equalsIgnoreCase(
                            DigitalCategoryItemData.DEFAULT_TYPE_DIGITAL
                    )) {
                        DigitalCategoryItemData data = new DigitalCategoryItemData();
                        data.setName(layoutRow.getName());
                        data.setImageUrl(layoutRow.getImageUrl());
                        data.setCategoryId(String.valueOf(layoutRow.getCategoryId()));
                        data.setRedirectValue(layoutRow.getUrl());
                        data.setAppLinks(layoutRow.getAppLinks());
                        data.setDescription("");
                        digitalCategoryItemDataList.add(data);
                    }
                }
        }
        return digitalCategoryItemDataList;
    }
}
