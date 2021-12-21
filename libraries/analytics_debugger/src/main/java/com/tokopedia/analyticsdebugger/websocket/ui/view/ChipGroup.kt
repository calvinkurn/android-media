package com.tokopedia.analyticsdebugger.websocket.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.tokopedia.analyticsdebugger.R

/**
 * Created By : Jonathan Darwin on December 21, 2021
 */
class ChipGroup: HorizontalScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val container = findViewById<LinearLayout>(R.id.chip_group_container)

    private val chipList = mutableListOf<Chip>()
    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.view_chip_group, this)
    }

    fun setOnCheckedListener(listener: Listener) {
        this.listener = listener
    }

    fun setChips(chipModelList: List<ChipModel>) {
        container.removeAllViews()

        chipModelList.forEach {
            val chip = Chip(context)
            chip.setModel(it)
            chip
            chip.setOnCheckedListener(object: Chip.Listener {
                override fun onCheckedListener(value: String) {
                    
                }

                override fun onCheckedListener(chipModel: ChipModel) {
                    this@ChipGroup.chipList.forEach { currChip ->
                        currChip
                        currChip.setModel()
                    }
                }
            })
            container.addView(chip)
            this.chipList.add(chip)
        }
    }

    interface Listener {
        fun onChecked(model: ChipModel)
    }
}