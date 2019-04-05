package com.tokopedia.digital.categorylist.data.mapper;

import com.tokopedia.digital.categorylist.data.cloud.entity.HomeCategoryMenuItem;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.digital.exception.MapperDataException;

import java.util.List;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface ICategoryDigitalListDataMapper {

    List<DigitalCategoryItemData> transformDigitalCategoryItemDataList(
            HomeCategoryMenuItem homeCategoryMenuItem
    ) throws MapperDataException;
}
