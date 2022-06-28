package com.tokopedia.media.editor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.component.ToolsUiComponent
import com.tokopedia.media.editor.ui.component.DrawerUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class EditorFragment @Inject constructor() : TkpdBaseV4Fragment()
    , ToolsUiComponent.Listener
    , DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""

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
        initObservable()
    }

    override fun onEditorToolClicked(type: Int) {
        DetailEditorActivity.start(
            requireContext(),
            EditorDetailUiModel(activeImageUrl, type)
        )
    }

    override fun onThumbnailDrawerClicked(url: String) {
        activeImageUrl = url

        viewBinding?.imgMainPreview?.loadImage(url) {
            centerCrop()
        }
    }

    private fun initObservable() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            editorToolComponent.setupView(it.editorTools)
            thumbnailDrawerComponent.setupView(it.imageUrls)
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Main Editor"
    }

}