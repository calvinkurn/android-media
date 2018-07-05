package com.tokopedia.paymentmanagementsystem.changeclickbca.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.paymentmanagementsystem.changeclickbca.view.ChangeClickBcaPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeClickBcaScope
@Module
public class ChangeClickBcaModule {

    @ChangeClickBcaScope
    @Provides
    ChangeClickBcaPresenter changeClickBcaPresenter(){
        return new ChangeClickBcaPresenter(new GraphqlUseCase());
    }
}
