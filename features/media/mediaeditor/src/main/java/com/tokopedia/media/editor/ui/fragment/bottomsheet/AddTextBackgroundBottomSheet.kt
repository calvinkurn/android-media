package com.tokopedia.media.editor.ui.fragment.bottomsheet

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.editor.databinding.AddTextBackgroundBottomsheetLayoutBinding
import com.tokopedia.media.editor.data.AddTextColorProvider
import com.tokopedia.media.editor.data.entity.AddTextBackgroundTemplate
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.widget.AddTextBackgroundBtmItemView
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import javax.inject.Inject

class AddTextBackgroundBottomSheet(
    private val imgUrl: String?,
    val onFinish: (color: Int, backgroundModel: AddTextBackgroundTemplate) -> Unit
) : BottomSheetUnify() {

    @Inject
    lateinit var addTextColorProvider: AddTextColorProvider

    private var colorButtonRef: ArrayList<ChipsUnify> = arrayListOf()
    private var templateModelRef: ArrayList<AddTextBackgroundBtmItemView> = arrayListOf()

    // relate with colorButtonRef
    private var colorSelectionIndex = 0
    private var modelSelectionIndex = 0

    private var backgroundColorCollection = mapOf<Int, String>()

    private var viewBinding: AddTextBackgroundBottomsheetLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AddTextBackgroundBottomsheetLayoutBinding.inflate(LayoutInflater.from(inflater.context))
        setChild(viewBinding?.root)

        initializeView(inflater.context)
        initializeActiveState()
        initializeButtonListener()
        setBackgroundItem()

        setTitle(getString(editorR.string.add_text_background_selection_title))

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeView(context: Context) {
        viewBinding?.apply {
            backgroundColorCollection = addTextColorProvider.getListOfTextWithBackgroundColor()
            backgroundColorCollection.mapValues { (color, text) ->
                val chip = ChipsUnify(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setMargin(0, 0, CHIP_PADDING.toPx(), 0)
                    chip_text.text = text
                    chip_image_icon.type = ImageUnify.TYPE_CIRCLE

                    chipImageResource = if (color == Color.BLACK) {
                        ColorDrawable(color)
                    } else {
                        ContextCompat.getDrawable(context, editorR.drawable.add_text_white_circle)
                    }
                }
                colorButtonRef.add(chip)
                btmshtAddTextColorContainer.addView(chip)
            }

            templateModelRef.add(
                btmshtAddTextTemplateModelFull
            )
            templateModelRef.add(
                btmshtAddTextTemplateModelFloating
            )
            templateModelRef.add(
                btmshtAddTextTemplateModelSideCut
            )
        }
    }

    private fun initializeActiveState() {
        if (colorSelectionIndex in (0 until colorButtonRef.count())) {
            colorButtonRef[colorSelectionIndex].chipType = ChipsUnify.TYPE_SELECTED
        }

        if (modelSelectionIndex in (0 until templateModelRef.count())) {
            templateModelRef[modelSelectionIndex].setActive()
        }
    }

    private fun initializeButtonListener() {
        colorButtonRef.forEachIndexed { index, chipsUnify ->
            chipsUnify.setOnClickListener {
                colorRefClick(index)
            }
        }

        templateModelRef.forEachIndexed { index, view ->
            view.setOnClickListener {
                modelRefClick(index)
            }
        }

        viewBinding?.btmshtAddTextActionLanjut?.setOnClickListener {
            onFinish(
                backgroundColorCollection.toList()[colorSelectionIndex].first,
                AddTextBackgroundTemplate.getBackgroundModelByIndex(modelSelectionIndex)
            )
            dismiss()
        }
    }

    private fun modelRefClick(index: Int) {
        // update previous view state normal
        templateModelRef[modelSelectionIndex].setViewState(false)

        // update previous view state selected
        modelSelectionIndex = index
        templateModelRef[modelSelectionIndex].setViewState(true)
    }

    private fun AddTextBackgroundBtmItemView.setViewState(isActive: Boolean) {
        if (isActive) {
            setActive()
        } else {
            setInactive()
        }
    }

    private fun colorRefClick(index: Int) {
        // update previous chips state normal
        colorButtonRef[colorSelectionIndex].setChipState(false)

        // update new chip state selected
        colorSelectionIndex = index
        colorButtonRef[colorSelectionIndex].setChipState(true)

        // update BottomSheetItem
        setBackgroundItem()
    }

    private fun ChipsUnify.setChipState(isActive: Boolean) {
        chipType = if (isActive) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    // initialize item image & background model
    private fun setBackgroundItem() {
        imgUrl?.let { imgUrlReady ->
            templateModelRef.forEachIndexed { index, item ->
                item.setImage(imgUrlReady)

                val colorIntRes = backgroundColorCollection.toList()[colorSelectionIndex].first

                item.setBackgroundModel(
                    index,
                    colorIntRes,
                    addTextColorProvider
                )
            }
        }
    }

    private fun initInjector() {
        activity?.applicationContext?.let {
            EditorInjector
                .get(it)
                .inject(this)
        }
    }

    companion object {
        private const val CHIP_PADDING = 16
    }
}
