package com.tokopedia.play.broadcaster.util.pageflow.fragment

import com.tokopedia.play.broadcaster.util.pageflow.PageFlowProcessor
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory

/**
 * Created by jegul on 22/03/21
 */
class FragmentPageFlowProcessor(
        private val fragmentFactory: PlayBroadcastFragmentFactory,
        private val classLoader: ClassLoader
) : PageFlowProcessor<FragmentPage> {

    override fun navigate(page: FragmentPage) {
        fragmentFactory.instantiate(classLoader, page.fragmentClass.name)
    }
}