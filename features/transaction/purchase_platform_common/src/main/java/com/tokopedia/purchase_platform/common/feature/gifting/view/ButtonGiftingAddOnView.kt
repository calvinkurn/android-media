package com.tokopedia.purchase_platform.common.feature.gifting.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ButtonGiftingAddOnView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr)  {

    private var leftIcon: IconUnify? = null
    private var titleLabel: Typography? = null
    private var descLabel: Typography? = null

    private fun getLayout(): Int {
        return R.layout.item_gifting_add_on
    }

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

    var chevronIcon: String = ""
        set(value) {
            field = value
            initView()
        }

    init {
        inflate(context, getLayout(), this)

        leftIcon = findViewById(R.id.icon_addon_product)
        titleLabel = findViewById(R.id.title_addon)

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.GiftingAddonButtonView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.GiftingAddonButtonView_stateButton, 1))
            title = styledAttributes.getString(R.styleable.GiftingAddonButtonView_title) ?: ""
            desc = styledAttributes.getString(R.styleable.GiftingAddonButtonView_desc) ?: ""
            chevronIcon = styledAttributes.getString(R.styleable.GiftingAddonButtonView_icon_right) ?: ""

        } finally {
            styledAttributes.recycle()
        }
    }

    private fun initView() {
        when (state) {
            // State.LOADING -> setViewLoading()
            State.ACTIVE -> setViewActive()
            State.INACTIVE -> setViewInactive()
        }

        setChevronIcon()

        invalidate()
        requestLayout()
    }

    private fun setChevronIcon() {
        if (chevronIcon.isEmpty()) {
            leftIcon?.setImage(IconUnify.PRODUCT)
        } else {
            // do the icon need to be changed?
            // if not, then use iconUnify instead
            // leftIcon?.
        }
    }

    fun setListenerChevronIcon(actionListener: () -> Unit) {
        leftIcon?.setOnClickListener {
            actionListener.invoke()
        }
    }

    private fun setViewActive() {

        titleLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.dark_N75))
        titleLabel?.text = title
        if (desc.isEmpty()) {
            descLabel?.gone()
        } else {
            descLabel?.visible()
            descLabel?.text = desc
            titleLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.light_N700_68))
        }
    }

    private fun setViewInactive() {
        titleLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
        descLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
    }

    enum class State(val id: Int) : Parcelable {

        // LOADING(0),
        ACTIVE(1),
        INACTIVE(2);

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<State> {

            fun fromId(id: Int): State {
                for (state: State in values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return ACTIVE
            }

            override fun createFromParcel(parcel: Parcel): State {
                return values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }
}