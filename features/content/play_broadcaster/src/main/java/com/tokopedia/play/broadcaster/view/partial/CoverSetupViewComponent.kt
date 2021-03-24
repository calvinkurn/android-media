package com.tokopedia.play.broadcaster.view.partial

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.util.KeyboardWatcher
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.play_common.util.extension.isLocal
import com.tokopedia.play_common.util.extension.setTextFieldColor
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play_common.viewcomponent.ViewComponentListener
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton


class CoverSetupViewComponent(
        container: ViewGroup,
        private val dataSource: DataSource,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_cover_setup) {

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
    private val clCropButton: ConstraintLayout = findViewById(R.id.cl_crop_button)
    private val flCropSize: FrameLayout = findViewById(R.id.fl_crop_size)
    private val clCropParent: ConstraintLayout = findViewById(R.id.cl_crop_parent)
    private val slCropParent: ScrollView = findViewById(R.id.sl_crop_parent)

    private val mMaxTitleChars: Int
        get() = dataSource.getMaxTitleCharacters()

    private val keyboardWatcher = KeyboardWatcher().apply {
        listen(rootView, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                stabilizeScroll(false)
                clCropButton.gone()
            }

            override fun onKeyboardHidden() {
                stabilizeScroll(true)
                clCropButton.visible()
            }
        })
    }

    init {
        llChangeCover.setOnClickListener { listener.onImageAreaClicked(this) }
        ivCoverImage.setOnClickListener { listener.onImageAreaClicked(this) }
        btnNext.setOnClickListener {
            etCoverTitle.clearFocus()

            if (btnNext.isLoading) return@setOnClickListener
            listener.onNextButtonClicked(this, coverTitle)
        }

        setupScrollView()

        showHint(true)
        setupTitleTextField()
        tvCoverTitleLabel.text = getCoverTitleLabelText(tvCoverTitleLabel.text.toString(), coverTitle)

        updateViewState()

        flCropSize.doOnLayout { flCrop ->
            clCropParent.layoutParams.height = flCrop.measuredHeight

            ivCoverImage.doOnPreDraw {
                ivCoverImage.layoutParams.height = flCrop.measuredHeight
                ivCoverImage.layoutParams.width = flCrop.measuredWidth

                ivCoverImage.invalidate()
                ivCoverImage.requestLayout()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        keyboardWatcher.unlisten(rootView)
        etCoverTitle.clearFocus()
        listener.onViewDestroyed(this)
    }

    fun getBottomActionView() = clCropButton

    fun setLoading(isLoading: Boolean) {
        btnNext.isLoading = isLoading
        etCoverTitle.isEnabled = !isLoading
    }

    fun setImage(uri: Uri?) {
        if (uri != null) {
            if (uri.isLocal()) ivCoverImage.setImageURI(uri)
            else {
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
        }
        else {
            ivCoverImage.setImageDrawable(null)
            loaderImage.hide()
        }

        updateAddChangeCover()
    }

    fun updateViewState() {
        updateAddChangeCover()
        updateButtonState()
    }

    fun updateButtonState() {
        btnNext.isEnabled = coverTitle.isNotBlank() && dataSource.getCurrentCoverUri() != null
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

    private fun updateCounterCount(text: String) {
        tvCoverTitleCounter.text = getString(R.string.play_prepare_cover_title_counter,
                text.length, mMaxTitleChars)
    }

    private fun updateCounterColor(text: String) {
        tvCoverTitleCounter.setTextColor(
                MethodChecker.getColor(
                        tvCoverTitleCounter.context,
                        if (!dataSource.isValidCoverTitle(text) && etCoverTitle.hasFocus()) com.tokopedia.unifyprinciples.R.color.Unify_R500
                        else com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                )
        )
    }

    private fun setupTitleLabel(currentTitle: CharSequence) {
        val currentLabel = tvCoverTitleLabel.text.toString()
        val newText = getCoverTitleLabelText(currentLabel, currentTitle.toString())
        if (currentLabel != newText.toString()) tvCoverTitleLabel.text = newText
    }

    private fun updateTextField(text: String, isFirstFocus: Boolean) {
        val isValid = dataSource.isValidCoverTitle(text)
        val hasFocus = etCoverTitle.hasFocus()
        etCoverTitle.setTextFieldColor(
                when {
                    isValid && hasFocus -> com.tokopedia.unifyprinciples.R.color.Unify_G400
                    !isValid && hasFocus && !isFirstFocus -> com.tokopedia.unifyprinciples.R.color.Unify_R500
                    else -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                }
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
                updateCounterCount(text.toString())
                updateCounterColor(text.toString())
                updateTextField(text.toString(), isFirstFocus = false)
                updateButtonState()
            }
        })
        etCoverTitle.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) v.clearFocus()
            false
        }
        etCoverTitle.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) listener.onTitleAreaHasFocus()
            else updateCounterColor(coverTitle)

            updateTextField(coverTitle, isFirstFocus = hasFocus)
            updateCounterCount(coverTitle)
            showHint(!hasFocus)
            showCounter(hasFocus)
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
                    ForegroundColorSpan(getColor(com.tokopedia.unifyprinciples.R.color.Unify_R500)),
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
    
    private fun showCounter(shouldShow: Boolean) {
        if (shouldShow) tvCoverTitleCounter.visible() else tvCoverTitleCounter.invisible()
    }

    private fun stabilizeScroll(shouldStabilize: Boolean) {
        if (shouldStabilize) slCropParent.smoothScrollTo(0, 0)
        else slCropParent.smoothScrollTo(0, slCropParent.bottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupScrollView() {
        slCropParent.setOnTouchListener { _, _ -> true }
    }

    interface Listener : ViewComponentListener<CoverSetupViewComponent> {

        fun onImageAreaClicked(view: CoverSetupViewComponent)
        fun onNextButtonClicked(view: CoverSetupViewComponent, coverTitle: String)
        fun onTitleAreaHasFocus()
    }

    interface DataSource {

        fun getMaxTitleCharacters(): Int
        fun isValidCoverTitle(coverTitle: String): Boolean
        fun getCurrentCoverUri(): Uri?
    }
}