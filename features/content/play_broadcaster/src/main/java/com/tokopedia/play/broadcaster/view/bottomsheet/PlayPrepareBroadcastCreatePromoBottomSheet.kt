package com.tokopedia.play.broadcaster.view.bottomsheet

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet_play_prepare_create_promo.*

/**
 * @author by furqan on 26/05/2020
 *
 * exit animation is set with animation, because there are two options of exit animation
 * that can be different in every kind of enter animation
 *
 * because we cannot set windows animation twice (on back button pressed), so I animate the exit
 * animation manually
 */
class PlayPrepareBroadcastCreatePromoBottomSheet : BottomSheetUnify() {

    lateinit var listener: Listener

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var currentState: Int = MAIN_STATE
    private var promoPercentage: Int = DEFAULT_PERCENT_QUOTA_VALUE
    private var promoQuota: Int = DEFAULT_PERCENT_QUOTA_VALUE
    private var isBack: Boolean = false
    private var isEditPage: Boolean = false

    private lateinit var percentageTextWatcher: TextWatcher
    private lateinit var quotaTextWatcher: TextWatcher

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!isEditPage) {
            dialog?.window?.setWindowAnimations(
                    if (isBack) R.style.DialogAnimationEnterLeft
                    else R.style.DialogAnimationEnterRight)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val displayMetrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }

        savedInstanceState?.let {
            currentState = it.getInt(EXTRA_CURRENT_STATE)
            promoQuota = it.getInt(EXTRA_PROMO_QUOTA)
            promoPercentage = it.getInt(EXTRA_PROMO_PERCENTAGE)
            isBack = it.getBoolean(EXTRA_IS_BACK)
        } ?: arguments?.let {
            currentState = MAIN_STATE
            promoQuota = it.getInt(EXTRA_PROMO_QUOTA)
            promoPercentage = it.getInt(EXTRA_PROMO_PERCENTAGE)
            isBack = it.getBoolean(EXTRA_IS_BACK)

            if (promoPercentage in MIN_PERCENT_QUOTA_VALUE..MAX_PERCENT_VALUE &&
                    promoQuota in MIN_PERCENT_QUOTA_VALUE..MAX_QUOTA_VALUE) {
                isEditPage = true
            }
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_CURRENT_STATE, currentState)
        outState.putInt(EXTRA_PROMO_PERCENTAGE, promoPercentage)
        outState.putInt(EXTRA_PROMO_QUOTA, promoQuota)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    private fun initBottomSheet() {
        showKnob = false
        isDragable = false
        isHideable = false
        isFullpage = true
        if (isEditPage) {
            setTitle(getString(R.string.play_prepare_broadcast_edit_promo_title))
        } else {
            setTitle(getString(R.string.play_prepare_broadcast_create_promo_title))
        }

        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_prepare_create_promo, null))

        setCloseClickListener {
            onBackButtonClicked()
        }
    }

    private fun initView() {
        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
        if (!isEditPage) {
            context?.let {
                bottomSheetClose.setImageDrawable(it.resources.getDrawable(
                        com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24))
            }
            btnPlayPrepareBroadcastNext.text = getString(R.string.play_next)
        } else {
            btnPlayPrepareBroadcastNext.text = getString(R.string.play_label_save)
        }

        svPlayPrepareBroadcastPromo.layoutParams.height = screenHeight - containerPlayPrepareBroadcastBottom.height
        radioPlayPrepareBroadcastWithPromo.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                toggleRadioButton(true)
                if (promoPercentage == DEFAULT_PERCENT_QUOTA_VALUE && promoQuota == DEFAULT_PERCENT_QUOTA_VALUE) {
                    onLiveWithPromoChecked()
                }
            }
        }
        radioPlayPrepareBroadcastWithoutPromo.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                toggleRadioButton(false)
                onLiveWithoutPromoChecked()
            }
        }
        ivPlayPrepareBroadcastWithPromoEdit.setOnClickListener { onLiveWithPromoChecked() }
        btnPlayPrepareBroadcastNext.setOnClickListener {
            dialog?.window?.let {
                it.decorView.animate()
                        .setDuration(ANIMATION_DURATION)
                        .translationX((screenWidth * -1).toFloat())
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(p0: Animator?) {}

                            override fun onAnimationEnd(p0: Animator?) {
                                listener?.onVoucherSaved(radioPlayPrepareBroadcastWithPromo.isChecked, promoPercentage, promoQuota)
                                dismiss()
                            }

                            override fun onAnimationCancel(p0: Animator?) {}

                            override fun onAnimationStart(p0: Animator?) {}

                        })
            }
        }

        percentageTextWatcher = createPercentageTextWatcher()
        quotaTextWatcher = createQuotaTextWatcher()

        showMainStateView()
    }

    private fun toggleRadioButton(isWithPromo: Boolean) {
        if (isWithPromo) {
            radioPlayPrepareBroadcastWithoutPromo.isChecked = false
        } else {
            radioPlayPrepareBroadcastWithPromo.isChecked = false
        }
    }

    private fun onBackButtonClicked() {
        when (currentState) {
            PERCENTAGE_STATE -> {
                hidePercetagePromoView()
                showMainStateView()
            }
            QUOTA_STATE -> {
                tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(quotaTextWatcher)
                showPercetageView()
            }
            else -> {
                if (!isEditPage) {
                    dialog?.window?.let {
                        it.decorView.animate()
                                .setDuration(ANIMATION_DURATION)
                                .translationX(screenWidth.toFloat())
                                .setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationRepeat(p0: Animator?) {}

                                    override fun onAnimationEnd(p0: Animator?) {
                                        dismiss()
                                    }

                                    override fun onAnimationCancel(p0: Animator?) {}

                                    override fun onAnimationStart(p0: Animator?) {}

                                })
                    }
                } else {
                    dismiss()
                }
            }
        }
    }

    private fun onLiveWithPromoChecked() {
        hideMainStateView()
        showPercetageView()
    }

    private fun onLiveWithoutPromoChecked() {
        btnPlayPrepareBroadcastNext.isEnabled = true
    }

    private fun showMainStateView() {
        currentState = MAIN_STATE
        if (containerPlayPrepareBroadcastWithPromo.visibility == View.GONE
                && containerPlayPrepareBroadcastWithoutPromo.visibility == View.GONE
                && containerPlayPrepareBroadcastBottom.visibility == View.GONE) {

            containerPlayPrepareBroadcastWithPromo.visibility = View.VISIBLE
            containerPlayPrepareBroadcastWithoutPromo.visibility = View.VISIBLE
            containerPlayPrepareBroadcastBottom.visibility = View.VISIBLE

            containerPlayPrepareBroadcastWithPromo.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(0f)
            containerPlayPrepareBroadcastWithoutPromo.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(0f)
            containerPlayPrepareBroadcastBottom.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(0f)
        }

        if (promoPercentage in MIN_PERCENT_QUOTA_VALUE..MAX_PERCENT_VALUE &&
                promoQuota in MIN_PERCENT_QUOTA_VALUE..MAX_QUOTA_VALUE) {
            ivPlayPrepareBroadcastWithPromoEdit.visibility = View.VISIBLE

            tvPlayPrepareBroadcastWithPromoTitle.setType(Typography.BODY_3)
            tvPlayPrepareBroadcastWithPromoTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            tvPlayPrepareBroadcastWithPromoTitle.text = getString(R.string.play_prepare_broadcast_promo_detail_title)

            tvPlayPrepareBroadcastWithPromoDescription.setType(Typography.BODY_1)
            tvPlayPrepareBroadcastWithPromoDescription.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            tvPlayPrepareBroadcastWithPromoDescription.text = HtmlCompat.fromHtml(getString(R.string.play_prepare_broadcast_promo_detail_description,
                    promoPercentage, promoQuota), HtmlCompat.FROM_HTML_MODE_LEGACY)

            containerConstraintPlayWithPromo.requestLayout()
            btnPlayPrepareBroadcastNext.isEnabled = true
        } else {
            ivPlayPrepareBroadcastWithPromoEdit.visibility = View.GONE

            tvPlayPrepareBroadcastWithPromoTitle.setType(Typography.HEADING_6)
            tvPlayPrepareBroadcastWithPromoTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            tvPlayPrepareBroadcastWithPromoTitle.text = getString(R.string.play_prepare_broadcast_promo_with_promo_title)

            tvPlayPrepareBroadcastWithPromoDescription.setType(Typography.SMALL)
            tvPlayPrepareBroadcastWithPromoDescription.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            tvPlayPrepareBroadcastWithPromoDescription.text = getString(R.string.play_prepare_broadcast_promo_with_promo_description)

            containerConstraintPlayWithPromo.requestLayout()
            btnPlayPrepareBroadcastNext.isEnabled = false
        }
    }

    private fun hideMainStateView() {
        if (containerPlayPrepareBroadcastWithPromo.visibility == View.VISIBLE
                && containerPlayPrepareBroadcastWithoutPromo.visibility == View.VISIBLE
                && containerPlayPrepareBroadcastBottom.visibility == View.VISIBLE) {

            containerPlayPrepareBroadcastWithPromo.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX((screenWidth * -1).toFloat())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animator: Animator) {}

                        override fun onAnimationEnd(animator: Animator) {
                            if (currentState != MAIN_STATE) {
                                containerPlayPrepareBroadcastWithPromo.visibility = View.GONE
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {}

                        override fun onAnimationStart(animator: Animator) {}
                    })

            containerPlayPrepareBroadcastWithoutPromo.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX((screenWidth * -1).toFloat())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animator: Animator) {}

                        override fun onAnimationEnd(animator: Animator) {
                            if (currentState != MAIN_STATE) {
                                containerPlayPrepareBroadcastWithoutPromo.visibility = View.GONE
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {}

                        override fun onAnimationStart(animator: Animator) {}
                    })

            containerPlayPrepareBroadcastBottom.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX((screenWidth * -1).toFloat())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animator: Animator) {}

                        override fun onAnimationEnd(animator: Animator) {
                            if (currentState != MAIN_STATE) {
                                containerPlayPrepareBroadcastBottom.visibility = View.GONE
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {}

                        override fun onAnimationStart(animator: Animator) {}
                    })
        }
    }

    private fun showPercetageView() {
        currentState = PERCENTAGE_STATE
        if (containerPlayPrepareBroadcastPercetageAndDiscount.visibility == View.GONE) {
            containerPlayPrepareBroadcastPercetageAndDiscount.x = screenWidth.toFloat()
            containerPlayPrepareBroadcastPercetageAndDiscount.visibility = View.VISIBLE
            containerPlayPrepareBroadcastPercetageAndDiscount.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(0f)
        }

        tvPlayPrepareBroadcastInputField.textFiedlLabelText.text = getString(R.string.play_prepare_broadcast_promo_percentage_label)
        if (promoPercentage in MIN_PERCENT_QUOTA_VALUE..MAX_PERCENT_VALUE) {
            tvPlayPrepareBroadcastInputField.textFieldInput.setText(promoPercentage.toString())
            btnPlayPrepareBroadcasatInputButton.isEnabled = true
        } else {
            tvPlayPrepareBroadcastInputField.textFieldInput.setText("")
            btnPlayPrepareBroadcasatInputButton.isEnabled = false
        }
        tvPlayPrepareBroadcastInputField.textFieldInput.addTextChangedListener(percentageTextWatcher)
        btnPlayPrepareBroadcasatInputButton.setOnClickListener {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(percentageTextWatcher)
            try {
                promoPercentage = tvPlayPrepareBroadcastInputField.textFieldInput.text.toString().toInt()
                showQuotaView()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun hidePercetagePromoView() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
        if (containerPlayPrepareBroadcastPercetageAndDiscount.visibility == View.VISIBLE) {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(percentageTextWatcher)
            containerPlayPrepareBroadcastPercetageAndDiscount.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(screenWidth.toFloat())
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animator: Animator) {}

                        override fun onAnimationEnd(animator: Animator) {
                            if (currentState != PERCENTAGE_STATE) {
                                containerPlayPrepareBroadcastPercetageAndDiscount.visibility = View.GONE
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {}

                        override fun onAnimationStart(animator: Animator) {}
                    })
        }
    }

    private fun showQuotaView() {
        currentState = QUOTA_STATE
        tvPlayPrepareBroadcastInputField.textFiedlLabelText.text = getString(R.string.play_prepare_broadcast_promo_quota_label)
        if (promoQuota in MIN_PERCENT_QUOTA_VALUE..MAX_QUOTA_VALUE) {
            tvPlayPrepareBroadcastInputField.textFieldInput.setText(promoQuota.toString())
            btnPlayPrepareBroadcasatInputButton.isEnabled = true
        } else {
            tvPlayPrepareBroadcastInputField.textFieldInput.setText("")
            btnPlayPrepareBroadcasatInputButton.isEnabled = false
        }
        tvPlayPrepareBroadcastInputField.textFieldInput.addTextChangedListener(quotaTextWatcher)
        btnPlayPrepareBroadcasatInputButton.setOnClickListener {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(quotaTextWatcher)
            try {
                promoQuota = tvPlayPrepareBroadcastInputField.textFieldInput.text.toString().toInt()
                hidePercetagePromoView()
                showMainStateView()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun createPercentageTextWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            try {
                val percent = text.toString().toInt()
                when {
                    percent < MIN_PERCENT_QUOTA_VALUE -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_percentage_min_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    percent > MAX_PERCENT_VALUE -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_percentage_max_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    else -> {
                        tvPlayPrepareBroadcastInputField.setError(false)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = ""
                        btnPlayPrepareBroadcasatInputButton.isEnabled = true
                    }
                }
            } catch (t: Throwable) {
                tvPlayPrepareBroadcastInputField.setError(false)
                tvPlayPrepareBroadcastInputField.textFieldWrapper.error = null
                btnPlayPrepareBroadcasatInputButton.isEnabled = false
                t.printStackTrace()
            }
        }
    }

    private fun createQuotaTextWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            try {
                val quota = text.toString().toInt()
                when {
                    quota < MIN_PERCENT_QUOTA_VALUE -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_quota_min_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    quota > MAX_QUOTA_VALUE -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_quota_max_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    else -> {
                        tvPlayPrepareBroadcastInputField.setError(false)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = ""
                        btnPlayPrepareBroadcasatInputButton.isEnabled = true
                    }
                }
            } catch (t: Throwable) {
                tvPlayPrepareBroadcastInputField.setError(false)
                tvPlayPrepareBroadcastInputField.textFieldWrapper.error = null
                btnPlayPrepareBroadcasatInputButton.isEnabled = false
                t.printStackTrace()
            }
        }
    }

    interface Listener {
        fun onVoucherSaved(isWithPromo: Boolean, promoPercentage: Int, promoQuota: Int)
    }

    companion object {
        const val TAG_CREATE_PROMO_BOTTOM_SHEETS = "playPrepareBroadcastCreatePromoBottomSheet"

        private const val EXTRA_PROMO_PERCENTAGE = "EXTRA_PROMO_PERCENTAGE"
        private const val EXTRA_PROMO_QUOTA = "EXTRA_PROMO_QUOTA"
        private const val EXTRA_CURRENT_STATE = "EXTRA_CURRENT_STATE"
        private const val EXTRA_IS_BACK = "EXTRA_IS_BACK"

        private const val ANIMATION_DURATION: Long = 300
        private const val MAIN_STATE = 1
        private const val PERCENTAGE_STATE = 2
        private const val QUOTA_STATE = 3

        private const val DEFAULT_PERCENT_QUOTA_VALUE = 0
        private const val MIN_PERCENT_QUOTA_VALUE = 1
        private const val MAX_PERCENT_VALUE = 99
        private const val MAX_QUOTA_VALUE = 999

        fun getInstance(promoPercentage: Int = DEFAULT_PERCENT_QUOTA_VALUE, promoQuota: Int = DEFAULT_PERCENT_QUOTA_VALUE, isBack: Boolean = false) =
                PlayPrepareBroadcastCreatePromoBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putInt(EXTRA_PROMO_PERCENTAGE, promoPercentage)
                        putInt(EXTRA_PROMO_QUOTA, promoQuota)
                        putBoolean(EXTRA_IS_BACK, isBack)
                    }
                }
    }
}