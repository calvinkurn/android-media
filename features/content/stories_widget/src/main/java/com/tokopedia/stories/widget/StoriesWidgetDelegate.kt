package com.tokopedia.stories.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.lifecycle.AbstractLifecycleBoundDelegate
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.stories.widget.domain.StoriesKey
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by kenny.hadisaputra on 04/08/23
 */
class StoriesWidgetDelegate<LO : LifecycleOwner>(
    private val delegate: AbstractLifecycleBoundDelegate<LO, StoriesWidgetManager>
) : ReadOnlyProperty<LO, StoriesWidgetManager> {

    override fun getValue(thisRef: LO, property: KProperty<*>): StoriesWidgetManager {
        return delegate.getValue(thisRef, property)
    }
}

fun activityStoriesManager(
    key: StoriesKey,
    builderOptions: StoriesWidgetManager.Builder.() -> Unit = {},
): StoriesWidgetDelegate<AppCompatActivity> {
    return StoriesWidgetDelegate(
        lifecycleBound(
            { StoriesWidgetManager.create(key, it, builderOptions) }
        )
    )
}

fun storiesManager(
    key: StoriesKey,
    builderOptions: StoriesWidgetManager.Builder.() -> Unit = {},
) = StoriesWidgetDelegate(
    viewLifecycleBound(
        { StoriesWidgetManager.create(key, it, builderOptions) }
    )
)
