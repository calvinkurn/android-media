package com.tokopedia.similarsearch.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.similarsearch.SimilarSearchFragmentFactory
import dagger.Module
import dagger.Provides

@Module
object SimilarSearchFragmentFactoryModule {
    @JvmStatic
    @SimilarSearchModuleScope
    @Provides
    fun provideSimilarSearchFragmentFactory() : FragmentFactory {
        return SimilarSearchFragmentFactory()
    }
}
