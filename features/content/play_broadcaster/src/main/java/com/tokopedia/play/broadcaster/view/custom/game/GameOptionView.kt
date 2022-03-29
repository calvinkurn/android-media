package com.tokopedia.play.broadcaster.view.custom.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.databinding.ViewGameOptionBinding
import com.tokopedia.play.broadcaster.ui.model.game.GameType

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

    private var mListener: (() -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.root.setOnClickListener {
            mListener?.invoke()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        binding.flQuizImage.layoutParams = binding.flQuizImage.layoutParams.apply {
            height = measuredWidth
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setGameType(gameType: GameType) {
        binding.tvGameName.text = gameType.name
    }

    fun setListener(listener: () -> Unit) {
        mListener = listener
    }
}