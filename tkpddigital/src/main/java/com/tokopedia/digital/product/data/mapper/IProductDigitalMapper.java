package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.data.entity.ResponseBanner;
import com.tokopedia.digital.product.model.BannerData;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public interface IProductDigitalMapper {

    BannerData transformBannerData(
            ResponseBanner responseBanner
    ) throws MapperDataException;
}
