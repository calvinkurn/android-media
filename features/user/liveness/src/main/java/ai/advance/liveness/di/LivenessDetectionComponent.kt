package ai.advance.liveness.di

import ai.advance.liveness.view.fragment.LivenessErrorFragment
import ai.advance.liveness.view.fragment.LivenessFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@LivenessDetectionScope
@Component(modules = [LivenessDetectionModule::class,
            LivenessDetectionViewModelModule::class,
            LivenessDetectionUploadImagesModule::class],
        dependencies = [BaseAppComponent::class])
interface LivenessDetectionComponent {
    fun inject(fragment: LivenessErrorFragment)
    fun inject(fragment: LivenessFragment)
}