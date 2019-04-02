package com.tokopedia.train.homepage.presentation.model;

import com.tokopedia.train.homepage.data.entity.BannerDetail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrainPromoViewModelMapper {

    @Inject
    public TrainPromoViewModelMapper() {
    }

    public List<TrainPromoViewModel> transform(List<BannerDetail> banners) {
        List<TrainPromoViewModel> viewModels = new ArrayList<>();
        TrainPromoViewModel viewModel;
        if (banners != null && banners.size() > 0) {
            for (BannerDetail banner : banners) {
                viewModel = transform(banner);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }

    public TrainPromoViewModel transform(BannerDetail banner) {
        TrainPromoViewModel viewModel = null;
        if (banner != null) {
            TrainPromoAttributesViewModel attributes = new TrainPromoAttributesViewModel(
                    banner.getAttributes().getDescription(),
                    banner.getAttributes().getLinkUrl(),
                    banner.getAttributes().getImageUrl(),
                    banner.getAttributes().getPromoCode()
            );
            viewModel = new TrainPromoViewModel(String.valueOf(banner.getId()), attributes);
        }
        return viewModel;
    }
}
