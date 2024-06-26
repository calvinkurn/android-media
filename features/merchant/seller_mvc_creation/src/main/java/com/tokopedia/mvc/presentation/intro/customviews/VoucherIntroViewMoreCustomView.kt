package com.tokopedia.mvc.presentation.intro.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.mvc.databinding.SmvcCustomviewIntroViewMoreBinding
import com.tokopedia.unifycomponents.BaseCustomView

class VoucherIntroViewMoreCustomView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var binding: SmvcCustomviewIntroViewMoreBinding? = null
    var listener: ViewMoreListener? = null

    fun setUpListener(listener: ViewMoreListener) {
        this.listener = listener
    }

    init {
        init()
    }

    private fun init() {
        binding = SmvcCustomviewIntroViewMoreBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.root?.apply {
            setOnClickListener {
                listener?.onClickButton()
                hide()
            }
        }
    }

    interface ViewMoreListener {
        fun onClickButton()
    }
}
