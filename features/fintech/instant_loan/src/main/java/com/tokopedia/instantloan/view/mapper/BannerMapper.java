package com.tokopedia.instantloan.view.mapper;

import android.support.annotation.NonNull;

import com.tokopedia.instantloan.domain.model.BannerModelDomain;
import com.tokopedia.instantloan.view.model.BannerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lavekush on 22/03/18.
 */

public class BannerMapper {

    @Inject
    public BannerMapper() {
    }

    private BannerViewModel transform(@NonNull BannerModelDomain bannerDomain) {
        BannerViewModel banner = null;
        banner = new BannerViewModel(bannerDomain.getImage(), bannerDomain.getLink());

        return banner;
    }


    public List<BannerViewModel> transform(@NonNull List<BannerModelDomain> bannerDomains) {
        final List<BannerViewModel> banners = new ArrayList<>();
        BannerViewModel bannerViewModel;
        for (BannerModelDomain bannerDomain : bannerDomains) {
            bannerViewModel = transform(bannerDomain);
            banners.add(bannerViewModel);
        }
        return banners;
    }
}
