package com.tokopedia.product_ar.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.data.MFETrackingData
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.modiface.mfemakeupkit.widgets.MFEMakeupView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.ImageRatioType
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.state.AnimatedTextIconClickMode
import com.tokopedia.product_ar.model.state.ModifaceViewMode
import com.tokopedia.product_ar.util.AnimatedTextIcon
import com.tokopedia.product_ar.util.ProductArConstant.REQUEST_CODE_CAMERA_PERMISSION
import com.tokopedia.product_ar.util.ProductArConstant.REQUEST_CODE_IMAGE_PICKER
import com.tokopedia.product_ar.view.ProductArActivity
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.product_ar.view.bottomsheet.ProductArBottomSheetBuilder
import com.tokopedia.product_ar.view.partialview.PartialBottomArView
import com.tokopedia.product_ar.viewmodel.ProductArSharedViewModel
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class ProductArFragment : Fragment(), ProductArListener, MFEMakeupEngine.MFEMakeupEngineDetectionCallback {

    companion object {
        @JvmStatic
        fun newInstance() = ProductArFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var viewModel: ProductArViewModel? = null
    private var sharedViewModel: ProductArSharedViewModel? = null

    private var mMakeupView: MFEMakeupView? = null

    private var partialBottomArView: PartialBottomArView? = null
    private var animatedTextIcon1: AnimatedTextIcon? = null
    private var animatedTextIcon2: AnimatedTextIcon? = null
    private var productArToolbar: NavToolbar? = null
    private var arViewLoader: LoaderUnify? = null
    private var icComparison: ImageUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_ar, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? ProductArActivity)?.component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
            activity?.let { activity ->
                sharedViewModel = ViewModelProvider(activity).get(ProductArSharedViewModel::class.java)
            }
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        arViewLoader?.show()
        icComparison?.setOnClickListener {
            goToArComparissonPage()
        }

        getMakeUpEngine()?.setDetectionCallbackForCameraFeed(this)
        getMakeUpEngine()?.attachMakeupView(mMakeupView)
        setupLiveCamera()
        setupNavToolbar()
    }

    private fun initView(view: View) {
        icComparison = view.findViewById(R.id.ic_compare_ar)
        mMakeupView = view.findViewById(R.id.main_img)
        arViewLoader = view.findViewById(R.id.ar_loader)
        animatedTextIcon2 = view.findViewById(R.id.animated_txt_icon_2)
        animatedTextIcon1 = view.findViewById(R.id.animated_txt_icon_1)
        productArToolbar = view.findViewById(R.id.product_ar_toolbar)
        partialBottomArView = PartialBottomArView.build(view, this)
    }

    private fun goToArComparissonPage() {
        val cameraMode = viewModel?.modifaceViewState?.value?.mode ?: ModifaceViewMode.LIVE

        if (cameraMode == ModifaceViewMode.LIVE) {
            captureLiveCameraToComparisson()
        } else {
            captureImageToComparisson()
        }

        getArActivity()?.goToArComparisonFragment()
    }

    private fun captureImageToComparisson() {
        viewModel?.imageDrawable?.let {
            sharedViewModel?.setArListData((viewModel?.productArList?.value as? Success)?.data
                    ?: listOf(), it)
        }
    }

    private fun captureLiveCameraToComparisson() {
        getMakeUpEngine()?.captureOutputWithCompletionHandler { before, after ->
            activity?.runOnUiThread {
                before?.let {
                    sharedViewModel?.setArListData((viewModel?.productArList?.value as? Success)?.data
                            ?: listOf(), it)
                    getArActivity()?.goToArComparisonFragment()
                }
            }
        }
    }

    private fun setupNavToolbar() {
        activity?.let { activity ->
            productArToolbar?.let {
                productArToolbar?.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                productArToolbar?.setToolbarTitle("")
                productArToolbar?.setupToolbarWithStatusBar(activity, NavToolbar.Companion.StatusBar.STATUS_BAR_DARK)
                productArToolbar?.setIcon(
                        IconBuilder()
                                .addIcon(IconUnify.INFORMATION) {
                                    context?.let { ctx ->
                                        ProductArBottomSheetBuilder.getArInfoBottomSheet(ctx)
                                                .show(activity.supportFragmentManager, "info ar")
                                    }
                                }
                                .addIcon(IconList.ID_CART) {}
                )
                viewLifecycleOwner.lifecycle.addObserver(it)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun setupLiveCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
        } else {
            getMakeUpEngine()?.startRunningWithCamera(context)
        }
    }

    private fun getBitmapFromPath(path: String): Bitmap? {
        return try {
            val file = File(path)
            if (file.exists()) {
                return BitmapFactory.decodeFile(path)
            } else {
                view?.showToasterError("file not exist")
            }
            null
        } catch (e: Throwable) {
            view?.showToasterError("error try catch ${e.localizedMessage}")
            null
        }
    }

    private fun setupUseImageCamera(drawablePath: String) {
        val selectedImageBitmap = getBitmapFromPath(drawablePath)
        viewModel?.imageDrawable = selectedImageBitmap
        selectedImageBitmap?.let {
            getMakeUpEngine()?.clearMakeupLook()
            getMakeUpEngine()?.startRunningWithPhoto(it, false,
                    object : MFEMakeupEngine.MFEMakeupEngineDetectionCallback {
                        override fun onMFEMakeupFinishedDetection(p0: MFETrackingData?) {
                            if (p0?.hasFacePoints() == false) {
                                // Looks like the image did not contain a face
                                // Ask your user to upload another
                                Log.e("asd", "no face bro")
                            }
                        }

                    },
                    object : MFEMakeupEngine.MFEMakeupEngineImageProcessedCallback {
                        override fun onMFEMakeupFinishedProcessingImage(p0: MFETrackingData?) {
                            // Rendering on the image is complete, you can use this
                            // to do things like clear your loader
                            Log.e("asd", "beres bro")
                        }

                    })
            getMakeUpEngine()?.setMakeupLook((viewModel?.mfeMakeUpLook?.value as? Success)?.data)
        }
    }

    private fun setupTextClickMode(view: AnimatedTextIcon,
                                   clickMode: AnimatedTextIconClickMode?) {
        when (clickMode) {
            AnimatedTextIconClickMode.CHANGE_PHOTO -> {
                view.setOnClickListener {
                    onAddImageClick()
                }
            }
            AnimatedTextIconClickMode.CHOOSE_FROM_GALLERY -> {
                view.setOnClickListener {
                    onAddImageClick()
                }
            }
            AnimatedTextIconClickMode.USE_CAMERA -> {
                view.setOnClickListener {
                    arViewLoader?.show()
                    viewModel?.changeMode(ModifaceViewMode.LIVE)
                }
            }

            else -> return
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.animatedTextIconState?.collectLatest {
                animatedTextIcon1?.run {
                    shouldShowWithAction(it.view1ClickMode != null) {
                        animatedTextIcon1?.renderText(requireContext().getString(it.view1ClickMode!!.textId), it.view1ClickMode.iconUnify)
                        setupTextClickMode(this, it.view1ClickMode)
                    }
                }

                animatedTextIcon2?.run {
                    shouldShowWithAction(it.view2ClickMode != null) {
                        animatedTextIcon2?.renderText(requireContext().getString(it.view2ClickMode!!.textId), it.view2ClickMode.iconUnify)
                        setupTextClickMode(this, it.view2ClickMode)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.modifaceViewState?.collectLatest {
                if (it.mode == ModifaceViewMode.LIVE) {
                    setupLiveCamera()
                } else {
                    setupUseImageCamera(it.imageDrawablePath)
                }
            }
        }

        viewModel?.selectedProductArData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.renderBottomInfoText(it.data)
                }
                is Fail -> {
                }
            }
        }

        viewModel?.productArList?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.renderRecyclerView(it.data)
                }
                is Fail -> {
                }
            }
        }

        viewModel?.mfeMakeUpLook?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    getMakeUpEngine()?.clearMakeupLook()
                    getMakeUpEngine()?.setMakeupLook(it.data)
                }
                is Fail -> {
                    // still noop
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMakeUpEngine()?.startRunningWithCamera(context)
            } else {
                activity?.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMakeUpEngine()?.onResume(context)
        arViewLoader?.hide()
        getMakeUpEngine()?.setDetectionCallbackForCameraFeed(this)
    }

    override fun onPause() {
        getMakeUpEngine()?.onPause()
        getMakeUpEngine()?.setDetectionCallbackForCameraFeed(null)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMakeUpEngine()?.clearMakeupLook()
        getMakeUpEngine()?.close()
    }

    override fun onVariantClicked(productId: String,
                                  isSelected: Boolean,
                                  selectedMfeProduct: MFEMakeupProduct) {
        if (isSelected) return
        viewModel?.let {
            it.onVariantClicked(
                    productId, it.getProductArUiModel(),
                    partialBottomArView?.adapter?.getCurrentArImageDatas() ?: listOf(),
                    selectedMfeProduct
            )
        }
    }

    override fun onButtonClicked(productId: String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE_PICKER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = ImagePickerResultExtractor.extract(data)
                    val imagePath = result.imageUrlOrPathList.firstOrNull()

                    imagePath?.let {
                        viewModel?.changeMode(ModifaceViewMode.IMAGE, it)
                    }
                }
            }
        }
    }

    private fun onAddImageClick() {
        context?.let {
            val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .withSimpleEditor()
                    .apply {
                        title = it.getString(R.string.txt_image_picker_title)
                        imageRatioType = ImageRatioType.RATIO_9_16
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.PRODUCT_AR)
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? = getArActivity()?.getMakeUpEngine()

    private fun getArActivity(): ProductArActivity? = (activity as? ProductArActivity)

    override fun onMFEMakeupFinishedDetection(p0: MFETrackingData?) {
        if (p0?.hasFacePoints() == false) {
            Log.e("asd", "no live face bro")
        }

        activity?.runOnUiThread {
            if (arViewLoader?.isShown == true) arViewLoader?.hide()
        }
    }
}