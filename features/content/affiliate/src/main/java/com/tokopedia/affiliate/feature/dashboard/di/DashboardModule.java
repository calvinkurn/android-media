package com.tokopedia.affiliate.feature.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 13/09/18.
 */
@Module
public class DashboardModule {

//    @DashboardScope
//    @Provides
//    DashboardPresenter provideDashboardPresenter(@ApplicationContext Context context) {
//        return new DashboardPresenter(
//                new GetDashboardUseCase(context),
//                new GetDashboardLoadMoreUseCase(context));
//    }

}
