package com.tokopedia.tokopoints.di

import android.os.Bundle
import dagger.Module
import dagger.Provides

@Module
class BundleModule (private val bundle : Bundle){

    @Provides
    @TokoPointScope
    fun getBundle() = bundle


}
