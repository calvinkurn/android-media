package com.tokopedia.libra.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.domain.model.LibraUiModel
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(context: Context) : CacheRepository {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun get(owner: LibraOwner): LibraUiModel {
        val json = preferences.getString(owner.type, "") ?: return LibraUiModel.default()
        return Gson().fromJson(json, LibraUiModel::class.java)
    }

    override fun save(owner: LibraOwner, data: LibraUiModel) {
        if (data.experiments.isEmpty()) return

        val json = Gson().toJson(data)
        preferences
            .edit()
            .putString(owner.type, json)
            .apply()
    }

    override fun dispose(owner: LibraOwner) {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "libra_cache.pref"
    }
}
