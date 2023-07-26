package com.tokopedia.editor.ui

import androidx.fragment.app.Fragment

interface EditorFragmentProvider : ImageFragmentProvider {
    /**
     * Fragment for main page of universal editor.
     * This fragment contains the editor tool, container view, etc.
     */
    fun mainEditorFragment(): Fragment

    /**
     * This fragment will maintain the input text for add text
     * for both Video and Image. The fragment contains the text adjustment,
     * custom styling, etc.
     */
    fun inputTextFragment(): Fragment
}

interface ImageFragmentProvider {

    /**
     * This tool allows user to scale, rotate, crop, nor resize the image within canvas.
     */
    fun imagePlacementFragment(): Fragment
}
