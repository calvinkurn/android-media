package com.tokopedia.people.views.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.content.common.util.WindowWidthSizeClass
import com.tokopedia.content.common.util.calculateWindowSizeClass
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.people.databinding.ViewAdaptivePostShimmerBinding
import com.tokopedia.people.views.adapter.UserFeedPostsBaseAdapter
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.views.uimodel.content.PostUiModel
import com.tokopedia.content.common.R as contentcommonR

class AdaptivePostShimmerView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val activity: Activity? get() = context as? Activity

    private val binding = ViewAdaptivePostShimmerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val adapter = UserFeedPostsBaseAdapter(
        listener = object : UserFeedPostsBaseAdapter.FeedPostsCallback {
            override fun onFeedPostsClick(appLink: String, itemID: String, imageUrl: String, position: Int, mediaType: String) {}
            override fun onImpressFeedPostData(item: PostUiModel, position: Int) {}
        },
        onLoadMore = {}
    )

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        object : GridLayoutManager(activity, spanCount) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    private val spanCount by lazyThreadSafetyNone {
        when(activity?.calculateWindowSizeClass()?.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 3
            WindowWidthSizeClass.Medium -> 4
            WindowWidthSizeClass.Expanded -> 5
            else -> 3
        }
    }

    init {
        val spacing = context.resources.getDimensionPixelOffset(contentcommonR.dimen.content_common_space_1)
        binding.rvPostShimmer.adapter = adapter
        binding.rvPostShimmer.layoutManager = gridLayoutManager
        binding.rvPostShimmer.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

        adapter.setItemsAndAnimateChanges(
            List(spanCount * 4) {
                UserFeedPostsBaseAdapter.Model.Shimmer
            }
        )
    }
}
