package com.tokopedia.shareexperience.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shareexperience.ui.adapter.ShareExImageCarouselAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExHorizontalSpacingItemDecoration
import com.tokopedia.shareexperience.ui.listener.ShareExCarouselImageListener
import com.tokopedia.shareexperience.ui.listener.ShareExImageGeneratorListener
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel

class ShareExCarouselImageRecyclerView : RecyclerView, ShareExCarouselImageListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val horizontalSpacingItemDecoration = ShareExHorizontalSpacingItemDecoration(
        4.dpToPx(context.resources.displayMetrics)
    )
    private val imageAdapter = ShareExImageCarouselAdapter(this)
    private var imageGeneratorListener: ShareExImageGeneratorListener? = null

    init {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        adapter = imageAdapter
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }

    fun initData(newList: List<ShareExImageUiModel>) {
        imageAdapter.updateData(newList)
        scrollToPosition(0)
        imageGeneratorListener?.onImageChanged(newList.firstOrNull()?.imageUrl ?: "")
    }

    fun setImageGeneratorListener(listener: ShareExImageGeneratorListener) {
        imageGeneratorListener = listener
    }

    override fun onClickImage(position: Int, imageUrl: String) {
        val newList = imageAdapter.currentList.mapIndexed { index, item ->
            item.copy(isSelected = index == position)
        }
        imageAdapter.updateData(newList)
        imageGeneratorListener?.onImageChanged(imageUrl)
    }
}
