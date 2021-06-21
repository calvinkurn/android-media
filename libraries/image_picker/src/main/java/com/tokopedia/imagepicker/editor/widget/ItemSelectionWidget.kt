package com.tokopedia.imagepicker.editor.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.editor.adapter.EditorItemSelectionAdapter

data class ItemSelection(
    val name: String = "",
    val preview: String = "",
    val placeholderText: String = "",
    val itemType: Int = 0,
    var isSelected: Boolean = false
)

class ItemSelectionWidget : FrameLayout {

    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: EditorItemSelectionAdapter

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

    fun setData(items: List<ItemSelection>, listener: EditorItemSelectionAdapter.EditorItemSelectionListener) {
        adapter = EditorItemSelectionAdapter(items, listener)

        recyclerView?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recyclerView?.adapter = adapter
    }

}