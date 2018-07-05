package com.tokopedia.paymentmanagementsystem.changebankaccount.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.paymentmanagementsystem.changebankaccount.view.ChangeBankAccountPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeBankAccountScope
@Module
public class ChangeBankAccountModule {

    @ChangeBankAccountScope
    @Provides
    ChangeBankAccountPresenter changeBankAccountPresenter(){
        return new ChangeBankAccountPresenter(new GraphqlUseCase());
    }
}
