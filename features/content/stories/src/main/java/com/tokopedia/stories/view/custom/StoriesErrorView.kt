package com.tokopedia.stories.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
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

    private var type : Type = Type.Unknown
        set(value) {
            field = value
            val (title, description, image) =
                when (value) {
                    else -> Triple ("Ini title", "ini desc", IconUnify.ERROR)
                }
            binding.storiesErrorIcon.setImage(newIconId = image)
            binding.storiesErrorTitle.text = title
            binding.storiesErrorDesc.text = description
        }

    //isRetryAble
    //hasShimmer / has upper
    //full screen

    fun setAction(action: (View) -> Unit) {
        binding.btnStoriesNoInetRetry.setOnClickListener(action)
        binding.btnStoriesNoInetRetry.show()
    }

    fun setCloseAction (action: (View) -> Unit) {
        binding.icCloseLoading.setOnClickListener(action)
        binding.icCloseLoading.show()
    }

    enum class Type {
        FailedLoad, NoContent, NoInternet, Unknown;
    }
}
