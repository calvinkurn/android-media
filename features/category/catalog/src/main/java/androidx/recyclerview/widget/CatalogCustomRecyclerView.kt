package androidx.recyclerview.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.OverScroller

open class CatalogCustomRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    val overScroller: OverScroller
        get() = mViewFlinger.mOverScroller
}
