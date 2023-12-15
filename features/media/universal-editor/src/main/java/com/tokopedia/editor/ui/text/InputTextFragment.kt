@file:Suppress("DEPRECATION", "DeprecatedMethod")

package com.tokopedia.editor.ui.text

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentInputTextBinding
import com.tokopedia.editor.ui.widget.EditorEditTextView
import com.tokopedia.editor.ui.widget.InputTextColorItemView
import com.tokopedia.editor.ui.widget.InputTextStyleItemView
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontDetail
import com.tokopedia.editor.util.provider.ColorProvider
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.editor.R as editorR

class InputTextFragment @Inject constructor(
    private val colorProvider: ColorProvider,
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseEditorFragment(editorR.layout.fragment_input_text) {

    private val viewModel: InputTextViewModel by activityViewModels { viewModelFactory }
    private val viewBinding: FragmentInputTextBinding? by viewBinding()
    private var handler: Handler? = null

    override fun initView() {
        initFontSelection()
        initFontColor()

        initListener()

        viewBinding?.addTextInput?.setText(viewModel.textValue.value)

        this.showSoftKeyboard()
    }

    override fun initObserver() {
        initAlignmentObserver()
        initBackgroundObserver()
        initColorObserver()
        initFontStyleObserver()
    }

    override fun onPause() {
        handler?.removeCallbacksAndMessages(null)
        super.onPause()
    }

    private fun initAlignmentObserver() {
        viewModel.selectedAlignment.observe(viewLifecycleOwner) {
            updateAlignmentIcon(it)

            viewBinding?.addTextInput?.let { viewRef: EditorEditTextView ->
                viewRef.gravity = when (it) {
                    FontAlignment.CENTER -> Gravity.CENTER
                    FontAlignment.LEFT -> Gravity.START
                    else -> Gravity.END
                }
                viewRef.setAlignment(it.value)
            }
        }
    }

    private fun initBackgroundObserver() {
        viewModel.backgroundColorSet.observe(viewLifecycleOwner) {
            updateBackgroundStateIcon(it != null)
            implementColor()
        }
    }

    private fun initColorObserver() {
        viewModel.selectedTextColor.observe(viewLifecycleOwner) {
            implementColor()
        }
    }

    private fun initFontStyleObserver() {
        viewModel.selectedFontStyle.observe(viewLifecycleOwner) {
            viewBinding?.addTextInput?.apply {
                try {
                    val typeFace = Typeface.createFromAsset(context.assets, it.fontName)
                    setTypeface(typeFace, it.fontStyle)
                } catch (ignored: Throwable) {}
            }
        }
    }

    private fun initListener() {
        viewBinding?.let {
            it.alignmentIconContainer.setOnClickListener {
                viewModel.increaseAlignment()
            }

            it.textBackgroundIconContainer.setOnClickListener {
                // if background color is filled, if empty then set new color base on selected color
                if (viewModel.backgroundColorSet.value == null) {
                    implementBackgroundColor()
                } else {
                    viewModel.updateBackgroundState(null)
                }
            }

            it.addTextInputWrapper.setOnClickListener { _ ->
                it.addTextInput.requestFocus()
            }

            it.addTextInputWrapper.setOnClickListener {
                handler?.removeCallbacksAndMessages(null)
                viewModel.saveInputText()
            }

            it.addTextInput.addTextChangedListener { newText ->
                viewModel.updateText(newText?.toString() ?: "")
            }
        }
    }

    private fun initFontSelection() {
        viewBinding?.fontSelectionContainer?.let {
            FontDetail.values().forEachIndexed { _, fontDetail ->
                val view = InputTextStyleItemView(it.context, null)

                view.let { styleItem ->
                    styleItem.setFont(fontDetail)
                    styleItem.tag = fontDetail.fontName + fontDetail.fontStyle

                    styleItem.setOnClickListener {
                        if (styleItem.isActive()) return@setOnClickListener
                        styleItem.setActive()
                        fontStyleListener(fontDetail)
                    }

                    if (fontDetail == viewModel.selectedFontStyle.value) {
                        styleItem.setActive()
                    }
                }

                it.addView(view)
            }
        }
    }

    private fun fontStyleListener(fontDetail: FontDetail) {
        var prevActiveStyle = ""
        viewModel.selectedFontStyle.value?.let {
            prevActiveStyle = it.fontName + it.fontStyle
        }

        setFontStyleItemInactive(prevActiveStyle)
        viewModel.updateFontStyle(fontDetail)
    }

    private fun setFontStyleItemInactive(viewTag: String) {
        viewBinding?.fontSelectionContainer?.findViewWithTag<InputTextStyleItemView>(viewTag)
            ?.setInactive()
    }

    private fun initFontColor() {
        colorProvider.getColorMap().toList().forEach { (colorInt, _) ->
            viewBinding?.fontColorContainer?.let {
                it.addView(
                    InputTextColorItemView(it.context).apply {

                        setColor(colorInt)
                        tag = colorInt
                        setOnClickListener { colorRefView ->
                            this.setActive()
                            colorClickListener(colorRefView.tag.toString().toIntOrZero())
                        }

                        if (viewModel.getCurrentSelectedColor() == colorInt) setActive()
                    }
                )
            }
        }
    }

    private fun colorClickListener(viewTag: Int) {
        val currentColor = viewModel.getCurrentSelectedColor()
        if (currentColor == viewTag) return
        setColorItemInactive(currentColor)

        viewModel.updateSelectedColor(viewTag)
    }

    private fun setColorItemInactive(viewTag: Int) {
        viewBinding?.fontColorContainer?.let {
            it.findViewWithTag<InputTextColorItemView>(
                viewTag
            )?.apply {
                setInactive()
            }
        }
    }

    private fun updateAlignmentIcon(alignment: FontAlignment) {
        val icon = when (alignment) {
            FontAlignment.LEFT -> IconUnify.FORMAT_ALIGN_LEFT
            FontAlignment.CENTER -> IconUnify.FORMAT_CENTER
            else -> IconUnify.FORMAT_ALIGN_RIGHT
        }

        viewBinding?.alignmentIcon?.setImage(icon)
    }

    private fun updateBackgroundStateIcon(stateEnable: Boolean) {
        viewBinding?.textBackgroundIcon?.let {
            if (stateEnable) {
                it.setImageResource(editorR.drawable.text_icon_filled)
            } else {
                it.setImage(IconUnify.TEXT)
            }
        }
    }

    private fun implementColor() {
        viewBinding?.addTextInput?.let {
            // config text with background
            viewModel.backgroundColorSet.value?.let { (textColor, backgroundColor) ->
                it.setColor(textColor, backgroundColor)
            } ?: run {
                val textColor = viewModel.selectedTextColor.value ?: DEFAULT_TEXT_COLOR
                it.setColor(textColor, Color.TRANSPARENT)
            }
        }
    }

    private fun implementBackgroundColor() {
        viewModel.selectedTextColor.value?.let { selectedColor ->
            (colorProvider.getColorMap()[selectedColor])?.textColorAlternate?.let { textColorInt ->
                viewModel.updateBackgroundState(
                    Pair(
                        textColorInt,
                        selectedColor
                    )
                )
            }
        }
    }

    private fun showSoftKeyboard() {
        handler = Handler()
        handler?.postDelayed({
            viewBinding?.addTextInput?.requestFocus()
            context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
                (it as InputMethodManager).toggleSoftInput(
                    InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
        }, SOFT_KEYBOARD_SHOW_DELAY)
    }

    companion object {
        private const val DEFAULT_TEXT_COLOR = -1
        private const val SOFT_KEYBOARD_SHOW_DELAY = 100L
    }
}
