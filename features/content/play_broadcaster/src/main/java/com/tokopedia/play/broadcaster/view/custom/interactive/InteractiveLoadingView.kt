package com.tokopedia.play.broadcaster.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveLoadingBinding
import com.tokopedia.play_common.view.RoundedConstraintLayout

/**
 * Created by jegul on 07/07/21
 */
class InteractiveLoadingView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        ViewPlayInteractiveLoadingBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
        )
    }
}