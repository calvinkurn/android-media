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
    public void call_NullModel_ThrowNullPointer1() {
        //given
        AccountModel accountModel = Mockito.mock(AccountModel.class);

        //when
        AssertableSubscriber as = Observable.just(accountModel)
                .map(new BuyerAccountMapper(new MockContext()))
                .test();

        //then
        as.assertError(NullPointerException.class);
    }

//    public AccountModel mockCompleteAccountModel() {
//        AccountModel accountModel = new AccountModel();
//
//        NotificationsModel notificationsModel = new NotificationsModel();
//        NotificationResolutionModel notificationResolutionModel = new NotificationResolutionModel();
//        notificationResolutionModel.setBuyer(1);
//        notificationsModel.setResolution(notificationResolutionModel);
//        NotificationBuyerOrderModel buyerOrder = new NotificationBuyerOrderModel();
//        buyerOrder.setArriveAtDestination(1);
//        buyerOrder.setConfirmed(1);
//        buyerOrder.setPaymentStatus("1");
//        buyerOrder.setProcessed(1);
//        buyerOrder.setShipped(1);
//        notificationsModel.setBuyerOrder(buyerOrder);
//        accountModel.setNotifications(notificationsModel);
//
//        LePreapproveModel lePreapproveModel = new LePreapproveModel();
//        accountModel.setLePreapprove(lePreapproveModel);
//
//        accountModel.setWallet(walletModel);
//        accountModel.setTokopointsSumCoupon();
//        accountModel.setTokopoints();
//        accountModel.setProfile();
//        accountModel.setIsAuthenticated();
//        accountModel.setDeposit();
//        accountModel.setPendingCashbackModel();
//        accountModel.setReputationShops();
//        accountModel.setVccUserBalance();
//        accountModel.setVccUserStatus();
//
//        return accountModel;
//    }
}
