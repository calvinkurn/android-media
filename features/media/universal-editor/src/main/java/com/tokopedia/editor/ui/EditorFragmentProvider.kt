package com.tokopedia.editor.ui

import com.tokopedia.editor.base.BaseEditorFragment

interface EditorFragmentProvider : ImageFragmentProvider, VideoFragmentProvider {
    /**
     * This fragment will maintain the input text for add text
     * for both Video and Image. The fragment contains the text adjustment,
     * custom styling, etc.
     */
    fun inputTextFragment(): BaseEditorFragment
}

interface ImageFragmentProvider {

    /**
     * A container of image editor page
     */
    fun imageMainEditorFragment(): BaseEditorFragment

    /**
     * This tool allows user to scale, rotate, crop, nor resize the image within canvas.
     */
    fun placementImageFragment(): BaseEditorFragment
}

interface VideoFragmentProvider {

    /**
     * A container of video editor page
     */
    fun videoMainEditorFragment(): BaseEditorFragment
}
