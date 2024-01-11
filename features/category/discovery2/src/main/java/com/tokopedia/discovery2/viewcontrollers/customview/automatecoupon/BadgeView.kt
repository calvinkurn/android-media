package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.discovery2.databinding.BadgeLayoutBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IBadgeView {

    private var binding = BadgeLayoutBinding.inflate(LayoutInflater.from(context), this)
    override fun render(text: String?) {
        if (text.isNullOrEmpty()) {
            binding.badgeGroup.hide()
            return
        }

        binding.badgeGroup.show()
        binding.tvBadge.text = text
    }
}
