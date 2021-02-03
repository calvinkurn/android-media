package com.tokopedia.feedback_form.drawonpicture.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedback_form.drawonpicture.presentation.fragment.DrawOnPictureFragment
import dagger.Component

/**
 * @author by furqan on 01/10/2020
 */
@DrawOnPictureScope
@Component(dependencies = [BaseAppComponent::class],
        modules = [DrawOnPictureModule::class, DrawOnPictureViewModelModule::class])
interface DrawOnPictureComponent {
    fun inject(drawOnPictureFragment: DrawOnPictureFragment)
}