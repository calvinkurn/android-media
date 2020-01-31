package com.tokopedia.digital.newcart.domain.model.mapper;

import com.tokopedia.digital.newcart.data.entity.response.deal.DealCategoryEntity;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealCategoryViewModelMapper {

    @Inject
    public DealCategoryViewModelMapper() {
    }

    public List<DealCategoryViewModel> transform(List<DealCategoryEntity> entities) {
        List<DealCategoryViewModel> viewModels = new ArrayList<>();
        if (entities != null && entities.size() > 0) {
            DealCategoryViewModel viewModel;
            for (DealCategoryEntity entity : entities) {
                viewModel = transform(entity);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }

    private DealCategoryViewModel transform(DealCategoryEntity entity) {
        DealCategoryViewModel viewModel = null;
        if (entity != null) {
            viewModel = new DealCategoryViewModel();
            viewModel.setId(entity.getId());
            viewModel.setName(entity.getName());
            viewModel.setUrl(entity.getUrl());
        }
        return viewModel;
    }
}
