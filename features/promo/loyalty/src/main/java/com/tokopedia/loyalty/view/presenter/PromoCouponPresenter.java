package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.domain.usecase.TrainCheckVoucherUseCase;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.view.IPromoCouponView;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponPresenter implements IPromoCouponPresenter {

    private static final String PARAM_CARTS = "carts";
    private static final String PARAM_PROMO_CODE = "promo_code";
    private static final String PARAM_SUGGESTED = "suggested";
    private static final String PARAM_LANG = "lang";
    private final IPromoCouponInteractor promoCouponInteractor;
    private final IPromoCouponView view;
    private FlightCheckVoucherUseCase flightCheckVoucherUseCase;
    private TrainCheckVoucherUseCase trainCheckVoucherUseCase;
    private UserSession userSession;

    @Inject
    public PromoCouponPresenter(IPromoCouponView view, IPromoCouponInteractor promoCouponInteractor, FlightCheckVoucherUseCase flightCheckVoucherUseCase,
                                TrainCheckVoucherUseCase trainCheckVoucherUseCase, UserSession userSession) {
        this.view = view;
        this.promoCouponInteractor = promoCouponInteractor;
        this.flightCheckVoucherUseCase = flightCheckVoucherUseCase;
        this.trainCheckVoucherUseCase = trainCheckVoucherUseCase;
        this.userSession = userSession;
    }

    @Override
    public void processGetCouponList(String platform) {
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        if (platform.equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.FLIGHT_STRING)) {
            platform = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING;
            param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.CATEGORY_ID, view.getCategoryId());
        } else if (platform.equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TRAIN_STRING)) {
            platform = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.KAI;
            param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.CATEGORY_ID, view.getCategoryId());
        } else if(platform.equalsIgnoreCase(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING)){
            param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.CATEGORY_ID, view.getCategoryId());
        }
        param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TYPE, platform);

        promoCouponInteractor.getCouponList(
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), param),
                new Subscriber<CouponsDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        view.enableSwipeRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TokoPointResponseErrorException) {
                            view.renderErrorGetCouponList(e.getMessage());
                        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            view.renderErrorNoConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            view.renderErrorTimeoutConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                            );
                        } else if (e instanceof HttpErrorException) {
                            view.renderErrorHttpGetCouponList(
                                    e.getMessage()
                            );
                        } else {
                            view.renderErrorGetCouponList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }

                    @Override
                    public void onNext(CouponsDataWrapper wrapper) {
                        if (wrapper.getCoupons().size() < 1) {
                            if (wrapper.getEmptyMessage() != null) {
                                view.couponDataNoResult(
                                        wrapper.getEmptyMessage().getTitle(),
                                        wrapper.getEmptyMessage().getSubTitle()
                                );
                            } else {
                                view.couponDataNoResult();
                            }
                        } else {
                            view.renderCouponListDataResult(wrapper.getCoupons());
                        }
                    }
                });
    }

    @Override
    public void processGetEventCouponList(int categoryId, int productId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        view.disableSwipeRefresh();
        param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.CATEGORY_ID, String.valueOf(categoryId));
        param.put("product_id", String.valueOf(productId));
        param.put("page", String.valueOf(1));
        param.put("page_size", String.valueOf(20));
        param.put(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TYPE, IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING);

        //TODO Revert Later
        promoCouponInteractor.getCouponList(
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), param),
                new Subscriber<CouponsDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        view.enableSwipeRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TokoPointResponseErrorException) {
                            view.renderErrorGetCouponList(e.getMessage());
                        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            view.renderErrorNoConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            view.renderErrorTimeoutConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                            );
                        } else if (e instanceof HttpErrorException) {
                            view.renderErrorHttpGetCouponList(
                                    e.getMessage()
                            );
                        } else {
                            view.renderErrorGetCouponList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }

                    @Override
                    public void onNext(CouponsDataWrapper wrapper) {
                        if (wrapper.getCoupons().size() < 1) {
                            if (wrapper.getEmptyMessage() != null) {
                                view.couponDataNoResult(
                                        wrapper.getEmptyMessage().getTitle(),
                                        wrapper.getEmptyMessage().getSubTitle()
                                );
                            } else {
                                view.couponDataNoResult();
                            }
                        } else {
                            view.renderCouponListDataResult(wrapper.getCoupons());
                        }
                    }
                });
    }

    @Override
    public void processCheckMarketPlaceCartListPromoCode(Activity activity, CouponData couponData,
                                                         String paramUpdateCartString) {
    }

    @Override
    public void submitDigitalVoucher(CouponData couponData, String categoryId) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(VOUCHER_CODE, couponData.getCode());
        param.put(CATEGORY_ID, categoryId);
        promoCouponInteractor.submitDigitalVoucher(
                couponData.getTitle(),
                couponData.getCode(),
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), param
                ), makeDigitalCouponSubscriber(couponData));
    }

    @Override
    public void submitTrainVoucher(CouponData couponData, String reservationId, String reservationCode) {
        view.showProgressLoading();
        trainCheckVoucherUseCase.execute(
                trainCheckVoucherUseCase.createVoucherRequest(reservationId, reservationCode, couponData.getCode()),
                checkTrainVoucherSubscriber(couponData));
    }

    @Override
    public void submitFlightVoucher(final CouponData data, String cartId) {
        view.showProgressLoading();
        flightCheckVoucherUseCase.execute(flightCheckVoucherUseCase.createCouponRequest(cartId, data.getCode()),
                checkFlightCouponSubscriber(data));
    }

    @NonNull
    private Subscriber<VoucherViewModel> checkFlightCouponSubscriber(final CouponData data) {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    data.setErrorMessage(e.getMessage());
                    view.couponError();
                } else if (e instanceof MessageErrorException) {
                    data.setErrorMessage(e.getMessage());
                    view.showSnackbarError(e.getMessage());
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                CouponViewModel viewModel = new CouponViewModel();
                viewModel.setAmount(voucherViewModel.getAmount());
                viewModel.setCode(voucherViewModel.getCode());
                viewModel.setRawCashback(voucherViewModel.getRawCashback());
                viewModel.setRawDiscount(voucherViewModel.getRawDiscount());
                viewModel.setMessage(voucherViewModel.getMessage());
                viewModel.setTitle(data.getTitle());
                viewModel.setSuccess(true);
                view.receiveDigitalResult(viewModel);
                view.hideProgressLoading();
            }
        };
    }

    @Override
    public void submitEventVoucher(final CouponData couponData, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", couponData.getCode());
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((LoyaltyModuleRouter) view.getContext().getApplicationContext()).verifyEventPromo(requestParams).subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException
                        || e instanceof com.tokopedia.abstraction.common.network.exception.ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object> resultMap) {
                view.hideProgressLoading();
                String promocode = (String) resultMap.get("promocode");
                int discount = (int) resultMap.get("promocode_discount");
                int cashback = (int) resultMap.get("promocode_cashback");
                String failmsg = (String) resultMap.get("promocode_failure_message");
                String successMsg = (String) resultMap.get("promocode_success_message");
                String status = (String) resultMap.get("promocode_status");
                if ((failmsg != null && failmsg.length() > 0) || status.length() == 0) {
                    couponData.setErrorMessage(failmsg);
                    view.couponError();
                    view.sendEventDigitalEventTracking(view.getContext(),"voucher failed - " + promocode, failmsg);
                } else {
                    CouponViewModel couponViewModel = new CouponViewModel();
                    couponViewModel.setCode(promocode);
                    couponViewModel.setMessage(successMsg);
                    couponViewModel.setSuccess(true);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(cashback);
                    couponViewModel.setRawDiscount(discount);
                    couponViewModel.setTitle("");
                    view.sendEventDigitalEventTracking(view.getContext(),"voucher success - " + promocode, successMsg);
                    view.receiveDigitalResult(couponViewModel);
                }
            }
        });

    }

    @Override
    public void submitDealVoucher(CouponData couponData, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", couponData.getCode());
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((LoyaltyModuleRouter) view.getContext().getApplicationContext()).verifyDealPromo(requestParams).subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException || e instanceof com.tokopedia.abstraction.common.network.exception.ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object> resultMap) {
                view.hideProgressLoading();
                String promocode = (String) resultMap.get("promocode");
                int discount = (int) resultMap.get("promocode_discount");
                int cashback = (int) resultMap.get("promocode_cashback");
                String failmsg = (String) resultMap.get("promocode_failure_message");
                String successMsg = (String) resultMap.get("promocode_success_message");
                String status = (String) resultMap.get("promocode_status");
                if ((failmsg != null && failmsg.length() > 0) || status.length() == 0) {
                    couponData.setErrorMessage(failmsg);
                    view.couponError();
                    view.sendEventDigitalEventTracking(view.getContext(),"voucher failed - " + promocode, failmsg);
                } else {
                    CouponViewModel couponViewModel = new CouponViewModel();
                    couponViewModel.setCode(promocode);
                    couponViewModel.setMessage(successMsg);
                    couponViewModel.setSuccess(true);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(cashback);
                    couponViewModel.setRawDiscount(discount);
                    couponViewModel.setTitle("");
                    view.sendEventDigitalEventTracking(view.getContext(),"voucher success - " + promocode, successMsg);
                    view.receiveDigitalResult(couponViewModel);
                }
            }
        });

    }

    @Override
    public void parseAndSubmitEventVoucher(String jsonbody, CouponData data, String platform) {
        JsonObject requestBody;
        if (jsonbody != null && jsonbody.length() > 0) {
            JsonElement jsonElement = new JsonParser().parse(jsonbody);
            requestBody = jsonElement.getAsJsonObject();
            if (platform.equals(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EVENT_STRING))
                submitEventVoucher(data, requestBody, false);
            else
                submitDealVoucher(data, requestBody, false);
        }
    }

    private Subscriber<CouponViewModel> makeCouponSubscriber(final CouponData couponData) {
        return new Subscriber<CouponViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CouponViewModel couponViewModel) {
                view.receiveResult(couponViewModel);
                view.hideProgressLoading();
            }
        };
    }

    private Subscriber<CouponViewModel> makeDigitalCouponSubscriber(final CouponData couponData) {
        return new Subscriber<CouponViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CouponViewModel couponViewModel) {
                view.receiveDigitalResult(couponViewModel);
                view.hideProgressLoading();
            }
        };
    }

    @NonNull
    private Subscriber<VoucherViewModel> checkTrainVoucherSubscriber(CouponData couponData) {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.sendTrackingOnCheckTrainVoucherError(e.getMessage());
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException || e instanceof com.tokopedia.abstraction.common.network.exception.ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                view.hideProgressLoading();
                CouponViewModel couponViewModel = new CouponViewModel();
                couponViewModel.setAmount(voucherViewModel.getAmount());
                couponViewModel.setCode(voucherViewModel.getCode());
                couponViewModel.setMessage(voucherViewModel.getMessage());
                couponViewModel.setRawCashback(voucherViewModel.getRawCashback());
                couponViewModel.setRawDiscount(voucherViewModel.getRawDiscount());
                couponViewModel.setSuccess(voucherViewModel.isSuccess());
                couponViewModel.setTitle(couponData.getTitle());
                view.receiveDigitalResult(couponViewModel);
            }
        };
    }

    @Override
    public void detachView() {
        promoCouponInteractor.unsubscribe();
    }

}
