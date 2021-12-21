package com.tokopedia.analyticsdebugger.websocket.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on December 21, 2021
 */
class Chip: FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val chipText: Typography = findViewById(R.id.chip_text)

    private val bgChecked = ContextCompat.getDrawable(context, R.drawable.bg_chip_checked)
    private val bgUnchecked = ContextCompat.getDrawable(context, R.drawable.bg_chip_unchecked)

    private var listener: Listener? = null
    private var isChecked: Boolean = false
    private var chipModel: ChipModel? = null

    init {
        View.inflate(context, R.layout.view_chip, this)

        chipText.setOnClickListener {
            if(!isChecked) {
                chipModel?.let {
                    setModel(it.copy(selected = true))
                    listener?.onCheckedListener(it.value)
                }
            }
        }
    }

    fun setModel(chipModel: ChipModel) {
        this.chipModel = chipModel

        setText(chipModel.label)
        setChecked(chipModel.selected)
    }

    fun setText(text: String) {
        chipText.text = text
    }

    fun setChecked(isChecked: Boolean) {
        if(isChecked) {
            chipText.background = bgChecked
        }
        else {
            chipText.background = bgUnchecked
        }
        this.isChecked = isChecked
    }

    fun setOnCheckedListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onCheckedListener(value: String)
    }
}