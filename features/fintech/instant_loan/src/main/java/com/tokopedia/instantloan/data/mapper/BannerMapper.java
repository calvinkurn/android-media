package com.tokopedia.instantloan.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.instantloan.data.model.response.BannerEntity;
import com.tokopedia.instantloan.domain.model.BannerModelDomain;

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

    @Nullable
    private BannerModelDomain transform(@NonNull BannerEntity bannerEntity) {
        BannerModelDomain banner = null;
        banner = new BannerModelDomain(bannerEntity.getImage(), bannerEntity.getLink());

        return banner;
    }

    public List<BannerModelDomain> transform(@NonNull List<BannerEntity> userEntityCollection) {
        final List<BannerModelDomain> banners = new ArrayList<>();
        for (BannerEntity userEntity : userEntityCollection) {
            final BannerModelDomain bannerDomain = transform(userEntity);
            if (bannerDomain != null) {
                banners.add(bannerDomain);
            }
        }
        return banners;
    }
}
