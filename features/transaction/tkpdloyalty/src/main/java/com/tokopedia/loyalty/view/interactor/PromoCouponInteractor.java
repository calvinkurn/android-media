package com.tokopedia.loyalty.view.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponInteractor implements IPromoCouponInteractor {
    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepository;
    private final ICartRepository cartRepository;

    @Inject
    public PromoCouponInteractor(CompositeSubscription compositeSubscription,
                                 ITokoPointRepository tokoplusRepository,
                                 ICartRepository cartRepository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepository = tokoplusRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    public void getCouponList(TKPDMapParam<String, String> param,
                              Subscriber<CouponsDataWrapper> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.getCouponList(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitDigitalVoucher(String couponTitle, String voucherCode,
                                     TKPDMapParam<String, String> param,
                                     Subscriber<CouponViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.checkDigitalCouponValidity(param, voucherCode, couponTitle)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitCheckPromoCodeMarketPlace(Map<String, String> paramUpdateCart,
                                                Map<String, String> paramCheckPromo,
                                                Subscriber<CouponViewModel> subscriber) {
        if (paramUpdateCart != null)
            compositeSubscription.add(cartRepository.updateCartData(paramUpdateCart)
                    .flatMap(updateCartDataResponse -> cartRepository.checkPromoCodeCartList(paramCheckPromo)
                            .map(
                                    getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1()
                            ))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(subscriber));
        else
            compositeSubscription.add(cartRepository.checkPromoCodeCartList(paramCheckPromo)
                    .map(
                            getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1()
                    )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(subscriber));
    }

    @NonNull
    private Func1<CheckPromoCodeCartListDataResponse, CouponViewModel>
    getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1() {
        return checkPromoCodeCartListDataResponse -> {
            CouponViewModel couponViewModel = new CouponViewModel();
            if (checkPromoCodeCartListDataResponse.getError() != null
                    && !checkPromoCodeCartListDataResponse.getError().isEmpty()) {
                couponViewModel.setSuccess(false);
                couponViewModel.setMessage(checkPromoCodeCartListDataResponse.getError());
            } else {
                couponViewModel.setSuccess(true);
                couponViewModel.setMessage(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getMessageSuccess()
                );
                couponViewModel.setCode(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCode()
                );
                couponViewModel.setAmount(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCashbackVoucherDescription()
                );
                couponViewModel.setRawCashback(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCashbackVoucherAmount()
                );
            }
            return couponViewModel;
        };
    }

    @Override
    public void unsubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}
