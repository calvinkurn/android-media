package com.tokopedia.feedplus.view.viewmodel.feeddetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * @author by astidhiyaa on 30/08/22
 */
object RetryModel : Visitable<FeedPlusTypeFactory> {
    override fun type(typeFactory: FeedPlusTypeFactory?): Int {
        return typeFactory?.type(retryModel = this).orZero()
    }
}
