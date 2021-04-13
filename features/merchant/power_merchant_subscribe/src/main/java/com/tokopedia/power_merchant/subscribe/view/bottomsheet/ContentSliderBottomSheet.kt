package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.ContentSliderAdapter
import com.tokopedia.power_merchant.subscribe.view.model.ContentSliderUiModel
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottom_sheet_pm_content_slider.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 01/03/21
 */

class ContentSliderBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "ContentSliderBottomSheet"

        fun createInstance(fm: FragmentManager): ContentSliderBottomSheet {
            return (fm.findFragmentByTag(TAG) as? ContentSliderBottomSheet)
                    ?: ContentSliderBottomSheet().apply {
                        clearContentPadding = true
                    }
        }
    }

    private var mTitle: String = ""
    private var primaryCtaClickCallback: (() -> Unit)? = null
    private var secondaryCtaClickCallback: (() -> Unit)? = null
    private var items: List<ContentSliderUiModel> = emptyList()

    private val sliderAdapter by lazy { ContentSliderAdapter() }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_content_slider

    override fun setupView() = childView?.run {
        setupSliderAdapter()

        btnPmContentSlider.setOnClickListener {
            setOnPrimaryBtnClicked()
        }
        tvPmContentSliderCta.setOnClickListener {
            secondaryCtaClickCallback?.invoke()
        }
    }

    private fun setOnPrimaryBtnClicked() {
        childView?.run {
            val indicatorCurrentPosition = indicatorPmContentSlider.indicatorCurrentPosition
            if (indicatorCurrentPosition == items.size.minus(1)) {
                primaryCtaClickCallback?.invoke()
            } else {
                rvPmContentSlider.smoothScrollToPosition(indicatorCurrentPosition.plus(1))
            }
        }
    }

    fun setContent(title: String, items: List<ContentSliderUiModel>) {
        this.mTitle = title
        this.items = items
    }

    fun setOnPrimaryCtaClickListener(action: () -> Unit) {
        this.primaryCtaClickCallback = action
    }

    fun setOnSecondaryCtaClickListener(action: () -> Unit) {
        secondaryCtaClickCallback = action
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupSliderAdapter() = childView?.run {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPmContentSlider.layoutManager = layoutManager
        rvPmContentSlider.adapter = sliderAdapter

        try {
            PagerSnapHelper().attachToRecyclerView(rvPmContentSlider)
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }

        rvPmContentSlider.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                setSelectedPageIndicator(layoutManager)
            }
        })

        indicatorPmContentSlider.setIndicator(items.size)
        if (items.size == 1) {
            indicatorPmContentSlider.gone()
        } else {
            indicatorPmContentSlider.visible()
        }

        tvPmContentSliderTitle.text = mTitle

        sliderAdapter.items = items
        sliderAdapter.notifyDataSetChanged()
    }

    private fun setSelectedPageIndicator(layoutManager: LinearLayoutManager) = childView?.run {
        val position = layoutManager.findFirstCompletelyVisibleItemPosition().orZero()
        if (position >= 0) {
            indicatorPmContentSlider.setCurrentIndicator(position)
        }

        if (position == items.size.minus(1)) {
            tvPmContentSliderCta.visible()
            btnPmContentSlider.buttonVariant = UnifyButton.Variant.FILLED
            btnPmContentSlider.text = context.getString(R.string.pm_content_slider_last_slide_button)
        } else {
            tvPmContentSliderCta.invisible()
            btnPmContentSlider.buttonVariant = UnifyButton.Variant.GHOST
            btnPmContentSlider.text = context.getString(R.string.pm_label_next)
        }
    }
}