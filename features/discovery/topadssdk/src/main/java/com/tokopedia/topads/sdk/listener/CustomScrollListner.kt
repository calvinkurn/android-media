package com.tokopedia.topads.sdk.listener

import androidx.recyclerview.widget.*
import android.view.View

/**
 * Created by errysuprayogi on 12/20/19.
 */

class CustomScrollListner(private val view: View?) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView?.let {
            if (it.computeHorizontalScrollOffset() != 0) {
                view?.setAlpha(getAlphaForView(it))
                it.bringToFront()
                it.invalidate()
            } else {
                view?.setAlpha(1f)
                view?.bringToFront()
                view?.invalidate()
            }
        }
    }

    private fun getAlphaForView(recyclerView: RecyclerView): Float {
        return recyclerView.computeHorizontalScrollOffset() * (-1.4f / recyclerView.computeHorizontalScrollExtent()) + 1f

    }

}
