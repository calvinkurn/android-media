package com.tokopedia.kyc_centralized.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.ItemBenefitListBinding
import com.tokopedia.media.loader.loadIcon

class KycBenefitItemView: ConstraintLayout {

    private var viewBinding: ItemBenefitListBinding = ItemBenefitListBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var iconUrl: String = ""
        set(value) = viewBinding.benefitIcon.loadIcon(value)

    var title: String = ""
        set(value) {
            viewBinding.benefitTitle.text = value
            field = value
        }

    var description: String = ""
        set(value) {
            viewBinding.benefitDescription.text = value
            field = value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        setAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        setAttributes(attributeSet)
    }

    private fun setAttributes(attributeSet: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.KycBenefitItemView,
            0,
            0
        )

        iconUrl = typedArray.getString(R.styleable.KycBenefitItemView_iconUrl).orEmpty()
        title = typedArray.getString(R.styleable.KycBenefitItemView_title).orEmpty()
        description = typedArray.getString(R.styleable.KycBenefitItemView_description).orEmpty()

        typedArray.recycle()
    }
}
