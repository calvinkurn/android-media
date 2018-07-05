package com.tokopedia.paymentmanagementsystem.bankdestinationlist.di;

import com.tokopedia.paymentmanagementsystem.bankdestinationlist.data.BankListDataSourceLocal;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.data.BankListRepositoryImpl;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.domain.BankListRepository;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.presenter.BankDestinationPresenter;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.domain.interactor.GetBankListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@BankDestinationScope
@Module
public class BankDestinationModule {

    @BankDestinationScope
    @Provides
    BankDestinationPresenter bankDestinationPresenter(GetBankListUseCase getBankListUseCase){
        return new BankDestinationPresenter(getBankListUseCase);
    }

    @BankDestinationScope
    @Provides
    BankListRepository bankListRepository(BankListDataSourceLocal bankListDataSourceLocal){
        return new BankListRepositoryImpl(bankListDataSourceLocal);
    }
}
