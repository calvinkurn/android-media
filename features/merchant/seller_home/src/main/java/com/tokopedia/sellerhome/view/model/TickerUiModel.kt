package com.tokopedia.sellerhome.view.model

import android.text.Spanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

data class TickerUiModel(
        val redirectUrl: String,
        val createdBy: String,
        val createdOn: String,
        val state: String,
        val expireTime: String,
        val id: String,
        val message: String,
        val title: String,
        val target: String,
        val device: String,
        val updatedOn: String,
        val updatedBy: String,
        val color: String
) {

    fun getHtmlMessage(): Spanned {
        return MethodChecker.fromHtml(message)
    }
}