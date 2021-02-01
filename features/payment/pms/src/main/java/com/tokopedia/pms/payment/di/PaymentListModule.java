package com.tokopedia.pms.payment.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.payment.view.mapper.PaymentListMapper;
import com.tokopedia.pms.payment.view.presenter.PaymentListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module
public class PaymentListModule {
    @Provides
    @PaymentListScope
    PaymentListPresenter providePaymentListPresenter(){
        return new PaymentListPresenter(new GraphqlUseCase(), new PaymentListMapper(), new GraphqlUseCase(), new GraphqlUseCase());
    }
}
