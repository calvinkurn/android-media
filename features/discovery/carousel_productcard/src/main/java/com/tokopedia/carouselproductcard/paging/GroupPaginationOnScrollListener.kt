package com.tokopedia.carouselproductcard.paging

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

internal class GroupPaginationOnScrollListener(
    currentGroup: CarouselPagingGroupModel? = null,
    currentPageInGroup: Int = 0,
    currentPageCount: Int = 0,
    private val paginationListener: PaginationListener,
): RecyclerView.OnScrollListener() {

    private var groupObservable by observable(currentGroup, ::groupSubscriber)
    private var pageInGroupObservable by observable(currentPageInGroup, ::pageSubscriber)
    private var pageCount by observable(currentPageCount, ::pageCountSubscriber)

    private var direction: Int = 0
    private var isScrolling = false

    private fun groupChangeDirection(): CarouselPagingGroupChangeDirection = when {
        direction > 0 -> CarouselPagingGroupChangeDirection.NEXT
        direction < 0 -> CarouselPagingGroupChangeDirection.PREVIOUS
        else -> CarouselPagingGroupChangeDirection.NO_DIRECTION
    }

    private fun groupSubscriber(
        ignored: KProperty<*>,
        oldValue: CarouselPagingGroupModel?,
        newValue: CarouselPagingGroupModel?
    ) {
        if (oldValue != newValue)
            paginationListener.onGroupChanged(selectedGroup(newValue))
    }

    private fun selectedGroup(group: CarouselPagingGroupModel?): CarouselPagingSelectedGroupModel? =
        group?.let {
            CarouselPagingSelectedGroupModel(
                group = it,
                direction = groupChangeDirection(),
            )
        }

    private fun pageSubscriber(ignored: KProperty<*>, oldValue: Int, newValue: Int) {
        if (oldValue != newValue)
            paginationListener.onPageInGroupChanged(newValue)
    }

    private fun pageCountSubscriber(ignored: KProperty<*>, oldValue: Int, newValue: Int) {
        if (oldValue != newValue)
            paginationListener.onPageCountChanged(newValue)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == SCROLL_STATE_IDLE && isScrolling) {
            isScrolling = false
            changePaginationInfo(recyclerView)
        }
    }

    private fun changePaginationInfo(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
        val adapter = recyclerView.adapter as? Adapter ?: return
        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

        if (firstVisibleItemPosition == RecyclerView.NO_POSITION) return

        val firstVisibleItem = adapter.getItemAt(firstVisibleItemPosition)

        if (firstVisibleItem !is HasGroup) return

        groupObservable = firstVisibleItem.group
        pageCount = firstVisibleItem.pageCount
        pageInGroupObservable = firstVisibleItem.pageInGroup
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dx != 0) {
            isScrolling = true
            direction = dx
        }
    }

    internal interface PaginationListener {
        fun onGroupChanged(group: CarouselPagingSelectedGroupModel?)

        fun onPageCountChanged(pageCount :Int)

        fun onPageInGroupChanged(pageInGroup: Int)
    }
}
