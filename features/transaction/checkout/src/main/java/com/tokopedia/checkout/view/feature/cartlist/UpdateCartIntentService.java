package com.tokopedia.checkout.view.feature.cartlist;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author Irfan Khoirul on 31/07/18.
 */

public class UpdateCartIntentService extends IntentService {

    public static final String EXTRA_CART_ITEM_DATA_LIST = "EXTRA_CART_ITEM_DATA_LIST";

    private UpdateCartUseCase updateCartUseCase;
    private com.tokopedia.abstraction.common.data.model.session.UserSession userSession;

    public UpdateCartIntentService() {
        super(UpdateCartIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (getApplication() != null) {
            if (updateCartUseCase == null) {
                updateCartUseCase = CartComponentInjector.newInstance(getApplication()).getUpdateCartUseCase();
            }
            if (userSession == null) {
                userSession = CartComponentInjector.newInstance(getApplication()).getUserSession();
            }
        }

        if (userSession != null && updateCartUseCase != null && intent != null && intent.hasExtra(EXTRA_CART_ITEM_DATA_LIST)) {
            List<CartItemData> cartItemDataList = intent.getParcelableArrayListExtra(EXTRA_CART_ITEM_DATA_LIST);
            List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
            for (CartItemData data : cartItemDataList) {
                updateCartRequestList.add(new UpdateCartRequest.Builder()
                        .cartId(data.getOriginData().getCartId())
                        .notes(data.getUpdatedData().getRemark())
                        .quantity(data.getUpdatedData().getQuantity())
                        .build());
            }
            TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
            paramUpdate.put(UpdateCartUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                    getGeneratedAuthParamNetwork(paramUpdate));

            updateCartUseCase.execute(requestParams, new Subscriber<UpdateCartData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(UpdateCartData updateCartData) {
                    // Expected to do nothing
                }
            });
        }
    }

    private TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return originParams == null
                ? AuthUtil.generateParamsNetwork(
                getBaseContext(), userSession.getUserId(),
                userSession.getDeviceId())
                : AuthUtil.generateParamsNetwork(
                getBaseContext(), originParams,
                userSession.getUserId(),
                userSession.getDeviceId()
        );
    }

}
