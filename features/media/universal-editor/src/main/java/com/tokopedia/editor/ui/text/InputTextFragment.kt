package com.tokopedia.editor.ui.text

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentInputTextBinding
import com.tokopedia.editor.ui.widget.InputTextColorItemView
import com.tokopedia.editor.ui.widget.InputTextStyleItemView
import com.tokopedia.editor.util.ColorProvider
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontDetail
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.editor.R as resourceR
import com.tokopedia.unifyprinciples.getTypeface as unifyTypeFaceGetter

class InputTextFragment @Inject constructor(
    private val colorProvider: ColorProvider,
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseEditorFragment(resourceR.layout.fragment_input_text) {

    private val viewModel: InputTextViewModel by activityViewModels { viewModelFactory }
    private val viewBinding: FragmentInputTextBinding? by viewBinding()

    override fun initView() {
        initFontSelection()
        initFontColor()

        initListener()

        viewBinding?.addTextInput?.let {
            it.setText(viewModel.textValue.value)
            it.requestFocus()
        }
    }

    override fun initObserver() {
        initAlignmentObserver()
        initBackgroundObserver()
        initColorObserver()
        initFontStyleObserver()
    }

    private fun initAlignmentObserver() {
        viewModel.selectedAlignment.observe(viewLifecycleOwner) {
            updateAlignmentIcon(it)

            viewBinding?.addTextInput?.let { viewRef: EditText ->
                val lp = viewRef.layoutParams as LinearLayout.LayoutParams
                lp.gravity = when (it) {
                    FontAlignment.CENTER -> Gravity.CENTER
                    FontAlignment.LEFT -> Gravity.START
                    else -> Gravity.END
                }

                viewRef.layoutParams = lp

                viewRef.gravity = when (it) {
                    FontAlignment.CENTER -> Gravity.CENTER
                    FontAlignment.LEFT -> Gravity.START
                    else -> Gravity.END
                }
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
                } catch (_: Exception) {}
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

            it.addTextInput.addTextChangedListener { newText ->
                updateHint(newText?.toString()?.isNotEmpty() == true)
                viewModel.updateText(newText.toString())
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
                        fontStyleListener(styleItem.tag.toString(), fontDetail)
                    }

                    if (fontDetail == viewModel.selectedFontStyle.value) {
                        styleItem.setActive()
                    }
                }

                it.addView(view)
            }
        }
    }

    private fun fontStyleListener(viewTag: String, fontDetail: FontDetail) {
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
                            colorClickListener(colorRefView.tag.toString().toInt())
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
                it.setImageResource(resourceR.drawable.text_icon_filled)
            } else {
                it.setImage(IconUnify.TEXT)
            }
        }
    }

    private fun implementColor() {
        viewBinding?.addTextInput?.let {
            // config text with background
            viewModel.backgroundColorSet.value?.let { (textColor, backgroundColor) ->
                updateEditTextDrawable(backgroundColor)
                it.setTextColor(textColor)
            } ?: run {
                updateEditTextDrawable(Color.TRANSPARENT)
                it.setTextColor(viewModel.selectedTextColor.value ?: DEFAULT_TEXT_COLOR)
            }
        }
    }

    private fun updateEditTextDrawable(colorInt: Int) {
        viewBinding?.addTextInput?.background?.let {
            it.setColorFilter(colorInt, PorterDuff.Mode.DST_ATOP)
        }
    }

    private fun updateHint(isClear: Boolean) {
        viewBinding?.addTextInput?.let {
            if (isClear) {
                it.hint = ""
            } else {
                it.hint = getString(resourceR.string.universal_editor_input_text_hint)
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

    companion object {
        private const val DEFAULT_TEXT_COLOR = -1
    }
}
