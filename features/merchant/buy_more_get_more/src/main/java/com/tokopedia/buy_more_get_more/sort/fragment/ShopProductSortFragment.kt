package com.tokopedia.buy_more_get_more.sort.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.di.component.DaggerShopProductSortComponent
import com.tokopedia.buy_more_get_more.di.module.ShopProductSortModule
import com.tokopedia.buy_more_get_more.sort.activity.ShopProductSortActivity
import com.tokopedia.buy_more_get_more.sort.adapter.ShopProductSortAdapterTypeFactory
import com.tokopedia.buy_more_get_more.sort.listener.ShopProductSortFragmentListener
import com.tokopedia.buy_more_get_more.sort.model.ShopProductSortModel
import com.tokopedia.buy_more_get_more.sort.viewmodel.ShopProductSortViewModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopProductSortFragment : BaseListFragment<ShopProductSortModel, ShopProductSortAdapterTypeFactory>() {
    //    @kotlin.jvm.JvmField
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sortName: String? = null
    private var shopFilterFragmentListener: ShopProductSortFragmentListener? = null
    private var viewModel: ShopProductSortViewModel? = null

    override fun loadData(i: Int) {
        viewModel?.getShopSortListData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.flush()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context != null && context is ShopProductSortFragmentListener) {
            shopFilterFragmentListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopProductSortViewModel::class.java)
        activity?.window?.decorView?.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
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
        context?.let {
            getRecyclerView(view)?.setPadding(
                Int.ZERO,
                it.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                Int.ZERO,
                Int.ZERO
            )
            getRecyclerView(view)?.clipToPadding = false
        }
        observeLiveData()
    }

    override fun getAdapterTypeFactory(): ShopProductSortAdapterTypeFactory {
        return ShopProductSortAdapterTypeFactory()
    }

    override fun initInjector() {
        DaggerShopProductSortComponent
            .builder()
            .shopProductSortModule(ShopProductSortModule())
            .build()
            .inject(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onItemClicked(filterModel: ShopProductSortModel?) {
        shopFilterFragmentListener?.onSortItemSelected(filterModel?.key, filterModel?.value, filterModel?.name)
    }

    private fun observeLiveData() {
        viewModel?.shopSortListData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        val sortListData = it.data.sortList
                        renderList(list = sortListData, hasNextPage = false)
                    }
                    is Fail -> {
                        showToasterError(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown))
                    }
                }
            }
        )
    }

    private fun showToasterError(message: String) {
        activity?.let {
            Toaster.make(requireView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
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
