package com.tokopedia.play_common.view.game.giveaway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.play_common.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play_common.view.game.setupGiveaway

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class GiveawayWidgetView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewGiveawayWidgetBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var mListener: Listener? = null

    init {
        orientation = VERTICAL

        setupView()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        binding.headerView.setupGiveaway(title)
    }

    private fun setupView() {
        setTitle("")
    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}