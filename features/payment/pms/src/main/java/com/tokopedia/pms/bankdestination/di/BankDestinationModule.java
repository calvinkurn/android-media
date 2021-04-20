package com.tokopedia.pms.bankdestination.di;

import android.content.Context;

import com.tokopedia.pms.bankdestination.data.BankListDataSourceLocal;
import com.tokopedia.pms.bankdestination.data.BankListRepositoryImpl;
import com.tokopedia.pms.bankdestination.domain.BankListRepository;
import com.tokopedia.pms.bankdestination.view.presenter.BankDestinationPresenter;
import com.tokopedia.pms.bankdestination.domain.interactor.GetBankListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module
public class BankDestinationModule {
    private Context context;
    public BankDestinationModule(Context context){
        this.context = context;
    }

    @BankDestinationScope
    @Provides
    Context getContext() {
        return context;
    }

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
