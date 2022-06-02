package com.tokopedia.telemetry.sensorlistener

import android.view.MotionEvent
import com.tokopedia.abstraction.base.view.listener.DispatchTouchListener
import com.tokopedia.telemetry.model.Telemetry

object TelemetryTouchListener: DispatchTouchListener {

    override fun onDispatchTouch(ev: MotionEvent) {
        Telemetry.addTouch(ev.action,
            ev.getAxisValue(MotionEvent.AXIS_X).toInt(),
            ev.getAxisValue(MotionEvent.AXIS_Y).toInt(),
            ev.pressure
        )

    }
}