package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.data.entity.ResponseBanner;
import com.tokopedia.digital.product.model.BannerData;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalMapper implements IProductDigitalMapper {
    @Override
    public BannerData transformBannerData(ResponseBanner responseBanner)
            throws MapperDataException {
        return new BannerData.Builder()
                .type(responseBanner.getType())
                .id(responseBanner.getId())
                .rechargeCmsbannerId(responseBanner.getAttributes().getRechargeCmsbannerId())
                .fileName(responseBanner.getAttributes().getFileName())
                .fileNameWebp(responseBanner.getAttributes().getFileNameWebp())
                .startDate(responseBanner.getAttributes().getStartDate())
                .endDate(responseBanner.getAttributes().getEndDate())
                .imgUrl(responseBanner.getAttributes().getImgUrl())
                .priority(responseBanner.getAttributes().getPriority())
                .status(responseBanner.getAttributes().getStatus())
                .title(responseBanner.getAttributes().getTitle())
                .subtitle(responseBanner.getAttributes().getSubtitle())
                .promocode(responseBanner.getAttributes().getPromocode())
                .dataTitle(responseBanner.getAttributes().getDataTitle())
                .build();
    }
}
