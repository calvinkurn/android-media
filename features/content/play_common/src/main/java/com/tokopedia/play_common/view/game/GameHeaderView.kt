package com.tokopedia.play_common.view.game

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewGameHeaderBinding
import com.tokopedia.play_common.util.extension.showKeyboard

/**
 * Created By : Jonathan Darwin on March 30, 2022
 */
class GameHeaderView : ConstraintLayout {

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

    private val binding = ViewGameHeaderBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var onTextChanged: ((String) -> Unit)? = null

    init {
        binding.etPlayGameHeaderTitle.afterTextChanged {
            onTextChanged?.invoke(it)
        }
    }

    var isEditable: Boolean = false
        set(value) {
            field = value
            binding.etPlayGameHeaderTitle.apply {
                setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                isFocusable = value
                isFocusableInTouchMode = value
                isEnabled = value
            }

            setFocus(value)
        }

    var title: String = binding.etPlayGameHeaderTitle.text.toString()
        set(value) {
            field = value

            if(binding.etPlayGameHeaderTitle.text.toString() != field) {
                binding.etPlayGameHeaderTitle.setText(value)
                binding.etPlayGameHeaderTitle.setSelection(value.length)
            }
        }
        get() {
            return binding.etPlayGameHeaderTitle.text.toString()
        }

    var maxLength: Int = 0
        set(value) {
            if(field != value) {
                field = value

                binding.etPlayGameHeaderTitle.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }
        }

    var type: Type = Type.UNKNOWN
        set(value) {
            field = value

            setIcon()
            setBackground()
        }

    init {
        binding.etPlayGameHeaderTitle.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChanged = listener
    }

    fun setFocus(isFocus: Boolean) {
        binding.etPlayGameHeaderTitle.apply {
            if(isFocus) requestFocus()
            else clearFocus()

            showKeyboard(isFocus)
        }
    }

    private fun setIcon() {
        val (icon, color) = when(type) {
            Type.TAPTAP -> {
                Pair(
                    IconUnify.GIFT,
                    ContextCompat.getColor(context, R.color.play_dms_giveaway_icon_color)
                )
            }
            else -> {
                Pair(
                    IconUnify.QUIZ,
                    ContextCompat.getColor(context, R.color.play_dms_quiz_icon_color)
                )
            }
        }

        binding.icPlayGameHeaderIcon.setImage(
            icon,
            newLightEnable = color,
            newDarkEnable = color,
        )
    }

    fun setContentBackground(drawable: Drawable) {
        binding.flPlayGameHeader.background = drawable
    }

    fun setIcon(icon: Drawable) {
        binding.icPlayGameHeaderIcon.setImageDrawable(icon)
    }

    fun setHint(hint: String) {
        binding.etPlayGameHeaderTitle.hint = hint
    }

    private fun setBackground() {
        binding.flPlayGameHeader.background = ContextCompat.getDrawable(
            context,
            when(type) {
                Type.TAPTAP -> R.drawable.bg_play_giveaway_header
                Type.QUIZ -> R.drawable.bg_play_quiz_header
                else -> R.drawable.bg_play_giveaway_header
            }
        )
    }

    enum class Type {
        TAPTAP, QUIZ, UNKNOWN
    }
}