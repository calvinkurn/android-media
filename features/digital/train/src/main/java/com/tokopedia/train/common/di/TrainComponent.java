package com.tokopedia.train.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.domain.TrainProvider;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by alvarisi on 2/19/18.
 */
@TrainScope
@Component(modules = TrainModule.class, dependencies = BaseAppComponent.class)
public interface TrainComponent {

    @ApplicationContext
    Context getContext();

    AnalyticTracker analyticTracker();

    TrainDateUtil trainDateUtil();

    UserSession userSession();

    TrainRouter trainRouter();

    TrainRepository trainRepository();

    GetDetailScheduleUseCase getDetailScheduleUseCase();

    GetScheduleDetailUseCase getScheduleDetailUseCase();

    TrainFlowUtil trainFlowUtil();

    void inject(TrainBaseActivity trainBaseActivity);

    TrainProvider getTrainProvider();
}