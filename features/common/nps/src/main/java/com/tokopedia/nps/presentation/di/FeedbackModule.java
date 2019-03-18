package com.tokopedia.nps.presentation.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.nps.NpsAnalytics;
import com.tokopedia.nps.data.repository.FeedbackDataRepository;
import com.tokopedia.nps.domain.repository.FeedbackRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by meta on 02/07/18.
 */
@Module
public class FeedbackModule {

    private Context context;

    public FeedbackModule(Context context) {
        this.context = context;
    }

    @FeedbackScope
    @Provides
    Context provideContext() {
        return this.context;
    }

    @FeedbackScope
    @Provides
    FeedbackRepository provideFeedbackRepository(FeedbackDataRepository repository) {
        return repository;
    }

    @FeedbackScope
    @Provides
    NpsAnalytics provideNpsAnalytics() {
        return new NpsAnalytics();
    }
}
