package com.tokopedia.createpost.producttag.view.fragment.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentProductTagParentBinding
import com.tokopedia.createpost.producttag.util.extension.currentSource
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.createpost.producttag.view.fragment.*
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.SelectedProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.createpost.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
* Created By : Jonathan Darwin on April 25, 2022
*/
class ProductTagParentFragment @Inject constructor(
    private val fragmentFactory: FragmentFactory,
    private val userSession: UserSessionInterface,
    private val viewModelFactoryCreator: ProductTagViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "ProductTagParentFragment"

    private lateinit var viewModel: ProductTagViewModel
    private var mListener: Listener? = null

    private var _binding: FragmentProductTagParentBinding? = null
    private val binding: FragmentProductTagParentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModelProvider()[ProductTagViewModel::class.java]
        childFragmentManager.fragmentFactory = fragmentFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductTagParentBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false,
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserve()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if(childFragment is BaseProductTagChildFragment) {
            childFragment.createViewModelProvider(createViewModelProvider())
        }

        when(childFragment) {
            is ProductTagSourceBottomSheet -> {
                childFragment.apply {
                    setListener(object : ProductTagSourceBottomSheet.Listener {
                        override fun onSelectProductTagSource(source: ProductTagSource) {
                            viewModel.submitAction(ProductTagAction.SelectProductTagSource(source))
                        }
                    })
                    setData(viewModel.productTagSourceList, viewModel.shopBadge)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.icCcProductTagBack.setOnClickListener {
            /** TODO: handle this */
        }

        binding.tvCcProductTagProductSource.setOnClickListener {
            ProductTagSourceBottomSheet.getFragment(
                childFragmentManager,
                requireActivity().classLoader
            ).showNow(childFragmentManager)
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSelectedProductTagSource(it.prevValue?.productTagSource, it.value.productTagSource)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is ProductTagUiEvent.ProductSelected -> {
                        val product = it.product

                        val data = Intent().apply {
                            putExtra(RESULT_PRODUCT_ID, product.id)
                            putExtra(RESULT_PRODUCT_NAME, product.name)
                            putExtra(RESULT_PRODUCT_PRICE, product.priceFmt)
                            putExtra(RESULT_PRODUCT_IMAGE, product.coverURL)
                            putExtra(RESULT_PRODUCT_PRICE_ORIGINAL_FMT, product.priceOriginalFmt)
                            putExtra(RESULT_PRODUCT_PRICE_DISCOUNT_FMT, product.discountFmt)
                            putExtra(RESULT_PRODUCT_IS_DISCOUNT, product.isDiscount)
                        }
                        requireActivity().setResult(Activity.RESULT_OK, data)
                        requireActivity().finish()
                    }
                }
            }
        }

    }

    private fun renderSelectedProductTagSource(
        prevState: ProductTagSourceUiState?,
        currState: ProductTagSourceUiState,
    ) {
        if(prevState == currState) return

        updateBreadcrumb(currState.productTagSourceStack)
        updateFragmentContent(prevState?.productTagSourceStack ?: emptySet(), currState.productTagSourceStack)
    }

    private fun updateBreadcrumb(productTagSourceStack: Set<ProductTagSource>) {
        val selectedSource = productTagSourceStack.currentSource
        binding.tvCcProductTagProductSource.text = getProductTagSourceText(selectedSource)

        if(selectedSource == ProductTagSource.MyShop && userSession.shopAvatar.isNotEmpty()) {
            binding.icCcProductTagShopBadge.setImageUrl(viewModel.shopBadge)
            binding.icCcProductTagShopBadge.show()
        }
        else {
            binding.icCcProductTagShopBadge.hide()
        }
    }

    private fun updateFragmentContent(prevStack: Set<ProductTagSource>, currStack: Set<ProductTagSource>) {
        if(currStack.isEmpty()) {
            mListener?.onCloseProductTag() ?: run {
                requireActivity().finish()
            }
            return
        }

        val selectedSource = currStack.currentSource
        val (fragment, tag) = getFragmentAndTag(selectedSource)
        if(currStack.size >= prevStack.size) {
            childFragmentManager.beginTransaction()
                .replace(binding.flCcProductTagContainer.id, fragment, tag)
                .apply {
                    if(currStack.size > prevStack.size)
                        addToBackStack(null)
                }
                .commit()
        }
        else {
            childFragmentManager.popBackStack()
        }
    }

    private fun getFragmentAndTag(productTagSource: ProductTagSource): Pair<BaseProductTagChildFragment, String> {
        return when(productTagSource) {
            ProductTagSource.LastTagProduct -> {
                Pair(
                    LastTaggedProductFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    LastTaggedProductFragment.TAG,
                )
            }
            ProductTagSource.LastPurchase -> {
                Pair(
                    LastPurchasedProductFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    LastPurchasedProductFragment.TAG,
                )
            }
            ProductTagSource.MyShop -> {
                Pair(
                    MyShopProductFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    MyShopProductFragment.TAG,
                )
            }
            ProductTagSource.GlobalSearch -> {
                Pair(
                    GlobalSearchFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    GlobalSearchFragment.TAG,
                )
            }
            ProductTagSource.Shop -> {
                Pair(
                    ShopProductFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    ShopProductFragment.TAG,
                )
            }
            else -> {
                Pair(
                    GlobalSearchFragment.getFragment(childFragmentManager, requireActivity().classLoader),
                    GlobalSearchFragment.TAG,
                )
            }
        }
    }

    private fun getProductTagSourceText(source: ProductTagSource): String {
        return when(source) {
            ProductTagSource.LastPurchase -> getString(R.string.content_creation_search_bs_item_last_purchase)
            ProductTagSource.Wishlist -> getString(R.string.content_creation_search_bs_item_wishlist)
            ProductTagSource.MyShop -> userSession.shopName
            else -> getString(R.string.content_creation_search_bs_item_tokopedia)
        }
    }

    private fun createViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            this,
            viewModelFactoryCreator.create(
                requireActivity(),
                getStringArgument(EXTRA_PRODUCT_TAG_LIST),
                getStringArgument(EXTRA_SHOP_BADGE),
                getStringArgument(EXTRA_AUTHOR_ID),
                getStringArgument(EXTRA_AUTHOR_TYPE),
            )
        )
    }

    private fun getStringArgument(key: String): String {
        return arguments?.getString(key) ?: ""
    }

    fun onBackPressed() {
        viewModel.submitAction(ProductTagAction.BackPressed)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        const val TAG = "ProductTagParentFragment"
        private const val EXTRA_PRODUCT_TAG_LIST = "EXTRA_PRODUCT_TAG_LIST"
        private const val EXTRA_SHOP_BADGE = "EXTRA_SHOP_BADGE"
        private const val EXTRA_AUTHOR_ID = "EXTRA_AUTHOR_ID"
        private const val EXTRA_AUTHOR_TYPE = "EXTRA_AUTHOR_TYPE"
        private const val EXTRA_SOURCE = "source"

        const val RESULT_PRODUCT_ID = "RESULT_PRODUCT_ID"
        const val RESULT_PRODUCT_NAME = "RESULT_PRODUCT_NAME"
        const val RESULT_PRODUCT_PRICE = "RESULT_PRODUCT_PRICE"
        const val RESULT_PRODUCT_IMAGE = "RESULT_PRODUCT_IMAGE"
        const val RESULT_PRODUCT_PRICE_ORIGINAL_FMT = "RESULT_PRODUCT_PRICE_ORIGINAL_FMT"
        const val RESULT_PRODUCT_PRICE_DISCOUNT_FMT = "RESULT_PRODUCT_PRICE_DISCOUNT_FMT"
        const val RESULT_PRODUCT_IS_DISCOUNT = "RESULT_PRODUCT_IS_DISCOUNT"

        const val SOURCE_FEED = "feed"
        const val SOURCE_PLAY = "play"

        fun findFragment(fragmentManager: FragmentManager): ProductTagParentFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
        }

        fun getFragmentWithFeedSource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
        ): ProductTagParentFragment {
            val oldInstance = findFragment(fragmentManager)
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, authorId, authorType, SOURCE_FEED)
        }

        fun getFragmentWithPlaySource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
        ): ProductTagParentFragment {
            val oldInstance = findFragment(fragmentManager)
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, authorId, authorType, SOURCE_PLAY)
        }

        private fun createFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
            source: String,
        ): ProductTagParentFragment {
            return (
                fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ProductTagParentFragment::class.java.name
                ) as ProductTagParentFragment
            ).apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_TAG_LIST, productTagSource)
                    putSerializable(EXTRA_SHOP_BADGE, shopBadge)
                    putSerializable(EXTRA_AUTHOR_ID, authorId)
                    putSerializable(EXTRA_AUTHOR_TYPE, authorType)
                    putString(EXTRA_SOURCE, source)
                }
            }
        }
    }

    interface Listener {
        fun onCloseProductTag()
    }
}