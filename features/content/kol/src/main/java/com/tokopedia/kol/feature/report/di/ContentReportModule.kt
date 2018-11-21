package com.tokopedia.kol.feature.report.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.presenter.ContentReportPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * @author by milhamj on 15/11/18.
 */

val kolReportModule = module(override = true) {
    factory { GraphqlUseCase() }
    single { SendReportUseCase(androidContext(), get()) }
    factory<ContentReportContract.Presenter> { ContentReportPresenter(get()) }
}
