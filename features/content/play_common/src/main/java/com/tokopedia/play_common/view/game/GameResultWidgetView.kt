package com.tokopedia.play_common.view.game

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.play_common.databinding.ViewGameInteractiveBinding
import com.tokopedia.play_common.databinding.ViewGameInteractiveResultBinding

class GameResultWidgetView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewGameInteractiveResultBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var text: String = ""
        set(value) {
            field = value
            binding.tvGameResult.text = value

        }

    fun setIcon(icon: Drawable) {
        binding.ivGameResult.setImageDrawable(icon)
    }
}