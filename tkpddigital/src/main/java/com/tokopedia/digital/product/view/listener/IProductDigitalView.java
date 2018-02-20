package com.tokopedia.digital.product.view.listener;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalView extends IBaseView {

    void renderBannerListData(String title, List<BannerData> bannerDataList);

    void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList);

    void renderStateSelectedAllData();

    void renderCheckPulsaBalanceData(int selectedSim, String ussdCode, String phoneNumber,
                                     String operatorErrorMsg, Boolean isSimActive, String carrierName);

    void renderErrorStyleNotSupportedProductDigitalData(String message);

    void renderErrorProductDigitalData(String message);

    void renderErrorHttpProductDigitalData(String message);

    void renderErrorNoConnectionProductDigitalData(String message);

    void renderErrorTimeoutConnectionProductDigitalData(String message);

    CategoryData getCategoryDataState();

    List<BannerData> getBannerDataListState();

    List<BannerData> getOtherBannerDataListState();

    HistoryClientNumber getHistoryClientNumberState();

    void showSnackBarCallbackCloseView(String message);

    boolean isUserLoggedIn();

    void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData);

    void showAccessibilityAlertDialog();

    void registerUssdReciever();

    void renderPulsaBalance(PulsaBalance pulsaBalance,int selectedSim);

    void showPulsaBalanceError(String message);
    
    void showMessageAlert(String message,String title);

    void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData, HistoryClientNumber historyClientNumber);

    void removeCheckPulsaCards();

    void showHelpMenu(String url);
}
