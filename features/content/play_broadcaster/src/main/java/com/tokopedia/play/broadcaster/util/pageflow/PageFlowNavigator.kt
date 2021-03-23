package com.tokopedia.play.broadcaster.util.pageflow

/**
 * Created by jegul on 22/03/21
 */
class PageFlowNavigator<T> private constructor(
        private val pageProcessor: PageFlowProcessor<T>,
        private val pageList: List<T>,
        private val listener: Listener?
) {

    private var currentPageIndex = -1

    private val nextIndex: Int
        get() = currentPageIndex + 1

    private val prevIndex: Int
        get() = currentPageIndex - 1

    companion object {

        fun <T> of(pageProcessor: PageFlowProcessor<T>, pageList: List<T>, listener: Listener? = null) {
            require(pageList.isNotEmpty())
        }
    }

    fun start() {
        pageProcessor.navigate(pageList.first())
        currentPageIndex++
    }

    fun next() {
        if (currentPageIndex < 0) error("Page is not started yet")

        if (nextIndex > pageList.size) {
            listener?.onNoMoreNextPage()
        } else {
            pageProcessor.navigate(pageList[nextIndex])
        }
        currentPageIndex++
    }

    fun previous() {
        if (currentPageIndex < 0) error("Page is not started yet")

        if (prevIndex < 0) {
            listener?.onNoMorePrevPage()
        } else {
            pageProcessor.navigate(pageList[prevIndex])
        }
        currentPageIndex--
    }

    interface Listener {

        fun onNoMoreNextPage()
        fun onNoMorePrevPage()
    }
}