package com.tokopedia.search.di.module

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.SmallGridSpanCount
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class StaggeredGridLayoutManagerModule {

    @SearchScope
    @Provides
    fun provideStaggeredGridLayoutManager(
        smallGridSpanCount: SmallGridSpanCount
    ): StaggeredGridLayoutManager {
        return object : StaggeredGridLayoutManager(smallGridSpanCount(), VERTICAL) {
            /**
             * Disable predictive animations. There is a bug in RecyclerView which causes views that
             * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
             * adapter size has decreased since the ViewHolder was recycled.
             */
            override fun supportsPredictiveItemAnimations(): Boolean {
                return false
            }

            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            override fun onItemsUpdated(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int,
                payload: Any?
            ) {
                try {
                    super.onItemsUpdated(recyclerView, positionStart, itemCount, payload)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            override fun onItemsAdded(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsAdded(recyclerView, positionStart, itemCount)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            override fun onItemsRemoved(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsRemoved(recyclerView, positionStart, itemCount)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            override fun onItemsMoved(
                recyclerView: RecyclerView,
                from: Int,
                to: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsMoved(recyclerView, from, to, itemCount)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }.apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }
}
