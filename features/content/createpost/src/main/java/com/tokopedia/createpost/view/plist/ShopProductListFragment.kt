package com.tokopedia.createpost.view.plist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.createpost.createpost.R
import com.tokopedia.library.baseadapter.AdapterCallback
import kotlinx.android.synthetic.main.fragment_shop_plist_page.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_shop_plist_page.view.recycler_view
import kotlinx.android.synthetic.main.layout_parent_product_list.*

class ShopProductListFragment : BaseDaggerFragment(), AdapterCallback {

    val presenter: ShopPageProductListViewModel by lazy { ViewModelProviders.of(this)[ShopPageProductListViewModel::class.java] }
    var getImeiBS: ShopPListSortFilterBs? = null
    private val mAdapter: ShopProductListBaseAdapter by lazy {
        ShopProductListBaseAdapter(
            presenter,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_parent_product_list, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        if (arguments != null) {
            presenter.getPageData(arguments?.getString("shopid"), arguments?.getString("source"))
        }
    }

    private fun initViews(view: View) {

        view.recycler_view.layoutManager = GridLayoutManager(activity, 2)
        view.recycler_view.adapter = mAdapter

        mAdapter.resetAdapter()
        mAdapter.startDataLoading()
        view.sb_shop_product.searchBarIcon.setImageDrawable(null)

        view.cu_sort_chip.setChevronClickListener {
            getImeiBS = ShopPListSortFilterBs.newInstance(presenter)
            fragmentManager?.let { fm -> getImeiBS?.show(fm, "") }
        }

        view.sb_shop_product.searchBarTextField.afterTextChanged {
            mAdapter.filter.filter(it)
        }

    }

    private fun initListener() {
        if (view == null) {
            return
        }

        addListObserver()
        addSortValObserver()
        addProductValObserver()
        addBsObserver()
    }

    private fun addListObserver() = presenter.productList.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mAdapter.resetAdapter()
                    mAdapter.notifyDataSetChanged()
                    mAdapter.startDataLoading()
                }
                is Success -> {
                    mAdapter.onSuccess(it.data)
                }
                is ErrorMessage -> {
                    mAdapter.onError()
                }
            }
        }
    })

    private fun addSortValObserver() = presenter.newSortValeLiveData.observe(this, Observer {
        view?.cu_sort_chip?.chipText = it.name
        view?.cu_sort_chip?.chipType = ChipsUnify.TYPE_SELECTED
        presenter.getPageData(
            arguments?.getString("shopid"),
            arguments?.getString("source"),
            it.value.toString()
        )
    }

    )

    private fun addProductValObserver() =
        presenter.newProductValLiveData.observe(this, Observer { product ->
            activity?.let {
                val data = Intent();
                data.putExtra("product", product)
                it.setResult(RESULT_OK, data);
                it.finish();
            }
        }
        )

    private fun addBsObserver() =
        presenter.showBs.observe(this, Observer { product ->
            activity?.let {
                getImeiBS?.dismiss()
            }
        }
        )

    override fun onRetryPageLoad(pageNumber: Int) {
        presenter.getPageData(arguments?.getString("shopid"), arguments?.getString("source"))
    }

    override fun onEmptyList(rawObject: Any) {
        container?.displayedChild = CONTAINER_EMPTY

    }

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onFinishFirstPageLoad(count: Int, rawObject: Any?) {
        hideLoader()
    }

    override fun onStartPageLoad(pageNumber: Int) {

    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {

    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            container.displayedChild = CONTAINER_ERROR
        }
        Toaster.build(
            requireView(),
            getString(R.string.feed_content_product_list_page_error),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(R.string.feed_content_coba_lagi_text),
            View.OnClickListener {
             onRetryPageLoad(1)
            }).show()
    }
    private fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
    }

    private fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun getScreenName(): String {
        // TODO("Not yet implemented")
        return ""
    }

    override fun initInjector() {

    }
    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_EMPTY = 2
        private val CONTAINER_ERROR = 3

        fun newInstance(shopId: String, source: String): ShopProductListFragment {
            val bundle = Bundle()
            bundle.putString("shopid", shopId)
            bundle.putString("source", source)
            val fragment = ShopProductListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
