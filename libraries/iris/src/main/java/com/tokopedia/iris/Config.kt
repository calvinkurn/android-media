package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.model.Configuration

/**
 * Created by meta on 18/04/19.
 */
class Config(context: Context) {

    var configuration: Configuration? = null

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        setEnabled()
    }

    private fun setEnabled() {
        if (configuration == null)
            return

        editor.putBoolean(IRIS_ENABLED, configuration!!.isEnabled)
        editor.commit()
    }

    fun isEnabled() : Boolean {
        return sharedPreferences.getBoolean(IRIS_ENABLED, false)
    }

    companion object {
        const val IRIS_ENABLED = "iris_enabled"
    }
}
