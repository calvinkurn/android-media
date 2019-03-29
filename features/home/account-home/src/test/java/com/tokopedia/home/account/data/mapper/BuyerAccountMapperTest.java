package com.tokopedia.home.account.data.mapper;

import android.test.mock.MockContext;

import com.tokopedia.home.account.data.model.AccountModel;

import org.junit.Test;
import org.mockito.Mockito;

import rx.Observable;
import rx.observers.AssertableSubscriber;

/**
 * @author okasurya on 11/15/18.
 */

public class BuyerAccountMapperTest {
    @Test
    public void call_NullModel_ThrowNullPointer() {
        //given
        AccountModel accountModel = new AccountModel();

        //when
        AssertableSubscriber as = Observable.just(accountModel)
                .map(new BuyerAccountMapper(new MockContext()))
                .test();

        //then
        as.assertError(NullPointerException.class);
    }

    @Test
    public void call_NormalCase() {
        //given
        AccountModel accountModel = Mockito.mock(AccountModel.class);

        //when
        AssertableSubscriber as = Observable.just(accountModel)
                .map(new BuyerAccountMapper(new MockContext()))
                .test();

        //then
        as.assertError(NullPointerException.class);
    }
}
