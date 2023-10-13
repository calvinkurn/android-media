package com.tokopedia.scp_rewards_widgets.medal_footer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards_widgets.databinding.WidgetMedalTaskFooterBinding
import com.tokopedia.unifycomponents.UnifyButton

class WidgetMedalFooter(private val context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalTaskFooterBinding.inflate(LayoutInflater.from(context), this)
    private var list = mutableListOf<UnifyButton>()

    private companion object {
        private const val STYLE_PRIMARY = "primary"
        private const val STYLE_SECONDARY = "secondary"
    }

    fun bindData(list: List<FooterData>, onButtonClick: (FooterData) -> Unit) {
        list.forEach { footer ->
            val button = UnifyButton(context)
            button.apply {
                id = View.generateViewId()
                text = footer.text
                buttonSize = UnifyButton.Size.MEDIUM
                layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                    horizontalWeight = 1f
                }
                isLoading = footer.isLoading
                footer.id = id
                applyStyle(footer)
                setOnClickListener {
                    isLoading = true
                    onButtonClick(footer)
                    isLoading = false
                }
            }
            this.list.add(button)
            (binding.root as ConstraintLayout).addView(button)
            binding.flowCta.addView(button)
        }
    }

    fun showLoading(id: Int?, isLoading: Boolean) {
        list.find { it.id == id }?.isLoading = isLoading
    }

    private fun UnifyButton.applyStyle(footer: FooterData) {
        when (footer.style) {
            STYLE_PRIMARY -> {
                this.buttonType = UnifyButton.Type.MAIN
                this.buttonVariant = UnifyButton.Variant.FILLED
            }

            STYLE_SECONDARY -> {
                this.buttonType = UnifyButton.Type.ALTERNATE
                this.buttonVariant = UnifyButton.Variant.GHOST
            }

            else -> {
                this.buttonType = UnifyButton.Type.MAIN
                this.buttonVariant = UnifyButton.Variant.FILLED
            }
        }
    }
}
