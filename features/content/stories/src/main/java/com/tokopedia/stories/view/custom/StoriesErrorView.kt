package com.tokopedia.stories.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.LayoutErrorViewBinding

/**
 * @author by astidhiyaa on 09/10/23
 */
class StoriesErrorView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = LayoutErrorViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    var type: Type = Type.Unknown
        set(value) {
            field = value
            val (title, description, image) =
                when (value) {
                    Type.EmptyStories, Type.EmptyCategory -> Triple(
                        R.string.stories_content_unavailable,
                        null,
                        IconUnify.IMAGE
                    )

                    Type.NoInternet -> Triple(
                        R.string.stories_content_no_network_title,
                        R.string.stories_content_no_network_description,
                        IconUnify.SIGNAL_INACTIVE
                    )

                    Type.FailedLoad, Type.NoContent -> Triple(
                        null,
                        R.string.stories_retry_description,
                        IconUnify.RELOAD
                    )

                    else -> Triple(null, null, IconUnify.ERROR)
                }
            binding.storiesErrorIcon.setImage(newIconId = image)
            binding.storiesErrorTitle.text = title?.let { context.getString(it) }
            binding.storiesErrorDesc.text = description?.let { context.getString(it) }
            binding.btnStoriesNoInetRetry.showWithCondition(value != Type.EmptyStories && value != Type.EmptyCategory)
            binding.loaderGroup.showWithCondition(value != Type.EmptyStories && value != Type.EmptyCategory)
//            binding.root.translationZ = if (value == Type.NoContent || value == Type.EmptyCategory) 0f else 1f
        }

    fun setAction(action: (View) -> Unit) {
        binding.btnStoriesNoInetRetry.setOnClickListener(action)
    }

    fun setCloseAction(action: (View) -> Unit) {
        binding.icCloseLoading.setOnClickListener(action)
    }

    enum class Type {
        FailedLoad, EmptyStories, EmptyCategory, NoContent, NoInternet, Unknown;
    }
}
