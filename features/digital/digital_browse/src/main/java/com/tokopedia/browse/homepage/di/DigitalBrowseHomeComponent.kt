package com.tokopedia.browse.homepage.di

import com.tokopedia.browse.common.di.DigitalBrowseComponent
import com.tokopedia.browse.homepage.presentation.activity.DigitalBrowseHomeActivity
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseMarketplaceFragment
import com.tokopedia.browse.homepage.presentation.fragment.DigitalBrowseServiceFragment

import dagger.Component

/**
 * @author by furqan on 30/08/18.
 */

@DigitalBrowseHomeScope
@Component(modules = arrayOf(DigitalBrowseHomeModule::class), dependencies = arrayOf(DigitalBrowseComponent::class))
interface DigitalBrowseHomeComponent {

    fun inject(digitalBrowseMarketplaceFragment: DigitalBrowseMarketplaceFragment)

    fun inject(digitalBrowseServiceFragment: DigitalBrowseServiceFragment)

    fun inject(digitalBrowseHomeActivity: DigitalBrowseHomeActivity)
}
