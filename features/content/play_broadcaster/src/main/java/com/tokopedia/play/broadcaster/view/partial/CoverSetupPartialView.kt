package com.tokopedia.play.broadcaster.view.partial

import android.net.Uri
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.UnifyButton

class CoverSetupPartialView(
        container: ViewGroup,
        private val dataSource: DataSource,
        listener: Listener
) : PartialView(container, R.id.cl_cover_setup) {

    val liveTitle: String
        get() = etCoverTitle.text?.toString() ?: ""

    private val ivCoverImage: ImageView = findViewById(R.id.iv_cover_image)
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
        btnNext.setOnClickListener { listener.onNextButtonClicked(this, liveTitle) }

        setupTitleTextField()
        tvCoverTitleLabel.text = getCoverTitleLabelText(tvCoverTitleLabel.text.toString(), liveTitle)

        updateViewState()
    }

    fun show() {
        rootView.show()
    }

    fun hide() {
        rootView.hide()
    }

    fun setImage(uri: Uri) {
        ivCoverImage.setImageURI(uri)
    }

    fun setMaxTitleChar(maxChar: Int) {
        mMaxTitleChars = maxChar
    }

    fun updateViewState() {
        updateAddChangeCover()
        updateButtonState()
    }

    private fun updateAddChangeCover() {
        tvAddChangeCover.text = getString(
                if (dataSource.getCurrentCoverUri() != null) R.string.play_prepare_cover_title_change_cover_label
                else R.string.play_prepare_cover_title_add_cover_label
        )
    }

    fun updateButtonState() {
        btnNext.isEnabled = liveTitle.isNotEmpty() && dataSource.getCurrentCoverUri() != null
    }

    private fun setupTitleCounter() {
        tvCoverTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                liveTitle.length, mMaxTitleChars)
    }

    private fun setupTitleLabel(currentTitle: CharSequence) {
        val currentLabel = tvCoverTitleLabel.text.toString()
        val newText = getCoverTitleLabelText(currentLabel, currentTitle.toString())
        if (currentLabel != newText.toString()) tvCoverTitleLabel.text = newText
    }

    private fun setupTitleTextField() {
        etCoverTitle.setTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
        etCoverTitle.setHintTextColor(getColor(R.color.play_white_68))
        etCoverTitle.setSingleLine(false)
        etCoverTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                setupTitleLabel(text)
                setupTitleCounter()
                updateButtonState()
            }
        })
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
                    ForegroundColorSpan(getColor(R.color.Red_R500)),
                    finalText.indexOf(asterisk),
                    finalText.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }

        return spanBuilder
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