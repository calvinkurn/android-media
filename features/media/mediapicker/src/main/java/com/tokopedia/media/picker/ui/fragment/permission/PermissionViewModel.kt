package com.tokopedia.media.picker.ui.fragment.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.media.R
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.media.picker.utils.permission.PermissionModel
import com.tokopedia.media.picker.utils.permission.permissions
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import javax.inject.Inject

class PermissionViewModel @Inject constructor(
    private val cacheManager: PickerCacheManager
) : ViewModel() {

    /*
    * will return a dynamic wording of permission
    * based on request following pageType and modeType.
    *
    * first = title
    * second = message
    * */
    private val _dynamicWording = MutableLiveData<Pair<Int, Int>>()
    val dynamicWording: LiveData<Pair<Int, Int>> get() = _dynamicWording

    private val _permissionList = MediatorLiveData<List<PermissionModel>>()
    val permissionList: LiveData<List<PermissionModel>> get() = _permissionList

    private val _permissionCodeName = MediatorLiveData<List<String>>()
    val permissionCodeName: LiveData<List<String>> get() = _permissionCodeName

    init {
        _permissionCodeName.addSource(_permissionList) {
            _permissionCodeName.value = it.map { model ->
                model.name
            }
        }
    }

    fun getDynamicPermissionList() {
        _permissionList.value = permissions(
            cacheManager.get().pageType(),
            cacheManager.get().modeType()
        )
    }

    fun initOrCreateDynamicWording() {
        val (title, message) = when (cacheManager.get().pageType()) {
            PageType.CAMERA -> {
                when (cacheManager.get().modeType()) {
                    ModeType.IMAGE_ONLY -> {
                        Pair(
                            R.string.picker_title_camera_photo_permission,
                            R.string.picker_message_camera_photo_permission,
                        )
                    }
                    else -> {
                        Pair(
                            R.string.picker_title_camera_video_permission,
                            R.string.picker_message_camera_video_permission,
                        )
                    }
                }
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
