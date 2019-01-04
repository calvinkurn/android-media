package com.tokopedia.trackingoptimizer.model

/**
 * Created by hendry on 26/12/18.
 */
data class EventModel(var event: String,
                      var category: String,
                      var action: String,
                      var label: String) {
    var key:String = ""
        get() = hashCode().toString()
}