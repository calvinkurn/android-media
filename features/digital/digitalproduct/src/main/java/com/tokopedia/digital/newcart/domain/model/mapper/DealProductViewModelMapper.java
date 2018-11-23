package com.tokopedia.digital.newcart.domain.model.mapper;

import com.tokopedia.digital.newcart.data.entity.DealProductEntity;
import com.tokopedia.digital.newcart.data.entity.DealProductsResponse;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealProductViewModelMapper {

    @Inject
    public DealProductViewModelMapper() {
    }

    public List<DealProductViewModel> transform(List<DealProductEntity> entities) {
        List<DealProductViewModel> viewModels = new ArrayList<>();
        DealProductViewModel viewModel;
        if (entities != null) {
            for (DealProductEntity entity : entities) {
                viewModel = transform(entity);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }

    private DealProductViewModel transform(DealProductEntity entity) {
        DealProductViewModel viewModel = null;
        if (entity != null) {
            viewModel = new DealProductViewModel();
            viewModel.setId(entity.getId());
            viewModel.setTitle(entity.getTitle());
            viewModel.setBrandName(entity.getBrand().getTitle());
            viewModel.setSalesPriceNumeric(entity.getSalesPrice());
            viewModel.setBeforePriceNumeric(entity.getMrp());
            viewModel.setImageUrl(entity.getImageThumbUrl());
            viewModel.setSelected(false);
            viewModel.setUrl(entity.getUrl());
        }
        return viewModel;
    }

    public DealProductsViewModel transformDealProduct(DealProductsResponse dealProductsResponse) {
        DealProductsViewModel viewModel = new DealProductsViewModel();
        viewModel.setNextUrl(dealProductsResponse.getPage().getUriNext());
        viewModel.setProducts(transform(dealProductsResponse.getProducts()));
        return viewModel;
    }
}
