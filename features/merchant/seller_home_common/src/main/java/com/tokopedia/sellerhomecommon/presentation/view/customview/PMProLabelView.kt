package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcPmProLabelViewBinding
import timber.log.Timber

/**
 * Created by @ilhamsuaib on 14/06/22.
 */

class PMProLabelView : LinearLayout {

    private var binding: ShcPmProLabelViewBinding? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    fun setLabel(label: String, iconUrl: String) {
        binding?.run {
            setLabelBackground()
            imgShcPmProIcon.loadImage(iconUrl)
            tvShcPmProLabel.text = label
        }
    }

    private fun initView(context: Context) {
        binding = ShcPmProLabelViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    private fun setLabelBackground() {
        binding?.viewShcPmProLabel?.run {
            try {
                setBackgroundResource(R.drawable.shc_base_progress_bar)
            } catch (e: Exception) {
                Timber.e(e)
                setBackgroundColor(context.getResColor(R.color.shc_static_n75_dms))
            }
        }
    }
}