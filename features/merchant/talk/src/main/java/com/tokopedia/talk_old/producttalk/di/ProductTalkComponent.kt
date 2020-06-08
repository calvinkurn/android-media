package com.tokopedia.talk_old.producttalk.di

import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.producttalk.view.activity.TalkProductActivity
import com.tokopedia.talk_old.producttalk.view.fragment.ProductTalkFragment
import dagger.Component

/**
 * @author by nisie on 8/29/18.
 */
@ProductTalkScope
@Component(modules = arrayOf(ProductTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface ProductTalkComponent {

    fun inject(productTalkActivity: TalkProductActivity)

    fun inject(productTalkFragment: ProductTalkFragment)


}