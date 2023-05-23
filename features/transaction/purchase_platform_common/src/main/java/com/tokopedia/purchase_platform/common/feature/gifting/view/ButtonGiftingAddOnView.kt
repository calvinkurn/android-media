package com.tokopedia.purchase_platform.common.feature.gifting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.ItemGiftingAddOnBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ButtonGiftingAddOnView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: ItemGiftingAddOnBinding? = ItemGiftingAddOnBinding.inflate(LayoutInflater.from(context), this, true)

    var state: State = State.ACTIVE
        set(value) {
            field = value
            initView()
        }

    var title: String = ""
        set(value) {
            field = value
            initView()
        }

    var desc: String = ""
        set(value) {
            field = value
            initView()
        }

    var urlLeftIcon: String = ""
        set(value) {
            field = value
            initView()
        }

    var urlRightIcon: String = ""
        set(value) {
            field = value
            initView()
        }

    init {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.GiftingButtonView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.GiftingButtonView_stateAddon, 1))
            title = styledAttributes.getString(R.styleable.GiftingButtonView_title) ?: ""
            desc = styledAttributes.getString(R.styleable.GiftingButtonView_desc) ?: ""
            urlLeftIcon = styledAttributes.getString(R.styleable.GiftingButtonView_icon_left) ?: ""
            urlRightIcon = styledAttributes.getString(R.styleable.GiftingButtonView_icon_right) ?: ""
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun initView() {
        when (state) {
            State.ACTIVE -> setViewActive()
            State.INACTIVE -> setViewInactive()
        }

        loadLeftImage()
        loadRightImage()
        invalidate()
        requestLayout()
    }

    private fun loadLeftImage() {
        binding?.iconLeft?.loadImage(urlLeftIcon)
    }

    private fun loadRightImage() {
        if (urlRightIcon.isEmpty()) {
            binding?.iconRight?.gone()
        } else {
            binding?.iconRight?.visible()
            binding?.iconRight?.loadImage(urlRightIcon)
        }
    }

    private fun setViewActive() {
        binding?.run {
            titleAddon.visible()
            titleAddon.text = title
            titleAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))

            if (desc.isEmpty()) {
                descAddon.gone()
            } else {
                descAddon.visible()
                descAddon.text = desc
                descAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            }
        }
    }

    private fun setViewInactive() {
        binding?.run {
            titleAddon.text = title
            titleAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))

            if (desc.isEmpty()) {
                descAddon.gone()
            } else {
                descAddon.visible()
                descAddon.text = desc
                descAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            }
        }
    }

    enum class State(val id: Int) {
        ACTIVE(1),
        INACTIVE(2);

        companion object {
            fun fromId(id: Int): State {
                for (state: State in values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return ACTIVE
            }
        }
    }
}
