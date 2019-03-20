package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.navigation.domain.model.Inbox
import com.tokopedia.navigation.domain.model.RecomTitle
import com.tokopedia.navigation.domain.model.Recomendation

/**
 * Author errysuprayogi on 13,March,2019
 */
interface InboxTypeFactory {

    fun type(inbox: Inbox): Int

    fun type(recomendation: Recomendation): Int

    fun type(recomTitle: RecomTitle): Int

}
