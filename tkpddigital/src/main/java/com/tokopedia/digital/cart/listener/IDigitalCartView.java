package com.tokopedia.digital.cart.listener;

import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.VoucherDigital;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface IDigitalCartView extends IBaseView {
    void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData);

    void renderVoucherInfoData(VoucherDigital voucherDigital);

    void renderLoadingGetCartInfo();

    void closeViewWithMessageAlert(String message);

    String getUserId();

    String getAccountToken();

    String getWalletRefreshToken();

    void renderErrorCheckVoucher(String message);

    String getDigitalCategoryId();
}
