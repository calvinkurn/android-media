package com.tokopedia.product_ar.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.modiface.mfemakeupkit.MFEMakeupEngine
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
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.state.AnimatedTextIconClickMode
import com.tokopedia.product_ar.model.state.ModifaceViewMode
import com.tokopedia.product_ar.util.AnimatedTextIcon
import com.tokopedia.product_ar.util.ProductArConstant.REQUEST_CODE_IMAGE_PICKER
import com.tokopedia.product_ar.view.bottomsheet.ProductArBottomSheetBuilder
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProductArFragment : Fragment(), ProductArListener {

    companion object {
        @JvmStatic
        fun newInstance() = ProductArFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var viewModel: ProductArViewModel? = null

    private var mMakeupView: MFEMakeupView? = null

    private var partialBottomArView: PartialBottomArView? = null
    private var animatedTextIcon1: AnimatedTextIcon? = null
    private var animatedTextIcon2: AnimatedTextIcon? = null
    private var productArToolbar: NavToolbar? = null

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
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMakeupView = view.findViewById(R.id.main_img)
        getMakeUpEngine()?.attachMakeupView(mMakeupView)
        setupLiveCamera()
        partialBottomArView = PartialBottomArView.build(view, this)

        setupAnimatedTextIcon(view)
        setupNavToolbar(view)
    }

    private fun setupNavToolbar(view: View) {
        productArToolbar = view.findViewById(R.id.product_ar_toolbar)
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

    private fun setupAnimatedTextIcon(view: View) {
        animatedTextIcon2 = view.findViewById(R.id.animated_txt_icon_2)
        animatedTextIcon1 = view.findViewById(R.id.animated_txt_icon_1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun setupLiveCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            getMakeUpEngine()?.startRunningWithCamera(context)
        }
    }

    private fun setupUseImageCamera(drawablePath: String) {
        val selectedImage = BitmapFactory.decodeFile(drawablePath)

        getMakeUpEngine()?.clearMakeupLook()
        getMakeUpEngine()?.startRunningWithPhoto(selectedImage, false)
        getMakeUpEngine()?.setMakeupLook((viewModel?.mfeMakeUpLook?.value as? Success)?.data)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.animatedTextIconState?.collectLatest {
                animatedTextIcon1?.run {
                    shouldShowWithAction(it.view1ClickMode != null) {
                        animatedTextIcon1?.renderText(requireContext().getString(it.view1ClickMode!!.textId), it.view1ClickMode.iconUnify)
                    }

                    setupTextClickMode(this, it.view1ClickMode)
                }

                animatedTextIcon2?.run {
                    shouldShowWithAction(it.view2ClickMode != null) {
                        animatedTextIcon2?.renderText(requireContext().getString(it.view2ClickMode!!.textId), it.view2ClickMode.iconUnify)
                    }

                    setupTextClickMode(this, it.view2ClickMode)
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
                    partialBottomArView?.renderBottomInfo(it.data)
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
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMakeUpEngine()?.startRunningWithCamera(context)
            } else {
                AlertDialog.Builder(requireContext())
                        .setTitle("Permission Error")
                        .setMessage("The camera permission is needed for live mode. You must click allow to start the live try-on")
                        .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getMakeUpEngine()?.onResume(context)
    }

    override fun onPause() {
        getMakeUpEngine()?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
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
            val builder = ImagePickerBuilder.getOriginalImageBuilder(it)
                    .apply {
                        title = it.getString(R.string.txt_image_picker_title)
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.PRODUCT_AR)
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? = (activity as? ProductArActivity)?.getMakeUpEngine()

}