package com.tokopedia.discovery_component.widgets.automatecoupon

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.discovery_component.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IBadgeView {

    private var badgeGroup: Group? = null
    private var tvBadge: Typography? = null

    init {
        val view = LayoutInflater.from(context).inflate(LAYOUT, this)

        badgeGroup = view.findViewById(R.id.badgeGroup)
        tvBadge = view.findViewById(R.id.tvBadge)
    }

    override fun render(text: String?) {
        if (text.isNullOrEmpty()) {
            badgeGroup?.hide()
            return
        }

        badgeGroup?.show()
        tvBadge?.text = text
    }

    companion object {

        @LayoutRes
        private val LAYOUT = R.layout.automate_coupon_badge_layout
    }
}
