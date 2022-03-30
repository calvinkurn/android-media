package com.tokopedia.play.broadcaster.view.custom.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.databinding.ViewQuizFormBinding

/**
 * Created By : Jonathan Darwin on March 30, 2022
 */
class QuizFormView : ConstraintLayout {

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

    private val binding = ViewQuizFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    init {
        binding.tvBroQuizFormNext.setOnClickListener {

        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }
}