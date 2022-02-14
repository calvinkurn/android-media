package com.tokopedia.imagepicker.editor.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.editor.adapter.EditorItemSelectionAdapter
import com.tokopedia.imagepicker.editor.data.ItemSelection
import com.tokopedia.imagepicker.editor.main.Constant
import kotlinx.android.synthetic.main.widget_image_edit_item_selection.view.*

class ItemSelectionWidget : FrameLayout {

    private var recyclerView: RecyclerView? = null
    private val adapter: EditorItemSelectionAdapter = EditorItemSelectionAdapter()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(
            R.layout.widget_image_edit_item_selection,
            this, true
        )

        recyclerView = findViewById(R.id.lst_item)
    }

    fun setData(items: List<ItemSelection>, listener: EditorItemSelectionAdapter.EditorItemSelectionListener?) {
        if (adapter.itemCount == 0) {
            adapter.updateAll(items)
            adapter.setListener(listener)
        }

        recyclerView?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recyclerView?.adapter = adapter
    }

    fun setData(item: ItemSelection) {
        setData(listOf(item), null)
    }

    fun clearData() {
        adapter.removeListener()
        adapter.clear()
    }

    fun resetPosition() {
        adapter.resetPosition()
    }

    fun hasData(): Boolean {
        return adapter.itemCount != 0
    }

    fun getData(): List<ItemSelection> {
        return adapter.items
    }
}