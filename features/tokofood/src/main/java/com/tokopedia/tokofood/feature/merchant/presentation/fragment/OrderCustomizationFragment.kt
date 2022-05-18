package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.tokofood.databinding.FragmentOrderCustomizationLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.CustomListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

class OrderCustomizationFragment : BaseMultiFragment() {

    companion object {

        const val BUNDLE_KEY_PRODUCT_UI_MODEL = "productUiModel"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel) = OrderCustomizationFragment().apply {
            this.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
            }
        }
    }

    private var binding: FragmentOrderCustomizationLayoutBinding? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewBinding = FragmentOrderCustomizationLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val productUiModel = arguments?.getParcelable<ProductUiModel>(BUNDLE_KEY_PRODUCT_UI_MODEL)
        productUiModel?.run {
            customListItems.run { setupCustomList(this) }
            binding?.qeuProductQtyEditor?.setValue(orderQty)
            if (!isAtc) binding?.subtotalProductPriceLabel?.text = priceFmt
            else binding?.subtotalProductPriceLabel?.text = subTotalFmt
        }
    }

    private fun setupCustomList(customListItems: List<CustomListItem>) {
        val customListAdapter = CustomListAdapter()
        binding?.rvCustomList?.let {
            it.adapter = customListAdapter
            it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            )
        }
        customListAdapter.setCustomListItems(customListItems = customListItems)
    }
}