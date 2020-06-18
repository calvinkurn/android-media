package com.tokopedia.play.broadcaster.view.bottomsheet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.type.PlayCoverImageType
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.adapter.PlayCoverProductAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by furqan on 03/06/2020
 */
class PlayCoverImageChooserBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : BottomSheetUnify() {

    var mListener: Listener? = null

    private lateinit var viewModel: PlayBroadcastCoverSetupViewModel

    private lateinit var llOpenCamera: LinearLayout
    private lateinit var rvProductCover: RecyclerView
    private lateinit var llOpenGallery: LinearLayout

    private val pdpCoverAdapter = PlayCoverProductAdapter(object : PlayCoverProductViewHolder.Listener {
        override fun onProductCoverClicked(productId: Long, imageUrl: String) {
            mListener?.onCoverChosen(PlayCoverImageType.Product(productId, imageUrl))
            dismiss()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayBroadcastCoverSetupViewModel::class.java)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_CAMERA -> onCameraRequestPermissionResult(grantResults)
            REQUEST_CODE_PERMISSION_GALLERY -> onGalleryRequestPermissionResult(grantResults)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getParcelableExtra<Uri>(PlayCoverCameraActivity.EXTRA_IMAGE_URI)
            imageUri?.let { uri ->
                mListener?.onCoverChosen(
                        PlayCoverImageType.Camera(uri)
                )
            }

            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProduct()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isFullpage = false
        isDragable = true
        isHideable = true

        val view = View.inflate(requireContext(), R.layout.bottom_sheet_play_choose_cover, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View) {
        with (view) {
            llOpenCamera = findViewById(R.id.ll_open_camera)
            rvProductCover = findViewById(R.id.rv_product_cover)
            llOpenGallery = findViewById(R.id.ll_open_gallery)
        }
    }

    private fun setupView(view: View) {
        bottomSheetHeader.visibility = View.GONE
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        llOpenCamera.setOnClickListener {
            if (isCameraPermissionGranted()) takeCoverFromCamera()
            else requestCameraPermission()
        }

        llOpenGallery.setOnClickListener {
            if (isGalleryPermissionGranted()) chooseCoverFromGallery()
            else requestGalleryPermission()
        }

        rvProductCover.adapter = pdpCoverAdapter
    }

    private fun onCameraRequestPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) llOpenCamera.performClick()
    }

    private fun onGalleryRequestPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) llOpenGallery.performClick()
    }

    /**
     * Camera Permission
     */
    private fun isCameraPermissionGranted(): Boolean = isPermissionGranted(Manifest.permission.CAMERA)

    private fun requestCameraPermission() = requestPermissions(
            arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION_CAMERA
    )

    /**
     * Gallery Permission
     */
    private fun isGalleryPermissionGranted(): Boolean =
            isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun requestGalleryPermission() = requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_GALLERY
    )

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(requireContext(),
                    permission) == PackageManager.PERMISSION_GRANTED

    private fun takeCoverFromCamera() {
        val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun chooseCoverFromGallery() {
        mListener?.onChooseFromGalleryClicked()
        dismiss()
    }

    /**
     * Observe
     */
    private fun observeSelectedProduct() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            pdpCoverAdapter.setItemsAndAnimateChanges(it)
        })
    }

    interface Listener {
        fun onCoverChosen(coverImage: PlayCoverImageType)
        fun onChooseFromGalleryClicked()
    }

    companion object {
        private const val TAG = "Choose Cover"

        private const val REQUEST_CODE_PERMISSION_CAMERA = 1002
        private const val REQUEST_CODE_PERMISSION_GALLERY = 1003
        private const val REQUEST_IMAGE_CAPTURE = 2222
    }

}