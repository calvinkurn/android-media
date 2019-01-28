package com.tokopedia.kelontongapp.notification

import android.os.Bundle

import java.util.HashMap

/**
 * Created by meta on 19/10/18.
 */
class NotificationModel {

    var title: String? = null
    var desc: String? = null
    var tkpCode: Int = 0
    var applinks: String? = null
    var counter: String? = null
    var toUserId: String? = null
    var senderId: String? = null
    var gId: String? = null
    var thumbnail: String? = null
    var fullName: String? = null
    var summary: String? = null
    var loginRequired: Boolean? = null
    var createTime: String? = null
    var targetApp: String? = null
    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties[name] = value
    }

    companion object {

        fun convertBundleToModel(data: Bundle): NotificationModel {
            val model = NotificationModel()
            model.applinks = data.getString("applinks", "")
            model.counter = data.getString("counter", "")
            model.createTime = data.getString("create_time", "")
            model.desc = data.getString("desc", "")
            model.fullName = data.getString("full_name", "")
            model.gId = data.getString("g_id", "")
            model.loginRequired = data.getString("login_required", "false") == "true"
            model.senderId = data.getString("sender_id", "")
            model.summary = data.getString("summary", "")
            model.thumbnail = data.getString("thumbnail", "")
            model.tkpCode = Integer.parseInt(data.getString("tkp_code", "0"))
            model.toUserId = data.getString("to_user_id", "")
            model.title = data.getString("title", "")
            model.targetApp = data.getString("target_app", "")
            return model
        }
    }
}
