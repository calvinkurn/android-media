package com.tokopedia.browse.homepage.domain.mapper;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryGroupEntity;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryRowEntity;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by furqan on 07/09/18.
 */

public class ServiceViewModelMapper {

    @Inject
    public ServiceViewModelMapper() {
    }

    public DigitalBrowseServiceViewModel transform(DigitalBrowseMarketplaceData data) {
        int titleIndex = 0;
        DigitalBrowseServiceViewModel returnData = new DigitalBrowseServiceViewModel();

        List<DigitalBrowseServiceCategoryViewModel> categoryViewModelList = new ArrayList<>();
        Map<String, IndexPositionModel> titleMap = new HashMap<>();

        for (DigitalBrowseCategoryGroupEntity row : data.getCategoryGroups().getDynamicHomeCategoryGroupEntities()) {
            IndexPositionModel indexPositionModel = new IndexPositionModel();
            indexPositionModel.setIndexPositionInTab(titleIndex);
            indexPositionModel.setIndexPositionInList(categoryViewModelList.size());
            titleMap.put(row.getTitle(), indexPositionModel);

            DigitalBrowseServiceCategoryViewModel returnRow = new DigitalBrowseServiceCategoryViewModel();

            returnRow.setId(row.getId());
            returnRow.setName(row.getTitle());
            returnRow.setTitle(true);

            categoryViewModelList.add(returnRow);
            categoryViewModelList.addAll(transform(row.getCategoryRow()));

            titleIndex++;
        }

        returnData.setCategoryViewModelList(categoryViewModelList);
        returnData.setTitleMap(titleMap);
        return returnData;
    }

    private List<DigitalBrowseServiceCategoryViewModel> transform(List<DigitalBrowseCategoryRowEntity> categoryRow) {
        List<DigitalBrowseServiceCategoryViewModel> returnData = new ArrayList<>();

        for (DigitalBrowseCategoryRowEntity row : categoryRow) {
            DigitalBrowseServiceCategoryViewModel data = new DigitalBrowseServiceCategoryViewModel();
            data.setId(row.getId());
            data.setName(row.getName());
            data.setAppLinks(row.getAppLinks());
            data.setCategoryId(row.getCategoryId());
            data.setCategoryLabel(row.getCategoryLabel());
            data.setImageUrl(row.getImageUrl());
            data.setType(row.getType());
            data.setUrl(row.getUrl());
            data.setTitle(false);

            returnData.add(data);
        }

        return returnData;
    }
}
