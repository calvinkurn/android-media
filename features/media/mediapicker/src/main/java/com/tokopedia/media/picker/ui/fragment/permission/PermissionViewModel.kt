package com.tokopedia.media.picker.ui.fragment.permission

import androidx.lifecycle.*
import com.tokopedia.media.R
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.ui.uimodel.PermissionUiModel
import com.tokopedia.picker.common.types.PageType
import javax.inject.Inject

class PermissionViewModel @Inject constructor(
    private val cacheManager: ParamCacheManager
) : ViewModel(), LifecycleObserver {

    /*
    * will return a dynamic wording of permission
    * based on request following pageType and modeType.
    *
    * first = title
    * second = message
    * */
    private val _dynamicWording = MutableLiveData<Pair<Int, Int>>()
    val dynamicWording: LiveData<Pair<Int, Int>> get() = _dynamicWording

    private val _permissionList = MediatorLiveData<List<PermissionUiModel>>()
    val permissionList: LiveData<List<PermissionUiModel>> get() = _permissionList

    private val _permissionCodeName = MediatorLiveData<List<String>>()
    val permissionCodeName: LiveData<List<String>> get() = _permissionCodeName

    init {
        _permissionCodeName.addSource(_permissionList) {
            _permissionCodeName.value = it.map { model ->
                model.name
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getDynamicPermissionList() {
        _permissionList.value = PermissionUiModel.getOrCreate(
            cacheManager.get().pageType(),
            cacheManager.get().modeType()
        )
    }

    fun initOrCreateDynamicWording() {
        val (title, message) = when (cacheManager.get().pageType()) {
            PageType.CAMERA -> if (cacheManager.get().isImageModeOnly()) {
                Pair(
                    R.string.picker_title_camera_photo_permission,
                    R.string.picker_message_camera_photo_permission,
                )
            } else {
                Pair(
                    R.string.picker_title_camera_video_permission,
                    R.string.picker_message_camera_video_permission,
                )
            }
            PageType.GALLERY -> Pair(
                R.string.picker_title_gallery_permission,
                R.string.picker_message_gallery_permission,
            )
            else -> Pair(
                R.string.picker_title_common_permission,
                R.string.picker_message_common_permission,
            )
        }

        _dynamicWording.value = Pair(title, message)
    }

}