package com.tokopedia.play.broadcaster.view.bottomsheet

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet_play_choose_cover.*

/**
 * @author by furqan on 03/06/2020
 */
class PlayBroadcastChooseCoverBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && isAllPermissionGranted()) {
                takeCoverFromCamera()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            listener?.onGetCoverFromCamera(imageUri)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isFullpage = false
        isDragable = true
        isHideable = true

        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_choose_cover, null))
    }

    private fun initView() {
        bottomSheetHeader.visibility = View.GONE
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        containerPlayTakePicture.setOnClickListener {
            if (!isAllPermissionGranted()) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else {
                takeCoverFromCamera()
            }
        }

    }

    private fun isAllPermissionGranted(): Boolean = isPermissionGranted(Manifest.permission.CAMERA) &&
            isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(requireContext(),
                    permission) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(permissionsArray: Array<String>) {
        requestPermissions(permissionsArray,
                PERMISSION_CODE)
    }

    private fun takeCoverFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    interface Listener {
        fun onGetCoverFromCamera(imageUri: Uri?)
    }

    companion object {
        const val TAG_CHOOSE_COVER = "TagChooseCover"

        private const val PERMISSION_CODE = 1111
        private const val REQUEST_IMAGE_CAPTURE = 2222

        fun getInstance(): PlayBroadcastChooseCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet()
    }

}