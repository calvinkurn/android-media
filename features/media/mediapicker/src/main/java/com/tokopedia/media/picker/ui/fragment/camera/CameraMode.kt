package com.tokopedia.media.picker.ui.fragment.camera

sealed class CameraMode(val value: Int) {
    object Photo: CameraMode(PHOTO_CAMERA_INDEX)
    object Video: CameraMode(VIDEO_CAMERA_INDEX)

    companion object {
        private const val PHOTO_CAMERA_INDEX = 0
        private const val VIDEO_CAMERA_INDEX = 1

        fun to(mode: CameraMode): Int {
            return mode.value
        }

        fun to(mode: Int): CameraMode {
            if (mode == PHOTO_CAMERA_INDEX) return Photo
            return Video
        }
    }
}