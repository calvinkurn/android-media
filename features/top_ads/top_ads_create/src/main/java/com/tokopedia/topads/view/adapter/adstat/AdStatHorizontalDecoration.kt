package com.tokopedia.topads.view.adapter.adstat

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class AdStatHorizontalDecoration(private val offset:Int) : RecyclerView.ItemDecoration() {

    companion object{
        private const val CIRCLE_RADIUS = 4f
        private const val FIRST_POS = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.getChildAdapterPosition(view).let {
            if(it==RecyclerView.NO_POSITION || it== FIRST_POS) return
        }
        outRect.left = offset
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
         val childCount = parent.childCount
        for(idx in  0 until childCount){
            val view = parent.getChildAt(idx)
            val adapterPos = parent.getChildAdapterPosition(view)
            if(adapterPos!=RecyclerView.NO_POSITION && adapterPos > FIRST_POS && !checkVhLoadingState(parent,adapterPos)){
                val rect = RectF()
                rect.left = view.left - offset/2f
                rect.right = rect.left + 2 * CIRCLE_RADIUS
                rect.top = parent.top + (parent.height + CIRCLE_RADIUS)/2
                rect.bottom = rect.top + 2 * CIRCLE_RADIUS
               drawDot(c,parent.context,rect)
            }
        }
    }

    private fun checkVhLoadingState(parent:RecyclerView,pos:Int) : Boolean{
        val adapter = parent.adapter as? AdStatAdapter
        val isLoading = adapter?.isViewholderInLoadingState(pos)
        return isLoading == true
    }

    private fun drawDot(c:Canvas,context: Context,rect:RectF){
        val paint = Paint()
        paint.style  = Paint.Style.FILL
        paint.color = ResourcesCompat.getColor(context.resources,com.tokopedia.unifycomponents.R.color.Unify_NN600,null)
        c.drawCircle(rect.left,rect.top, CIRCLE_RADIUS,paint)
    }
}
