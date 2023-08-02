package com.tokopedia.picker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.picker.common.EXTRA_PICKER_PARAM
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.databinding.ActivityPickerDebugBinding
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.widget.drawerselector.DebugDrawerActionType
import com.tokopedia.picker.widget.drawerselector.DebugDrawerSelectionWidget
import com.tokopedia.utils.view.binding.viewBinding

class DebugPickerActivity : AppCompatActivity(), DebugDrawerSelectionWidget.Listener {

    private val binding: ActivityPickerDebugBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_debug)
        initDefaultApplink()
        initConfig()

        binding?.btnAction?.setOnClickListener {
            val appLink = binding?.edtApplink?.editText?.text ?: ""
            val intent = RouteManager.getIntent(applicationContext, appLink.toString()).apply {
                val editorJson = binding?.editorConfig?.text ?: ""
                val fromEditorJson = Gson().fromJson(editorJson.toString(), EditorParam::class.java)

                val pickerJson = binding?.pickerConfig?.text ?: ""
                val fromPickerJson = Gson().fromJson(pickerJson.toString(), PickerParam::class.java).apply {
                    if (isEditorEnabled()) {
                        withEditor {
                            resetRatioList()
                            fromEditorJson.ratioList().forEach {
                                when (it) {
                                    ImageRatioType.RATIO_1_1 -> ratioListAdd1to1()
                                    ImageRatioType.RATIO_3_4 -> ratioListAdd3to4()
                                    ImageRatioType.RATIO_2_1 -> ratioListAdd2to1()
                                }
                            }

                            fromEditorJson.editorToolsList().apply {
                                if (contains(EditorToolType.REMOVE_BACKGROUND)) {
                                    withRemoveBackground()
                                }

                                if (contains(EditorToolType.WATERMARK)) {
                                    withWatermark()
                                }

                                if (contains(EditorToolType.ADD_LOGO)) {
                                    withAddLogo()
                                }

                                if (contains(EditorToolType.ADD_TEXT)) {
                                    withAddText()
                                }
                            }

                            when (fromEditorJson.autoCropRatio()) {
                                ImageRatioType.RATIO_1_1 -> autoCrop1to1()
                                ImageRatioType.RATIO_3_4 -> autoCrop3to4()
                                ImageRatioType.RATIO_2_1 -> autoCrop2to1()
                                else -> {}
                            }
                        }
                    }
                }

                putExtra(EXTRA_PICKER_PARAM, fromPickerJson)
            }

            startActivityForResult(intent, REQUEST_PICKER_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICKER_CODE && resultCode == RESULT_OK) {
            val elements = data?.getParcelableExtra(EXTRA_RESULT_PICKER) ?: PickerResult()

            val resultList = if (elements.editedImages.isEmpty()) elements.originalPaths else elements.editedImages

            val rawList = resultList.mapIndexed { index, imagePath ->
                if (imagePath.isEmpty()) {
                    elements.originalPaths[index]
                } else {
                    imagePath
                }
            }

            val uiModels = rawList
                .map { PickerFile(it) }
                .map { it.toUiModel() }

            binding?.drawerSelector?.addAllData(uiModels)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onDataSetChanged(action: DebugDrawerActionType) {
    }

    override fun onItemClicked(media: MediaUiModel) {
    }

    private fun initDefaultApplink() {
        binding?.edtApplink?.editText?.setText(
            ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
        )
    }

    private fun initConfig() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val pickerConfigJson = gson.toJson(PickerParam().apply { })
        val editorConfigJson = gson.toJson(
            EditorParam().apply {
                withRemoveBackground()
                withWatermark()
                autoCrop1to1()
            }
        )

        binding?.pickerConfig?.setText(pickerConfigJson)
        binding?.editorConfig?.setText(editorConfigJson)
    }

    companion object {
        private const val REQUEST_PICKER_CODE = 123
    }
}
