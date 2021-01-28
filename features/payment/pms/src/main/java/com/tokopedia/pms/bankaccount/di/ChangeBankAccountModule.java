package com.tokopedia.pms.bankaccount.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.bankaccount.view.ChangeBankAccountPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module
public class ChangeBankAccountModule {

    @ChangeBankAccountScope
    @Provides
    ChangeBankAccountPresenter changeBankAccountPresenter(){
        return new ChangeBankAccountPresenter(new GraphqlUseCase());
    }
}
