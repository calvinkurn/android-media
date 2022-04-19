package com.tokopedia.homenav.mainnav.view.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NavRecyclerViewLayoutManager(context: Context, val windowHeight: Int) : LinearLayoutManager(context) {

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        extraLayoutSpace[0] = 15*windowHeight
        extraLayoutSpace[1] = 15*windowHeight
        super.calculateExtraLayoutSpace(state, extraLayoutSpace)
    }
}