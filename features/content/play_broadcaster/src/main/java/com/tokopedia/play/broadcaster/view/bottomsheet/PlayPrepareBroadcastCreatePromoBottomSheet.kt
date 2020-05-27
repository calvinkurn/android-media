package com.tokopedia.play.broadcaster.view.bottomsheet

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet_play_prepare_create_promo.*

/**
 * @author by furqan on 26/05/2020
 */
class PlayPrepareBroadcastCreatePromoBottomSheet : BottomSheetUnify() {

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var currentState: Int = MAIN_STATE
    private var promoPercentage: Int = 1
    private var promoQuota: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()

        activity?.let {
            val displayMetrics = DisplayMetrics()
            it.windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }
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

        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_prepare_create_promo, null))

        setCloseClickListener {
            onBackButtonClicked()
        }
    }

    private fun initView() {
        context?.let {
            bottomSheetClose.setImageDrawable(it.resources.getDrawable(
                    com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24))
        }

        svPlayPrepareBroadcastPromo.layoutParams.height = screenHeight - containerPlayPrepareBroadcastBottom.height
        radioPlayPrepareBroadcastWithPromo.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) toggleRadioButton(true)
        }
        radioPlayPrepareBroadcastWithoutPromo.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) toggleRadioButton(false)
        }
        radioPlayPrepareBroadcastWithPromo.setOnClickListener { onLiveWithPromoChecked() }
        radioPlayPrepareBroadcastWithoutPromo.setOnClickListener { onLiveWithoutPromoChecked() }

        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
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
                showPercetageView()
            }
            else -> {
                dismiss()
            }
        }
    }

    private fun onLiveWithPromoChecked() {
        hideMainStateView()
        showPercetageView()
    }

    private fun onLiveWithoutPromoChecked() {

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
                    .translationX(0f)
            containerPlayPrepareBroadcastWithoutPromo.animate()
                    .translationX(0f)
            containerPlayPrepareBroadcastBottom.animate()
                    .translationX(0f)
        }
    }

    private fun hideMainStateView() {
        if (containerPlayPrepareBroadcastWithPromo.visibility == View.VISIBLE
                && containerPlayPrepareBroadcastWithoutPromo.visibility == View.VISIBLE
                && containerPlayPrepareBroadcastBottom.visibility == View.VISIBLE) {

            containerPlayPrepareBroadcastWithPromo.animate()
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
                    .translationX(0f)
        }

        tvPlayPrepareBroadcastInputField.textFiedlLabelText.text = getString(R.string.play_prepare_broadcast_promo_percentage_label)
        btnPlayPrepareBroadcasatInputButton.isEnabled = false
        tvPlayPrepareBroadcastInputField.textFieldInput.setText("")
        tvPlayPrepareBroadcastInputField.textFieldInput.addTextChangedListener(promoPercentageTextWatcher())
        btnPlayPrepareBroadcasatInputButton.setOnClickListener {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(promoPercentageTextWatcher())
            try {
                promoPercentage = tvPlayPrepareBroadcastInputField.textFieldInput.text.toString().toInt()
                showQuotaView()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun hidePercetagePromoView() {
        if (containerPlayPrepareBroadcastPercetageAndDiscount.visibility == View.VISIBLE) {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(promoPercentageTextWatcher())
            containerPlayPrepareBroadcastPercetageAndDiscount.animate()
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
        btnPlayPrepareBroadcasatInputButton.isEnabled = false
        tvPlayPrepareBroadcastInputField.textFieldInput.setText("")
        tvPlayPrepareBroadcastInputField.textFieldInput.addTextChangedListener(promoQuotaTextWatcher())
        btnPlayPrepareBroadcasatInputButton.setOnClickListener {
            tvPlayPrepareBroadcastInputField.textFieldInput.removeTextChangedListener(promoQuotaTextWatcher())
            try {
                promoQuota = tvPlayPrepareBroadcastInputField.textFieldInput.text.toString().toInt()
                hidePercetagePromoView()
                showMainStateView()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun promoPercentageTextWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            try {
                val percent = text.toString().toInt()
                when {
                    percent <= 0 -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_percentage_min_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    percent > 100 -> {
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
                tvPlayPrepareBroadcastInputField.textFieldWrapper.error = ""
                btnPlayPrepareBroadcasatInputButton.isEnabled = false
                t.printStackTrace()
            }
        }

    }

    private fun promoQuotaTextWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            try {
                val quota = text.toString().toInt()
                when {
                    quota <= 0 -> {
                        tvPlayPrepareBroadcastInputField.setError(true)
                        tvPlayPrepareBroadcastInputField.textFieldWrapper.error = getString(R.string.play_prepare_broadcast_promo_quota_min_error_label)
                        btnPlayPrepareBroadcasatInputButton.isEnabled = false
                    }
                    quota > 999 -> {
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
                tvPlayPrepareBroadcastInputField.textFieldWrapper.error = ""
                btnPlayPrepareBroadcasatInputButton.isEnabled = false
                t.printStackTrace()
            }
        }

    }

    companion object {
        const val TAG_CREATE_PROMO_BOTTOM_SHEETS = "playPrepareBroadcastCreatePromoBottomSheet"

        private const val EXTRA_PROMO_PERCENTAGE = "EXTRA_PROMO_PERCENTAGE"
        private const val EXTRA_PROMO_QUOTA = "EXTRA_PROMO_QUOTA"
        private const val EXTRA_CURRENT_STATE = "EXTRA_CURRENT_STATE"

        private const val MAIN_STATE = 1
        private const val PERCENTAGE_STATE = 2
        private const val QUOTA_STATE = 3

        fun getInstance(promoPercentage: Int = 0, promoQuota: Int = 0) =
                PlayPrepareBroadcastCreatePromoBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putInt(EXTRA_PROMO_PERCENTAGE, promoPercentage)
                        putInt(EXTRA_PROMO_QUOTA, promoQuota)
                    }
                }
    }
}