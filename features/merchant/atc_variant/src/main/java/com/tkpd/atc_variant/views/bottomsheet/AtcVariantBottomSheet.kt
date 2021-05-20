package com.tkpd.atc_variant.views.bottomsheet

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.data.uidata.PartialButtonDataModel
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.di.AtcVariantComponent
import com.tkpd.atc_variant.di.DaggerAtcVariantComponent
import com.tkpd.atc_variant.util.AtcVariantMapper
import com.tkpd.atc_variant.views.*
import com.tkpd.atc_variant.views.adapter.AtcVariantAdapter
import com.tkpd.atc_variant.views.adapter.AtcVariantAdapterTypeFactoryImpl
import com.tkpd.atc_variant.views.adapter.AtcVariantDiffutil
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantBottomSheet : BottomSheetUnify(), AtcVariantListener, PartialAtcButtonListener, HasComponent<AtcVariantComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AtcVariantViewModel::class.java)
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AtcVariantSharedViewModel::class.java)
    }

    private var loadingProgressDialog: ProgressDialog? = null

    private val adapterFactory by lazy { AtcVariantAdapterTypeFactoryImpl(this) }
    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AtcVariantDiffutil())
                .build()
        AtcVariantAdapter(asyncDifferConfig, adapterFactory)
    }

    private var viewContent: View? = null

    private var baseAtcBtn: PartialAtcButtonView? = null
    private var listener: AtcVariantBottomSheetListener? = null
    private var rvVariantBottomSheet: RecyclerView? = null
    private var buttonActionType = 0

    fun show(fragmentManager: FragmentManager,
             tag: String,
             listener: AtcVariantBottomSheetListener) {
        this.listener = listener
        show(fragmentManager, tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT -> {
                viewModel.updateActivityResult(requestCode = ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initLayout() {
        isHideable = true
        clearContentPadding = true

        updateBottomSheetTitle("Pilih varian")

        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }

        viewContent = View.inflate(context, R.layout.bottomsheet_atc_variant, null)
        viewContent?.let {
            baseAtcBtn = PartialAtcButtonView.build(it.findViewById(R.id.base_atc_btn), this)
        }
        setupRv(viewContent)
        setChild(viewContent)
    }

    private fun updateBottomSheetTitle(value: String) {
        setTitle(context?.getString(R.string.title_bottomsheet_atc_variant, value) ?: "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun setupRv(view: View?) {
        rvVariantBottomSheet = view?.findViewById(R.id.rv_atc_variant_bottomsheet)
        rvVariantBottomSheet?.adapter = adapter
    }

    private fun observeData() {
        sharedViewModel.aggregatorParams.observeOnce(viewLifecycleOwner, {
            viewModel.decideInitialValue(it)
        })

        viewModel.initialData.observe(viewLifecycleOwner, {
            if (it is Success) {
                adapter.submitList(it.data)

                (it.data.firstOrNull { it is VariantComponentDataModel } as? VariantComponentDataModel)?.getSelectedVariantName()?.also { title ->
                    if (title.isNotEmpty()) {
                        updateBottomSheetTitle(title)
                    }
                }
            }
        })

        viewModel.buttonData.observe(viewLifecycleOwner, {
            if (it is Success) {
                renderButton(it.data)
            } else {
                baseAtcBtn?.visibility = false
            }
        })

        observeCart()
    }

    private fun observeCart() {
        viewModel.addToCartLiveData.observe(viewLifecycleOwner, {
            loadingProgressDialog?.dismiss()
            if (it is Success) {
                onSuccessTransaction(it.data)
            } else if (it is Fail) {
                it.throwable.run {
                    viewModel.updateActivityResult(atcSuccessMessage = "")
                    if (this is AkamaiErrorException && message != null) {
                        viewContent?.showToasterError(message
                                ?: "", ctaText = getString(R.string.atc_variant_oke_label))
                    } else {
                        viewContent?.showToasterError(getErrorMessage(this), ctaText = getString(R.string.atc_variant_oke_label))
                    }
                }
            }
        })
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ErrorHandler.getErrorMessage(it, throwable);
        }
                ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
    }

    private fun onSuccessTransaction(result: AddToCartDataModel) {
        val cartId = result.data.cartId
        when (buttonActionType) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
                onSuccessOcs(result)
            }
            ProductDetailCommonConstant.OCC_BUTTON -> {
//                sendTrackingATC(cartId)
                ProductCartHelper.goToOneClickCheckout(getAtcActivity())
            }
            ProductDetailCommonConstant.BUY_BUTTON -> {
//                sendTrackingATC(cartId)
                ProductCartHelper.goToCartCheckout(getAtcActivity(), cartId)
            }
            ProductDetailCommonConstant.ATC_BUTTON -> {
//                sendTrackingATC(cartId)
                onSuccessAtc(result.errorMessage.firstOrNull())
            }
        }
    }

    private fun onSuccessOcs(result: AddToCartDataModel) {
        if (result.data.success == 0) {
            val parentId = viewModel.getVariantData()?.parentId ?: ""
            ProductCartHelper.validateOvo(activity, result, parentId, userSessionInterface.userId, {
                viewModel.updateActivityResult(shouldRefreshValidateOvo = true)
                dismiss()
            }, {
                viewContent?.showToasterError(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
                        ctaText = getString(R.string.atc_variant_oke_label))
            })
        } else {
//                    sendTrackingATC(cartId)
            goToCheckout()
        }
    }

    private fun getAtcActivity(): Activity {
        return context as AtcVariantActivity
    }

    private fun onSuccessAtc(successMessage: String?) {
        context?.let {
            val message = if (successMessage == null || successMessage.isEmpty()) it.getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_success_atc_default) else
                successMessage
            viewModel.updateActivityResult(atcSuccessMessage = message)
            viewContent?.showToasterSuccess(message)
        }
    }

    private fun goToCheckout() {
        context?.let {
            ProductCartHelper.goToCheckout(it as AtcVariantActivity, ShipmentFormRequest
                    .BundleBuilder()
                    .deviceId("")
                    .build()
                    .bundle)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.variantActivityResult.value?.let {
            sharedViewModel.setActivityResult(it)
        } ?: listener?.onBottomSheetDismiss()

        super.onDismiss(dialog)
    }

    override fun getComponent(): AtcVariantComponent {
        return DaggerAtcVariantComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        viewModel.onVariantClicked(variantOptions.variantCategoryKey, variantOptions.variantId, variantOptions.imageOriginal, variantOptions.level)
    }

    override fun getStockWording(): String {
        return ""
    }

    override fun addToCartClick(buttonText: String) {
        doAtc(ProductDetailCommonConstant.ATC_BUTTON)
    }

    override fun buyNowClick(buttonText: String) {
        doAtc(ProductDetailCommonConstant.BUY_BUTTON)
    }

    override fun buttonCartTypeClick(cartType: String, buttonText: String, isAtcButton: Boolean) {
        val atcKey = ProductCartHelper.generateButtonAction(cartType, isAtcButton, false)
        doAtc(atcKey)
    }

    private fun doAtc(buttonAction: Int) {
        buttonActionType = buttonAction
        context?.let {
            val isPartialySelected = AtcVariantMapper.isPartiallySelectedOptionId(viewModel.getSelectedOptionIds())

            if (isPartialySelected) {
                showErrorVariantUnselected()
                return@let
            }

            val sharedData = sharedViewModel.aggregatorParams.value

            showProgressDialog {
                loadingProgressDialog?.dismiss()
            }
            viewModel.hitAtc(buttonAction,
                    sharedData?.shopId?.toIntOrZero() ?: 0,
                    sharedData?.categoryName ?: "",
                    userSessionInterface.userId,
                    sharedData?.isTradein ?: false,
                    sharedData?.minimumShippingPrice ?: 0,
                    sharedData?.trackerAttribution ?: "",
                    sharedData?.trackerListNamePdp ?: ""
            )
        }
    }

    private fun showProgressDialog(onCancelClicked: (() -> Unit)? = null) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(
                    getString(com.tokopedia.abstraction.R.string.title_loading),
                    cancelable = onCancelClicked != null,
                    onCancelClicked = {
                        onCancelClicked?.invoke()
                    })
        }
        loadingProgressDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun showErrorVariantUnselected() {
        val variantErrorMessage = if (viewModel.getVariantData()?.getVariantsIdentifier()?.isEmpty() == true) {
            getString(com.tokopedia.product.detail.common.R.string.add_to_cart_error_variant)
        } else {
            getString(com.tokopedia.product.detail.common.R.string.add_to_cart_error_variant_builder, viewModel.getVariantData()?.getVariantsIdentifier()
                    ?: "")
        }
        viewContent?.showToasterError(variantErrorMessage, ctaText = getString(R.string.atc_variant_oke_label))
    }


    private fun renderButton(data: PartialButtonDataModel) {
        baseAtcBtn?.renderButtonView(
                data.isProductSelectedBuyable,
                data.isShopOwner,
                data.cartTypeData
        )
    }
}

interface AtcVariantBottomSheetListener {
    fun onBottomSheetDismiss()
}