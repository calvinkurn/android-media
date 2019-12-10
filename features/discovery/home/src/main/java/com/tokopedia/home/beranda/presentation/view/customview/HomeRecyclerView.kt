package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.player.TokopediaPlayManager

class HomeRecyclerView : RecyclerView {
    private val TAG = "Home:HomeRecyclerView"
    val SOME_BLINKS = 50
    val playerManager: TokopediaPlayManager = TokopediaPlayManager.getInstance(context)
    private var recyclerListener: RecyclerListenerImpl? = null

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle){
    }

    override fun setRecyclerListener(listener: RecyclerListener?) {
        if (recyclerListener == null) recyclerListener = RecyclerListenerImpl(this)
        recyclerListener?.delegate = listener
        super.setRecyclerListener(recyclerListener)
    }

    class RecyclerListenerImpl internal constructor(private val container: HomeRecyclerView) : RecyclerListener {
        var delegate: RecyclerListener? = null
        override fun onViewRecycled(holder: ViewHolder) {
            if (delegate != null) delegate!!.onViewRecycled(holder)
//            if (holder is TokopediaPlayer) {
//                val player: TokopediaPlayer = holder
//                container.playbackInfoCache.onPlayerRecycled(player)
//                container.playerManager.recycle(player)
//            }
        }
    }
}