package com.tokopedia.browse.common.di

import android.content.Context

import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.browse.common.DigitalBrowseRouter
import com.tokopedia.browse.common.domain.DigitalBrowseRepository
import com.tokopedia.browse.common.presentation.DigitalBrowseBaseActivity

import dagger.Component

/**
 * @author by furqan on 30/08/18.
 */

@DigitalBrowseScope
@Component(modules = arrayOf(DigitalBrowseModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DigitalBrowseComponent {

    @get:ApplicationContext
    val context: Context

    fun digitalBrowseRouter(): DigitalBrowseRouter

    fun digitalBrowseRepository(): DigitalBrowseRepository

    fun cacheManager(): CacheManager

    fun inject(digitalBrowseBaseActivity: DigitalBrowseBaseActivity)
}
