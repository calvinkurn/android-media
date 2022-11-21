package com.tokopedia.media.editor.ui.activity.detail

import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.*
import com.tokopedia.media.editor.domain.GetWatermarkUseCase
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.domain.param.WatermarkUseCaseParam
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.ResourceProvider
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DetailEditorViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userSession: UserSessionInterface,
    private val colorFilterRepository: ColorFilterRepository,
    private val removeBackgroundUseCase: SetRemoveBackgroundUseCase,
    private val contrastFilterRepository: ContrastFilterRepository,
    private val watermarkFilterRepository: WatermarkFilterRepository,
    private val rotateFilterRepository: RotateFilterRepository,
    private val saveImageRepository: SaveImageRepository,
    private val getWatermarkUseCase: GetWatermarkUseCase
) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _intentDetailUiModel = MutableLiveData<EditorDetailUiModel>()
    val intentDetailUiModel: LiveData<EditorDetailUiModel> get() = _intentDetailUiModel

    private var _intentUiModel = MutableLiveData<EditorUiModel>()
    val intentUiModel: LiveData<EditorUiModel> get() = _intentUiModel

    private var _brightnessFilter = MutableLiveData<ColorMatrixColorFilter>()
    val brightnessFilter: LiveData<ColorMatrixColorFilter> get() = _brightnessFilter

    private var _removeBackground = MutableLiveData<File?>()
    val removeBackground: LiveData<File?> get() = _removeBackground

    private var _contrastFilter = MutableLiveData<Bitmap>()
    val contrastFilter: LiveData<Bitmap> get() = _contrastFilter

    private var _watermarkFilter = MutableLiveData<Bitmap>()
    val watermarkFilter: LiveData<Bitmap> get() = _watermarkFilter

    private var _editorParam = MutableLiveData<EditorParam>()
    val editorParam: LiveData<EditorParam> get() = _editorParam

    fun setEditorParam(data: EditorParam) {
        _editorParam.postValue(data)
    }

    fun setIntentDetailUiModel(data: EditorDetailUiModel) {
        _intentDetailUiModel.postValue(data)
    }

    fun setIntentUiModel(data: EditorUiModel) {
        _intentUiModel.postValue(data)
    }

    fun setBrightness(value: Float?) {
        if (value == null) return
        _brightnessFilter.value = colorFilterRepository.brightness(value)
    }

    fun setContrast(value: Float?, sourceBitmap: Bitmap?) {
        if (value == null || sourceBitmap == null) return
        _contrastFilter.value = contrastFilterRepository.contrast(
            value,
            sourceBitmap.copy(sourceBitmap.config, true)
        )
    }

    fun setRemoveBackground(filePath: String, onError: (t: Throwable) -> Unit) {
        if (_removeBackground.value != null) {
            _removeBackground.value = _removeBackground.value
            return
        }

        viewModelScope.launch {
            try {
                removeBackgroundUseCase(filePath)
                    .onStart {
                        _isLoading.value = true
                    }
                    .onCompletion {
                        _isLoading.value = false
                    }.collect {
                        _removeBackground.value = it
                    }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun setWatermark(
        bitmapSource: Bitmap,
        type: WatermarkType,
        isThumbnail: Boolean = false,
        detailUiModel: EditorDetailUiModel,
        useStorageColor: Boolean
    ) {
        initializeWatermarkAsset()

        _watermarkFilter.value = getWatermarkUseCase(
            WatermarkUseCaseParam(
                source = bitmapSource,
                type = type,
                shopNameParam = userSession.shopName,
                isThumbnail = isThumbnail,
                element = detailUiModel,
                useStorageColor = useStorageColor
            )
        )
    }

    fun setWatermarkFilterThumbnail(
        implementedBaseBitmap: Bitmap
    ): Pair<Bitmap, Bitmap> {
        initializeWatermarkAsset()

        return watermarkFilterRepository.watermarkDrawerItem(
            implementedBaseBitmap,
            userSession.shopName
        )
    }

    fun setRotate(
        uCropRef: EditorDetailPreviewWidget?,
        rotateDegree: Float,
        isRotateRatio: Boolean,
        ratio: Pair<Float, Float>? = null,
        isPreviousState: Boolean = false
    ) {
        uCropRef?.let {
            rotateFilterRepository.rotate(it, rotateDegree, isRotateRatio, ratio, isPreviousState)
        }
    }

    fun setMirror(uCropRef: EditorDetailPreviewWidget?) {
        uCropRef?.let {
            rotateFilterRepository.mirror(it)
        }
    }

    var rotateNumber: Int
        get() = rotateFilterRepository.rotateNumber
        set(value) {
            rotateFilterRepository.rotateNumber = value
        }

    var rotateSliderValue: Float
        get() = rotateFilterRepository.sliderValue
        set(value) {
            rotateFilterRepository.sliderValue = value
        }

    val rotateRotationFinalDegree: Float get() = rotateFilterRepository.getFinalRotationDegree()

    var rotateInitialScale: Float
        get() = rotateFilterRepository.initialScale
        set(value) {
            rotateFilterRepository.initialScale = value
        }

    var rotatePreviousDegree: Float
        get() = rotateFilterRepository.previousDegree
        set(value) {
            rotateFilterRepository.previousDegree = value
        }

    fun saveImageCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File? {
        return saveImageRepository.saveToCache(
            bitmapParam, filename, sourcePath
        )
    }

    fun getAvatarShop(): String {
        return userSession.shopAvatar
    }

    private fun initializeWatermarkAsset() {
        if (!watermarkFilterRepository.isAssetInitialize()) {
            resourceProvider.getWatermarkLogoDrawable()?.let {
                val textColor = resourceProvider.getWatermarkTextColor()
                watermarkFilterRepository.setAsset(
                    it,
                    textLightColor = textColor.first ?: WATERMARK_TEXT_COLOR_DEFAULT,
                    textDarkColor = textColor.second ?: WATERMARK_TEXT_COLOR_DEFAULT
                )
            }
        }
    }

    companion object {
        private const val WATERMARK_TEXT_COLOR_DEFAULT = 0
    }
}
