package com.tokopedia.shop.sort.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.sort.di.component.DaggerShopProductSortComponent
import com.tokopedia.shop.sort.di.module.ShopProductSortModule
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.shop.sort.view.adapter.ShopProductSortAdapterTypeFactory
import com.tokopedia.shop.sort.view.listener.ShopProductSortFragmentListener
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.shop.sort.view.presenter.ShopProductSortPresenter
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/23/18.
 */
class ShopProductSortFragment : BaseListFragment<ShopProductSortModel, ShopProductSortAdapterTypeFactory>() {
    @kotlin.jvm.JvmField
    @Inject
    var shopProductFilterPresenter: ShopProductSortPresenter? = null
    private var sortName: String? = null
    private var shopFilterFragmentListener: ShopProductSortFragmentListener? = null
    override fun loadData(i: Int) {
        shopProductFilterPresenter?.getShopFilterList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (shopProductFilterPresenter != null) {
            shopProductFilterPresenter?.detachView()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context != null && context is ShopProductSortFragmentListener) {
            shopFilterFragmentListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopProductFilterPresenter?.attachView(this)
    }

    override fun renderList(list: List<ShopProductSortModel?>, hasNextPage: Boolean) {
        if (sortName != null) {
            for (i in list.indices) {
                if (list[i]?.value.equals(sortName, ignoreCase = true)) {
                    list[i]?.isSelected = true
                }
            }
        }
        super.renderList(list, hasNextPage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null && savedInstanceState == null) {
            sortName = requireArguments().getString(ShopProductSortActivity.Companion.SORT_VALUE)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view)?.setPadding(
                0,
                resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                0,
                0
        )
        getRecyclerView(view)?.clipToPadding = false
    }

    override fun getAdapterTypeFactory(): ShopProductSortAdapterTypeFactory {
        return ShopProductSortAdapterTypeFactory()
    }

    override fun initInjector() {
        DaggerShopProductSortComponent
                .builder()
                .shopProductSortModule(ShopProductSortModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onItemClicked(filterModel: ShopProductSortModel?) {
        shopFilterFragmentListener?.select(filterModel?.key, filterModel?.value, filterModel?.name)
    }

    companion object {
        fun createInstance(sortName: String?): ShopProductSortFragment {
            val fragment = ShopProductSortFragment()
            val arguments = Bundle()
            arguments.putString(ShopProductSortActivity.Companion.SORT_VALUE, sortName)
            fragment.arguments = arguments
            return fragment
        }
    }
}