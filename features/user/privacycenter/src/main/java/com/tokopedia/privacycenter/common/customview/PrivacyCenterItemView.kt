package com.tokopedia.privacycenter.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.customview.PrivacyCenterItemView.PrivacyCenterItemType.DEFAULT
import com.tokopedia.privacycenter.common.customview.PrivacyCenterItemView.PrivacyCenterItemType.TOGGLE
import com.tokopedia.privacycenter.databinding.CustomviewItemPrivacyCenterBinding
import com.tokopedia.unifyprinciples.Typography

class PrivacyCenterItemView : FrameLayout {

    constructor(
        context: Context
    ) : super(context) {
        setupView()
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    private val viewBinding: CustomviewItemPrivacyCenterBinding =
        CustomviewItemPrivacyCenterBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    private var itemType: PrivacyCenterItemType = DEFAULT

    val view = viewBinding.root

    var title: String = viewBinding.itemTextTitle.text.toString()
        set(value) {
            field = value
            viewBinding.itemTextTitle.text = field
        }

    var description: String = viewBinding.itemTextDescription.text.toString()
        set(value) {
            field = value
            viewBinding.itemTextDescription.text = field
        }

    /**
     * @param iconId list id please refer into this class [com.tokopedia.iconunify.IconUnify]
     */
    fun setIcon(iconId: Int) {
        viewBinding.itemImage.setImage(iconId)
    }

    fun setIcon(url: String) {
        viewBinding.itemImage.loadImage(url) {
            setPlaceHolder(-1)
            useCache(true)
        }
    }

    fun onToggleClicked(onClicked: (buttonView: View, isChecked: Boolean) -> Unit) {
        viewBinding.itemSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            onClicked.invoke(buttonView, isChecked)
        }
    }

    fun forceToggleState(isActive: Boolean) {
        viewBinding.itemSwitch.isChecked = isActive
    }

    fun onNavigationButtonClicked(onClicked: (View) -> Unit) {
        viewBinding.itemNavigationButton.setOnClickListener {
            onClicked.invoke(it)
        }
    }

    private fun setAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.PrivacyCenterItemView,
            0,
            0
        )

        title = typedArray.getString(R.styleable.PrivacyCenterItemView_title).orEmpty()
        description = typedArray.getString(R.styleable.PrivacyCenterItemView_description).orEmpty()

        val type = typedArray.getInteger(
            R.styleable.PrivacyCenterItemView_type,
            DEFAULT.code
        )

        PrivacyCenterItemType.map(type)?.let {
            when (it) {
                DEFAULT -> initDefaultView()
                TOGGLE -> initToggleView()
            }
        }
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        if (attributeSet != null) setAttributes(attributeSet)
    }

    private fun initDefaultView() {
        viewBinding.apply {
            itemSwitch.hide()
            itemTextDescription.hide()
            itemTextTitle.setWeight(Typography.REGULAR)

            itemNavigationButton.show()
        }
    }

    private fun initToggleView() {
        viewBinding.apply {
            itemNavigationButton.hide()
            itemTextTitle.setWeight(Typography.BOLD)

            if (description.isNotEmpty()) {
                itemTextDescription.show()
            }

            itemSwitch.show()
        }
    }

    internal enum class PrivacyCenterItemType(val code: Int) {
        DEFAULT(1),
        TOGGLE(2);

        companion object {
            fun map(code: Int): PrivacyCenterItemType? {
                val map = values().associateBy(PrivacyCenterItemType::code)
                return map[code]
            }
        }
    }
}
