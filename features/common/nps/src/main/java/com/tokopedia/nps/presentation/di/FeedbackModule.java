package com.tokopedia.nps.presentation.di;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
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

    @Provides
    Context provideContext() {
        return this.context;
    }

    @Provides
    FeedbackRepository provideFeedbackRepository(FeedbackDataRepository repository) {
        return repository;
    }
}
