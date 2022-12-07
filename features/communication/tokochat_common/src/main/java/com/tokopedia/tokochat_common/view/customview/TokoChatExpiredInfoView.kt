package com.tokopedia.tokochat_common.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.tokochat_common.databinding.TokochatExpiredInfoBinding

class TokoChatExpiredInfoView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: TokochatExpiredInfoBinding? = null

    init {
        binding = TokochatExpiredInfoBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }

    fun setExpiredInfoDesc(description: String) {
        binding?.tokochatTvExpiredInfo?.text = description
    }
}
