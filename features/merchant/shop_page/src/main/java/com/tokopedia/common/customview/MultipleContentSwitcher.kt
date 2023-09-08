package com.tokopedia.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.setRetainCardBackgroundColor
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.shop.databinding.LayoutMultipleContentSwitcherBinding
import com.tokopedia.unifycomponents.toPx

class MultipleContentSwitcher : FrameLayout {

    interface MultipleContentSwitcherListener {
        fun onMultipleSwitcherSelected(
            selectedIndex: Int,
            selectedItem: String,
            isSelectByAction: Boolean
        )
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    private var data = emptyList<String>()
    private var selectedIndex = 0
    private var listener: MultipleContentSwitcherListener? = null
    private var colorPallete: ColorPallete? = null

    private val binding = LayoutMultipleContentSwitcherBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        setUpSwitcher()
    }

    fun getSelectedIndex() = selectedIndex

    /**
     * set data into contentSwitcher
     * example data: "Men", "Woman", "Kids"
     */
    fun setData(data: List<String>) {
        val prevData = this@MultipleContentSwitcher.data
        this@MultipleContentSwitcher.data = data
        if (hasChanged(prevData, data)) {
            setUpSwitcher()
            requestLayout()
            this.post {
                doSelectItem(selectedIndex, false)
            }
        }
    }

    /**
     * set which data to be selected in contentSwitcher
     * Only render the view in the index is between 0 to size and different with prev selectedIndex
     */
    fun selectItem(index: Int) {
        if (index < -1 || index > data.size - 1 || selectedIndex == index) {
            return
        }
        doSelectItem(index, true)
    }

    /**
     * Set listener which is useful to handle what happen after we click the content switcher
     */
    fun setListener(listener: MultipleContentSwitcherListener) {
        this@MultipleContentSwitcher.listener = listener
    }

    private fun hasChanged(prevData: List<String>, inputData: List<String>): Boolean {
        if (prevData == inputData) {
            return false
        }
        if (prevData.size != inputData.size) return true
        for ((i, str) in prevData.withIndex()) {
            if (str != inputData[i]) {
                return true
            }
        }
        return false
    }

    private fun doSelectItem(index: Int, isSelectByAction: Boolean){
        deselectView(selectedIndex)
        selectedIndex = index
        selectView(index)
        listener?.onMultipleSwitcherSelected(index, data[index], isSelectByAction)
    }

    private fun setUpSwitcher() {
        val data = data
        binding.ll.removeAllViews()
        if (data.isNotEmpty()) {
            for ((index, option) in data.withIndex()) {
                val item = ItemContentSwitcher(context)
                val paddingSide = 8.toPx()
                val paddingTop = 2.toPx()
                item.setPadding(paddingSide, paddingTop, paddingSide, paddingTop)
                item.setColorPallete(colorPallete)
                item.text = option
                if (selectedIndex == index) {
                    item.setRetainTextColor(colorPallete, ColorPallete.ColorType.WHITE)
                    item.setItemChecked(true)
                } else {
                    item.setRetainTextColor(colorPallete, ColorPallete.ColorType.DARK_GREY)
                    item.setItemChecked(false)
                }
                item.setOnClickListener {
                    selectItem(index)
                }
                binding.ll.addView(item)
            }
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    fun setColor(colorPallete: ColorPallete) {
        this.colorPallete = colorPallete
        setUpColor()
    }

    private fun setUpColor(){
        binding.cardView.setRetainCardBackgroundColor(colorPallete, ColorPallete.ColorType.WHITE)
    }

    private fun deselectView(index: Int) {
        (binding.ll.getChildAt(index) as? ItemContentSwitcher)?.setItemChecked(false)
    }

    private fun selectView(index: Int) {
        (binding.ll.getChildAt(index) as? ItemContentSwitcher)?.setItemChecked(true)
    }


}
