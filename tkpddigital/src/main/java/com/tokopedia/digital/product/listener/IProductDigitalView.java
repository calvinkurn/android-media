package com.tokopedia.digital.product.listener;

import android.app.Application;
import android.content.ContentResolver;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalView extends IBaseView {


    void renderBannerListData(String categoryName, List<BannerData> bannerDataList);

    void renderStateSelectedAllData();

    void renderCategoryProductDataStyle1(CategoryData categoryData);

    void renderCategoryProductDataStyle2(CategoryData categoryData);

    void renderCategoryProductDataStyle3(CategoryData categoryData);

    void renderCategoryProductDataStyle4(CategoryData categoryData);

    void renderErrorStyleNotSupportedProductDigitalData(String message);

    void renderErrorProductDigitalData(String message);

    void renderErrorHttpProductDigitalData(String message);

    void renderErrorNoConnectionProductDigitalData(String message);

    void renderErrorTimeoutConnectionProductDigitalData(String message);

    ContentResolver getContentResolver();

    CategoryData getCategoryDataState();

    List<BannerData> getBannerDataListState();

    String getVersionInfoApplication();

    String getUserLoginId();

    Application getMainApplication();

    String getCategoryId();

    void closeViewWithMessageAlert(String message);

    LocalCacheHandler getLastInputClientNumberChaceHandler();
}
