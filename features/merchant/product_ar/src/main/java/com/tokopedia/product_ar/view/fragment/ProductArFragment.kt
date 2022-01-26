package com.tokopedia.product_ar.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.ImagePickerTab
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.SingleClick
import com.tokopedia.product.detail.common.pref.CoachMarkProductPref
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.databinding.FragmentProductArBinding
import com.tokopedia.product_ar.model.ArMetaData
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.model.state.AnimatedTextIconClickMode
import com.tokopedia.product_ar.model.state.ArGlobalErrorMode
import com.tokopedia.product_ar.model.state.ModifaceViewMode
import com.tokopedia.product_ar.tracker.ProductArTracker
import com.tokopedia.product_ar.util.AnimatedTextIcon
import com.tokopedia.product_ar.util.ArCoachMarkUtil
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil.getBitmapFromPath
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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

    private val coachMarkSharedPref by lazy {
        context?.let {
            CoachMarkProductPref(it, CoachMarkProductPref.PRODUCT_AR_PAGE_COACHMARK)
        }
    }

    private var viewModel: ProductArViewModel? = null
    private var sharedViewModel: ProductArSharedViewModel? = null

    private var binding by autoClearedNullable<FragmentProductArBinding>()
    private var partialBottomArView: PartialBottomArView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductArBinding.inflate(inflater, container, false)
        return binding?.root
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
        ProductArTracker.openScreen(
                viewModel?.initialProductId ?: "",
                viewModel?.userSessionInterface?.userId ?: "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binding?.mainImg?.let {
            getMakeUpEngine()?.setDetectionCallbackForCameraFeed(this)
            getMakeUpEngine()?.attachMakeupView(it)
        }

        binding?.icCompareAr?.run {
            show()
            setOnClickListener {
                binding?.arLoader?.show()
                goToArComparissonPage()
            }
        }

        binding?.imgShadowBackground?.setImageResource(R.drawable.ic_gradient_ar)
        setupNavToolbar()
    }

    private fun setupCoachMark() {
        val shouldShowCoachmark = coachMarkSharedPref?.getCoachMarkState() == false

        if (shouldShowCoachmark) {
            context?.let {
                val coachMarkView = CoachMark2(it)

                val steps = ArCoachMarkUtil.buildCoachMark(
                        context = requireContext(),
                        rvVariant = partialBottomArView?.rvVariant,
                        animatedTxtIcon2 = binding?.animatedTxtIcon2,
                        icCompareAr = binding?.icCompareAr
                )
                coachMarkView.showCoachMark(steps)
                coachMarkSharedPref?.setCoachMarkState(true)
            }
        }
    }


    private fun initView() {
        binding?.root?.let {
            partialBottomArView = PartialBottomArView.build(it, this)
        }
    }

    private fun goToArComparissonPage() {
        ProductArTracker.clickComparisson(getSelectedProductId())
        captureImageAndSetupData()
    }

    private fun captureImageAndSetupData() {
        getMakeUpEngine()?.captureOutputWithCompletionHandler { before, after ->
            activity?.runOnUiThread {
                before?.let {
                    sharedViewModel?.setArListData(
                            listOfArData = (viewModel?.productArList?.value as? Success)?.data
                                    ?: listOf(),
                            processedPhoto = after,
                            originalPhoto = before)
                    binding?.arLoader?.hide()
                    getArActivity()?.goToArComparisonFragment()
                }
            }
        }
    }

    private fun setupNavToolbar() {
        activity?.let { activity ->
            binding?.productArToolbar?.run {
                setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                setToolbarTitle("")
                setupToolbarWithStatusBar(activity, NavToolbar.Companion.StatusBar.STATUS_BAR_DARK)
                setCustomBackButton(color = ContextCompat.getColor(context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                viewLifecycleOwner.lifecycle.addObserver(this)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        partialBottomArView = null
        super.onDestroyView()
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

    private fun setupUseImageCamera(drawablePath: String) {
        val selectedImageBitmap = getBitmapFromPath(drawablePath)
        selectedImageBitmap?.let {
            getMakeUpEngine()?.startRunningWithPhoto(it, true,
                    object : MFEMakeupEngine.MFEMakeupEngineDetectionCallback {
                        override fun onMFEMakeupFinishedDetection(p0: MFETrackingData?) {
                            faceDetection(p0)
                        }

                    },
                    object : MFEMakeupEngine.MFEMakeupEngineImageProcessedCallback {
                        override fun onMFEMakeupFinishedProcessingImage(p0: MFETrackingData?) {
                            // Rendering on the image is complete, you can use this
                            // to do things like clear your loader
                        }
                    })
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
                    viewModel?.changeMode(ModifaceViewMode.LIVE)
                }
            }

            else -> return
        }
    }

    private fun observeData() {
        observeLoadingState()
        observeBottomShimmerState()
        observeAnimatedTextIcon()
        observeModifaceMode()
        observeBottomData()
        observeBackendData()
        observeMakeUpLook()
        observeAtc()
        observeGlobalError()
    }

    private fun observeGlobalError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.globalErrorState?.collectLatest { data ->
                when (data.state) {
                    ArGlobalErrorMode.ERROR -> {
                        data.throwable?.let {
                            setupNavBarIconPageError()
                            showGlobalError(it)
                        }
                    }
                    ArGlobalErrorMode.SUCCESS -> {
                        setupNavBarIconPage()
                        binding?.globalErrorProductAr?.hide()
                    }
                }
            }
        }
    }

    private fun observeAtc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.addToCartLiveData?.collectLatest { data ->
                partialBottomArView?.stopLoadingButton()
                when (data) {
                    is Success -> {
                        ProductArTracker.successAtc(
                                arData = getSelectedProductData(),
                                shopId = data.data.data.shopId.toString(),
                                arMetaData = viewModel?.getProductArUiModel()?.metaData
                                        ?: ArMetaData(),
                                userId = viewModel?.userSessionInterface?.userId ?: "",
                                cartId = data.data.data.cartId
                        )

                        onSuccessAtc(data.data.errorMessage.firstOrNull())
                    }
                    is Fail -> {
                        onFailAtc(data.throwable)
                    }
                }
            }
        }
    }

    private fun observeMakeUpLook() {
        viewModel?.selectedMfMakeUpLook?.observe(viewLifecycleOwner) {
            getMakeUpEngine()?.setMakeupLook(it)
        }
    }

    private fun observeBackendData() {
        viewModel?.productArList?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.renderRecyclerView(it.data)
                    setupCoachMark()
                }
                is Fail -> {
                }
            }
        }
    }

    private fun observeBottomData() {
        viewModel?.selectedProductArData?.observe(viewLifecycleOwner) {
            partialBottomArView?.renderBottomInfoText(it)
        }
    }

    private fun observeBottomShimmerState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.bottomLoadingState?.collectLatest {
                if (it) {
                    binding?.arShimmer?.root?.show()
                } else {
                    binding?.arShimmer?.root?.hide()
                    partialBottomArView?.showView()
                }
            }
        }
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.modifaceLoadingState?.collectLatest {
                if (it) {
                    binding?.arLoader?.show()
                } else {
                    binding?.arLoader?.hide()
                }
            }
        }
    }

    private fun observeModifaceMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.modifaceViewState?.collectLatest {
                if (it.mode == ModifaceViewMode.LIVE) {
                    setupLiveCamera()
                } else {
                    setupUseImageCamera(it.imageDrawablePath)
                }
            }
        }
    }

    private fun observeAnimatedTextIcon() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.animatedTextIconState?.collectLatest {
                binding?.animatedTxtIcon1?.run {
                    shouldShowWithAction(it.view1ClickMode != null) {
                        renderText(requireContext().getString(it.view1ClickMode!!.textId), it.view1ClickMode.iconUnify)
                        setupTextClickMode(this, it.view1ClickMode)
                    }
                }

                binding?.animatedTxtIcon2?.run {
                    shouldShowWithAction(it.view2ClickMode != null) {
                        renderText(requireContext().getString(it.view2ClickMode!!.textId), it.view2ClickMode.iconUnify)
                        setupTextClickMode(this, it.view2ClickMode)
                    }
                }
            }
        }
    }

    private fun onSuccessAtc(successMessage: String?) {
        context?.let {
            val message = if (successMessage == null || successMessage.isEmpty())
                it.getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_success_atc_default)
            else
                successMessage

            view?.showToasterSuccess(message, ctaText = getString(R.string.product_ar_see)) {
                ProductCartHelper.goToCartCheckout(requireActivity(), "")
            }
        }
    }

    private fun onFailAtc(throwable: Throwable) {
        val cause = throwable.cause ?: throwable

        cause.run {
            if (this is AkamaiErrorException && message != null) {
                view?.showToasterError(
                        message ?: "",
                        ctaText = getString(R.string.product_ar_oke_label)) {

                }
            } else {
                view?.showToasterError(getErrorMessage(this),
                        ctaText = getString(R.string.product_ar_oke_label)) {

                }
            }
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return if (throwable is ResponseErrorException) {
            throwable.message
                    ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
        } else if (throwable is AkamaiErrorException && throwable.message != null) {
            throwable.message
                    ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
        } else {
            context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
                    ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
        }
    }

    private fun showGlobalError(throwable: Throwable) {
        val errorType = if (throwable is SocketTimeoutException
                || throwable is UnknownHostException
                || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }

        binding?.globalErrorProductAr?.run {
            (errorAction as? UnifyButton)?.isLoading = false
            show()
            setType(errorType)
            setActionClickListener {
                (errorAction as? UnifyButton)?.isLoading = true
                viewModel?.getArData()
            }
        }
    }

    private fun setupNavBarIconPage() {
        activity?.let {
            binding?.productArToolbar?.setIcon(
                    IconBuilder()
                            .addIcon(IconUnify.INFORMATION) {
                                context?.let { ctx ->
                                    ProductArBottomSheetBuilder.getArInfoBottomSheet(ctx)
                                            .show(it.supportFragmentManager, "info ar")
                                }
                            }
                            .addIcon(IconList.ID_CART) {}
            )

            binding?.productArToolbar?.setCustomBackButton(color = ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        }
    }

    private fun setupNavBarIconPageError() {
        context?.let {
            binding?.productArToolbar?.setIcon(
                    IconBuilder()
            )
            binding?.productArToolbar?.setCustomBackButton(color = ContextCompat.getColor(it,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN700))
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
        getMakeUpEngine()?.onResume(requireContext())
        getMakeUpEngine()?.setDetectionCallbackForCameraFeed(this)
    }

    override fun onPause() {
        getMakeUpEngine()?.setDetectionCallbackForCameraFeed(null)
        getMakeUpEngine()?.onPause()
        super.onPause()
    }

    override fun onVariantClicked(productId: String,
                                  productName: String,
                                  isSelected: Boolean,
                                  selectedMfeProduct: MFEMakeupProduct) {
        if (isSelected) return

        ProductArTracker.clickVariant(
                productId,
                productName,
                viewModel?.userSessionInterface?.userId ?: ""
        )

        viewModel?.let {
            it.onVariantClicked(
                    productId, it.getProductArUiModel(),
                    partialBottomArView?.adapter?.getCurrentArImageDatas() ?: listOf(),
                    selectedMfeProduct
            )
        }
    }

    override fun onButtonClicked(productId: String) {
        if (viewModel?.userSessionInterface?.isLoggedIn == false) {
            SingleClick.doSomethingBeforeTime {
                context?.let {
                    RouteManager.route(it, ApplinkConst.LOGIN)
                    partialBottomArView?.stopLoadingButton()
                }
            }
        } else {
            SingleClick.doSomethingBeforeTime(delayInterval = 300) {
                viewModel?.doAtc()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE_PICKER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = ImagePickerResultExtractor.extract(data)
                    val imagePath = result.imageUrlOrPathList.firstOrNull()

                    imagePath?.let {
                        getMakeUpEngine()?.onResume(requireContext())
                        viewModel?.changeMode(ModifaceViewMode.IMAGE, it)
                    }
                }
            }
        }
    }

    private fun getSelectedProductId(): String {
        return getSelectedProductData()?.productID ?: ""
    }

    private fun getSelectedProductData(): ProductAr? {
        return viewModel?.selectedProductArData?.value
    }

    private fun onAddImageClick() {
        context?.let {
            ProductArTracker.clickGallery(getSelectedProductId())

            val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .apply {
                        title = it.getString(R.string.txt_image_picker_title)
                        imagePickerTab = arrayOf(ImagePickerTab.TYPE_GALLERY)
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.PRODUCT_AR)
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? = getArActivity()?.getMakeUpEngine()

    private fun getArActivity(): ProductArActivity? = (activity as? ProductArActivity)

    private fun faceDetection(p0: MFETrackingData?) {
        activity?.runOnUiThread {
            if (p0?.hasFacePoints() == false) {
                binding?.txtNoDetection?.showAnimated()
            } else {
                binding?.txtNoDetection?.hideAnimated()
            }

            Handler().postDelayed({
                if (p0?.hasFacePoints() == false) {
                    disableCompareIcon()
                } else {
                    enableCompareIcon()
                }
            }, 500)
        }
    }

    private fun disableCompareIcon() {
        binding?.icCompareAr?.run {
            if (!isEnabled) return@run

            isEnabled = false
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_compare_disabled))
        }
    }

    private fun enableCompareIcon() {
        binding?.icCompareAr?.run {
            if (isEnabled) return@run
            isEnabled = true
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_compare_icon))
        }
    }

    override fun onMFEMakeupFinishedDetection(p0: MFETrackingData?) {
        faceDetection(p0)
        activity?.runOnUiThread {
            if (binding?.arLoader?.isShown == true) {
                if (viewModel?.modifaceLoadingState?.value != null
                        && viewModel?.modifaceLoadingState?.value == false) return@runOnUiThread

                viewModel?.setLoadingState(false)
            }
        }
    }
}