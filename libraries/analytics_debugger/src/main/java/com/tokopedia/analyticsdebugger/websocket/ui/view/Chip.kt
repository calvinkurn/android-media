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

    private val chipText: Typography

    private val bgChecked = ContextCompat.getDrawable(context, R.drawable.bg_chip_checked)
    private val bgUnchecked = ContextCompat.getDrawable(context, R.drawable.bg_chip_unchecked)

    private var listener: Listener? = null
    var chipModel: ChipModel? = null

    init {
        View.inflate(context, R.layout.view_chip, this)

        chipText = findViewById(R.id.chip_text)

        chipText.setOnClickListener {
            chipModel?.let {
                if(!it.selected) {
                    setModel(it.copy(selected = true))
                    listener?.onCheckedListener(it)
                }
            }
        }
    }

    fun setModel(chipModel: ChipModel) {
        this.chipModel = chipModel

        setText(chipModel.label)
        setChecked(chipModel.selected)
    }

    private fun setText(text: String) {
        chipText.text = text
    }

    private fun setChecked(isChecked: Boolean) {
        chipText.background = if(isChecked) bgChecked else bgUnchecked
    }

    fun setOnCheckedListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onCheckedListener(chipModel: ChipModel)
    }
}