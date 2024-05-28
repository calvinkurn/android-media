package com.tokopedia.navigation.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("bottom_nav_cache")

class BottomNavBarItemCacheManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private val gson = GsonBuilder().apply {
        registerTypeAdapter(BottomNavBarAsset.Type::class.java, BottomNavBarAssetTypeTypeAdapter)
        registerTypeAdapter(BottomNavBarAsset.Id::class.java, BottomNavBarAssetIdDeserializer)
    }.create()

    private val bottomNav = stringSetPreferencesKey(KEY_BOTTOM_NAV)
    suspend fun saveBottomNav(model: List<BottomNavBarUiModel>) {
        appContext.dataStore.edit { settings ->
            settings[bottomNav] = model.map {
                gson.toJson(it)
            }.toSet()
        }
    }

    suspend fun getBottomNav(): List<BottomNavBarUiModel>? {
        return appContext.dataStore.data.firstOrNull()?.get(bottomNav)?.map {
            gson.fromJson(it, BottomNavBarUiModel::class.java)
        }
    }

    companion object {
        private const val KEY_BOTTOM_NAV = "bottom_nav"
    }
}
