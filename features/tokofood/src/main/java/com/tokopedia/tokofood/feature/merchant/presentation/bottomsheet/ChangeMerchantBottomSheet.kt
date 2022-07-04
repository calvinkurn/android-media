package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.common.constants.ImageUrl
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.databinding.BottomsheetChangeMerchantLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChangeMerchantBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetChangeMerchantLayoutBinding>()

    private var changeMerchantListener: ChangeMerchantListener? = null
    private var updateParam: UpdateParam? = null

    private val productUiModel by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getParcelable(KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
    }

    private val productPosition by lazy {
        arguments?.getInt(KEY_PRODUCT_POSITION, Int.ZERO) ?: Int.ZERO
    }

    private val adapterPosition by lazy {
        arguments?.getInt(KEY_ADAPTER_POSITION, Int.ZERO) ?: Int.ZERO
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetChangeMerchantLayoutBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpdateParamFromCacheManager()
        setupViews()
    }

    private fun setUpdateParamFromCacheManager() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        val updateParamCm = cacheManager?.get(
            KEY_UPDATE_PARAM,
            UpdateParam::class.java
        ) ?: UpdateParam()

        this.updateParam = updateParamCm
    }

    private fun setupViews() {
        binding?.ivChangeMerchant?.loadImage(ImageUrl.Merchant.IV_CHANGE_MERCHANT_URL)
        setBtnConfirmOrder()
        setBtnCancelOrder()
    }

    private fun setBtnCancelOrder() {
        binding?.btnCancelOrder?.setOnClickListener {
            dismiss()
        }
    }

    private fun setBtnConfirmOrder() {
        binding?.btnConfirmOrder?.setOnClickListener {
            updateParam?.let { updateParam ->
                changeMerchantListener?.changeMerchantConfirmAddToCart(
                    updateParam,
                    productUiModel,
                    Pair(productPosition, adapterPosition)
                )
            }
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun setChangeMerchantListener(changeMerchantListener: ChangeMerchantListener) {
        this.changeMerchantListener = changeMerchantListener
    }

    interface ChangeMerchantListener {
        fun changeMerchantConfirmAddToCart(
            updateParam: UpdateParam,
            productUiModel: ProductUiModel,
            cardPositions: Pair<Int, Int>
        )
    }

    companion object {
        fun newInstance(bundle: Bundle?): ChangeMerchantBottomSheet {
            return if (bundle == null) {
                return ChangeMerchantBottomSheet()
            } else {
                ChangeMerchantBottomSheet().apply {
                    arguments = bundle
                }
            }
        }

        const val KEY_UPDATE_PARAM = "key_update_param_change_merchant"
        const val KEY_CACHE_MANAGER_ID = "key_cache_manager_id_change_merchant"
        const val KEY_PRODUCT_UI_MODEL = "key_product_ui_model"
        const val KEY_PRODUCT_POSITION = "key_product_position"
        const val KEY_ADAPTER_POSITION = "key_adapter_position"

        val TAG: String = ChangeMerchantBottomSheet::class.java.simpleName
    }

}