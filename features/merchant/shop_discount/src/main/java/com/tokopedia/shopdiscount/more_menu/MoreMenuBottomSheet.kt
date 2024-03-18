package com.tokopedia.shopdiscount.more_menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.BottomsheetMoreMenuBinding
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductRuleUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable


class MoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        private const val PRODUCT_RULE = "productRule"
        @JvmStatic
        fun newInstance(productRule: ShopDiscountProductRuleUiModel): MoreMenuBottomSheet {
            return MoreMenuBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_RULE, productRule)
                }
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetMoreMenuBinding>()

    private var onDeleteMenuClicked: () -> Unit = {}

    private var onOptOutSubsidyClicked: () -> Unit = {}

    private var productRule: ShopDiscountProductRuleUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetMoreMenuBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.sd_manage))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    @SuppressLint("DeprecatedMethod")
    private fun getArgumentsData() {
        arguments?.run {
            productRule = getParcelable(PRODUCT_RULE)
        }
    }

    fun setOnDeleteMenuClicked(onDeleteMenuClicked: () -> Unit = {}) {
        this.onDeleteMenuClicked = onDeleteMenuClicked
    }

    fun setOnOptOutSubsidyMenuClicked(onOptOutSubsidyClicked: () -> Unit = {}) {
        this.onOptOutSubsidyClicked = onOptOutSubsidyClicked
    }

    private fun setupView() {
        binding?.run {
            tpgDelete.setOnClickListener {
                onDeleteMenuClicked()
                dismiss()
            }
            configOptOutSubsidyOption()
        }
    }

    private fun configOptOutSubsidyOption() {
        if (productRule?.isAbleToOptOut == true) {
            binding?.icOptOutSubsidy?.show()
            binding?.textOptOutSubsidy?.apply {
                show()
                setOnClickListener {
                    onOptOutSubsidyClicked()
                    dismiss()
                }
            }
        } else {
            binding?.icOptOutSubsidy?.hide()
            binding?.textOptOutSubsidy?.hide()
        }
    }

}
