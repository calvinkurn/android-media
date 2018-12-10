package com.tokopedia.digital.cart.domain.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.core.otp.domain.OtpRepository;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.presentation.model.OtpData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public class OtpVerificationInteractor implements IOtpVerificationInteractor {

    private final OtpRepository otpRepository;
    private final ICartMapperData cartMapperData;
    private final CompositeSubscription compositeSubscription;

    public OtpVerificationInteractor(OtpRepository otpRepository,
                                     ICartMapperData cartMapperData,
                                     CompositeSubscription compositeSubscription) {
        this.otpRepository = otpRepository;
        this.cartMapperData = cartMapperData;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void requestOtp(TKPDMapParam<String, Object> parameters, Subscriber<OtpData> subscriber) {
        compositeSubscription.add(otpRepository.requestOtp(parameters)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .map(new Func1<RequestOtpModel, OtpData>() {
                    @Override
                    public OtpData call(RequestOtpModel requestOtpModel) {
                        return cartMapperData.transformOtpData(requestOtpModel);
                    }
                })
                .subscribe(subscriber));
    }

    @Override
    public void verifyOtp(TKPDMapParam<String, Object> parameters, Subscriber<OtpData> subscriber) {
        compositeSubscription.add(otpRepository.validateOtp(parameters)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .map(new Func1<ValidateOtpModel, OtpData>() {
                    @Override
                    public OtpData call(ValidateOtpModel validateOtpModel) {
                        return cartMapperData.transformOtpData(validateOtpModel);
                    }
                })
                .subscribe(subscriber));
    }
}
