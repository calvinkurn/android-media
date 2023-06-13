package com.tokopedia.tkpd.flashsale.presentation.common.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TitleValueView : BaseCustomView {

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

    var value: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var status: IconStatusEnum = IconStatusEnum.NO_ICON_STATUS
        set(value) {
            field = value
            refreshIcon()
        }

    var tfValue: Typography? = null
    var tfTitle: Typography? = null
    var tfSubtitle: Typography? = null
    var iconRating: IconUnify? = null
    var iconStatus: IconUnify? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.ssfs_customview_title_value, this)
        setupViews(view)
        defineCustomAttributes(attrs)
    }

    private fun setupViews(view: View) {
        tfTitle = view.findViewById(R.id.tfTitle)
        tfSubtitle = view.findViewById(R.id.tfSubtitle)
        tfValue = view.findViewById(R.id.tfValue)
        iconRating = view.findViewById(R.id.iconRating)
        iconStatus = view.findViewById(R.id.iconStatus)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TextValueView)
            try {
                val statusInt = styledAttributes.getInteger(R.styleable.TextValueView_textvalue_icon_status, NO_ICON)
                title = styledAttributes.getString(R.styleable.TextValueView_textvalue_title).orEmpty()
                subtitle = styledAttributes.getString(R.styleable.TextValueView_textvalue_subtitle).orEmpty()
                value = styledAttributes.getString(R.styleable.TextValueView_textvalue_value).orEmpty()
                status = IconStatusEnum.values().first { it.ordinal == statusInt }
                iconRating?.isVisible = styledAttributes.getBoolean(R.styleable.TextValueView_textvalue_icon_rating_visibility, false)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tfTitle?.text = title
        tfSubtitle?.text = subtitle
        tfValue?.text = value
        tfSubtitle?.isVisible = subtitle.isNotEmpty()
    }

    private fun refreshIcon() {
        val colorForegroundPassed = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val colorBackgroundPassed = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN100)
        val colorForegroundWarning = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        val colorBackgroundWarning = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN100)

        when(status) {
            IconStatusEnum.PASSED_STATUS -> {
                iconStatus?.visible()
                iconStatus?.setImage(IconUnify.CHECK_BIG, colorForegroundPassed)
                iconStatus?.backgroundTintList = ColorStateList.valueOf(colorBackgroundPassed)
            }
            IconStatusEnum.WARNING_STATUS -> {
                iconStatus?.visible()
                iconStatus?.setImage(IconUnify.WARNING, colorForegroundWarning)
                iconStatus?.backgroundTintList = ColorStateList.valueOf(colorBackgroundWarning)
            }
            else -> {
                iconStatus?.gone()
            }
        }
    }

    fun setStatusPassed(isPassed: Boolean) {
        val icon = if (isPassed) IconStatusEnum.PASSED_STATUS else IconStatusEnum.WARNING_STATUS
        status = icon
    }

    enum class IconStatusEnum(value: Int) {
        NO_ICON_STATUS(NO_ICON),
        PASSED_STATUS(PASSED),
        WARNING_STATUS(WARNING)
    }

    companion object {
        private const val NO_ICON = 0
        private const val PASSED = 1
        private const val WARNING = 2
    }
}
