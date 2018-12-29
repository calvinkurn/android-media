package com.tokopedia.cod.di

import com.tokopedia.cod.CodContract
import com.tokopedia.cod.CodPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by fajarnuha on 29/12/18.
 */
@Module
class CodModule{

    @CodScope
    @Provides
    fun providePresenter(presenter: CodPresenter): CodContract.Presenter = presenter

}