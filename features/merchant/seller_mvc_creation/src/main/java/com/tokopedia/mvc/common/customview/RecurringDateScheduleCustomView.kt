package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

class RecurringDateScheduleCustomView : BaseCustomView {

    var firstSchedule: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var secondSchedule: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var isShowOtherScheduleButton: Boolean = false
        set(value) {
            field = value
            refreshViews()
        }

    var tpgFirstSchedule: Typography? = null
    var tpgSecondSchedule: Typography? = null
    var btnSeeOtherSchedule: Typography? = null
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
            View.inflate(context, R.layout.smvc_custom_view_voucher_schedule_repetition, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tpgFirstSchedule = view.findViewById(R.id.tpg_date_1)
        tpgSecondSchedule = view.findViewById(R.id.tpg_date_2)
        btnSeeOtherSchedule = view.findViewById(R.id.btn_see_other_schedule)
        cardParent = view.findViewById(R.id.card_parent)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes =
                context.obtainStyledAttributes(attrs, R.styleable.VoucherRecurringPeriodView, 0, 0)
            try {
                firstSchedule =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_first_period)
                        .orEmpty()
                secondSchedule =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_second_period)
                        .orEmpty()
                isShowOtherScheduleButton =
                    styledAttributes.getBoolean(
                        R.styleable.VoucherRecurringPeriodView_is_show_button,
                        false
                    )
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tpgFirstSchedule?.text = firstSchedule
        tpgSecondSchedule?.text = secondSchedule
        btnSeeOtherSchedule?.isVisible = isShowOtherScheduleButton
        cardParent?.cardType = CardUnify2.TYPE_BORDER_DISABLED
    }
}
