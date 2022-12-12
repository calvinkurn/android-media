package com.tokopedia.play_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play_common.databinding.BottomSheetHeaderBinding

/**
 * @author by astidhiyaa on 02/12/22
 */
class PlayHeaderView : ConstraintLayout {
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

    private val binding = BottomSheetHeaderBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    var title: String = ""
        set(value) {
            field = value
            binding.tvSheetTitle.text = value
        }

    var closeListener: OnClickListener = OnClickListener {  }
        set(value) {
            field = value
            binding.ivSheetClose.setOnClickListener {
                value.onClick(it)
            }
        }
}
