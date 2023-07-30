package com.tokopedia.editor.ui

import androidx.fragment.app.Fragment

interface EditorFragmentProvider : ImageFragmentProvider, VideoFragmentProvider {
    /**
     * This fragment will maintain the input text for add text
     * for both Video and Image. The fragment contains the text adjustment,
     * custom styling, etc.
     */
    fun inputTextFragment(): Fragment
}

interface ImageFragmentProvider {

    /**
     * A container of image editor page
     */
    fun imageMainEditorFragment(): Fragment

    /**
     * This tool allows user to scale, rotate, crop, nor resize the image within canvas.
     */
    fun imagePlacementFragment(): Fragment
}

interface VideoFragmentProvider {

    /**
     * A container of video editor page
     */
    fun videoMainEditorFragment(): Fragment
}
