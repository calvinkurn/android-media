package com.tokopedia.digital.product.listener;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.PulsaBalance;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalView extends IBaseView {

    void renderBannerListData(String title, List<BannerData> bannerDataList);

    void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList);

    void renderStateSelectedAllData();

    void renderCategoryProductDataStyle1(CategoryData categoryData,
                                         HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle2(CategoryData categoryData,
                                         HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle3(CategoryData categoryData,
                                         HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle4(CategoryData categoryData,
                                         HistoryClientNumber historyClientNumber);
    void renderCheckPulsaBalanceData(int selectedSim,String ussdCode, String phoneNumber,String operatorErrorMsg,Boolean isSimActive,String carrierName);

    void renderErrorStyleNotSupportedProductDigitalData(String message);

    void renderErrorProductDigitalData(String message);

    void renderErrorHttpProductDigitalData(String message);

    void renderErrorNoConnectionProductDigitalData(String message);

    void renderErrorTimeoutConnectionProductDigitalData(String message);

    ContentResolver getContentResolver();

    CategoryData getCategoryDataState();

    List<BannerData> getBannerDataListState();

    List<BannerData> getOtherBannerDataListState();

    HistoryClientNumber getHistoryClientNumberState();

    String getVersionInfoApplication();

    String getUserLoginId();

    Application getMainApplication();

    void closeViewWithMessageAlert(String message);

    void showSnackBarCallbackCloseView(String message);

    LocalCacheHandler getLastInputClientNumberChaceHandler();

    boolean isUserLoggedIn();

    void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData);

    void showAccessibilityAlertDialog();

    void registerUssdReciever();

    void renderPulsaBalance(PulsaBalance pulsaBalance,int selectedSim);

    void showPulsaBalanceError(String message);

    Activity getActivity();
    
    void showMessageAlert(String message,String title);

    void removeCheckPulsaCards();

}
