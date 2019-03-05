package com.tokopedia.checkout.view.feature.emptycart.subscriber;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.BuildConfig;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.view.feature.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class CancelAutoApplySubscriber extends Subscriber<String> {

    private final EmptyCartContract.View view;

    public CancelAutoApplySubscriber(EmptyCartContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.showErrorToast(ErrorHandler.getErrorMessage(view.getContext(), e));
        }
    }

    @Override
    public void onNext(String stringResponse) {
        if (view != null) {
            boolean resultSuccess = false;
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                NullCheckerKt.isContainNull(jsonObject, s -> {
                    ContainNullException exception = new ContainNullException("Found " + s + " on " + CancelAutoApplySubscriber.class.getSimpleName());
                    if (!BuildConfig.DEBUG) {
                        Crashlytics.logException(exception);
                    }
                    throw exception;
                });

                resultSuccess = jsonObject.getJSONObject(CancelAutoApplyCouponUseCase.RESPONSE_DATA)
                        .getBoolean(CancelAutoApplyCouponUseCase.RESPONSE_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (resultSuccess) {
                view.renderCancelAutoApplyCouponSuccess();
            } else {
                view.showErrorToast(null);
            }
        }
    }

}
