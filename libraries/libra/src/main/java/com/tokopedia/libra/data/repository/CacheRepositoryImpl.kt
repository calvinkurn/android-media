package com.tokopedia.libra.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.domain.model.LibraUiModel
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
    context: Context,
    private val gson: Gson
) : CacheRepository {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun get(owner: LibraOwner): LibraUiModel {
        val json = preferences.getString(owner.type, "")

        return try {
            gson.fromJson(json, LibraUiModel::class.java)
        } catch (_: Throwable) {
            LibraUiModel.default()
        }
    }

    override fun save(owner: LibraOwner, data: LibraUiModel) {
        if (data.experiments.isEmpty()) return

        preferences
            .edit()
            .putString(owner.type, gson.toJson(data))
            .apply()
    }

    override fun clear(owner: LibraOwner) {
        preferences.edit().remove(owner.type).apply()
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "libra_cache.pref"
    }
}
