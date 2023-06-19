package com.tokopedia.media.editor.ui.fragment

import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.data.AddTextColorProvider
import com.tokopedia.media.editor.databinding.FragmentAddTextLayoutBinding
import com.tokopedia.media.editor.ui.activity.addtext.AddTextActivity
import com.tokopedia.media.editor.ui.activity.addtext.AddTextViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.widget.AddTextColorItemView
import com.tokopedia.media.editor.ui.widget.AddTextStyleItemView
import com.tokopedia.media.editor.data.entity.AddTextAlignment
import com.tokopedia.media.editor.data.entity.AddTextAlignment.Companion.increaseIndex
import com.tokopedia.media.editor.data.entity.AddTextPosition
import com.tokopedia.media.editor.data.entity.AddTextStyle
import com.tokopedia.media.editor.data.entity.AddTextTemplateMode
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class AddTextFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    addTextColorProvider: AddTextColorProvider
) : BaseEditorFragment() {

    private val viewBinding: FragmentAddTextLayoutBinding? by viewBinding()
    private val viewModel: AddTextViewModel by activityViewModels { viewModelFactory }

    private var defaultPadding = 0

    private var colorList = addTextColorProvider.getListOfTextColor()

    /**
     * 0 -> regular
     * 1 -> bold
     * 2 -> italic
     */
    private val textStyleItemRef: Array<AddTextStyleItemView?> = Array(3) { null }
    private var activeStyleIndex = AddTextStyle.REGULAR.value
        set(value) {
            field = value
            viewModel.textData.textStyle = value
        }

    private var alignmentIndex = AddTextAlignment.CENTER
        set(value) {
            field = value
            viewModel.textData.textAlignment = value
        }

    private val textColorItemRef: Array<AddTextColorItemView?> = Array(2) { null }
    private var activeColorIndex = DEFAULT_COLOR_INDEX
        set(value) {
            field = value
            viewModel.textData.textColor = colorList[field]
        }

    private var isColorState = false

    private var positionIndex = AddTextPosition.BOTTOM
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
            textPosition = positionIndex,
            textTemplate = viewModel.textData.textTemplate,
            textTemplateBackgroundDetail = viewModel.textData.getBackgroundTemplate()
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

        when (viewModel.pageMode.value) {
            AddTextActivity.TEXT_MODE -> {
                viewBinding?.textOverlayContainer?.show()

                viewBinding?.addTextInput?.requestFocus()
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }

            AddTextActivity.POSITION_MODE -> {
                viewBinding?.let {
                    it.positionOverlayContainer.show()
                    it.actionBtnContainer.show()
                }

                implementAddTextData()
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

            it.btnCancel.setOnClickListener {
                try {
                    (activity as AddTextActivity).finish()
                } catch (_: Exception) {
                }
            }

            renderPositionButton { view, index ->
                view.setOnClickListener {
                    try {
                        positionIndex = AddTextPosition.getPositionByIndex(index)
                        implementAddTextData()
                        renderPositionButton()
                    } catch (_: Exception) {
                    }
                }
            }

            it.btnSave.setOnClickListener {
                try {
                    (activity as AddTextActivity).finishPage()
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun renderPositionButton(
        viewAction: (view: View, index: Int) -> Unit = { _, _ -> }
    ) {
        viewBinding?.let {
            // left -> right -> top -> bottom
            val positionViewContainer = it.positionOverlayContainer
            for (i in 0 until positionViewContainer.childCount) {
                // skip top & left when template is using background
                if (viewModel.textData.textTemplate == AddTextTemplateMode.BACKGROUND && (i == 0 || i == 2)) {
                    continue
                }

                positionViewContainer.getChildAt(i).apply {
                    if (i == positionIndex.value) {
                        this.hide()
                    } else {
                        this.show()
                    }

                    viewAction(this, i)
                }
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
                AddTextStyle.REGULAR.value -> setTypeface(null, Typeface.NORMAL)
                AddTextStyle.BOLD.value -> setTypeface(null, Typeface.BOLD)
                AddTextStyle.ITALIC.value -> setTypeface(null, Typeface.ITALIC)
            }
        }

        activeStyleIndex = styleIndex
    }

    // color item click listener
    private fun changeFontColor(colorRef: Int) {
        viewBinding?.addTextInput?.let {
            it.setTextColor(colorRef)
            it.setHintTextColor(colorRef)
        }
    }

    // alignment click listener
    private fun setAlignment(icon: IconUnify) {
        alignmentIndex = alignmentIndex.increaseIndex()
//        alignmentIndex++

        if (alignmentIndex > AddTextAlignment.LEFT) alignmentIndex = AddTextAlignment.CENTER

        val gravity: Int
        val iconRef = when (alignmentIndex) {
            AddTextAlignment.CENTER -> {
                gravity = Gravity.CENTER
                IconUnify.FORMAT_CENTER
            }

            AddTextAlignment.LEFT -> {
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
            for (styleIndex in AddTextStyle.REGULAR.value..AddTextStyle.ITALIC.value) {
                val view = View.inflate(context, editorR.layout.add_text_font_selection_item, null)

                viewToFontCard(view)?.let { styleItem ->
                    when (styleIndex) {
                        AddTextStyle.BOLD.value -> {
                            styleItem.setStyleBold()
                        }

                        AddTextStyle.ITALIC.value -> {
                            styleItem.setStyleItalic()
                        }
                    }
                    textStyleItemRef[styleIndex] = styleItem
                    styleItem.card.setOnClickListener {
                        changeFontStyle(styleIndex)
                    }

                    if (styleIndex == activeStyleIndex) {
                        styleItem.setActive()
                        changeFontStyle(styleIndex)
                    }
                }

                it.addView(view)
            }
        }
    }

    private fun initFontColor() {
        colorList.forEachIndexed { index, color ->
            viewBinding?.fontColorContainer?.addView(
                AddTextColorItemView(requireContext()).apply {
                    setColor(color)
                    setOnClickListener {
                        changeFontColor(color)
                        textColorItemRef[index]?.setActive()
                        textColorItemRef[activeColorIndex]?.setInactive()

                        activeColorIndex = index
                    }
                    if (index == activeColorIndex) {
                        this.setActive()
                        changeFontColor(color)
                    }

                    textColorItemRef[index] = this
                }
            )
        }
    }

    private fun viewToFontCard(view: View): AddTextStyleItemView? {
        return try {
            val card = (view as CardUnify2)
            val typo = card.findViewById<Typography>(editorR.id.font_selection_item)
            AddTextStyleItemView(card, typo)
        } catch (_: Exception) {
            null
        }
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
        val textColor = colorList.indexOf(viewModel.textData.textColor)

        activeStyleIndex = viewModel.textData.textStyle
        alignmentIndex = viewModel.textData.textAlignment
        activeColorIndex = if (textColor == -1) DEFAULT_COLOR_INDEX else textColor
        positionIndex = viewModel.textData.textPosition
    }

    private fun initPositionButton() {
        val positionViewContainer = viewBinding?.positionOverlayContainer

        positionViewContainer?.post {
            positionViewContainer.let {
                val size = (it.width.coerceAtMost(it.height) * POSITION_BOX_PERCENTAGE).toInt()
                for (i in 0 until it.childCount) {
                    it.getChildAt(i).apply {
                        this.layoutParams.apply {
                            width = size
                            height = size
                        }
                        requestLayout()
                    }
                }
            }
        }
    }

    private fun implementAddTextData() {
        val bitmap = viewModel.getAddTextFilterOverlay()
        viewBinding?.overlayAddText?.setImageBitmap(bitmap)
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

        private const val DEFAULT_COLOR_INDEX = 1
    }
}
