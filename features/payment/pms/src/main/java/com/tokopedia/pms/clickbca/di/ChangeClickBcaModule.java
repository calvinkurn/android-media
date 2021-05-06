package com.tokopedia.pms.clickbca.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.clickbca.view.ChangeClickBcaPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Module
public class ChangeClickBcaModule {

    @ChangeClickBcaScope
    @Provides
    ChangeClickBcaPresenter changeClickBcaPresenter(){
        return new ChangeClickBcaPresenter(new GraphqlUseCase());
    }
}
