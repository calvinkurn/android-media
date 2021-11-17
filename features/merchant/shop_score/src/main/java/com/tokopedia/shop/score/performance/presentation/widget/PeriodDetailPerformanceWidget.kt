package com.tokopedia.shop.score.performance.presentation.widget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemSectionDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.model.BasePeriodDetailUiModel
import timber.log.Timber

class PeriodDetailPerformanceWidget: ConstraintLayout {

    val binding: ItemSectionDetailPerformanceBinding?
        get() = _binding

    private var _binding: ItemSectionDetailPerformanceBinding? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        _binding = ItemSectionDetailPerformanceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(
        element: BasePeriodDetailUiModel?
    ) {
        setupViews(element)
    }

    private fun setupViews(element: BasePeriodDetailUiModel?) {
        binding?.run {
            tvPerformanceDetailLabel.text =
                context.getString(R.string.title_detail_performa, element?.period.orEmpty())
            tvPerformanceDetailDate.text =
                context.getString(R.string.title_update_date, element?.nextUpdate.orEmpty())

            if (element?.isNewSeller == true) {
                tvPerformanceDetailLabel.text =
                    context.getString(R.string.title_detail_performance_new_seller)
                tvPerformanceDetailDate.text =
                    root.context?.getString(R.string.title_update_date_new_seller, element.period)
                tvPerformanceDetailDateNewSeller.text =
                    context.getString(R.string.title_update_date, element.nextUpdate)
            } else {
                tvPerformanceDetailLabel.text =
                    context.getString(R.string.title_detail_performa, element?.period.orEmpty())
                tvPerformanceDetailDate.text =
                    context.getString(R.string.title_update_date, element?.nextUpdate.orEmpty())
            }

            tvPerformanceDetailDateNewSeller.showWithCondition(element?.isNewSeller == true)
        }
    }
}