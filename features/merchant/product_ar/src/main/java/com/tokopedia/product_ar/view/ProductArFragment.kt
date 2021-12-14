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
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.modiface.mfemakeupkit.widgets.MFEMakeupView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.util.AnimatedTextIcon
import com.tokopedia.product_ar.view.bottomsheet.ProductArBottomSheetBuilder
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        partialBottomArView = PartialBottomArView.build(view, this)

        renderAnimatedTextIcon(view)
        setupNavToolbar(view)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            getMakeUpEngine()?.startRunningWithCamera(context)
        }
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
                                                .show(activity.supportFragmentManager,"info ar")
                                    }
                                }
                                .addIcon(IconList.ID_CART) {}
                )
                viewLifecycleOwner.lifecycle.addObserver(it)
            }
        }
    }

    private fun renderAnimatedTextIcon(view: View) {
        animatedTextIcon2 = view.findViewById(R.id.animated_txt_icon_2)
        animatedTextIcon1 = view.findViewById(R.id.animated_txt_icon_1)
        renderInitialAnimatedTextIcon()
    }

    private fun renderInitialAnimatedTextIcon() {
        animatedTextIcon2?.apply {
            renderText("Ambil dari Galeri", IconUnify.IMAGE)
            setOnClickListener {
                onAddImageClick()
            }
            show()
        }
        animatedTextIcon1?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() {
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
            123 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = ImagePickerResultExtractor.extract(data)
                    val selectedImage = BitmapFactory.decodeFile(result.imageUrlOrPathList.first())

                    updateAnimatedIconAfterChoosePhoto()
                    getMakeUpEngine()?.clearMakeupLook()
                    getMakeUpEngine()?.startRunningWithPhoto(selectedImage, false)
                    getMakeUpEngine()?.setMakeupLook((viewModel?.mfeMakeUpLook?.value as? Success)?.data)
                }
            }
        }
    }

    private fun updateAnimatedIconAfterChoosePhoto() {
        animatedTextIcon1?.renderText("Pakai Kamera", IconUnify.CAMERA)
        animatedTextIcon1?.show()
        animatedTextIcon1?.setOnClickListener {
            getMakeUpEngine()?.startRunningWithCamera(context)
            renderInitialAnimatedTextIcon()
        }

        animatedTextIcon2?.renderText("Ganti Foto", IconUnify.IMAGE)
        animatedTextIcon2?.show()

    }

    private fun onAddImageClick() {
        context?.let {
            val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .withSimpleEditor()
                    .apply {
                        title = "Pilih Media"
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.PRODUCT_AR)
            startActivityForResult(intent, 123)
        }
    }

    private fun getMakeUpEngine(): MFEMakeupEngine? = (activity as? ProductArActivity)?.getMakeUpEngine()

}