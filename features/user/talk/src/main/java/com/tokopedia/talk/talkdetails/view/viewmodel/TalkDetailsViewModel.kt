package com.tokopedia.talk.talkdetails.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory

/**
 * Created by Hendri on 05/09/18.
 */
data class TalkDetailsViewModel(
        var listItem: ArrayList<Visitable<*>> = ArrayList()
        )