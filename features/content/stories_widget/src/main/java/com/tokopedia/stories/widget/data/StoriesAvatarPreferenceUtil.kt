package com.tokopedia.stories.widget.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("stories_avatar_settings")

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarPreferenceUtil @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val hasSeenCoachMark = booleanPreferencesKey(KEY_HAS_SEEN_COACH_MARK)

    suspend fun hasSeenCoachMark(): Boolean {
        return appContext.dataStore.data.firstOrNull()?.get(hasSeenCoachMark) ?: false
    }

    suspend fun setHasSeenCoachMark() {
        appContext.dataStore.edit { settings ->
            settings[hasSeenCoachMark] = true
        }
    }

    companion object {
        private const val KEY_HAS_SEEN_COACH_MARK = "has_seen_coach_mark"
    }
}
