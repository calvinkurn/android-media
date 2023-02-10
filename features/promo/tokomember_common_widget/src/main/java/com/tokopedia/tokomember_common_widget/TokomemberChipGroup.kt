package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.unifycomponents.ChipsUnify
import kotlin.math.roundToInt

class TokomemberChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var defaultPosition = 0
    var currentPosition = defaultPosition
    private val chipsList = ArrayList<ChipsUnify>()
    private var linearChip: LinearLayout? = null
    private var chipGroupCallback: ChipGroupCallback? = null

    init {
        View.inflate(context, R.layout.tm_chip_group_view, this)
        linearChip = this.findViewById(R.id.linearChip)
    }

    fun setCallback(chipGroupCallback: ChipGroupCallback) {
        this.chipGroupCallback = chipGroupCallback
    }

    fun setDefaultSelection(defaultPosition: Int = 0) {
        this.defaultPosition = defaultPosition
        this.currentPosition = defaultPosition
    }

    fun addChip(text: String) {
        val chip = context?.let { ChipsUnify(it) }
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(0, 0, dpToPx(8).roundToInt(), 0)
        chip?.chip_text?.text = text
        chip?.layoutParams = param
        chip?.setOnClickListener {
            chip.chipType = ChipsUnify.TYPE_SELECTED
            val index = chipsList.indexOf(chip)
            currentPosition = index
            chipsList.forEach {
                if (chipsList.get(index) != it) {
                    it.chipType = ChipsUnify.TYPE_NORMAL
                }
            }
            chipGroupCallback?.chipSelected(index)
        }
        linearChip?.addView(chip)
        if (chip != null) {
            chipsList.add(chip)
            val index = chipsList.indexOf(chip)
            if (index == defaultPosition) {
                chip.chipType = ChipsUnify.TYPE_SELECTED
            } else {
                chip.chipType = ChipsUnify.TYPE_NORMAL
            }
        }
    }

    fun addChips(list: ArrayList<String>) {
        list.forEach {
            addChip(it)
        }
    }

    fun setChecked(index: Int) {
        chipsList.forEach {
            if (chipsList.get(index) != it) {
                it.chipType = ChipsUnify.TYPE_NORMAL
            }
        }
        chipsList.get(index).chipType = ChipsUnify.TYPE_SELECTED
    }
}
