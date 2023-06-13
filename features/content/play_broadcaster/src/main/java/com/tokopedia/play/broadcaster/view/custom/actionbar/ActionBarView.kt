package com.tokopedia.play.broadcaster.view.custom.actionbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroActionBarBinding

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
class ActionBarView : ConstraintLayout  {
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

    private val binding = ViewPlayBroActionBarBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    init {
        binding.icBroPreparationClose.setOnClickListener { mListener?.onClickClosePreparation() }
    }

    fun setShopName(label: String) {
        binding.tvBroPreparationShopName.text = MethodChecker.fromHtml(label)
    }

    fun setShopIcon(iconUrl: String) {
        binding.ivBroPreparationShopIcon.setImageUrl(iconUrl)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    interface Listener {
        fun onClickClosePreparation()
    }
}