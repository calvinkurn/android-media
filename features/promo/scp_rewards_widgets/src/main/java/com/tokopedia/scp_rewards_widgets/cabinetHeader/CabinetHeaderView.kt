package com.tokopedia.scp_rewards_widgets.cabinetHeader

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.scp_rewards_common.utils.loadImageOrFallback
import com.tokopedia.scp_rewards_common.utils.parseColor
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCabinetHeaderBinding

class CabinetHeaderView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetCabinetHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: CabinetHeader) {
        with(binding) {
            tvTitle.text = data.title.orEmpty()
            tvSubTitle.text = data.subTitle
            with(parseColor(data.textColor) ?: ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)) {
                tvTitle.setTextColor(this)
                tvSubTitle.setTextColor(this)
            }
            loadBackground(data.background, data.backgroundColor)
        }
    }

    private fun WidgetCabinetHeaderBinding.loadBackground(
        backgroundUrl: String?,
        backgroundColor: String?
    ) {
        ivBackground.loadImageOrFallback(backgroundUrl, R.drawable.ic_cabinet_header_background)
        binding.apply { setBackgroundColor(parseColor(backgroundColor) ?: Color.BLACK) }
    }
}
