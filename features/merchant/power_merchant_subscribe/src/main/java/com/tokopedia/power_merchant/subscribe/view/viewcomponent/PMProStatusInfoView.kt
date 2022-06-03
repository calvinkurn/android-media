package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmProMembershipCheckListViewBinding
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
        binding.tvPmProStatusInfo.setMargin(
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        )
    }

    fun hideIcon() {
        binding.icPmProStatusInfo.hide()
        binding.tvPmProStatusInfo.setMargin(
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        )
    }

    fun setText(stringId: Int) {
        binding.tvPmProStatusInfo.text = context.getString(stringId)
    }
}