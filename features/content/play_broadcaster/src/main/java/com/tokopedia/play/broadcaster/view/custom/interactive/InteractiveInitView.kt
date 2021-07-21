package com.tokopedia.play.broadcaster.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveInitBinding
import com.tokopedia.play_common.view.RoundedConstraintLayout

/**
 * Created by jegul on 07/07/21
 */
class InteractiveInitView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewPlayInteractiveInitBinding = ViewPlayInteractiveInitBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
    )

    private var mListener: Listener? = null

    init {
        binding.root.setCornerRadius(
                resources.getDimension(R.dimen.play_interactive_create_radius)
        )

        binding.root.setOnClickListener {
            mListener?.onCreateNewGameClicked(this)
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {

        fun onCreateNewGameClicked(view: InteractiveInitView)
    }
}