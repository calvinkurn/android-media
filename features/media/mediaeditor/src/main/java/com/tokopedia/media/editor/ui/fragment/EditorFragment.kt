package com.tokopedia.media.editor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.activity.detail.EditorDetailActivity
import com.tokopedia.media.editor.ui.component.EditorToolUiComponent
import com.tokopedia.media.editor.ui.component.ThumbnailDrawerUiComponent
import com.tokopedia.media.editor.ui.param.EditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import javax.inject.Inject

class EditorFragment @Inject constructor() : TkpdBaseV4Fragment(), EditorToolUiComponent.Listener {

    private val editorToolComponent by uiComponent {
        EditorToolUiComponent(it, this)
    }

    private val thumbnailDrawerComponent by uiComponent {
        ThumbnailDrawerUiComponent(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_main_editor,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editorToolComponent.setupView()

        thumbnailDrawerComponent.setupView(
            listOf(
                "https://images.pexels.com/photos/1926404/pexels-photo-1926404.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1088614/pexels-photo-1088614.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/415980/pexels-photo-415980.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1926404/pexels-photo-1926404.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/1088614/pexels-photo-1088614.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                "https://images.pexels.com/photos/415980/pexels-photo-415980.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            )
        )
    }

    override fun onEditorToolClicked(type: Int) {
        EditorDetailActivity.start(
            requireContext(),
            EditorParam("", type)
        )
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Main Editor"
    }

}