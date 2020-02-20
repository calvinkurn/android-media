package com.tokopedia.tokopoints.di

import android.os.Bundle
import dagger.Module
import dagger.Provides

@Module
class BundleModule constructor(private val bundle : Bundle = Bundle()){

    @Provides
    @TokoPointScope
    fun getBundle() = bundle


}
