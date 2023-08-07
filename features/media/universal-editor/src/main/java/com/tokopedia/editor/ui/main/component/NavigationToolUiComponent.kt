package com.tokopedia.editor.ui.main.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editor.R
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.ui.main.adapter.NavigationToolAdapter
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.types.ToolType

class NavigationToolUiComponent constructor(
    parent: ViewGroup,
    private val listener: Listener? = null
) : UiComponent(parent, R.id.uc_navigation_tool) {

    private val lstTool: RecyclerView = findViewById(R.id.lst_tool)
    private var mAdapter: NavigationToolAdapter? = null

    fun setupView(tools: List<NavigationTool>) {
        setupRecyclerView()
        mAdapter?.setData(tools)
    }

    private fun setupRecyclerView() {
        lstTool.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mAdapter = NavigationToolAdapter {
            listener?.toolClicked(it.type)
        }

        lstTool.adapter = mAdapter
    }

    override fun release() {
        super.release()
        mAdapter = null
    }

    fun interface Listener {
        fun toolClicked(@ToolType type: Int)
    }
}
