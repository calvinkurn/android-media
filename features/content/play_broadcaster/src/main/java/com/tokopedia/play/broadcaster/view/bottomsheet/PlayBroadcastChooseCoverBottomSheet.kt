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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play.broadcaster.view.adapter.PlayCoverProductAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.*
import javax.inject.Inject

/**
 * @author by furqan on 03/06/2020
 */
class PlayBroadcastChooseCoverBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : BottomSheetUnify() {

    var listener: Listener? = null

    private lateinit var viewModel: PlayBroadcastCoverSetupViewModel

    private lateinit var llOpenCamera: LinearLayout
    private lateinit var rvProductCover: RecyclerView
    private lateinit var llOpenGallery: LinearLayout

    private val pdpCoverAdapter = PlayCoverProductAdapter(object : PlayCoverProductViewHolder.Listener {
        override fun onProductCoverClicked(productId: Long, imageUrl: String) {
            if (isAllPermissionGranted()) {
                listener?.onGetCoverFromProduct(productId, imageUrl)
                dismiss()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getParcelableExtra<Uri>(PlayCoverCameraActivity.EXTRA_IMAGE_URI)
            listener?.onGetCoverFromCamera(imageUri)
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProduct()
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
            if (isAllPermissionGranted()) {
                takeCoverFromCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }

        llOpenGallery.setOnClickListener {
            if (isAllPermissionGranted()) {
                chooseCoverFromGallery()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }

        rvProductCover.adapter = pdpCoverAdapter

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
        requestPermissions(permissionsArray, PERMISSION_CODE)
    }

    private fun takeCoverFromCamera() {
        val cameraIntent = Intent(context, PlayCoverCameraActivity::class.java)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun chooseCoverFromGallery() {
        listener?.onChooseFromGalleryClicked()
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
        fun onGetCoverFromCamera(imageUri: Uri?)
        fun onGetCoverFromProduct(productId: Long, imageUrl: String)
        fun onChooseFromGalleryClicked()
    }

    companion object {
        const val TAG_CHOOSE_COVER = "TagChooseCover"

        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"

        private const val PERMISSION_CODE = 1111
        private const val REQUEST_IMAGE_CAPTURE = 2222
    }

}