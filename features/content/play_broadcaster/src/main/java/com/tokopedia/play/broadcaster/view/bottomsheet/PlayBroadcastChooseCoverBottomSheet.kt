package com.tokopedia.play.broadcaster.view.bottomsheet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.adapter.PlayCoverProductAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.bottom_sheet_play_choose_cover.*
import java.io.ByteArrayOutputStream

/**
 * @author by furqan on 03/06/2020
 */
class PlayBroadcastChooseCoverBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null

    private var imageUrlList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            imageUrlList = it.getStringArrayList(EXTRA_IMAGE_URL) ?: arrayListOf()
        } ?: arguments?.let {
            imageUrlList = it.getStringArrayList(EXTRA_IMAGE_URL) ?: arrayListOf()
        }
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getParcelableExtra<Uri>(PlayCoverCameraActivity.EXTRA_IMAGE_URI)
            listener?.onGetCoverFromCamera(imageUri)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(EXTRA_IMAGE_URL, imageUrlList)
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
            if (isAllPermissionGranted()) {
                takeCoverFromCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }

        containerPlayChooseFromGallery.setOnClickListener {
            if (isAllPermissionGranted()) {
                chooseCoverFromGallery()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }

        rvPlayCoverProduct.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvPlayCoverProduct.setHasFixedSize(true)
        rvPlayCoverProduct.adapter = PlayCoverProductAdapter(imageUrlList, object : PlayCoverProductViewHolder.Listener {
            override fun onCoverSelectedFromProduct(selectedImageBitmap: Bitmap) {
                if (isAllPermissionGranted()) {
                    listener?.onGetCoverFromProduct(getImageUriFromBitmap(selectedImageBitmap))
                    dismiss()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            }
        })

        if (!isAllPermissionGranted()) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private fun isAllPermissionGranted(): Boolean = isPermissionGranted(Manifest.permission.CAMERA) &&
            isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
            isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(requireContext(),
                    permission) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(permissionsArray: Array<String>) {
        requestPermissions(permissionsArray,
                PERMISSION_CODE)
    }

    private fun takeCoverFromCamera() {
        val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun chooseCoverFromGallery() {
        listener?.onChooseFromGalleryClicked()
        dismiss()
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap,
                COVER_TITLE, null)
        return Uri.parse(path.toString())
    }

    interface Listener {
        fun onGetCoverFromCamera(imageUri: Uri?)
        fun onGetCoverFromProduct(imageUri: Uri?)
        fun onChooseFromGalleryClicked()
    }

    companion object {
        const val TAG_CHOOSE_COVER = "TagChooseCover"

        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        private const val COVER_TITLE = "PlayCover"

        private const val PERMISSION_CODE = 1111
        private const val REQUEST_IMAGE_CAPTURE = 2222

        fun getInstance(imageUrlList: ArrayList<String>): PlayBroadcastChooseCoverBottomSheet =
                PlayBroadcastChooseCoverBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putStringArrayList(EXTRA_IMAGE_URL, imageUrlList)
                    }
                }
    }

}