package com.tokopedia.checkout.view.feature.emptycart;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartPresenter extends BaseDaggerPresenter<EmptyCartContract.View>
        implements EmptyCartContract.Presenter {

    private final GetCartListUseCase getCartListUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CompositeSubscription compositeSubscription;

    @Inject
    public EmptyCartPresenter(GetCartListUseCase getCartListUseCase,
                              CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                              CartApiRequestParamGenerator cartApiRequestParamGenerator,
                              CompositeSubscription compositeSubscription) {
        this.getCartListUseCase = getCartListUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
    }

    @Override
    public void processInitialGetCartData() {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                getView().getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );
        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CartListData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideLoading();
                            getView().showErrorToast(ErrorHandler.getErrorMessage(getView().getContext(), e));
                        }
                    }

                    @Override
                    public void onNext(CartListData cartListData) {
                        if (getView() != null) {
                            getView().hideLoading();
                            if (!cartListData.getShopGroupDataList().isEmpty()) {
                                getView().navigateToCartFragment(cartListData);
                            }
                        } else {
                            String autoApplyMessage = null;
                            if (cartListData != null && cartListData.getAutoApplyData() != null &&
                                    cartListData.getAutoApplyData().isSuccess()) {
                                autoApplyMessage = cartListData.getAutoApplyData().getTitleDescription();
                            }
                            getView().renderEmptyCart(autoApplyMessage);
                        }
                    }
                })
        );
    }

    @Override
    public void processCancelAutoApply() {
        compositeSubscription.add(cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (getView() != null) {
                            getView().showErrorToast(ErrorHandler.getErrorMessage(getView().getContext(), e));
                        }
                    }

                    @Override
                    public void onNext(String stringResponse) {
                        if (getView() != null) {
                            boolean resultSuccess = false;
                            try {
                                JSONObject jsonObject = new JSONObject(stringResponse);
                                resultSuccess = jsonObject.getJSONObject(CancelAutoApplyCouponUseCase.RESPONSE_DATA)
                                        .getBoolean(CancelAutoApplyCouponUseCase.RESPONSE_SUCCESS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (resultSuccess) {
                                getView().renderCancelAutoApplyCouponSuccess();
                            } else {
                                getView().showErrorToast(null);
                            }
                        }
                    }
                })
        );

    }
}
