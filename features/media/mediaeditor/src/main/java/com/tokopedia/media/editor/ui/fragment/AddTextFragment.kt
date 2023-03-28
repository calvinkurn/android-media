package com.tokopedia.media.editor.ui.fragment

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.databinding.FragmentAddTextLayoutBinding
import com.tokopedia.media.editor.ui.activity.addtext.AddTextActivity
import com.tokopedia.media.editor.ui.activity.addtext.AddTextViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_ALIGNMENT_CENTER
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_ALIGNMENT_LEFT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_ALIGNMENT_RIGHT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_POSITION_BOTTOM
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_STYLE_REGULAR
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_STYLE_BOLD
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_STYLE_ITALIC
import com.tokopedia.media.editor.ui.widget.AddTextColorItem
import com.tokopedia.media.editor.ui.widget.AddTextStyleItem
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class AddTextFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseEditorFragment() {

    private val viewBinding: FragmentAddTextLayoutBinding? by viewBinding()
    private val viewModel: AddTextViewModel by activityViewModels { viewModelFactory }

    private var defaultPadding = 0

    private val colorList = listOf(
        editorR.color.Unify_NN0,
        editorR.color.Unify_NN1000
    )

    /**
     * 0 -> regular
     * 1 -> bold
     * 2 -> italic
     */
    private val textStyleItemRef: Array<AddTextStyleItem?> = Array(3) { null }
    private var activeStyleIndex = TEXT_STYLE_REGULAR
    set(value) {
        field = value
        viewModel.textData.textStyle = value
    }

    /**
     * 0 -> center
     * 1 -> left
     * 2 -> right
     */
    private var alignmentIndex = TEXT_ALIGNMENT_CENTER
    set(value) {
        field = value
        viewModel.textData.textAlignment = value
    }

    private val textColorItemRef: Array<AddTextColorItem?> = Array(2) { null }
    private var activeColorIndex = 0
    set(value) {
        field = value
        viewModel.textData.textColor = colorList[value]
    }

    private var isColorState = false

    private var positionIndex = TEXT_POSITION_BOTTOM
    set(value) {
        field = value
        viewModel.textData.textPosition = value
    }

    fun getInputResult(): EditorAddTextUiModel {
        return EditorAddTextUiModel(
            textAlignment = alignmentIndex,
            textColor = colorList[activeColorIndex],
            textStyle = activeStyleIndex,
            textValue = getUserInput(),
            textPosition = positionIndex
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            editorR.layout.fragment_add_text_layout,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(viewModel.pageMode.value) {
            AddTextActivity.TEXT_MODE -> {
                viewBinding?.textOverlayContainer?.show()
            }
            AddTextActivity.POSITION_MODE -> {
                viewBinding?.let {
                    it.positionOverlayContainer.show()
                    it.actionBtnContainer.show()
                }
            }
        }

        initStartValue()
        initScreenHeight()
        initFontSelection()
        initListener()
        initFontColor()
        initTextChangeListener()
        initPositionButton()

        setColorGradient()
    }

    override fun initObserver() {
        viewModel.imgUrl.observe(viewLifecycleOwner) {
            viewBinding?.addTextFragmentImg?.loadImage(it)
        }
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    private fun initListener() {
        viewBinding?.let {
            it.alignmentIcon.setOnClickListener { _ ->
                setAlignment(it.alignmentIcon)
            }

            it.textColor.setOnClickListener { _ ->
                changeTextAndColor(it.textColor)
            }

            // left -> right -> top -> bottom
            val positionViewContainer = it.positionOverlayContainer
            for (i in 0 until positionViewContainer.childCount) {
                positionViewContainer.getChildAt(i).setOnClickListener {
                    try {
                        positionIndex = i
                        implementAddTextData()
                    } catch (_: Exception) {}
                }
            }

            it.btnCancel.setOnClickListener {
                try {
                    (activity as AddTextActivity).finish()
                } catch (_: Exception) {}
            }

            it.btnSave.setOnClickListener {
                try {
                    (activity as AddTextActivity).finishPage()
                } catch (_: Exception) {}
            }
        }
    }

    // style item click listener
    private fun changeFontStyle(styleIndex: Int) {
        // set inactive style
        textStyleItemRef[activeStyleIndex]?.setInactive()

        // set active style
        textStyleItemRef[styleIndex]?.setActive()

        viewBinding?.addTextInput?.apply {
            when (styleIndex) {
                TEXT_STYLE_REGULAR -> setTypeface(null, Typeface.NORMAL)
                TEXT_STYLE_BOLD -> setTypeface(null, Typeface.BOLD)
                TEXT_STYLE_ITALIC -> setTypeface(null, Typeface.ITALIC)
            }
        }

        activeStyleIndex = styleIndex
    }

    // color item click listener
    private fun changeFontColor(colorRef: Int) {
        viewBinding?.addTextInput?.let {
            val color = ContextCompat.getColor(requireContext(), colorRef)
            it.setTextColor(color)
            it.setHintTextColor(color)
        }
    }

    // alignment click listener
    private fun setAlignment(icon: IconUnify) {
        alignmentIndex++
        if (alignmentIndex > TEXT_ALIGNMENT_RIGHT) alignmentIndex = 0

        val gravity: Int
        val iconRef = when (alignmentIndex) {
            TEXT_ALIGNMENT_CENTER -> {
                gravity = Gravity.CENTER
                IconUnify.FORMAT_CENTER
            }
            TEXT_ALIGNMENT_LEFT -> {
                gravity = Gravity.START
                IconUnify.FORMAT_ALIGN_LEFT
            }
            else -> {
                gravity = Gravity.END
                IconUnify.FORMAT_ALIGN_RIGHT
            }
        }

        viewBinding?.addTextInput?.gravity = gravity
        icon.setImage(iconRef)
    }

    // change between color wheel & text style
    private fun changeTextAndColor(icon: IconUnify) {
        isColorState = !isColorState

        if (isColorState) {
            viewBinding?.fontColorContainer?.show()
            viewBinding?.fontSelectionContainer?.hide()

            icon.setImage(IconUnify.TEXT)
        } else {
            viewBinding?.fontColorContainer?.hide()
            viewBinding?.fontSelectionContainer?.show()

            setColorGradient()
        }
    }

    private fun initFontSelection() {
        viewBinding?.fontSelectionContainer?.let { it ->
            for (styleIndex in TEXT_STYLE_REGULAR..TEXT_STYLE_ITALIC) {
                val view = View.inflate(context, editorR.layout.add_text_font_selection_item, null)

                viewToFontCard(view)?.let { styleItem ->
                    when (styleIndex) {
                        TEXT_STYLE_BOLD -> {
                            styleItem.setStyleBold()
                        }
                        TEXT_STYLE_ITALIC -> {
                            styleItem.setStyleItalic()
                        }
                    }
                    textStyleItemRef[styleIndex] = styleItem
                    styleItem.card.setOnClickListener {
                        changeFontStyle(styleIndex)
                    }

                    if (styleIndex == activeStyleIndex) {
                        styleItem.setActive()
                    }
                }

                it.addView(view)
            }
        }
    }

    private fun initFontColor() {
        colorList.forEachIndexed { index, colorRef ->
            viewBinding?.fontColorContainer?.addView(
                AddTextColorItem(requireContext()).apply {
                    setColor(colorRef)
                    setOnClickListener {
                        changeFontColor(colorRef)
                        textColorItemRef[index]?.setActive()
                        textColorItemRef[activeColorIndex]?.setInactive()

                        activeColorIndex = index
                    }
                    if (index == activeColorIndex) this.setActive()

                    textColorItemRef[index] = this
                }
            )
        }
    }

    private fun viewToFontCard(view: View): AddTextStyleItem? {
        try {
            val card = (view as CardUnify2)
            val typo = card.findViewById<Typography>(editorR.id.font_selection_item)
            return AddTextStyleItem(card, typo)
        } catch (_: Exception) {
        }
        return null
    }

    private fun getUserInput(): String {
        return viewBinding?.addTextInput?.text?.toString() ?: ""
    }

    private fun initScreenHeight() {
        viewBinding?.addTextFragmentImg?.let {
            it.viewTreeObserver?.addOnGlobalLayoutListener {
                val r = Rect()
                val parent = (it.parent as View)
                parent.getWindowVisibleDisplayFrame(r)

                val screenHeight: Int = parent.rootView.height
                val heightDifference: Int = screenHeight - (r.bottom - r.top)

                if (defaultPadding == 0) {
                    defaultPadding = heightDifference
                }

                viewBinding?.parentContainer?.apply {
                    setPadding(0, 0, 0, heightDifference - defaultPadding)
                }
            }
        }
    }

    private fun initTextChangeListener() {
        viewBinding?.addTextInput?.let {
            it.setText(viewModel.textData.textValue)
            it.doAfterTextChanged { result ->
                result?.let { text ->
                    viewModel.setTextInput(text.toString())
                }
            }
        }
    }

    private fun initStartValue() {
        activeStyleIndex = viewModel.textData.textStyle
        alignmentIndex = viewModel.textData.textAlignment
        activeColorIndex = colorList.find { viewModel.textData.textColor == it } ?: 0
        positionIndex = viewModel.textData.textPosition
    }

    private fun initPositionButton() {
        val positionViewContainer = viewBinding?.positionOverlayContainer

        positionViewContainer?.post {
            positionViewContainer.let {
                val size = (Math.min(it.width, it.height) * POSITION_BOX_PERCENTAGE).toInt()
                for (i in 0 until it.childCount) {
                    it.getChildAt(i).layoutParams.apply {
                        width = size
                        height = size
                    }
                }
            }
        }
    }

    private fun implementAddTextData() {
        val bitmap = viewModel.getAddTextFilterOverlay()
        viewBinding?.overlayTest?.setImageBitmap(bitmap)
    }

    // Temporary, will be delete later when icon already on unify icon collection
    private fun setColorGradient() {
        viewBinding?.textColor?.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), editorR.drawable.ic_color_gradient)
        )
    }

    companion object {
        private const val SCREEN_NAME = "AddTextInputPage"

        private const val POSITION_BOX_PERCENTAGE = 0.2f
    }
}
