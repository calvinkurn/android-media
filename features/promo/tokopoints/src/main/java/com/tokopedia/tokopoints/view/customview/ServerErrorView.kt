package com.tokopedia.tokopoints.view.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

class ServerErrorView : NestedScrollView {
    private var imageError: AppCompatImageView? = null
    private var tvTitleError: AppCompatTextView? = null
    private var tvLabelError: AppCompatTextView? = null
    private var btnError: RoundButton? = null
    private var errorTitle: CharSequence? = null
    private var errorSubTitle: CharSequence? = null
    private var errorBackArrow: UnifyButton? = null
    private var buttonFontSize: Int? = null
    private var buttonColor: Int? = null
    private var buttonFontColor: Int? = null
    private var containerToolbar: Toolbar?= null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(null)
    }

    private fun init(attrs: AttributeSet?) {
        readAttributes(attrs)
        inflateLayout()
        setDefaultValues()
    }

    private fun readAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.theme
                    .obtainStyledAttributes(attrs, R.styleable.ServerErrorView, 0, 0)
            errorTitle = array.getString(R.styleable.ServerErrorView_errorTitle)
            errorSubTitle = array.getString(R.styleable.ServerErrorView_errorSubtitle)
            array.recycle()
        }
    }

    private fun setDefaultValues() {
        if (TextUtils.isEmpty(errorTitle)) {
            errorTitle = resources.getText(R.string.tp_label_server_error)
        }
        if (TextUtils.isEmpty(errorSubTitle)) {
            errorSubTitle = resources.getText(R.string.tp_label_try_again)
        }
        buttonFontSize = resources.getInteger(R.integer.tp_error_btn_large)
        buttonColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        buttonFontColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
    }

    private fun inflateLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_tp_server_error, this, true)
        imageError = findViewById(R.id.img_error)
        tvTitleError = findViewById(R.id.text_title_error)
        tvLabelError = findViewById(R.id.text_label_error)
        btnError = findViewById(R.id.text_failed_action)
        errorBackArrow = findViewById(R.id.iv_back_error)
        containerToolbar = findViewById(R.id.top_container)

        errorBackArrow?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tp_arrow_back,0,0,0)
    }

    fun showErrorUi(hasInternet: Boolean, error: Int = 0) {
        if (error == 1) {
            containerToolbar?.show()
        }
        var noConnectionImageId = com.tokopedia.globalerror.R.drawable.unify_globalerrors_500
        if (!hasInternet) {
            noConnectionImageId = com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            errorTitle = resources.getText(R.string.tp_no_internet_title)
            errorSubTitle = resources.getText(R.string.tp_no_internet_label)
        }
        imageError?.setImageResource(noConnectionImageId)
        buttonFontColor?.let { btnError?.setTextColor(it) }
        buttonColor?.let { btnError?.setButtonColor(it) }
        buttonFontSize?.toFloat()?.let { btnError?.setTextSize(TypedValue.COMPLEX_UNIT_SP, it) }
        tvTitleError?.text = errorTitle
        tvLabelError?.text = errorSubTitle
    }

    fun setErrorButtonClickListener(onClickListener: OnClickListener?) {
        btnError?.setOnClickListener(onClickListener)
    }

    fun setErrorBackButtonClickListener(onClickListener: OnClickListener?) {
        errorBackArrow?.setOnClickListener(onClickListener)
    }
}