package com.tokopedia.product.addedit.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setTextOrGone
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TitleLayoutView : BaseCustomView {

    var isActionButtonVisible: Boolean = false
        set(value) {
            field = value
            refreshViews()
        }
    var title: String = ""
        set(value) {
            field = value
            refreshViews()
        }
    var subtitle: String = ""
        set(value) {
            field = value
            refreshViews()
        }
    var actionText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var titleLayoutViewTitle: Typography? = null
    var titleLayoutViewSubtitle: Typography? = null
    var titleLayoutViewActionText: Typography? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        titleLayoutViewActionText?.isEnabled = enabled
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_customview_title_layout_view, this)
        titleLayoutViewTitle = view.findViewById(R.id.typography_title)
        titleLayoutViewSubtitle = view.findViewById(R.id.typography_subtitle)
        titleLayoutViewActionText = view.findViewById(R.id.typography_action_button)

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleLayoutView, 0, 0)

            try {
                title = styledAttributes.getString(R.styleable.TitleLayoutView_titleLayoutViewTitle).orEmpty()
                subtitle = styledAttributes.getString(R.styleable.TitleLayoutView_titleLayoutViewSubtitle).orEmpty()
                actionText = styledAttributes.getString(R.styleable.TitleLayoutView_titleLayoutViewActionText).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        titleLayoutViewTitle?.setTextOrGone(title)
        titleLayoutViewSubtitle?.setTextOrGone(subtitle)
        titleLayoutViewActionText?.setTextOrGone(actionText)
        titleLayoutViewActionText?.isVisible = isActionButtonVisible
    }

    fun setActionButtonOnClickListener(onClickListener: OnClickListener) {
        titleLayoutViewActionText?.setOnClickListener(onClickListener)
    }
}
