package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class SpendingEstimationLabelView : BaseCustomView {

    var titleText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var descriptionText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var spendingEstimationText: String = ""
        set(value) {
            field = value
            refreshViews()
        }


    var tpgTitle: Typography? = null
    var iconInfo: IconUnify? = null
    var tpgDescription: Typography? = null
    var tpgSpendingValue: Typography? = null

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
        val view = View.inflate(context, R.layout.smvc_customview_spending_estimation_label_view, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tpgTitle = view.findViewById(R.id.tpg_title)
        iconInfo = view.findViewById(R.id.info_icon)
        tpgDescription = view.findViewById(R.id.tpg_description)
        tpgSpendingValue = view.findViewById(R.id.tpg_spending_estimation)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SpendingEstimationLabelView, 0, 0)
            try {
                titleText = styledAttributes.getString(R.styleable.SpendingEstimationLabelView_title_placeholder_text).orEmpty()
                descriptionText = styledAttributes.getString(R.styleable.SpendingEstimationLabelView_description_placeholder_text).orEmpty()
                spendingEstimationText = styledAttributes.getString(R.styleable.SpendingEstimationLabelView_spending_estimation_placeholder_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tpgTitle?.text = titleText
        tpgDescription?.text = MethodChecker.fromHtml(descriptionText)
        tpgSpendingValue?.text = spendingEstimationText
    }
}
