package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcWidgetTitleViewBinding
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created by @ilhamsuaib on 24/08/22.
 */

class WidgetTitleView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var binding: ShcWidgetTitleViewBinding? = null

    private fun initView(context: Context) {
        View.inflate(context, R.layout.shc_widget_title_view, this)
        binding = ShcWidgetTitleViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun setText(text: String) {
        binding?.tvShcWidgetTitleView?.text = text.parseAsHtml()
    }

    fun showTooltipIcon(shouldShow: Boolean, onIconClicked: () -> Unit) {
        if (shouldShow) {
            binding?.tvShcWidgetTitleView?.setUnifyDrawableEnd(IconUnify.INFORMATION)
            binding?.tvShcWidgetTitleView?.setOnClickListener {
                onIconClicked()
            }
        } else {
            binding?.tvShcWidgetTitleView?.clearUnifyDrawableEnd()
            binding?.tvShcWidgetTitleView?.setOnClickListener(null)
        }
    }

    fun showTag(tag: String) {
        val isTagVisible = tag.isNotBlank()
        binding?.notifShcTag?.showWithCondition(isTagVisible)
        if (isTagVisible) {
            binding?.notifShcTag?.setNotification(
                tag,
                NotificationUnify.TEXT_TYPE,
                NotificationUnify.COLOR_TEXT_TYPE
            )
        }
    }
}