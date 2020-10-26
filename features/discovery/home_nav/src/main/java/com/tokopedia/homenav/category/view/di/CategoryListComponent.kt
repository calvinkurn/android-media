package com.tokopedia.homenav.category.view.di

import com.tokopedia.homenav.category.view.fragment.CategoryListFragment
import com.tokopedia.homenav.di.BaseNavComponent
import dagger.Component

/**
 * Created by Lukas on 21/10/20.
 */

@CategoryListScope
@Component(modules = [], dependencies = [BaseNavComponent::class])
interface CategoryListComponent {
    fun inject(fragment: CategoryListFragment)
}