package com.tokopedia.digital.newcart.presentation.compoundview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.R

class DigitalCartMyBillsView : FrameLayout {

    private lateinit var titleContainer: ConstraintLayout
    private lateinit var headerTitleTextView: TextView
    private lateinit var subscriptionCheckbox: CheckBox
    private lateinit var descriptionTextView: TextView
    private lateinit var moreInfoIcon: ImageView

    private var moreInfoClickListener: OnMoreInfoClickListener? = null
    set(value) {
        field = value
        field?.let { listener -> moreInfoIcon.setOnClickListener { listener.onMoreInfoClicked() }}
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_holder_digital_cart_my_bills, this, true)
        titleContainer = view.findViewById(R.id.rl_title_container)
        headerTitleTextView = view.findViewById(R.id.tv_header_title)
        subscriptionCheckbox = view.findViewById(R.id.cb_subscription)
        descriptionTextView = view.findViewById(R.id.tv_description)
        moreInfoIcon = view.findViewById(R.id.ic_more_info)

        // Enlarge touchable area
        titleContainer.post {
            val delegateArea = Rect()
            moreInfoIcon.getHitRect(delegateArea)

            delegateArea.top -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.left -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.bottom += INFO_TOUCH_AREA_SIZE_PX
            delegateArea.right += INFO_TOUCH_AREA_SIZE_PX

            titleContainer.apply { touchDelegate = TouchDelegate(delegateArea, moreInfoIcon) }
        }
    }

    fun getSubscriptionCheckbox(): CheckBox = subscriptionCheckbox

    fun setDescription(description: String) {
        descriptionTextView.text = MethodChecker.fromHtml(description)
    }

    fun setDescription(description: CharSequence) {
        descriptionTextView.text = description
    }

    fun setChecked(checked: Boolean) {
        subscriptionCheckbox.isChecked = checked
    }

    fun setHeaderTitle(title: String) {
        headerTitleTextView.text = title
    }

    fun isChecked(): Boolean = subscriptionCheckbox.isChecked

    fun hasMoreInfo(state: Boolean) {
        moreInfoIcon.visibility = if (state) View.VISIBLE else View.GONE
    }

    fun setOnCheckedChangeListener(listener : CompoundButton.OnCheckedChangeListener){
        subscriptionCheckbox.setOnCheckedChangeListener(listener)
    }

    fun setOnMoreInfoClickedListener(listener : OnMoreInfoClickListener){
        moreInfoClickListener = listener
    }

    interface OnMoreInfoClickListener {
        fun onMoreInfoClicked()
    }

    companion object {
        const val INFO_TOUCH_AREA_SIZE_PX = 20
    }
}