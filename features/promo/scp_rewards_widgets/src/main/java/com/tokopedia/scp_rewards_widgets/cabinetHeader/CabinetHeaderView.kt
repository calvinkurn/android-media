package com.tokopedia.scp_rewards_widgets.cabinetHeader

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCabinetHeaderBinding

class CabinetHeaderView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = WidgetCabinetHeaderBinding.inflate(LayoutInflater.from(context), this)

    fun bindData(data: CabinetHeader) {
        with(binding) {
            tvTitle.text = data.title
            tvSubTitle.text = data.subTitle
            with(parseColor(data.textColor) ?: Color.WHITE) {
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
        ivBackground.setImageUrl(backgroundUrl.orEmpty())
        binding.apply { setBackgroundColor(parseColor(backgroundColor) ?: Color.BLACK) }
    }
}
