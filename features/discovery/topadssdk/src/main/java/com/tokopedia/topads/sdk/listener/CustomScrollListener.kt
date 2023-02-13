package com.tokopedia.topads.sdk.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO

class CustomScrollListener(private val view: View?) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView.let {
            if (it.computeHorizontalScrollOffset() != Int.ZERO) {
                view?.alpha = getAlphaForView(it)
                it.bringToFront()
                it.invalidate()
            } else {
                view?.alpha = 1f
                view?.bringToFront()
                view?.invalidate()
            }
        }
    }

    private fun getAlphaForView(recyclerView: RecyclerView): Float {
        return recyclerView.computeHorizontalScrollOffset() * (-1.4f / recyclerView.computeHorizontalScrollExtent()) + 1f
    }

}
