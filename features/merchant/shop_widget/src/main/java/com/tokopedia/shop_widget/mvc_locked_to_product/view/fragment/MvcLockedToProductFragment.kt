package com.tokopedia.shop_widget.mvc_locked_to_product.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.FragmentMvcLockedToProductBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.ProductItemDecoration
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductAdapter
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory
import com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel.MvcChooseProductViewModel
import com.tokopedia.utils.view.binding.viewBinding

class MvcLockedToProductFragment : BaseDaggerFragment() {

    companion object {
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val GRID_SPAN_COUNT = 2
        fun createInstance() = MvcLockedToProductFragment()
    }

    private val viewBinding: FragmentMvcLockedToProductBinding? by viewBinding()
    private var mvcChooseProductViewModel: MvcChooseProductViewModel? = null
    private var cartLocalCacheHandler: LocalCacheHandler? = null
    private val isUserLogin: Boolean?
        get() = mvcChooseProductViewModel?.isUserLogin
    private val adapter by lazy {
        MvcLockedToProductAdapter(
            typeFactory = MvcLockedToProductTypeFactory(
            )
        )
    }
    private val staggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
    }

    private fun getIntentData() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMvcLockedToProductBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartLocalCacheHandler = LocalCacheHandler(context, CART_LOCAL_CACHE_NAME)
        configToolbar()
        configRecyclerView()
        setVoucherSectionData()
        setTotalProductAndSortSectionData()
        setProductListSectionData()
    }

    private fun configRecyclerView() {
        viewBinding?.rvProductList?.apply {
            adapter = this@MvcLockedToProductFragment.adapter
            layoutManager = staggeredGridLayoutManager
            itemAnimator = null
            addProductItemDecoration()
        }
    }
    protected open fun RecyclerView.addProductItemDecoration() {
        try {
            val context = context ?: return
            val unifySpace16 = com.tokopedia.unifyprinciples.R.dimen.unify_space_16
            val spacing = context.getDimensionPixelSize(unifySpace16)

            if (itemDecorationCount >= 1)
                invalidateItemDecorations()

            addItemDecoration(ProductItemDecoration(spacing))
        } catch (throwable: Throwable) {

        }
    }

    private fun Context.getDimensionPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

    private fun setVoucherSectionData() {
        adapter.setVoucherData()
    }

    private fun setTotalProductAndSortSectionData() {
        adapter.setTotalProductAndSortData()
    }

    private fun setProductListSectionData() {
        adapter.setProductListData()
    }

    private fun configToolbar() {
        viewBinding?.navigationToolbar?.apply {
            val iconBuilder = IconBuilder()
            iconBuilder.addIcon(IconList.ID_CART) {}
            iconBuilder.addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            if (isUserLogin == true)
                setBadgeCounter(IconList.ID_CART, getCartCounter())
            setToolbarPageName(getString(R.string.mvc_choose_product_page_toolbar_name))
        }
    }

    private fun getCartCounter(): Int {
        return cartLocalCacheHandler?.getInt(TOTAL_CART_CACHE_KEY, 0).orZero()
    }


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }

}