package com.tokopedia.stories.common

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.lifecycle.AbstractLifecycleBoundDelegate
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by kenny.hadisaputra on 04/08/23
 */
class StoriesAvatarDelegate<LO : LifecycleOwner>(
    private val delegate: AbstractLifecycleBoundDelegate<LO, StoriesAvatarManager>
) : ReadOnlyProperty<LO, StoriesAvatarManager> {

    override fun getValue(thisRef: LO, property: KProperty<*>): StoriesAvatarManager {
        return delegate.getValue(thisRef, property)
    }
}

fun activityStoriesManager(key: StoriesKey): StoriesAvatarDelegate<AppCompatActivity> {
    return StoriesAvatarDelegate(
        lifecycleBound(
            { StoriesAvatarManager.create(key, it) }
        )
    )
}

fun storiesManager(key: StoriesKey) = StoriesAvatarDelegate(
    viewLifecycleBound(
        { StoriesAvatarManager.create(key, it) }
    )
)
