package com.tokopedia.media.picker.ui.fragment.permission

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.media.R
import com.tokopedia.media.picker.utils.permission.PermissionModel
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PermissionViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val cacheManager = mockk<PickerCacheManager>(relaxed = true)
    private val permissionCodeName = mockk<Observer<List<String>>>(relaxed = true)

    private lateinit var viewModel: PermissionViewModel

    @Before
    fun setUp() {
        viewModel = PermissionViewModel(cacheManager)

        viewModel.permissionCodeName.observeForever(permissionCodeName)
    }

    @Test
    fun `get dynamic permission list for camera page type with image mode only`() {
        // Given
        val expectedValue = listOf(
            PermissionModel(
                R.string.picker_permission_camera,
                Manifest.permission.CAMERA
            )
        )

        every { cacheManager.get().pageType() } returns PageType.CAMERA
        every { cacheManager.get().modeType() } returns ModeType.IMAGE_ONLY

        // When
        viewModel.getDynamicPermissionList()

        // Then
        assert(viewModel.permissionList.value == expectedValue)
    }

    @Test
    fun `get dynamic permission list for camera page type with video mode only`() {
        // Given
        val expectedValue = listOf(
            PermissionModel(
                R.string.picker_permission_camera,
                Manifest.permission.CAMERA
            ),
            PermissionModel(
                R.string.picker_permission_microphone,
                Manifest.permission.RECORD_AUDIO
            )
        )

        every { cacheManager.get().pageType() } returns PageType.CAMERA
        every { cacheManager.get().modeType() } returns ModeType.VIDEO_ONLY

        // When
        viewModel.getDynamicPermissionList()

        // Then
        assert(viewModel.permissionList.value == expectedValue)
    }

    @Test
    fun `get dynamic permission list for gallery page type`() {
        // Given
        val expectedValue = listOf(
            PermissionModel(
                R.string.picker_permission_gallery,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        every { cacheManager.get().pageType() } returns PageType.GALLERY

        // When
        viewModel.getDynamicPermissionList()

        // Then
        assert(viewModel.permissionList.value == expectedValue)
    }

    @Test
    fun `get dynamic permission list for camera and gallery page type`() {
        // Given
        val expectedValue = listOf(
            PermissionModel(
                R.string.picker_permission_microphone,
                Manifest.permission.RECORD_AUDIO
            ),
            PermissionModel(
                R.string.picker_permission_camera,
                Manifest.permission.CAMERA
            ),
            PermissionModel(
                R.string.picker_permission_gallery,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        every { cacheManager.get().pageType() } returns PageType.COMMON

        // When
        viewModel.getDynamicPermissionList()

        // Then
        assert(viewModel.permissionList.value?.size == expectedValue.size)
    }

    @Test
    fun `filter dynamic permission to get permission code name list for common page type properly`() {
        // Given
        every { cacheManager.get().pageType() } returns PageType.COMMON

        // When
        viewModel.getDynamicPermissionList()

        // Then
        verify(atLeast = 1) { permissionCodeName.onChanged(any()) }
    }

    @Test
    fun `get permission dynamic wording for camera page type with image mode only`() {
        // Given
        val expectedValue = Pair(
            R.string.picker_title_camera_photo_permission,
            R.string.picker_message_camera_photo_permission,
        )

        every { cacheManager.get().pageType() } returns PageType.CAMERA
        every { cacheManager.get().modeType() } returns ModeType.IMAGE_ONLY

        // When
        viewModel.initOrCreateDynamicWording()

        // Then
        assert(viewModel.dynamicWording.value == expectedValue)
    }

    @Test
    fun `get permission dynamic wording for camera page type with video mode only`() {
        // Given
        val expectedValue = Pair(
            R.string.picker_title_camera_video_permission,
            R.string.picker_message_camera_video_permission,
        )

        every { cacheManager.get().pageType() } returns PageType.CAMERA
        every { cacheManager.get().modeType() } returns ModeType.VIDEO_ONLY

        // When
        viewModel.initOrCreateDynamicWording()

        // Then
        assert(viewModel.dynamicWording.value == expectedValue)
    }

    @Test
    fun `get permission dynamic wording for gallery page type`() {
        // Given
        val expectedValue = Pair(
            R.string.picker_title_gallery_permission,
            R.string.picker_message_gallery_permission,
        )

        every { cacheManager.get().pageType() } returns PageType.GALLERY

        // When
        viewModel.initOrCreateDynamicWording()

        // Then
        assert(viewModel.dynamicWording.value == expectedValue)
    }

    @Test
    fun `get permission dynamic wording for camera and gallery page type`() {
        // Given
        val expectedValue = Pair(
            R.string.picker_title_common_permission,
            R.string.picker_message_common_permission,
        )

        every { cacheManager.get().pageType() } returns PageType.COMMON

        // When
        viewModel.initOrCreateDynamicWording()

        // Then
        assert(viewModel.dynamicWording.value == expectedValue)
    }

}
