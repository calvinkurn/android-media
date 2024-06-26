package com.tokopedia.play.broadcaster.view.bottomsheet

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.ui.itemdecoration.CarouselCoverItemDecoration
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverCameraViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.play.broadcaster.util.permission.PermissionHelper
import com.tokopedia.play.broadcaster.view.adapter.PlayCoverProductAdapter
import com.tokopedia.play.broadcaster.view.fragment.setup.cover.PlayCoverSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by furqan on 03/06/2020
 */
class PlayCoverImageChooserBottomSheet @Inject constructor(
    private val analytic: PlayBroCoverPickerAnalytic,
    private val dialogCustomizer: ContentDialogCustomizer
) : BottomSheetUnify() {

    var mListener: Listener? = null

    private lateinit var viewModel: PlayCoverSetupViewModel

    private lateinit var rvProductCover: RecyclerView
    private lateinit var llOpenGallery: LinearLayout

    private val pdpCoverAdapter = PlayCoverProductAdapter(
            coverProductListener = object : PlayCoverProductViewHolder.Listener {
                override fun onProductCoverClicked(productId: String, imageUrl: String) {
                    mListener?.onChooseProductCover(this@PlayCoverImageChooserBottomSheet, productId, imageUrl)
                    analytic.clickAddCoverFromPdpSource(viewModel.account, viewModel.pageSource)
                }
            },
            coverCameraListener = object : PlayCoverCameraViewHolder.Listener {
                override fun onCameraButtonClicked() {
                    getCoverFromCamera()
                    analytic.clickAddCoverFromCameraSource(viewModel.account, viewModel.pageSource)
                }
            }
    )

    override fun onStart() {
        super.onStart()
        analytic.viewAddCoverSourceBottomSheet(viewModel.account, viewModel.pageSource)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parent = requireParentFragment() as PlayCoverSetupFragment
        viewModel = ViewModelProvider(parent, parent.getViewModelFactory()).get(PlayCoverSetupViewModel::class.java)
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

        llOpenGallery.setOnClickListener {
            if (isGalleryPermissionGranted()) chooseCoverFromGallery()
            else requestGalleryPermission()
            analytic.clickAddCoverFromGallerySource(viewModel.account, viewModel.pageSource)
        }

        rvProductCover.adapter = pdpCoverAdapter
        rvProductCover.addItemDecoration(CarouselCoverItemDecoration(requireContext()))
    }

    private fun onCameraRequestPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) getCoverFromCamera()
    }

    private fun onGalleryRequestPermissionResult(grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) llOpenGallery.performClick()
    }

    private fun getCoverFromCamera() {
        if (isCameraPermissionGranted()) mListener?.onGetFromCamera(this@PlayCoverImageChooserBottomSheet)
        else requestCameraPermission()
    }

    /**
     * Camera Permission
     */
    private fun isCameraPermissionGranted(): Boolean =
            isPermissionGranted(Manifest.permission.CAMERA)
                && isPermissionGranted(PermissionHelper.READ_EXTERNAL_STORAGE)

    private fun requestCameraPermission() = requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                PermissionHelper.READ_EXTERNAL_STORAGE
            ),
        REQUEST_CODE_PERMISSION_CAMERA
    )

    /**
     * Gallery Permission
     */
    private fun isGalleryPermissionGranted(): Boolean =
            isPermissionGranted(PermissionHelper.READ_EXTERNAL_STORAGE)

    private fun requestGalleryPermission() = requestPermissions(
            arrayOf(PermissionHelper.READ_EXTERNAL_STORAGE),
        REQUEST_CODE_PERMISSION_GALLERY
    )

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(requireContext(),
                    permission) == PackageManager.PERMISSION_GRANTED

    private fun chooseCoverFromGallery() {
        mListener?.onChooseFromGalleryClicked(this@PlayCoverImageChooserBottomSheet)
        dismiss()
    }

    /**
     * Observe
     */
    private fun observeSelectedProduct() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.productList.collectLatest {
                pdpCoverAdapter.setItemsAndAnimateChanges(
                    listOf(CarouselCoverUiModel.Camera) + it.map(CarouselCoverUiModel::Product)
                )
            }
        }
    }

    interface Listener {
        fun onChooseProductCover(bottomSheet: PlayCoverImageChooserBottomSheet, productId: String, imageUrl: String)
        fun onGetFromCamera(bottomSheet: PlayCoverImageChooserBottomSheet)
        fun onChooseFromGalleryClicked(bottomSheet: PlayCoverImageChooserBottomSheet)
    }

    companion object {
        private const val TAG = "Choose Cover"

        private const val REQUEST_CODE_PERMISSION_CAMERA = 1002
        private const val REQUEST_CODE_PERMISSION_GALLERY = 1003
    }

}
