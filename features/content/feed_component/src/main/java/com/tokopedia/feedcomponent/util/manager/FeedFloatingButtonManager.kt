package com.tokopedia.feedcomponent.util.manager

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 25, 2022
 */
class FeedFloatingButtonManager {

    private val dispatchers = CoroutineDispatchersProvider
    private val scope = CoroutineScope(dispatchers.computation)
    private var job: Job? = null

    private lateinit var mParentFragment: Fragment

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerViewAtMostTop(recyclerView)) {
                setDelayForExpandFab(recyclerView)
            }
            else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                shrinkFab()
            }
        }
    }

    fun setInitialData(parentFragment: Fragment) {
        mParentFragment = parentFragment
    }

    fun setDelayForExpandFab(recyclerView: RecyclerView) {
        job = scope.launch {
            delay(FAB_EXPAND_WAITING_DELAY)

            withContext(dispatchers.main) {
                if(recyclerViewAtMostTop(recyclerView)) expandFab()
            }
        }
    }

    private fun expandFab() {
        if(::mParentFragment.isInitialized && mParentFragment is FeedPlusContainerListener) {
            (mParentFragment as FeedPlusContainerListener).expandFab()
        }
    }

    fun shrinkFab() {
        if(::mParentFragment.isInitialized && mParentFragment is FeedPlusContainerListener) {
            (mParentFragment as FeedPlusContainerListener).shrinkFab()
        }
    }

    fun cancel() {
        job?.cancel()
    }

    private fun recyclerViewAtMostTop(recyclerView: RecyclerView): Boolean {
        return !recyclerView.canScrollVertically(-1)
    }

    companion object {
        private const val FAB_EXPAND_WAITING_DELAY = 1000L
    }
}