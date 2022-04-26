package com.tokopedia.play.broadcaster.view.custom.game

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewGameOptionBinding
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play_common.util.extension.changeConstraint
import com.tokopedia.play_common.view.game.giveaway.GiveawayWidgetView

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class GameOptionView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewGameOptionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val previewWidth by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimensionPixelSize(R.dimen.play_interactive_preview_width)
    }
    private val previewHeight by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimensionPixelSize(R.dimen.play_interactive_preview_height)
    }

    private var mListener: (() -> Unit)? = null

    init {
        clipChildren = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.root.setOnClickListener {
            mListener?.invoke()
        }
    }

    fun setGameType(gameType: GameType) {
        binding.tvGameName.text = gameType.name

        when (gameType) {
            GameType.Giveaway -> addGiveawayImage()
            GameType.Quiz -> addQuizImage()
            else -> {}
        }
    }

    fun setListener(listener: () -> Unit) {
        mListener = listener
    }

    private fun addGiveawayImage() {
        binding.flPreview.setChildView { GiveawayWidgetView(context) }.apply {
            setTitle("Giveaway Kacamata Hitam")
            setEditable(false)
            showTimer(false)

            scaleX = 0.5f
            scaleY = 0.5f
            val lp = FrameLayout.LayoutParams(previewWidth, previewHeight)
            lp.gravity = Gravity.CENTER
            layoutParams = lp
        }
    }

    private fun addQuizImage() {
    }

    private inline fun <reified V: View> ViewGroup.setChildView(viewCreator: () -> V): V {
        val firstChild = getChildAt(0)
        return if (firstChild !is V) {
            removeAllViews()
            val view = viewCreator()
            addView(view)
            view
        } else firstChild
    }
}