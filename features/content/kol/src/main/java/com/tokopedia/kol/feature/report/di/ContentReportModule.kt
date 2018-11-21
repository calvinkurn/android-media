package com.tokopedia.kol.feature.report.di

import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.presenter.ContentReportPresenter
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 15/11/18.
 */

@Module
class ContentReportModule {
    @Provides
    @ContentReportScope
    fun providePresenter(contentReportPresenter: ContentReportPresenter)
            : ContentReportContract.Presenter {
        return contentReportPresenter
    }
}