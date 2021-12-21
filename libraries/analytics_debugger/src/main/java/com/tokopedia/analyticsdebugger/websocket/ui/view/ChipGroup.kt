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

    private val container: LinearLayout

    private val chipList = mutableListOf<Chip>()
    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.view_chip_group, this)

        container = findViewById(R.id.chip_group_container)
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
    }

    fun setOnCheckedListener(listener: Listener) {
        this.listener = listener
    }

    fun setChips(chipModelList: List<ChipModel>) {
        this.chipList.clear()
        container.removeAllViews()

        chipModelList.forEach {
            val chip = Chip(context)
            chip.setModel(it)
            chip.setOnCheckedListener(object: Chip.Listener {
                override fun onCheckedListener(chipModel: ChipModel) {
                    this@ChipGroup.chipList.forEach updateForLoop@ { chip ->
                        val currentChipModel = chip.chipModel ?: return@updateForLoop
                        currentChipModel.selected = currentChipModel.value == chipModel.value
                        chip.setModel(currentChipModel)
                    }

                    listener?.onChecked(chipModel)
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