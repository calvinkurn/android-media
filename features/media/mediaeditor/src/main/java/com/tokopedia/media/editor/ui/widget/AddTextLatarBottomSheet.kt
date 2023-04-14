package com.tokopedia.media.editor.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify

class AddTextLatarBottomSheet(val onFinish: (color: Int, latarModel: Int) -> Unit) :
    BottomSheetUnify() {

    private var colorButtonRef: ArrayList<ChipsUnify> = arrayListOf()
    private var templateModelRef: ArrayList<View> = arrayListOf()

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
        }
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
        }
    }

    private fun initializeActiveState() {
        if (colorSelectionIndex in(0..colorButtonRef.count())) {
            colorButtonRef[colorSelectionIndex].chipType = ChipsUnify.TYPE_SELECTED
        }

        if (modelSelectionIndex in(0..templateModelRef.count())) {
            templateModelRef[modelSelectionIndex].alpha = 0.5f
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
    }

    private fun modelRefClick(index: Int) {
        // update previous view state normal
        templateModelRef[modelSelectionIndex].setViewState(false)

        // update previous view state selected
        modelSelectionIndex = index
        templateModelRef[modelSelectionIndex].setViewState(true)
    }

    private fun View.setViewState(isActive: Boolean) {
        this.alpha = if (isActive) {
             0.2f
        } else {
            1f
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
}
