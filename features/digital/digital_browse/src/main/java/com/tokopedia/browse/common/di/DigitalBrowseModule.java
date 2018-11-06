package com.tokopedia.browse.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.browse.common.DigitalBrowseRouter;
import com.tokopedia.browse.common.data.DigitalBrowseRepositoryImpl;
import com.tokopedia.browse.common.domain.DigitalBrowseRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author by furqan on 30/08/18.
 */

@Module
public class DigitalBrowseModule {

    public DigitalBrowseModule() {
    }

    @DigitalBrowseScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

    @DigitalBrowseScope
    @Provides
    public DigitalBrowseRouter provideDigitalBrowseRouter(@ApplicationContext Context context) {
        if (context instanceof DigitalBrowseRouter) {
            return ((DigitalBrowseRouter) context);
        }
        throw new RuntimeException("Application must implement " + DigitalBrowseRouter.class.getCanonicalName());
    }

    @DigitalBrowseScope
    @Provides
    public DigitalBrowseRepository provideDigitalBrowseRepository() {
        return new DigitalBrowseRepositoryImpl();
    }
}
