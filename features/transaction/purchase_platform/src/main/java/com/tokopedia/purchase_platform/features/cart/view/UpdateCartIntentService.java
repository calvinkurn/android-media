package com.tokopedia.purchase_platform.features.cart.view;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.purchase_platform.features.cart.view.di.DaggerNewCartComponent;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Irfan Khoirul on 31/07/18.
 */

public class UpdateCartIntentService extends JobIntentService {

    public static final String EXTRA_CART_ITEM_DATA_LIST = "EXTRA_CART_ITEM_DATA_LIST";

    @Inject
    UpdateCartUseCase updateCartUseCase;

    @Inject
    UserSessionInterface userSession;

    public UpdateCartIntentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (userSession != null && updateCartUseCase != null && intent.hasExtra(EXTRA_CART_ITEM_DATA_LIST)) {
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

    private void initInjector() {
        BaseMainApplication baseMainApplication = (BaseMainApplication) getApplication();
        DaggerNewCartComponent.builder()
                .baseAppComponent(baseMainApplication.getBaseAppComponent())
                .build()
                .inject(this);
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
