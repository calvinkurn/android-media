package com.tokopedia.libra.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.domain.model.LibraUiModel
import com.tokopedia.libra.utils.PreferencesDelegate
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(context: Context, gson: Gson) : CacheRepository {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val preferenceDelegate by lazy { PreferencesDelegate(preferences, gson) }

    override fun get(owner: LibraOwner): LibraUiModel {
        return preferenceDelegate.get(owner.type, LibraUiModel.default())
    }

    override fun save(owner: LibraOwner, data: LibraUiModel) {
        if (data.experiments.isEmpty()) return
        return preferenceDelegate.set(owner.type, data)
    }

    override fun clear(owner: LibraOwner) {
        preferenceDelegate.clear(owner.type)
    }

    companion object {
        private const val PREF_NAME = "libra_cache.pref"
    }
}
