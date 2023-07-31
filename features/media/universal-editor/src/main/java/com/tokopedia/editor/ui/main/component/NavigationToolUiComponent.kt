package com.tokopedia.editor.ui.main.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editor.R
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.ui.main.adapter.NavigationToolAdapter
import com.tokopedia.picker.common.basecomponent.UiComponent

class NavigationToolUiComponent constructor(
    parent: ViewGroup
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

        mAdapter = NavigationToolAdapter()
        lstTool.adapter = mAdapter
    }

    override fun release() {
        super.release()
        mAdapter = null
    }
}
