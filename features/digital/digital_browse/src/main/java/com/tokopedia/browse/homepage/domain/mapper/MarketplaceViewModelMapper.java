package com.tokopedia.browse.homepage.domain.mapper;

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryRowEntity;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowsePopularBrand;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 05/09/18.
 */

public class MarketplaceViewModelMapper {

    @Inject
    public MarketplaceViewModelMapper() {
    }

    public DigitalBrowseMarketplaceViewModel transform(DigitalBrowseMarketplaceData data) {
        DigitalBrowseMarketplaceViewModel returnData = new DigitalBrowseMarketplaceViewModel();

        returnData.setRowViewModelList(transformRow(data.getCategoryGroups()
                .getDynamicHomeCategoryGroupEntities().get(0).getCategoryRow()));
        returnData.setPopularBrandsList(transformPopular(data.getPopularBrandDatas()));

        return returnData;
    }

    public List<DigitalBrowseRowViewModel> transformRow(List<DigitalBrowseCategoryRowEntity> categoryRowEntityList) {
        List<DigitalBrowseRowViewModel> returnDataList = new ArrayList<>();

        for (DigitalBrowseCategoryRowEntity row : categoryRowEntityList) {
            DigitalBrowseRowViewModel returnRow = new DigitalBrowseRowViewModel();

            returnRow.setAppLinks(row.getAppLinks());
            returnRow.setCategoryId(row.getCategoryId());
            returnRow.setCategoryLabel(row.getCategoryLabel());
            returnRow.setId(row.getId());
            returnRow.setImageUrl(row.getImageUrl());
            returnRow.setName(row.getName());
            returnRow.setType(row.getType());
            returnRow.setUrl(row.getUrl());

            returnDataList.add(returnRow);
        }

        return returnDataList;
    }

    public List<DigitalBrowsePopularBrandsViewModel> transformPopular(List<DigitalBrowsePopularBrand> popularBrandsEntityList) {
        List<DigitalBrowsePopularBrandsViewModel> returnDataList = new ArrayList<>();

        for (DigitalBrowsePopularBrand row : popularBrandsEntityList) {
            DigitalBrowsePopularBrandsViewModel returnRow = new DigitalBrowsePopularBrandsViewModel();

            returnRow.setId(row.getId());
            returnRow.setLogoUrl(row.getLogoUrl());
            returnRow.setName(row.getName());
            returnRow.setNew(row.isNew());
            returnRow.setUrl(row.getUrl());

            returnDataList.add(returnRow);
        }

        return returnDataList;
    }
}
