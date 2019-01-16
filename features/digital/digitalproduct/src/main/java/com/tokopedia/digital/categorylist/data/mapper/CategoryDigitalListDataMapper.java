package com.tokopedia.digital.categorylist.data.mapper;

import com.tokopedia.digital.categorylist.data.cloud.entity.HomeCategoryMenuItem;
import com.tokopedia.digital.categorylist.data.cloud.entity.LayoutRow;
import com.tokopedia.digital.categorylist.data.cloud.entity.LayoutSection;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.digital.exception.MapperDataException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class CategoryDigitalListDataMapper implements ICategoryDigitalListDataMapper {
    private static final int DEFAULT_BILL_ID = 57;
    private static final int DEFAULT_TICKET_ID = 58;
    @Override
    public List<DigitalCategoryItemData>
    transformDigitalCategoryItemDataList(HomeCategoryMenuItem homeCategoryMenuItem)
            throws MapperDataException {
        List<DigitalCategoryItemData> digitalCategoryItemDataList = new ArrayList<>();
        for (LayoutSection layoutSection : homeCategoryMenuItem.getData().getLayoutSections()) {
            if (layoutSection.getId() == DEFAULT_BILL_ID || layoutSection.getId() == DEFAULT_TICKET_ID)
                for (LayoutRow layoutRow : layoutSection.getLayoutRows()) {
                    if (layoutRow.getType().equalsIgnoreCase(
                            DigitalCategoryItemData.DEFAULT_TYPE_DIGITAL
                    )) {
                        DigitalCategoryItemData data = new DigitalCategoryItemData();
                        data.setId(String.valueOf(layoutRow.getId()));
                        data.setName(layoutRow.getName());
                        data.setImageUrl(layoutRow.getImageUrl());
                        data.setCategoryId(String.valueOf(layoutRow.getCategoryId()));
                        data.setRedirectValue(layoutRow.getUrl());
                        data.setAppLinks(layoutRow.getAppLinks());
                        data.setDescription("");
                        if (!digitalCategoryItemDataList.contains(data)) {
                            digitalCategoryItemDataList.add(data);
                        }
                    }
                }
        }
        return digitalCategoryItemDataList;
    }
}
