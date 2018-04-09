package com.tokopedia.gm.subscribe.data.source.cart.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public interface GmSubscribeCartApi {
    @POST(TkpdBaseURL.TkpdCart.CHECK_VOUCHER)
    Observable<Response<GmVoucherServiceModel>> checkVoucher(@Body VoucherCodeInputModel inputModel);

    @POST(TkpdBaseURL.TkpdCart.CHECKOUT_ORDER)
    Observable<Response<GmCheckoutServiceModel>> checkoutGMSubscribe(@Body GmCheckoutInputModel bodyModel);
}
