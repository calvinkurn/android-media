package com.tokopedia.play.broadcaster.util.pageflow

/**
 * Created by jegul on 22/03/21
 */
interface PageFlowProcessor<Page> {

    fun navigate(page: Page)
}