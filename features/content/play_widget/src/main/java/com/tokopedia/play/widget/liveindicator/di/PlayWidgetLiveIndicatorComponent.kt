package com.tokopedia.play.widget.liveindicator.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.play.widget.di.PlayWidgetInternalModule
import com.tokopedia.play.widget.di.PlayWidgetScope
import dagger.Component

@PlayWidgetScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [PlayWidgetInternalModule::class],
)
internal abstract class PlayWidgetLiveIndicatorComponent : ViewModel() {

    abstract fun getAnalytic(): PlayWidgetLiveIndicatorAnalytic
}

@Composable
internal fun rememberDaggerComponent(): PlayWidgetLiveIndicatorComponent {

    val context = LocalContext.current
    val view = LocalView.current

    return remember {
        val componentFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DaggerPlayWidgetLiveIndicatorComponent.builder()
                    .baseAppComponent(
                        (context.applicationContext as BaseMainApplication).baseAppComponent
                    ).build() as T
            }
        }

        ViewModelProvider(
            checkNotNull(view.findViewTreeViewModelStoreOwner()) {
                "No ViewModelStoreOwner was found for view $view"
            },
            componentFactory
        ).get()
    }
}
