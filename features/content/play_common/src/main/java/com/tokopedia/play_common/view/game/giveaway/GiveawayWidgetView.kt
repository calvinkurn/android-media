package com.tokopedia.play_common.view.game.giveaway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupGiveaway
import java.util.*

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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerRemaining.pause()
        mListener = null
    }

    fun getHeaderView(): GameHeaderView {
        return binding.headerView
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        getHeaderView().setupGiveaway(title)
    }

    fun showTimer(shouldShow: Boolean) {
        binding.timerRemaining.showWithCondition(shouldShow)
    }

    fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
        binding.timerRemaining.apply {
            pause()

            targetDate = targetTime
            onFinish = onFinished

            resume()
        }
    }

    fun setEditable(isEditable: Boolean) {
        binding.headerView.isEditable = isEditable
    }

    private fun setupView() {
        setTitle("")
        binding.headerView.setHint(
            context.getString(R.string.play_giveaway_header_hint)
        )
    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}