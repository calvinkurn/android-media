package com.tokopedia.shareexperience.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.shareexperience.ui.adapter.ShareExChipsAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExHorizontalSpacingItemDecoration

class ShareExChipRecyclerView: RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val horizontalSpacingItemDecoration = ShareExHorizontalSpacingItemDecoration(
        8.dpToPx(context.resources.displayMetrics)
    )
    private val chipsAdapter = ShareExChipsAdapter()

    init {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        adapter = chipsAdapter
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }

    fun updateData(newList: List<ShareExChipModel>) {
        chipsAdapter.updateData(newList)
    }
}
