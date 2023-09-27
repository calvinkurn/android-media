package com.tokopedia.stories.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.stories.databinding.ViewStoriesOnboardingBinding

/**
 * @author by astidhiyaa on 28/08/23
 */
class StoriesOnboardView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewStoriesOnboardingBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    init {
        binding.lottieSwipeProduct.setFailureListener {  }
        binding.lottieTapNext.setFailureListener {  }
        binding.lottieTapMoveCategory.setFailureListener {  }
    }
}
