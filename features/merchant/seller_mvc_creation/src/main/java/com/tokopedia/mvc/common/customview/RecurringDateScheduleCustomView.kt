package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

class RecurringDateScheduleCustomView : BaseCustomView {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_ERROR = 1
    }

    var title: String = ""
        set(value) {
            field = value
            refreshViews()
        }

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

    var type: Int = TYPE_NORMAL
        set(value) {
            field = value
            refreshViews()
        }

    var isShowOtherScheduleButton: Boolean = false
        set(value) {
            field = value
            refreshViews()
        }

    var tptTitle: Typography? = null
    var tpgFirstSchedule: Typography? = null
    var tpgSecondSchedule: Typography? = null
    var iconTooltip: ImageView? = null
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
        tptTitle = view.findViewById(R.id.tpg_title)
        tpgFirstSchedule = view.findViewById(R.id.tpg_date_1)
        tpgSecondSchedule = view.findViewById(R.id.tpg_date_2)
        iconTooltip = view.findViewById(R.id.ic_tooltip)
        btnSeeOtherSchedule = view.findViewById(R.id.btn_see_other_schedule)
        cardParent = view.findViewById(R.id.card_parent)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes =
                context.obtainStyledAttributes(attrs, R.styleable.VoucherRecurringPeriodView, 0, 0)
            try {
                title =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_title)
                        .orEmpty()
                firstSchedule =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_first_period)
                        .orEmpty()
                secondSchedule =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_second_period)
                        .orEmpty()
                type =
                    styledAttributes.getString(R.styleable.VoucherRecurringPeriodView_voucher_recurring_type)
                        .toIntOrZero()

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
        tptTitle?.text = title
        tpgFirstSchedule?.text = firstSchedule
        tpgSecondSchedule?.text = secondSchedule
        btnSeeOtherSchedule?.isVisible = isShowOtherScheduleButton
        if (type == TYPE_NORMAL) {
            cardParent?.apply {
                setCardUnifyBackgroundColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN50
                    )
                )
            }
            iconTooltip?.visible()
        } else {
            cardParent?.apply {
                setCardUnifyBackgroundColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN50
                    )
                )
            }
            iconTooltip?.gone()
        }
    }
}
