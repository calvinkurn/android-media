package com.tokopedia.stories.internal

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 18/08/23
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("stories_settings")

class StoriesPreferenceUtil @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val hasAckStoriesFeature = booleanPreferencesKey(KEY_HAS_ACKNOWLEDGED_STORIES_FEATURE)

    suspend fun hasAckStoriesFeature(): Boolean {
        return appContext.dataStore.data.firstOrNull()?.get(hasAckStoriesFeature) ?: false
    }

    suspend fun setHasAckStoriesFeature() {
        appContext.dataStore.edit { settings ->
            settings[hasAckStoriesFeature] = true
        }
    }

    companion object {
        private const val KEY_HAS_ACKNOWLEDGED_STORIES_FEATURE = "has_ack_stories_feature"
    }
}

