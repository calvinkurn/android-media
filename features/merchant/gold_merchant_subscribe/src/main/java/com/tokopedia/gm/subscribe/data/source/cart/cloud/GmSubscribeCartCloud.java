package com.tokopedia.gm.subscribe.data.source.cart.cloud;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;
import com.tokopedia.gm.subscribe.data.tools.GetResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartCloud {
    private static final String SOURCE_SELLERAPP = "sellerapp";
    private static final String SOURCE_MAINAPP = "mainapp";
    private final GmSubscribeCartApi api;

    @Inject
    public GmSubscribeCartCloud(GmSubscribeCartApi api) {
        this.api = api;
    }

    public Observable<GmVoucherServiceModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return api
                .checkVoucher(
                        VoucherCodeInputModel
                                .buildInputModel(
                                        voucherCode,
                                        selectedProduct
                                )
                )
                .map(new GetResponse<GmVoucherServiceModel>());
    }

    public Observable<GmCheckoutServiceModel> checkoutGMSubscribe(
            Integer selectedProduct,
            Integer autoExtendSelectedProduct,
            String voucherCode
    ) {
        String sourceApp = GlobalConfig.isSellerApp()? SOURCE_SELLERAPP : SOURCE_MAINAPP;
        return api
                .checkoutGMSubscribe(
                        GmCheckoutInputModel
                                .getBodyModel(
                                        selectedProduct,
                                        autoExtendSelectedProduct,
                                        voucherCode, sourceApp
                                )
                )
                .map(new GetResponse<GmCheckoutServiceModel>());
    }


}
