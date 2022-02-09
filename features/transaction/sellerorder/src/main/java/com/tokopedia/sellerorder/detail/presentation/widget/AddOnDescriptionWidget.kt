package com.tokopedia.sellerorder.detail.presentation.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Spanned
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.WidgetProductAddOnDescriptionBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper

class AddOnDescriptionWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val DESCRIPTION_MIN_LINES = 1
        private const val ANIMATION_DURATION = 500L
        private const val VIEW_MAX_HEIGHT_ATTRIBUTE = "maxHeight"
        private const val LABEL_ADD_ON_DESCRIPTION = "addOnDescription"
    }

    private val binding: WidgetProductAddOnDescriptionBinding = inflateContent()
    /*
        this listener is used to synchronize description text, see less text and see more text
        visibility to prevent a glitch where see less text or see more text was visible when it is
        expected to be not visible

        this listener should be added only when we're change the description text
     */
    private val descriptionOnPreDrawListener = createDescriptionOnPreDrawListener()

    private var animatorSet: AnimatorSet = AnimatorSet()
    private var receiverName: String = ""
    private var senderName: String = ""
    private var description: String = ""
    var listener: Listener? = null

    init {
        setupViews()
    }

    private fun inflateContent(): WidgetProductAddOnDescriptionBinding {
        return WidgetProductAddOnDescriptionBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun createDescriptionOnPreDrawListener(): ViewTreeObserver.OnPreDrawListener {
        return ViewTreeObserver.OnPreDrawListener {
            var continueDraw = true
            binding.tvAddOnDescription.layout?.run {
                val lines = lineCount
                val hasDescriptionSeeLessText = text?.contains(context.getString(R.string.som_detail_add_on_see_less_description)) == true
                val isDescriptionSeeMoreVisible = binding.containerAddOnDescriptionSeeMore.maxHeight > Int.ZERO
                if (lines == DESCRIPTION_MIN_LINES) {
                    val isEllipsized = getEllipsisCount(lines - 1) > Int.ZERO
                    if (hasDescriptionSeeLessText) {
                        setDescriptionText(withSeeLessText = false)
                        continueDraw = false
                    }
                    if (isEllipsized && !isDescriptionSeeMoreVisible) {
                        binding.containerAddOnDescriptionSeeMore.maxHeight = Int.MAX_VALUE
                        continueDraw = false
                    }
                    if (!isEllipsized && isDescriptionSeeMoreVisible) {
                        binding.containerAddOnDescriptionSeeMore.maxHeight = Int.ZERO
                        continueDraw = false
                    }
                } else {
                    if (!hasDescriptionSeeLessText) {
                        setDescriptionText(withSeeLessText = true)
                        continueDraw = false
                    }
                    if (isDescriptionSeeMoreVisible) {
                        binding.containerAddOnDescriptionSeeMore.maxHeight = Int.ZERO
                        continueDraw = false
                    }
                }
            }
            binding.tvAddOnDescription.viewTreeObserver.removeOnPreDrawListener(descriptionOnPreDrawListener)
            continueDraw
        }
    }

    private fun setupViews() {
        setupCopyIcon()
        setupDescription()
        setupDescriptionSeeMore()
    }

    private fun setupCopyIcon() {
        binding.maskTriggerCopyArea.setOnClickListener {
            it.generateHapticFeedback()
            listener?.onCopyDescriptionClicked(
                LABEL_ADD_ON_DESCRIPTION,
                MethodChecker.fromHtmlWithoutExtraSpace(
                    context.getString(
                        R.string.som_detail_add_on_description_copyable_format,
                        receiverName,
                        senderName,
                        description
                    )
                )
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDescription() {
        binding.tvAddOnDescription.setOnTouchListener { v, event ->
            if (v is TextView) {
                val action = event.action
                val text = v.text
                if (text is Spanned) {
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                        var x = event.x
                        var y = event.y.toInt()

                        x -= v.totalPaddingLeft
                        y -= v.totalPaddingTop

                        x += v.scrollX
                        y += v.scrollY

                        val layout = v.layout
                        val line = layout.getLineForVertical(y)
                        val off = layout.getOffsetForHorizontal(line, x)

                        val link = text.getSpans(off, off, URLSpan::class.java)
                        if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                            listener?.onDescriptionSeeLessClicked()
                        }
                        return@setOnTouchListener true
                    }
                }
            }
            super.onTouchEvent(event)
        }
    }

    private fun setupDescriptionSeeMore() {
        with(binding.tvAddOnDescriptionSeeMore) {
            text = HtmlLinkHelper(
                binding.root.context,
                context.getString(R.string.som_detail_add_on_see_more_description)
            ).spannedString ?: ""
            setOnClickListener {
                listener?.onDescriptionSeeMoreClicked()
            }
        }
    }

    private fun animateDescription(expanded: Boolean): Animator {
        if (expanded) {
            setDescriptionText(true)
        }
        updateDescriptionMaxLines(expanded)
        measureDescriptionSize()
        val onAnimationEnd = if (expanded) ::onDescriptionExpanded else ::onDescriptionCollapsed
        val animator = createDescriptionAnimator(onAnimationEnd)
        if (!expanded) {
            resetDescriptionState()
        }
        return animator
    }

    private fun setDescriptionText(withSeeLessText: Boolean) {
        binding.tvAddOnDescription.text = if (withSeeLessText) {
            HtmlLinkHelper(
                context,
                context.getString(
                    R.string.som_detail_add_on_description,
                    description,
                    context.getString(R.string.som_detail_add_on_see_less_description)
                )
            ).spannedString ?: ""
        } else description
    }

    private fun animateDescriptionSeeMore(expanded: Boolean): Animator {
        measureDescriptionMoreSize()
        return createDescriptionSeeMoreAnimator(expanded)
    }

    private fun measureDescriptionSize() {
        binding.tvAddOnDescription.measure(
            MeasureSpec.makeMeasureSpec(binding.root.measuredWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
        )
    }

    private fun measureDescriptionMoreSize() {
        binding.tvAddOnDescriptionSeeMore.measure(
            MeasureSpec.makeMeasureSpec(binding.root.measuredWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
        )
    }

    private fun updateDescriptionMaxLines(expanded: Boolean) {
        binding.tvAddOnDescription.maxLines = if (expanded) Int.MAX_VALUE else DESCRIPTION_MIN_LINES
    }

    private fun resetDescriptionState() {
        binding.tvAddOnDescription.maxLines = Int.MAX_VALUE
    }

    private fun createDescriptionAnimator(onAnimationEnd: () -> Unit): Animator {
        return binding.tvAddOnDescription.createHeightAnimator(onAnimationEnd = onAnimationEnd)
    }

    private fun createDescriptionSeeMoreAnimator(expanded: Boolean): Animator {
        return with(binding.tvAddOnDescriptionSeeMore) {
            val targetMaxHeight = if (expanded) Int.ZERO else measuredHeight
            ObjectAnimator.ofInt(
                binding.containerAddOnDescriptionSeeMore,
                VIEW_MAX_HEIGHT_ATTRIBUTE,
                binding.containerAddOnDescriptionSeeMore.measuredHeight,
                targetMaxHeight
            )
        }
    }

    private fun View.createHeightAnimator(onAnimationEnd: () -> Unit = {}): Animator {
        val currentHeight = height
        val targetHeight = measuredHeight
        return ValueAnimator.ofInt(currentHeight, targetHeight).apply {
            addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams = layoutParams
                layoutParams.height = `val`
                this@createHeightAnimator.layoutParams = layoutParams
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    onAnimationEnd()
                }
            })
        }
    }

    private fun onDescriptionExpanded() {
        // noop for now
    }

    private fun onDescriptionCollapsed() {
        setDescriptionText(false)
        binding.tvAddOnDescription.maxLines = DESCRIPTION_MIN_LINES
    }

    private fun View.generateHapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }

    fun setIsCopyable(copyable: Boolean) {
        with(binding) {
            tvLabelCopyAddOnDescription.showWithCondition(copyable)
            icCopyAddOnDescription.showWithCondition(copyable)
        }
    }

    fun setReceiverName(name: String) {
        with(binding) {
            receiverName = name
            tvValueAddOnReceiverName.text = receiverName
            tvLabelAddOnReceiverName.showWithCondition(receiverName.isNotBlank())
            tvColonAddOnReceiverName.showWithCondition(receiverName.isNotBlank())
            tvValueAddOnReceiverName.showWithCondition(receiverName.isNotBlank())
        }
    }

    fun setSenderName(name: String) {
        with(binding) {
            senderName = name
            tvValueAddOnSenderName.text = senderName
            tvLabelAddOnSenderName.showWithCondition(senderName.isNotBlank())
            tvColonAddOnSenderName.showWithCondition(senderName.isNotBlank())
            tvValueAddOnSenderName.showWithCondition(senderName.isNotBlank())
        }
    }

    fun setDescription(description: String, expanded: Boolean) {
        this@AddOnDescriptionWidget.description = description
        with(binding.tvAddOnDescription) {
            animatorSet.cancel()
            if (this@AddOnDescriptionWidget.description.isBlank()) {
                gone()
                binding.containerAddOnDescriptionSeeMore.maxHeight = Int.ZERO
            } else {
                show()
                viewTreeObserver.removeOnPreDrawListener(descriptionOnPreDrawListener)
                viewTreeObserver.addOnPreDrawListener(descriptionOnPreDrawListener)
                setDescriptionText(expanded)
                updateDescriptionMaxLines(expanded)
                val layoutParamsCopy = layoutParams
                layoutParamsCopy.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams = layoutParamsCopy
            }
        }
    }

    fun expand() {
        animatorSet.cancel()
        animatorSet = AnimatorSet()
        animatorSet.play(animateDescription(expanded = true))
            .with(animateDescriptionSeeMore(expanded = true))
        animatorSet.duration = ANIMATION_DURATION
        animatorSet.start()
    }

    fun collapse() {
        animatorSet.cancel()
        animatorSet = AnimatorSet()
        animatorSet.play(animateDescription(expanded = false))
            .with(animateDescriptionSeeMore(expanded = false))
        animatorSet.duration = ANIMATION_DURATION
        animatorSet.start()
    }

    interface Listener {
        fun onDescriptionSeeLessClicked()
        fun onDescriptionSeeMoreClicked()
        fun onCopyDescriptionClicked(label: String, description: CharSequence)
    }
}