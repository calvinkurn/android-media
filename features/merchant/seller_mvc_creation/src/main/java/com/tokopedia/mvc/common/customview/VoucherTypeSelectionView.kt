package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class VoucherTypeSelectionView: BaseCustomView {
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
    var imgVoucherType: ImageUnify? = null
    var radioButton: RadioButtonUnify? = null
    var cardParent: CardUnify2? = null

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
        val view = View.inflate(context, R.layout.smvc_custom_view_voucher_type_selection, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tpgTitle = view.findViewById(R.id.tpg_title)
        tpgDescription = view.findViewById(R.id.tpg_description)
        imgVoucherType = view.findViewById(R.id.image_voucher)
        radioButton = view.findViewById(R.id.radio_btn_voucher)
        cardParent = view.findViewById(R.id.card_parent)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.VoucherTypeSelectionView, 0, 0)
            try {
                titleText = styledAttributes.getString(R.styleable.VoucherTypeSelectionView_voucher_type_title_text).orEmpty()
                descriptionText = styledAttributes.getString(R.styleable.VoucherTypeSelectionView_voucher_type_description_text).orEmpty()
                isActive = styledAttributes.getBoolean(R.styleable.VoucherTypeSelectionView_is_voucher_type_active, false)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        try {
            tpgTitle?.text = titleText
            tpgDescription?.text = descriptionText
            radioButton?.isChecked = isActive
            if (isActive) {
                cardParent?.cardType = CardUnify2.TYPE_BORDER_ACTIVE
            } else {
                cardParent?.cardType = CardUnify2.TYPE_BORDER
            }
        } catch (t: Throwable) { }
    }
}
