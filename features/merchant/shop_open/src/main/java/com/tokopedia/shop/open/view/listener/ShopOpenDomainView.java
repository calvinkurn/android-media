package com.tokopedia.shop.open.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenDomainView extends CustomerView {

    boolean isShopNameInValidRange();

    void onSuccessCheckShopName(boolean existed,String domainSuggestion);

    void onErrorCheckShopName(String message);

    boolean isShopDomainInValidRange();

    void onSuccessCheckShopDomain(boolean existed);

    void onErrorCheckShopDomain(String message);

    void onErrorReserveShop(Throwable t);

    void showSubmitLoading();

    void hideSubmitLoading();

    void onSuccessCreateShop(String message, String shopId);

    void onErrorCreateShop(String message);

}