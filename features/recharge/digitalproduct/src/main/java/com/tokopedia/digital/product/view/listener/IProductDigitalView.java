package com.tokopedia.digital.product.view.listener;

import android.app.Activity;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.newcart.presentation.compoundview.listener.IBaseView;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalView extends IBaseView {

    void renderBannerListData(String title, List<BannerData> bannerDataList);

    void renderOtherBannerListData(String title, List<BannerData> otherBannerDataList);

    void renderGuideListData(List<GuideData> guideDataList);

    void renderStateSelectedAllData();

    void renderCheckETollBalance(String text, String buttonText);

    void renderErrorStyleNotSupportedProductDigitalData(String message);

    void renderErrorProductDigitalData(String message);

    void renderErrorHttpProductDigitalData(String message);

    void renderErrorNoConnectionProductDigitalData(String message);

    void stopTrace();

    void sendOpenScreenEventTracking(CategoryData categoryData);

    void renderErrorTimeoutConnectionProductDigitalData(String message);

    CategoryData getCategoryDataState();

    List<BannerData> getBannerDataListState();

    List<BannerData> getOtherBannerDataListState();

    List<GuideData> getGuideDataListState();

    HistoryClientNumber getHistoryClientNumberState();

    void showSnackBarCallbackCloseView(String message);

    void showSnackBar(String message);

    boolean isUserLoggedIn();

    void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData);

    void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData, HistoryClientNumber historyClientNumber);

    Activity getActivity();

    boolean isDigitalSmartcardEnabled();

    void renderPromoGuideTab(int tabCount, String firstTab);

    void hidePromoGuideTab();

    void showPromoGuideTab();

    void goToCartPage(ProductDigitalData productDigitalData);

    void showPromoContainer();

    void navigateToDigitalCart(DigitalCheckoutPassData digitalCheckoutPassData);

    void onBuyButtonLoading(Boolean showLoading);
}