package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentOrderCustomizationLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.CustomListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductAddOnViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class OrderCustomizationFragment : BaseMultiFragment(), ProductAddOnViewHolder.OnAddOnSelectListener {

    companion object {

        const val BUNDLE_KEY_PRODUCT_UI_MODEL = "productUiModel"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel) = OrderCustomizationFragment().apply {
            this.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(OrderCustomizationViewModel::class.java)
    }

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding: FragmentOrderCustomizationLayoutBinding? = null

    private var customListAdapter: CustomListAdapter? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
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

            // set subtotal price if product is already added to cart
            if (!isAtc) binding?.subtotalProductPriceLabel?.text = priceFmt
            else binding?.subtotalProductPriceLabel?.text = subTotalFmt

            // setup atc button click listener
            binding?.atcButton?.setOnClickListener {
                customListAdapter?.getCustomListItems()?.run {
                    val addOnUiModels = this.map { it.addOnUiModel ?: AddOnUiModel() }

                    val isError = viewModel.isCustomOrderContainError(addOnUiModels)
                    if (isError) {
                        customListAdapter?.notifyDataSetChanged()
                        return@setOnClickListener
                    }

                    val updateParam = TokoFoodMerchantUiModelMapper.mapProductUiModelToAtcRequestParam(
                            shopId = userSession.shopId,
                            productUiModels = listOf(productUiModel),
                            addOnUiModels = addOnUiModels
                    )
                    activityViewModel?.addToCart(updateParam = updateParam, source = "")
                }
            }
        }

    }

    private fun setupCustomList(customListItems: List<CustomListItem>) {
        customListAdapter = CustomListAdapter(this)
        binding?.rvCustomList?.let {
            it.adapter = customListAdapter
            it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            )
        }
        customListAdapter?.setCustomListItems(customListItems = customListItems)
    }

    override fun onAddOnSelected(isSelected: Boolean, addOnPositions: Pair<Int, Int>) {
        customListAdapter?.updateAddOnSelection(isSelected, addOnPositions)
    }
}