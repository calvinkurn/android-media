package com.tokopedia.privacycenter.ui.dsar

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class DsarHelper @Inject constructor(@ApplicationContext val context: Context) {

    fun getSecret(): String {
        return context.getString(com.tokopedia.keys.R.string.one_trust_client_secret)
    }

    fun getClientId(): String {
        return context.getString(com.tokopedia.keys.R.string.one_trust_client_id)
    }

    fun getTemplateId(): String {
        return context.getString(com.tokopedia.keys.R.string.one_trust_template_id)
    }
}
