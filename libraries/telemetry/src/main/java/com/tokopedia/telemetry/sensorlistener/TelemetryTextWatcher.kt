package com.tokopedia.telemetry.sensorlistener

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.telemetry.model.Telemetry.addTyping


object TelemetryTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val diff: Int = count - before
        addTyping(diff)
    }

    override fun afterTextChanged(s: Editable?) { }
}