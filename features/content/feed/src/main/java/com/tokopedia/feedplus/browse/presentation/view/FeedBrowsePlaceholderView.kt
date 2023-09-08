package com.tokopedia.feedplus.browse.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ViewFeedBrowsePlaceholderBinding

/**
 * Created by meyta.taliti on 28/08/23.
 */
class FeedBrowsePlaceholderView : LinearLayout {

    enum class Type {
        Title,
        Chips,
        Cards;
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        ViewFeedBrowsePlaceholderBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private val dp12 = resources.getDimensionPixelOffset(R.dimen.feed_space_12)
    private val dp16 = resources.getDimensionPixelOffset(R.dimen.feed_space_16)

    fun show(type: Type) {
        removeCurrentView()
        val defaultLayoutParams = getDefaultLayoutParams()
        val placeholderView = when (type) {
            Type.Title -> {
                getView(R.layout.view_feed_browse_title_placeholder).apply {
                    layoutParams = defaultLayoutParams.apply {
                        topMargin = dp16
                        marginStart = dp16
                        marginEnd = dp16
                    }
                }
            }
            Type.Chips -> {
                getView(R.layout.view_feed_browse_chip_placeholder).apply {
                    layoutParams = defaultLayoutParams.apply {
                        topMargin = dp12
                        marginStart = dp16
                    }
                }
            }
            Type.Cards -> {
                getView(R.layout.view_feed_browse_card_placeholder).apply {
                    layoutParams = defaultLayoutParams.apply {
                        topMargin = dp12
                        bottomMargin = dp16
                        marginStart = dp16
                    }
                }
            }
        }
        addView(placeholderView)
    }

    private fun getView(@LayoutRes rLayout: Int): View {
        return LayoutInflater.from(context).inflate(rLayout, null)
    }

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getDefaultLayoutParams() =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}
