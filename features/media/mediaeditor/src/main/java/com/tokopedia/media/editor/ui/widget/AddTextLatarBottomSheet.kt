package com.tokopedia.media.editor.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.UnifyButton

class AddTextLatarBottomSheet(private val imgUrl: String?, val onFinish: (color: Int, latarModel: Int) -> Unit) :
    BottomSheetUnify() {

    private var colorButtonRef: ArrayList<ChipsUnify> = arrayListOf()
    private var templateModelRef: ArrayList<AddTextLatarBtmItem> = arrayListOf()
    private var mNextButton: UnifyButton? = null

    private var colorSelectionIndex = 0
    private var modelSelectionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(editorR.layout.add_text_latar_bottomsheet, container)?.apply {
            setChild(this)

            initializeViewRef(this)
            initializeActiveState()
            initializeButtonListener()
            initializeLatarItem()
        }

        setTitle(TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeViewRef(parent: View) {
        parent.apply {
            colorButtonRef.add(
                findViewById(editorR.id.btmsht_add_text_color_black)
            )
            colorButtonRef.add(
                findViewById(editorR.id.btmsht_add_text_color_white)
            )

            templateModelRef.add(
                findViewById(editorR.id.btmsht_add_text_template_model_full)
            )
            templateModelRef.add(
                findViewById(editorR.id.btmsht_add_text_template_model_floating)
            )
            templateModelRef.add(
                findViewById(editorR.id.btmsht_add_text_template_model_side_cut)
            )

            mNextButton = findViewById(editorR.id.btmsht_add_text_action_lanjut)
        }
    }

    private fun initializeActiveState() {
        if (colorSelectionIndex in(0..colorButtonRef.count())) {
            colorButtonRef[colorSelectionIndex].chipType = ChipsUnify.TYPE_SELECTED
        }

        if (modelSelectionIndex in(0..templateModelRef.count())) {
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
            view.setClickListener {
                modelRefClick(index)
            }
            view.setOnClickListener {
                modelRefClick(index)
            }
        }

        mNextButton?.setOnClickListener {
            onFinish(colorSelectionIndex, modelSelectionIndex)
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

    private fun AddTextLatarBtmItem.setViewState(isActive: Boolean) {
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
    }

    private fun ChipsUnify.setChipState(isActive: Boolean) {
        chipType = if (isActive) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    // initialize item image & latar model
    private fun initializeLatarItem() {
        imgUrl?.let { imgUrlReady ->
            templateModelRef.forEachIndexed { index, item ->
                item.setImage(imgUrlReady)

                // please refer index with EditorAddTextUiModel.TEXT_LATAR_TEMPLATE_FULL
                item.setLatarModel(index)
            }
        }
    }

    companion object {
        private const val TITLE = "Warna & Template"
    }
}
