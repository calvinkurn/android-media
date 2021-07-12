package com.tokopedia.loginregister.common.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.loginregister.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription

/**
 * Created by stevenfredian on 6/2/16.
 */
class LoginTextView : LinearLayout {
    var backgroundColorCustom = 0
    private var customText: String? = null
    var textColor = 0
    var borderColorCustom = 0
    var cornerSize = 0
    var borderSize = 0
    var imageEnabled = false
    var shape: GradientDrawable? = null

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

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.custom_login_text_view, this)
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
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LoginTextView, 0, 0)
        val resourceId: Int
        try {
            customText = a.getString(R.styleable.LoginTextView_customText)
            textColor = a.getColor(R.styleable.LoginTextView_textColor, MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            borderColorCustom = a.getInt(R.styleable.LoginTextView_borderColor, 0)
            cornerSize = a.getInt(R.styleable.LoginTextView_loginTextViewCornerSize, 3)
            borderSize = a.getInt(R.styleable.LoginTextView_borderSize, 1)
            imageEnabled = a.getBoolean(R.styleable.LoginTextView_imageEnabled, true)
            resourceId = a.getResourceId(R.styleable.LoginTextView_iconButton, 0)
        } finally {
            a.recycle()
        }
        if (!TextUtils.isEmpty(customText)) {
            (findViewById<View>(R.id.provider_name) as Typography).text = customText
        }
        (findViewById<View>(R.id.provider_name) as Typography).setTextColor(textColor)
        shape?.cornerRadii = floatArrayOf(cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize
                .toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat())
        shape?.setStroke(borderSize, borderColorCustom)
        val providerImage = findViewById<View>(R.id.provider_image)
        if (resourceId != 0) {
            val drawable = AppCompatResources.getDrawable(context, resourceId)
            providerImage.background = drawable
        }
        if (!imageEnabled || resourceId == 0) {
            providerImage.visibility = GONE
        }
    }

    fun setUp() {
        background = shape
    }

    private fun setDefaultShape(context: Context) {
        shape?.shape = GradientDrawable.RECTANGLE
        shape?.cornerRadii = floatArrayOf(3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f)
        shape?.setColor(backgroundColorCustom)
        if (backgroundColorCustom == MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)) shape?.setStroke(1,
                MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
    }

    fun setText(name: String) {
        val typography: Typography = findViewById(R.id.provider_name)
        setTextAndContentDescription(typography, name, typography.context.getString(R.string.content_desc_provider_name))
    }

    fun setImage(image: String) {
        if (!TextUtils.isEmpty(image)) {
            val imageUnify: ImageUnify = findViewById(R.id.provider_image)
            ImageHandler.LoadImage(imageUnify, image)
        }
    }

    fun setColor(color: Int) {
        backgroundColorCustom = color
        shape?.setColor(color)
        setUp()
    }

    fun setBorderColor(color: Int) {
        shape?.setStroke(1, color)
        setUp()
    }

    fun setRoundCorner(cornerSize: Int) {
        shape?.cornerRadii = floatArrayOf(cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize
                .toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat(), cornerSize.toFloat())
        setUp()
    }

    fun setImageResource(imageResource: Int) {
        val imageUnify: ImageUnify = findViewById(R.id.provider_image)
        ImageHandler.loadImageWithIdWithoutPlaceholder(imageUnify, imageResource)
    }
}