package com.tokopedia.digital.product.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalView extends IBaseView {
    String getCategoryId();

    void renderProductDigitalData(CategoryData categoryData, List<BannerData> bannerDataList);
}
