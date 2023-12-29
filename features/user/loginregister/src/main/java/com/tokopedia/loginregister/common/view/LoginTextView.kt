package com.tokopedia.loginregister.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.CustomLoginTextViewBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription

/**
 * Created by stevenfredian on 6/2/16.
 */
class LoginTextView : FrameLayout {
    private var customText: String? = null
    var backgroundColorCustom = 0
    var textColor = 0
    var borderColorCustom = 0
    var cornerSize = 0
    var borderSize = 0
    var imageEnabled = false
    var shape: GradientDrawable? = null

    private val viewBinding: CustomLoginTextViewBinding = CustomLoginTextViewBinding.inflate(
        LayoutInflater.from(context)
    ).also {
        addView(it.root)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, color: Int) : super(context) {
        backgroundColorCustom = color
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        shape = GradientDrawable()
        shape?.setColor(Color.TRANSPARENT)
        if (attrs == null) {
            setDefaultShape(context)
        } else {
            setAttrs(context, attrs)
        }
        setUp()
    }

    private fun setAttrs(context: Context, attrs: AttributeSet) {
        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoginTextView,
            0,
            0
        )
        val resourceId: Int
        try {
            customText = attr.getString(R.styleable.LoginTextView_customText)
            textColor = attr.getColor(
                R.styleable.LoginTextView_textColor, MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
            borderColorCustom = attr.getInt(R.styleable.LoginTextView_borderColor, 0)
            cornerSize = attr.getInt(R.styleable.LoginTextView_loginTextViewCornerSize, CORNER_SIZE)
            borderSize = attr.getInt(R.styleable.LoginTextView_borderSize, BORDER_SIZE)
            imageEnabled = attr.getBoolean(R.styleable.LoginTextView_imageEnabled, true)
            resourceId = attr.getResourceId(R.styleable.LoginTextView_iconButton, 0)
        } finally {
            attr.recycle()
        }
        if (customText?.isNotEmpty() == true) {
            viewBinding.providerName.text = customText
        }
        viewBinding.providerName.setTextColor(textColor)
        shape?.cornerRadii = floatArrayOf(
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat()
        )
        shape?.setStroke(borderSize, borderColorCustom)

        if (resourceId != 0) {
            val drawable = AppCompatResources.getDrawable(context, resourceId)
            viewBinding.providerImage.background = drawable
        }
        if (!imageEnabled || resourceId == 0) {
            viewBinding.providerImage.hide()
        }
    }

    private fun setUp() {
        background = shape
    }

    private fun setDefaultShape(context: Context) {
        shape?.shape = GradientDrawable.RECTANGLE
        shape?.cornerRadii = floatArrayOf(
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII,
            CORNER_RADII
        )
        shape?.setColor(backgroundColorCustom)
        if (backgroundColorCustom == MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        ) shape?.setStroke(
            BORDER_SIZE,
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
            )
        )
    }

    fun setText(name: String) {
        setTextAndContentDescription(
            viewBinding.providerName,
            name,
            context.getString(R.string.content_desc_provider_name)
        )
    }

    fun setImage(image: String) {
        if (image.isNotEmpty()) {
            viewBinding.providerImage.loadImage(image)
        }
    }

    fun setImage(image: Int) {
        viewBinding.providerImage.loadImage(image)
    }

    fun setColor(color: Int) {
        backgroundColorCustom = color
        shape?.setColor(color)
        setUp()
    }

    fun setBorderColor(color: Int) {
        shape?.setStroke(BORDER_SIZE, color)
        setUp()
    }

    fun setRoundCorner(cornerSize: Int) {
        shape?.cornerRadii = floatArrayOf(
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat(),
            cornerSize.toFloat()
        )
        setUp()
    }

    fun setImageResource(imageResource: Int) {
        viewBinding.providerImage.loadImage(imageResource, properties = {
            setPlaceHolder(EMPTY_PLACEHOLDER)
        })
    }

    companion object {
        private const val CORNER_SIZE = 3
        private const val BORDER_SIZE = 1
        private const val CORNER_RADII = 3f
        private const val EMPTY_PLACEHOLDER = -1
    }
}
