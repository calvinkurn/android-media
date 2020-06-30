package com.tokopedia.play.broadcaster.view.partial

import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.setTextFieldColor
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton


class CoverSetupPartialView(
        container: ViewGroup,
        private val dataSource: DataSource,
        listener: Listener
) : PartialView(container, R.id.cl_cover_setup) {

    var coverTitle: String
        get() = etCoverTitle.text?.toString() ?: ""
        set(value) {
            etCoverTitle.setText(value)
            etCoverTitle.setSelection(etCoverTitle.length())
        }

    private val ivCoverImage: ImageView = findViewById(R.id.iv_cover_image)
    private val loaderImage: LoaderUnify = findViewById(R.id.loader_image)
    private val llChangeCover: LinearLayout = findViewById(R.id.ll_change_cover)
    private val etCoverTitle: EditText = findViewById(R.id.et_cover_title)
    private val tvCoverTitleLabel: TextView = findViewById(R.id.tv_cover_title_label)
    private val tvCoverTitleCounter: TextView = findViewById(R.id.tv_cover_title_counter)
    private val tvAddChangeCover: TextView = findViewById(R.id.tv_add_change_cover)
    private val btnNext: UnifyButton = findViewById(R.id.btn_next)

    private var mMaxTitleChars = DEFAULT_MAX_CHAR

    init {
        llChangeCover.setOnClickListener { listener.onImageAreaClicked(this) }
        ivCoverImage.setOnClickListener { listener.onImageAreaClicked(this) }
        btnNext.setOnClickListener {
            etCoverTitle.clearFocus()

            if (btnNext.isLoading) return@setOnClickListener
            listener.onNextButtonClicked(this, coverTitle)
        }

        showHint(true)
        setupTitleTextField()
        tvCoverTitleLabel.text = getCoverTitleLabelText(tvCoverTitleLabel.text.toString(), coverTitle)

        updateViewState()
    }

    fun show() {
        rootView.show()
    }

    fun hide() {
        rootView.hide()
    }

    fun setLoading(isLoading: Boolean) {
        btnNext.isLoading = isLoading
    }

    fun setImage(uri: Uri?) {
        if (uri != null) {
            loaderImage.show()
            Glide.with(ivCoverImage.context)
                    .load(uri)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            loaderImage.hide()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                            loaderImage.hide()
                            return false
                        }
                    })
                    .into(ivCoverImage)
        }
        else {
            ivCoverImage.setImageDrawable(null)
            loaderImage.hide()
        }

        updateAddChangeCover()
    }

    fun setMaxTitleChar(maxChar: Int) {
        mMaxTitleChars = maxChar
    }

    fun updateViewState() {
        updateAddChangeCover()
        updateButtonState()
    }

    fun updateButtonState() {
        btnNext.isEnabled = coverTitle.isNotEmpty() && dataSource.getCurrentCoverUri() != null
    }

    fun clickNext() {
        btnNext.performClick()
    }

    private fun updateAddChangeCover() {
        tvAddChangeCover.text = getString(
                if (dataSource.getCurrentCoverUri() != null) R.string.play_prepare_cover_title_change_cover_label
                else R.string.play_prepare_cover_title_add_cover_label
        )
    }

    private fun updateTitleCounter(text: String) {
        tvCoverTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                text.length, mMaxTitleChars)

        tvCoverTitleCounter.setTextColor(
                MethodChecker.getColor(
                        tvCoverTitleCounter.context,
                        if (text.isEmpty() && etCoverTitle.hasFocus()) com.tokopedia.unifyprinciples.R.color.Red_R500 else com.tokopedia.unifyprinciples.R.color.Neutral_N0
                )
        )
    }

    private fun setupTitleLabel(currentTitle: CharSequence) {
        val currentLabel = tvCoverTitleLabel.text.toString()
        val newText = getCoverTitleLabelText(currentLabel, currentTitle.toString())
        if (currentLabel != newText.toString()) tvCoverTitleLabel.text = newText
    }

    private fun updateTextField(text: String) {
        etCoverTitle.setTextFieldColor(
                if (text.isEmpty() && etCoverTitle.hasFocus()) com.tokopedia.unifyprinciples.R.color.Red_R500 else com.tokopedia.unifyprinciples.R.color.Neutral_N0
        )
    }

    private fun setupTitleTextField() {
        etCoverTitle.setRawInputType(InputType.TYPE_CLASS_TEXT)
        etCoverTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                setupTitleLabel(text)
                updateTitleCounter(text.toString())
                updateTextField(text.toString())
                updateButtonState()
            }
        })
        etCoverTitle.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) v.clearFocus()
            false
        }
        etCoverTitle.setOnFocusChangeListener { _, hasFocus ->
            updateTitleCounter(coverTitle)
            updateTextField(coverTitle)
            showHint(!hasFocus)
            if (hasFocus) tvCoverTitleCounter.visible() else tvCoverTitleCounter.invisible()
        }
        etCoverTitle.filters = arrayOf(InputFilter.LengthFilter(mMaxTitleChars))
    }

    private fun getCoverTitleLabelText(label: String, coverTitle: String): CharSequence {
        val isCoverValid = dataSource.isValidCoverTitle(coverTitle)
        val asterisk = '*'
        val finalText =
                if (!isCoverValid && label.last() != asterisk) "$label*"
                else if (isCoverValid && label.last() == asterisk) label.removeSuffix(asterisk.toString())
                else label

        val spanBuilder = SpannableStringBuilder(finalText)
        if (spanBuilder.contains(asterisk)) {
            spanBuilder.setSpan(
                    ForegroundColorSpan(getColor(com.tokopedia.unifyprinciples.R.color.Red_R500)),
                    finalText.indexOf(asterisk),
                    finalText.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }

        return spanBuilder
    }

    private fun showHint(shouldShow: Boolean) {
        etCoverTitle.hint =
                if (shouldShow) getString(R.string.play_prepare_cover_title_default_title_placeholder)
                else ""
    }

    interface Listener {

        fun onImageAreaClicked(view: CoverSetupPartialView)
        fun onNextButtonClicked(view: CoverSetupPartialView, coverTitle: String)
    }

    interface DataSource {

        fun isValidCoverTitle(coverTitle: String): Boolean
        fun getCurrentCoverUri(): Uri?
    }

    companion object {

        private const val DEFAULT_MAX_CHAR = 38
    }

}