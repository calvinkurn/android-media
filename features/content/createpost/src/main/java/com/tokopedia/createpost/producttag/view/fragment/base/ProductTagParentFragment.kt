package com.tokopedia.createpost.producttag.view.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentProductTagParentBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.createpost.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.createpost.view.bottomSheet.SearchCategoryTypeBottomSheet
import com.tokopedia.createpost.view.bottomSheet.SearchTypeData
import com.tokopedia.createpost.view.listener.SearchCategoryBottomSheetListener
import com.tokopedia.imagepicker_insta.common.ui.bottomsheet.FeedAccountTypeBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.content_item_product_tag_view.*
import kotlinx.coroutines.InternalCoroutinesApi
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

    private var _binding: FragmentProductTagParentBinding? = null
    private val binding: FragmentProductTagParentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createProductTagViewModel()
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
    }

    private fun renderSelectedProductTagSource(
        prevState: ProductTagSourceUiState?,
        currState: ProductTagSourceUiState,
    ) {
        if(prevState == currState) return

        /** Update Breadcrumb */
        binding.tvCcProductTagProductSource.text = getProductTagSourceText(currState.selectedProductTagSource)

        if(currState.selectedProductTagSource == ProductTagSource.MyShop && userSession.shopAvatar.isNotEmpty()) {
            binding.icCcProductTagShopBadge.setImageUrl(viewModel.shopBadge)
            binding.icCcProductTagShopBadge.show()
        }
        else {
            binding.icCcProductTagShopBadge.hide()
        }

        /** TODO: Update Fragment Content */
    }

    private fun getProductTagSourceText(source: ProductTagSource): String {
        return when(source) {
            ProductTagSource.GlobalSearch,
            ProductTagSource.LastTagProduct -> getString(R.string.content_creation_search_bs_item_tokopedia)
            ProductTagSource.LastPurchase -> getString(R.string.content_creation_search_bs_item_last_purchase)
            ProductTagSource.Wishlist -> getString(R.string.content_creation_search_bs_item_wishlist)
            ProductTagSource.MyShop -> userSession.shopName
            else -> ""
        }
    }

    private fun createProductTagViewModel(): ProductTagViewModel {
        return ViewModelProvider(
            this,
            viewModelFactoryCreator.create(
                requireActivity(),
                getStringArgument(EXTRA_PRODUCT_TAG_LIST),
                getStringArgument(EXTRA_SHOP_BADGE),
            )
        )[ProductTagViewModel::class.java]
    }

    private fun getStringArgument(key: String): String {
        return arguments?.getString(key) ?: ""
    }

    companion object {
        private const val TAG = "ProductTagParentFragment"
        private const val EXTRA_PRODUCT_TAG_LIST = "EXTRA_PRODUCT_TAG_LIST"
        private const val EXTRA_SHOP_BADGE = "EXTRA_SHOP_BADGE"
        private const val EXTRA_SOURCE = "source"
        const val SOURCE_FEED = "feed"
        const val SOURCE_PLAY = "play"

        fun getFragmentWithFeedSource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
        ): ProductTagParentFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, SOURCE_FEED)
        }

        fun getFragmentWithPlaySource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
        ): ProductTagParentFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, SOURCE_PLAY)
        }

        private fun createFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
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
                    putString(EXTRA_SOURCE, source)
                }
            }
        }
    }
}