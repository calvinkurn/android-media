package com.tokopedia.purchase_platform.common.feature.gifting.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ButtonGiftingAddOnView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr)  {

    private var leftImage: ImageUnify? = null
    private var rightImage: ImageUnify? = null
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
        inflate(context, getLayout(), this)

        leftImage = findViewById(R.id.icon_left)
        rightImage = findViewById(R.id.icon_right)
        titleLabel = findViewById(R.id.title_addon)
        descLabel = findViewById(R.id.desc_addon)

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
        if (urlLeftIcon.isNotEmpty()) {
            leftImage?.loadImage(urlLeftIcon)
        } else {
            leftImage?.gone()
        }
    }

    private fun loadRightImage() {
        if (urlRightIcon.isNotEmpty()) {
            rightImage?.loadImage(urlRightIcon)
        } else {
            rightImage?.gone()
        }
    }

    fun setOnButtonClickedListener(actionListener: () -> Unit) {
        rootView.setOnClickListener {
            actionListener.invoke()
        }
    }

    private fun setViewActive() {
        titleLabel?.visible()
        descLabel?.visible()
        titleLabel?.text = title
        descLabel?.text = desc
    }

    private fun setViewInactive() {
        titleLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
        descLabel?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))

        titleLabel?.text = title
        descLabel?.text = desc
    }

    enum class State(val id: Int) : Parcelable {
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