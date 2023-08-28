package com.tokopedia.feedplus.browse.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.feedplus.databinding.ViewFeedBrowsePlaceholderBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

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


    private val binding = ViewFeedBrowsePlaceholderBinding.inflate(LayoutInflater.from(context), this, true)

    fun show(type: Type) {
        when(type) {
            Type.Title -> {
                binding.viewTitlePlaceholder.inflate()
            }
            Type.Chips -> {
                binding.viewChipsPlaceholder.inflate()
            }
            Type.Cards -> {
                binding.viewCardsPlaceholder.inflate()
            }
        }
    }
}
