package com.tokopedia.productcard.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_ATC
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_SHARE_PRODUCT
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_VISIT_SHOP
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTION_RESULT_PRODUCT
import com.tokopedia.discovery.common.manager.startSimilarSearch
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.share.ProductShare
import com.tokopedia.productcard.options.databinding.ProductCardOptionsFragmentLayoutBinding
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.onboarding.OnBoardingListenerDelegate
import com.tokopedia.productcard.options.tracking.ProductCardOptionsTracking
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject


internal class ProductCardOptionsFragment @Inject constructor(
    private val onBoardingListenerDelegate: OnBoardingListenerDelegate,
): TkpdBaseV4Fragment(), ProductCardOptionsListener {

    private var productCardOptionsViewModel: ProductCardOptionsViewModel? = null
    private var binding: ProductCardOptionsFragmentLayoutBinding? by viewBinding()

    override fun getScreenName(): String {
        return "product card options"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_card_options_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        observeViewModelData()
        observeLifeCycle()
    }

    private fun initViewModel() {
        activity?.let {
            productCardOptionsViewModel = ViewModelProvider(it).get(ProductCardOptionsViewModel::class.java)
        }
    }

    private fun observeViewModelData() {
        observeOptionListLiveData()
        observeRouteToSimilarSearchEventLiveData()
        observeCloseProductCardOptionsEventLiveData()
        observeAddWishlistEventLiveData()
        observeTrackingSeeSimilarProductsEventLiveData()
        observeAddToCartEventLiveData()
        observeRouteToShopPageEvent()
        observeShareProductEvent()
        observeIsLoadingEvent()
        observeCoachmarkEvent()
    }

    private fun observeLifeCycle() {
        onBoardingListenerDelegate.observeLifeCycle(viewLifecycleOwner)
    }

    private fun observeOptionListLiveData() {
        productCardOptionsViewModel?.getOptionsListLiveData()?.observe(viewLifecycleOwner, Observer {
            loadOptions(it)
        })
    }

    private fun loadOptions(optionList: List<Visitable<*>>) {
        val productCardOptionsRecyclerView = binding?.productCardOptionsRecyclerView ?:return
        val typeFactory = ProductCardOptionsTypeFactoryImpl(this)
        productCardOptionsRecyclerView.adapter = ProductCardOptionsAdapter(typeFactory).also {
            it.setList(optionList)
        }

        productCardOptionsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeRouteToSimilarSearchEventLiveData() {
        productCardOptionsViewModel?.getRouteToSimilarSearchEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            routeToSimilarSearch()
        })
    }

    private fun routeToSimilarSearch() {
        activity?.let { activity ->
            val productCardOptionsModel = productCardOptionsViewModel?.productCardOptionsModel
                    ?: return

            startSimilarSearch(activity, productCardOptionsModel.productId, productCardOptionsModel.keyword)
        }
    }

    private fun observeCloseProductCardOptionsEventLiveData() {
        productCardOptionsViewModel?.getCloseProductCardOptionsEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            activity?.finish()
        })
    }

    private fun observeAddWishlistEventLiveData() {
        productCardOptionsViewModel?.getWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST)
        })
    }

    private fun sendProductCardOptionsResult(resultCode: Int) {
        activity?.setResult(resultCode, createProductCardOptionsResult())
        activity?.finish()
    }

    private fun createProductCardOptionsResult(): Intent {
        return Intent().also {
            it.putExtra(PRODUCT_CARD_OPTION_RESULT_PRODUCT, productCardOptionsViewModel?.productCardOptionsModel)
        }
    }

    private fun observeTrackingSeeSimilarProductsEventLiveData() {
        productCardOptionsViewModel?.getTrackingSeeSimilarProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            ProductCardOptionsTracking.eventClickSeeSimilarProduct(
                    productCardOptionsViewModel?.productCardOptionsModel?.seeSimilarProductEvent ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.screenName ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.keyword ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.productId ?: ""
            )
        })
    }

    private fun observeAddToCartEventLiveData() {
        productCardOptionsViewModel?.getAddToCartEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_ATC)
        })
    }

    private fun observeRouteToShopPageEvent() {
        productCardOptionsViewModel?.getRouteToShopPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_VISIT_SHOP)
        })
    }

    private fun observeShareProductEvent() {
        productCardOptionsViewModel?.getShareProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            showLoading()

            activity?.let { activity ->
                ProductShare(activity).share(it, { }, {
                    sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_SHARE_PRODUCT)
                })
            }
        })
    }

    private fun showLoading() {
        val binding = binding ?: return
        binding.productCardOptionsRecyclerView.visibility = View.INVISIBLE
        binding.productCardOptionsLoading.visible()
    }

    private fun observeIsLoadingEvent() {
        productCardOptionsViewModel?.getIsLoadingEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            showLoading()
        })
    }

    private fun observeCoachmarkEvent() {
        productCardOptionsViewModel?.getCoachmarkEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            val recyclerView = binding?.productCardOptionsRecyclerView ?: return@EventObserver
            onBoardingListenerDelegate.buildCoachMark(recyclerView, it.adapterPosition)
        })
    }

    override fun onProductCardOptionsItemImpressed(
        option: ProductCardOptionsItemModel,
        adapterPosition: Int,
    ) {
        productCardOptionsViewModel?.checkShouldDisplaySimilarSearchCoachmark(
            option,
            adapterPosition,
        )
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        internal fun newInstance(
            classLoader: ClassLoader,
            fragmentFactory: FragmentFactory,
        ): Fragment {
            return fragmentFactory.instantiate(
                classLoader,
                ProductCardOptionsFragment::class.java.name,
            )
        }
    }
}
