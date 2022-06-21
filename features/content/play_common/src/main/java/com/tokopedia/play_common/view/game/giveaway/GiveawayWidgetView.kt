package com.tokopedia.play_common.view.game.giveaway

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play_common.databinding.ViewGiveawayWidgetBinding
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.game.setupGiveaway
import java.util.*

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class GiveawayWidgetView : ConstraintLayout {

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
        setupView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.timerRemaining.pause()
        mListener = null
    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)

        binding.flTap.isClickable = clickable
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setTitle(title: String) {
        getHeader().setupGiveaway(title)
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

    private fun setupView() {
        setTitle("")
        getHeader().setHint(
            context.getString(R.string.play_giveaway_header_hint)
        )

        binding.flTap.isHapticFeedbackEnabled = true
        binding.flTap.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            mListener?.onTapTapClicked(this)
        }
    }

    interface Listener {

        fun onTapTapClicked(view: GiveawayWidgetView)
    }
}