package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.model.Configuration

/**
 * Created by meta on 18/04/19.
 */
class Cache(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setEnabled(config: Configuration) {
        editor.putBoolean(IRIS_ENABLED, config.isEnabled)
        editor.commit()
    }

    fun isEnabled() : Boolean {
        return sharedPreferences.getBoolean(IRIS_ENABLED, false)
    }

    companion object {
        const val IRIS_ENABLED = "iris_enabled"
    }
}
