package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.domain.usecase.GetAllCartUseCase;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentCloudRepository;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusItemDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author okasurya on 5/3/18.
 */

public class CheckTransactionUseCase extends UseCase<PaymentStatusDomain> {
    private PaymentRepository paymentRepository;
    private GetAllCartUseCase getAllCartUseCase;

    @Inject
    CheckTransactionUseCase(PaymentCloudRepository paymentCloudRepository,
                            GetAllCartUseCase getAllCartUseCase) {
        this.paymentRepository = paymentCloudRepository;
        this.getAllCartUseCase = getAllCartUseCase;
    }

    @Override
    public Observable<PaymentStatusDomain> createObservable(RequestParams requestParams) {
        return Observable.zip(
                paymentRepository.checkTransaction(requestParams),
                getAllCartUseCase.createObservable(null),
                new Func2<PaymentStatusDomain, List<CartDomain>, PaymentStatusDomain>() {
                    @Override
                    public PaymentStatusDomain call(PaymentStatusDomain paymentStatusDomain, List<CartDomain> cartDomains) {
                        if(cartDomains != null && cartDomains.size() > 0) {
                            List<PaymentStatusItemDomain> items = new ArrayList<>();
                            for (CartDomain cartItem : cartDomains) {
                                PaymentStatusItemDomain paymentItem = new PaymentStatusItemDomain();
                                paymentItem.setId(cartItem.getProductId());
                                paymentItem.setQuantity(cartItem.getQuantity());
                                paymentItem.setPrice(cartItem.getProductPriceUnformatted());
                                paymentItem.setName(cartItem.getProductName());
                                paymentItem.setImageUrl(cartItem.getProductImage300());
                                items.add(paymentItem);
                            }
                            paymentStatusDomain.setItems(items);
                        }

                        return paymentStatusDomain;
                    }
                }
        );
    }
}
