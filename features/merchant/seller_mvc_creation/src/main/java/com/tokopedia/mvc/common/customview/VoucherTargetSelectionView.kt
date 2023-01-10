package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class VoucherTargetSelectionView : BaseCustomView {
    var isActive: Boolean = false
        set(value) {
            field = value
            refreshViews()
        }

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

    var tpgTitle: Typography? = null
    var tpgDescription: Typography? = null
    var imgVoucherTarget: ImageUnify? = null
    var cardParent: CardUnify2? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view =
            View.inflate(context, R.layout.smvc_custom_view_voucher_creation_voucher_target, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tpgTitle = view.findViewById(R.id.tpg_title_voucher_target)
        tpgDescription = view.findViewById(R.id.tpg_description_voucher_target)
        imgVoucherTarget = view.findViewById(R.id.icon_voucher_target)
        cardParent = view.findViewById(R.id.card_parent_voucher_target)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes =
                context.obtainStyledAttributes(attrs, R.styleable.VoucherTargetSelectionView, 0, 0)
            try {
                titleText =
                    styledAttributes.getString(R.styleable.VoucherTargetSelectionView_voucher_target_title_text)
                        .orEmpty()
                descriptionText =
                    styledAttributes.getString(R.styleable.VoucherTargetSelectionView_voucher_target_description_text)
                        .orEmpty()
                isActive = styledAttributes.getBoolean(
                    R.styleable.VoucherTargetSelectionView_voucher_target_description_text,
                    false
                )
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tpgTitle?.text = titleText
        tpgDescription?.text = MethodChecker.fromHtml(descriptionText)
        if (isActive) {
            cardParent?.cardType = CardUnify2.TYPE_BORDER_ACTIVE
        } else {
            cardParent?.cardType = CardUnify2.TYPE_BORDER
        }
    }
}
