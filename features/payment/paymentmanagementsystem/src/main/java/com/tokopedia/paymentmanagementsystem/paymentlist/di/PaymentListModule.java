package com.tokopedia.paymentmanagementsystem.paymentlist.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.mapper.PaymentListMapper;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter.PaymentListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@PaymentListScope
@Module
public class PaymentListModule {
    @Provides
    @PaymentListScope
    PaymentListPresenter providePaymentListPresenter(){
        return new PaymentListPresenter(new GraphqlUseCase(), new PaymentListMapper(), new GraphqlUseCase(), new GraphqlUseCase());
    }
}
