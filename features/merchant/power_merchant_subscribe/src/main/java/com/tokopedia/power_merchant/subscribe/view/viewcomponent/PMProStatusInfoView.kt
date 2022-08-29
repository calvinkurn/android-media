package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmProStatusInfoBinding

/**
 * Created By @ilhamsuaib on 17/06/21
 */

class PMProStatusInfoView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding: ViewPmProStatusInfoBinding by lazy {
        ViewPmProStatusInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun showIcon() {
        binding.icPmProStatusInfo.show()
        val left = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_4
        )
        val top = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val right = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val bottom = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        binding.tvPmProStatusInfo.setMargin(left, top, right, bottom)
    }

    fun hideIcon() {
        binding.icPmProStatusInfo.hide()
        val left = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val top = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val right = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        val bottom = context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_0
        )
        binding.tvPmProStatusInfo.setMargin(left, top, right, bottom)
    }

    fun setText(stringId: Int) {
        binding.tvPmProStatusInfo.text = context.getString(stringId)
    }
}