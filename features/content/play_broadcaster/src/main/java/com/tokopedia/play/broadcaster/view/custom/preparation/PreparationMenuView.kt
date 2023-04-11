package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import com.tokopedia.content.common.R.color.Unify_Static_White
import com.tokopedia.content.common.R.color.content_dms_white_disable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationMenuBinding

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
class PreparationMenuView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayBroPreparationMenuBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    init {
        binding.clBroSetTitle.setOnClickListener { mListener?.onClickSetTitle() }
    }

    fun isSetTitleChecked(isChecked: Boolean) = with(binding) {
        icBroTitleChecked.showWithCondition(isChecked)
        val stateColor = if (isChecked) {
            clBroSetCover.setOnClickListener { mListener?.onClickSetCover() }
            clBroSetProduct.setOnClickListener { mListener?.onClickSetProduct() }
            clBroSetSchedule.setOnClickListener { mListener?.onClickSetSchedule() }
            getColor(context, Unify_Static_White)
        } else {
            clBroSetCover.setOnClickListener(null)
            clBroSetProduct.setOnClickListener(null)
            clBroSetSchedule.setOnClickListener(null)
            getColor(context, content_dms_white_disable)
        }
        icBroSetCover.setImage(newLightEnable = stateColor, newDarkEnable = stateColor)
        tvBroSetCover.setTextColor(stateColor)
        icBroSetProduct.setImage(newLightEnable = stateColor, newDarkEnable = stateColor)
        tvBroSetProduct.setTextColor(stateColor)
        icBroSetSchedule.setImage(newLightEnable = stateColor, newDarkEnable = stateColor)
        tvBroSetSchedule.setTextColor(stateColor)
    }

    fun isSetCoverChecked(isChecked: Boolean) {
        binding.icBroCoverChecked.showWithCondition(isChecked)
    }

    fun isSetProductChecked(isChecked: Boolean) {
        binding.icBroProductChecked.showWithCondition(isChecked)
    }

    fun isSetScheduleChecked(isChecked: Boolean) {
        binding.icBroScheduleChecked.showWithCondition(isChecked)
    }

    fun showScheduleMenu(shouldShow: Boolean) {
        binding.clBroSetSchedule.showWithCondition(shouldShow)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    interface Listener {
        fun onClickSetTitle()
        fun onClickSetCover()
        fun onClickSetProduct()
        fun onClickSetSchedule()
    }
}
