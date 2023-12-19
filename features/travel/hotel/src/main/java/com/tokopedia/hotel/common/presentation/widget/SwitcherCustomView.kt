package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.LayoutWidgetSwitcherBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by furqan on 25/04/19
 */
class SwitcherCustomView @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutWidgetSwitcherBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.SwitcherCustomView)

            binding.run {
                tvSwitcherLeftSubtitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_leftSubtitleText)
                tvSwitcherLeftTitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_leftTitleText)
                tvSwitcherRightSubtitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_rightSubtitleText)
                tvSwitcherRightTitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_rightTitleText)
            }

            attributeArray.recycle()
        }
    }

    fun setLeftSubtitleText(value: String) {
        binding.tvSwitcherLeftSubtitle.text = value
    }

    fun setLeftTitleText(value: String) {
        binding.tvSwitcherLeftTitle.text = value
    }

    fun setRightSubtitleText(value: String) {
        binding.tvSwitcherRightSubtitle.text = value
    }

    fun setRightTitleText(value: String) {
        binding.tvSwitcherRightTitle.text = value
    }

    private fun init(attrs: AttributeSet? = null) {
    }
}
